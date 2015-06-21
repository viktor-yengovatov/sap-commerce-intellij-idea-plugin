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
import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
        if (this.isInConflict(item)) {
            return AllIcons.Actions.Cancel;
        } else if (this.getContext().getHybrisProjectDescriptor().getAlreadyOpenedModules().contains(item)) {
            return AllIcons.General.InspectionsOK;
        }

        return null;
    }

    protected boolean isInConflict(@NotNull final HybrisModuleDescriptor item) {
        Validate.notNull(item);

        return this.fileChooser.getMarkedElements().contains(item)
               && this.calculateSelectedModuleDuplicates().contains(item.getModuleName());
    }

    @Override
    public AbstractHybrisProjectImportBuilder getContext() {
        return (AbstractHybrisProjectImportBuilder) this.getBuilder();
    }

    @NotNull
    protected Set<String> calculateSelectedModuleDuplicates() {
        final Set<String> duplicateModules = new HashSet<String>();
        final Collection<String> uniqueModules = new HashSet<String>();

        for (HybrisModuleDescriptor moduleDescriptor : this.fileChooser.getMarkedElements()) {

            if (!uniqueModules.add(moduleDescriptor.getModuleName())) {
                duplicateModules.add(moduleDescriptor.getModuleName());
            }
        }

        return duplicateModules;
    }

    @Override
    protected String getElementText(final HybrisModuleDescriptor item) {

        final StringBuilder builder = new StringBuilder();

        builder.append(item.getModuleName());
        builder.append("         (");
        builder.append(item.getModuleRelativePath());
        builder.append(')');

        return builder.toString();
    }

    @Override
    public boolean validate() throws ConfigurationException {
        final Set<String> moduleDuplicates = this.calculateSelectedModuleDuplicates();

        if (!moduleDuplicates.isEmpty()) {
            throw new ConfigurationException(
                HybrisI18NBundleUtils.message(
                    "hybris.project.import.duplicate.projects.found",
                    StringUtil.join(ArrayUtil.toStringArray(moduleDuplicates), "\n")
                ),
                HybrisI18NBundleUtils.message("hybris.project.error")
            );
        }

        return super.validate();
    }
}
