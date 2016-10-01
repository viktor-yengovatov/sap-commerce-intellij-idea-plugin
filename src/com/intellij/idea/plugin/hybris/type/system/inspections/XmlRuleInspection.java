/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2016 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.intellij.idea.plugin.hybris.type.system.inspections;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import com.intellij.idea.plugin.hybris.common.services.CommonIdeaService;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptor.DescriptorType;
import com.intellij.idea.plugin.hybris.type.system.utils.TypeSystemUtils;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.xml.XmlFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.intellij.openapi.util.io.FileUtil.normalize;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.RULESET_XML;

public class XmlRuleInspection extends LocalInspectionTool {

    private static final Logger LOG = Logger.getInstance(XmlRuleInspection.class);

    private XmlRule[] myRules;

    @Nullable
    @Override
    public ProblemDescriptor[] checkFile(
        final @NotNull PsiFile file,
        final @NotNull InspectionManager manager,
        final boolean isOnTheFly
    ) {
        if (!TypeSystemUtils.isTypeSystemXmlFile(file)) {
            return null;
        }
        final XmlFile xmlFile = (XmlFile) file;

        if (!this.shouldCheckFile(file)) {
            return null;
        }

        final ValidateContext sharedContext = ValidateContextImpl.createFileContext(manager, isOnTheFly, xmlFile);
        if (sharedContext == null) {
            return null;
        }

        final List<ProblemDescriptor> result = new ArrayList<>();
        for (XmlRule nextRule : this.getRules()) {
            try {
                this.validateOneRule(nextRule, sharedContext, result);
            } catch (XPathExpressionException e) {
                result.add(this.createValidationFailedProblem(sharedContext, xmlFile, nextRule, e));
            }
        }

        return result.toArray(new ProblemDescriptor[result.size()]);
    }

    protected boolean shouldCheckFile(@NotNull final PsiFileSystemItem file) {
        if (file.getVirtualFile() == null) {
            return false;
        }

        final Module module = ModuleUtilCore.findModuleForPsiElement(file);
        final String descriptorTypeName = module.getOptionValue(HybrisConstants.DESCRIPTOR_TYPE);

        if (descriptorTypeName == null) {
            if (shouldCheckFilesWithoutHybrisSettings(file.getProject())) {
                return estimateIsCustomExtension(file);
            }
            return false;
        }

        final DescriptorType descriptorType = DescriptorType.valueOf(descriptorTypeName);
        return descriptorType == DescriptorType.CUSTOM;
    }

    /*
     * This method disqualifies known hybris extensions. Anything else is considered for TSV validation.
     */
    private boolean estimateIsCustomExtension(final PsiFileSystemItem file) {
        final File itemsfile = VfsUtilCore.virtualToIoFile(file.getVirtualFile());
        final String itemsfilePath = normalize(itemsfile.getAbsolutePath());

        if (itemsfilePath.contains(normalize(HybrisConstants.HYBRIS_OOTB_MODULE_PREFIX))) {
            return false;
        }
        if (itemsfilePath.contains(normalize(HybrisConstants.PLATFORM_EXT_MODULE_PREFIX))) {
            return false;
        }
        return true;
    }

    @NotNull
    private XmlRule[] getRules() {
        if (this.myRules == null) {
            try {
                this.myRules = this.loadRules();
            } catch (IOException e) {
                LOG.error("Error loading ruleset", e);
                this.myRules = new XmlRule[0];
            }
        }

        return this.myRules;
    }

    protected void validateOneRule(
        @NotNull final XmlRule rule,
        @NotNull final ValidateContext context,
        @NotNull final Collection<? super ProblemDescriptor> output
    ) throws XPathExpressionException {
        final XPathService xPathService = ServiceManager.getService(XPathService.class);

        final NodeList selection = xPathService.computeNodeSet(rule.getSelectionXPath(), context.getDocument());
        for (int i = 0; i < selection.getLength(); i++) {
            final Node nextSelected = selection.item(i);
            //noinspection BooleanVariableAlwaysNegated
            final boolean passed = xPathService.computeBoolean(rule.getTestXPath(), nextSelected);
            if (!passed) {
                output.add(this.createProblem(context, nextSelected, rule));
            }
        }
    }

    protected ProblemDescriptor createValidationFailedProblem(
        @NotNull final ValidateContext context,
        @NotNull final PsiElement file,
        @NotNull final XmlRule failedRule,
        @NotNull final Exception failure
    ) {

        return context.getManager().createProblemDescriptor(
            file,
            "XmlRule '" + failedRule.getID() + "' has failed to validate: " + failure.getMessage(),
            true,
            ProblemHighlightType.GENERIC_ERROR,
            context.isOnTheFly()
        );
    }

    protected boolean shouldCheckFilesWithoutHybrisSettings(@NotNull final Project project) {
        // at least it needs to have hybris flag
        final CommonIdeaService commonIdeaService = ServiceManager.getService(CommonIdeaService.class);
        return commonIdeaService.isHybrisProject(project);
    }

    private XmlRule[] loadRules() throws IOException {
        try (InputStream input = this.getClass().getClassLoader().getResourceAsStream(RULESET_XML)) {
            if (input == null) {
                throw new IOException("Ruleset file is not found");
            }
            final List<XmlRule> rules = new XmlRuleParser().parseRules(new BufferedInputStream(input));
            return rules.toArray(new XmlRule[rules.size()]);
        }
    }

    protected ProblemDescriptor createProblem(
        @NotNull final ValidateContext context,
        @NotNull final Node problemNode,
        @NotNull final XmlRule rule
    ) {
        final PsiElement problemPsi = context.mapNodeToPsi(problemNode);
        final ProblemHighlightType highlightType = this.computePriority(rule);

        return context.getManager().createProblemDescriptor(
            problemPsi,
            rule.getDescription(),
            true,
            highlightType,
            context.isOnTheFly()
        );
    }

    @NotNull
    protected ProblemHighlightType computePriority(@NotNull final XmlRule rule) {
        switch (rule.getPriority()) {
            case LOW:
                return ProblemHighlightType.WEAK_WARNING;
            default:
                return ProblemHighlightType.ERROR;
        }
    }

}
