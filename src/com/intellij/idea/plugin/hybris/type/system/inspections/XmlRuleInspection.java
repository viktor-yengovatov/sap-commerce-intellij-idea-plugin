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
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettings;
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettingsComponent;
import com.intellij.idea.plugin.hybris.type.system.file.TypeSystemDomFileDescription;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        if (!isTypeSystemFile(file)) {
            return null;
        }
        XmlFile xmlFile = (XmlFile) file;

        if (!shouldCheckFile(file)) {
            return null;
        }

        ValidateContext sharedContext = ValidateContextImpl.createFileContext(manager, isOnTheFly, xmlFile);
        if (sharedContext == null) {
            return null;
        }

        List<ProblemDescriptor> result = new ArrayList<>();
        for (XmlRule nextRule : getRules()) {
            try {
                validateOneRule(nextRule, sharedContext, result);
            } catch (XPathExpressionException e) {
                result.add(createValidationFailedProblem(sharedContext, xmlFile, nextRule, e));
            }
        }

        return result.toArray(new ProblemDescriptor[result.size()]);
    }

    @NotNull
    protected Optional<String> getCustomDirectory(@NotNull PsiFile file) {
        return Optional.ofNullable(HybrisProjectSettingsComponent.getInstance(file.getProject()))
                       .map(HybrisProjectSettingsComponent::getState)
                       .map(HybrisProjectSettings::getCustomDirectory);
    }

    protected boolean shouldCheckFilesWithoutHybrisSettings() {
        //probably it is a test project where we DO want to show warnings
        return true;
    }

    private boolean isTypeSystemFile(@NotNull PsiFile file) {
        return TypeSystemDomFileDescription.isTypeSystemXmlFile(file);
    }

    protected boolean shouldCheckFile(@NotNull PsiFile file) {
        if (file.getVirtualFile() == null) {
            return false;
        }

        Optional<String> optionalCustomDir = getCustomDirectory(file);

        //FIXME: workaround to always enable validation in test projects without hybris settings
        if (!optionalCustomDir.isPresent() && shouldCheckFilesWithoutHybrisSettings()) {
            return true;
        }

        final String DEFAULT_LOCATION = "bin/custom";
        String customDir = optionalCustomDir.orElse(DEFAULT_LOCATION);

        //next line enforces <code>customDit.endsWith(VfsUtilCore.VFS_SEPARATOR_CHAR)</code>
        customDir = StringUtil.trimEnd(customDir, VfsUtilCore.VFS_SEPARATOR_CHAR) + VfsUtilCore.VFS_SEPARATOR_CHAR;

        //FIXME: revisit: according to DefaultHybrisProjectImportBuilder#saveCustomDirectoryLocation
        //FIXME: customDir is stored as a relative path from hybris home
        //FIXME: However, I don't see how to access the hybris home here, so will assume that
        //FIXME: it is the somewhere under project base directory
        //FIXME: Hence, we are checking contains() instead of some kind of startsWith(..)
        String relativePath = VfsUtilCore.getRelativePath(file.getVirtualFile(), file.getProject().getBaseDir());
        return relativePath != null && relativePath.contains(customDir);
    }

    protected void validateOneRule(
        @NotNull XmlRule rule,
        @NotNull ValidateContext context,
        @NotNull List<? super ProblemDescriptor> output
    ) throws XPathExpressionException {

        NodeList selection = context.getXPath().computeNodeSet(rule.getSelectionXPath(), context.getDocument());
        for (int i = 0; i < selection.getLength(); i++) {
            Node nextSelected = selection.item(i);
            //noinspection BooleanVariableAlwaysNegated
            boolean passed = context.getXPath().computeBoolean(rule.getTestXPath(), nextSelected);
            if (!passed) {
                output.add(createProblem(context, nextSelected, rule));
            }
        }
    }

    protected ProblemDescriptor createProblem(
        @NotNull ValidateContext context,
        @NotNull Node problemNode,
        @NotNull XmlRule rule
    ) {

        @NotNull PsiElement problemPsi = context.mapNodeToPsi(problemNode);
        @NotNull ProblemHighlightType highlightType = computePriority(rule);

        return context.getManager().createProblemDescriptor(
            problemPsi,
            rule.getDescription(),
            true,
            highlightType,
            context.isOnTheFly()
        );
    }

    protected ProblemDescriptor createValidationFailedProblem(
        @NotNull ValidateContext context,
        @NotNull PsiElement file,
        @NotNull XmlRule failedRule,
        @NotNull Exception failure
    ) {

        return context.getManager().createProblemDescriptor(
            file,
            "XmlRule '" + failedRule.getID() + "' has failed to validate: " + failure.getMessage(),
            true,
            ProblemHighlightType.GENERIC_ERROR,
            context.isOnTheFly()
        );
    }

    @NotNull
    protected ProblemHighlightType computePriority(@NotNull XmlRule rule) {
        switch (rule.getPriority()) {
            case LOW:
                return ProblemHighlightType.WEAK_WARNING;
            default:
                return ProblemHighlightType.ERROR;
        }
    }

    @NotNull
    private XmlRule[] getRules() {
        if (myRules == null) {
            try {
                myRules = loadRules();
            } catch (IOException e) {
                LOG.error("Error loading ruleset", e);
                myRules = new XmlRule[0];
            }
        }
        return myRules;
    }

    private XmlRule[] loadRules() throws IOException {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("ruleset.xml")) {
            if (input == null) {
                throw new IOException("Ruleset file is not found");
            }
            List<XmlRule> rules = new XmlRuleParser().parseRules(new BufferedInputStream(input));
            return rules.toArray(new XmlRule[rules.size()]);
        }
    }

}
