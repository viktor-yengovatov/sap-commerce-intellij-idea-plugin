/*
 * Copyright 2015 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.idea.plugin.hybris.project.wizard;

import com.intellij.icons.AllIcons;
import com.intellij.ide.util.ElementsChooser;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.idea.plugin.hybris.project.AbstractHybrisProjectImportBuilder;
import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.utils.HybrisI18NBundleUtils;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.projectImport.SelectImportedProjectsStep;
import com.intellij.util.ArrayUtil;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;

/**
 * @author Vlad Bozhenok <vladbozhenok@gmail.com>
 */
public class SelectHybrisImportedProjectsStep extends SelectImportedProjectsStep<HybrisModuleDescriptor> {

    public SelectHybrisImportedProjectsStep(final WizardContext context) {
        super(context);

        this.fileChooser.addElementsMarkListener(new ElementsChooser.ElementsMarkListener<HybrisModuleDescriptor>() {

            @Override
            public void elementMarkChanged(final HybrisModuleDescriptor element, final boolean isMarked) {
                if (isMarked) {
                    for (HybrisModuleDescriptor moduleDescriptor : element.getDependenciesPlainList()) {
                        if (fileChooser.isElementMarked(moduleDescriptor)) {
                            continue;
                        }

                        fileChooser.setElementMarked(moduleDescriptor, true);
                    }
                }

                fileChooser.repaint();
            }
        });
    }

    @Override
    @Nullable
    protected Icon getElementIcon(final HybrisModuleDescriptor item) {
        if (this.getContext().getHybrisProjectDescriptor().getAlreadyOpenedModules().contains(item)) {
            return AllIcons.General.InspectionsOK;
        } else if (this.isInConflict(item)) {
            return AllIcons.Actions.Cancel;
        }

        return null;
    }

    @Override
    public AbstractHybrisProjectImportBuilder getContext() {
        return (AbstractHybrisProjectImportBuilder) this.getBuilder();
    }

    protected boolean isInConflict(@NotNull final HybrisModuleDescriptor item) {
        Validate.notNull(item);

        return this.fileChooser.getMarkedElements().contains(item)
               && this.calculateSelectedModuleDuplicates().contains(item);
    }

    @NotNull
    protected Set<HybrisModuleDescriptor> calculateSelectedModuleDuplicates() {
        final Set<HybrisModuleDescriptor> duplicateModules = new HashSet<HybrisModuleDescriptor>();
        final Map<String, HybrisModuleDescriptor> uniqueModules = new HashMap<String, HybrisModuleDescriptor>();

        for (HybrisModuleDescriptor moduleDescriptor : this.fileChooser.getMarkedElements()) {

            final HybrisModuleDescriptor alreadySelected = uniqueModules.get(moduleDescriptor.getName());

            if (null == alreadySelected) {
                uniqueModules.put(moduleDescriptor.getName(), moduleDescriptor);
            } else {
                duplicateModules.add(alreadySelected);
                duplicateModules.add(moduleDescriptor);
            }
        }

        return duplicateModules;
    }

    @Override
    protected String getElementText(final HybrisModuleDescriptor item) {

        final StringBuilder builder = new StringBuilder();

        builder.append(item.getName());
        builder.append("         (");
        builder.append(item.getRelativePath());
        builder.append(')');

        return builder.toString();
    }

    @Override
    public boolean validate() throws ConfigurationException {
        final Set<HybrisModuleDescriptor> moduleDuplicates = this.calculateSelectedModuleDuplicates();
        final Collection<String> moduleDuplicateNames = new ArrayList<String>(moduleDuplicates.size());

        for (HybrisModuleDescriptor moduleDuplicate : moduleDuplicates) {
            moduleDuplicateNames.add(this.getModuleNameAndPath(moduleDuplicate));
        }

        if (!moduleDuplicates.isEmpty()) {
            throw new ConfigurationException(
                HybrisI18NBundleUtils.message(
                    "hybris.project.import.duplicate.projects.found",
                    StringUtil.join(ArrayUtil.toStringArray(moduleDuplicateNames), "\n")
                ),
                HybrisI18NBundleUtils.message("hybris.project.error")
            );
        }

        if (this.fileChooser.getMarkedElements().isEmpty()) {
            throw new ConfigurationException(
                HybrisI18NBundleUtils.message("hybris.project.import.error.nothing.found.to.import"),
                HybrisI18NBundleUtils.message("hybris.project.import.error.unable.to.proceed")
            );
        }

        this.getContext().setList(this.fileChooser.getMarkedElements());

        return true;
    }

    @NotNull
    private String getModuleNameAndPath(@NotNull final HybrisModuleDescriptor moduleDescriptor) {
        Validate.notNull(moduleDescriptor);

        final StringBuilder builder = new StringBuilder();

        builder.append(moduleDescriptor.getName());
        builder.append(' ');
        builder.append('(');
        builder.append(moduleDescriptor.getRelativePath());
        builder.append(')');

        return builder.toString();
    }
}
