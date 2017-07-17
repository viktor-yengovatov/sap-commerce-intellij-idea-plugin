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

package com.intellij.idea.plugin.hybris.project.wizard;

import com.intellij.icons.AllIcons;
import com.intellij.ide.util.ElementsChooser;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils;
import com.intellij.idea.plugin.hybris.project.AbstractHybrisProjectImportBuilder;
import com.intellij.idea.plugin.hybris.project.descriptors.ConfigHybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettings;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.projectImport.SelectImportedProjectsStep;
import com.intellij.util.ArrayUtil;
import com.intellij.util.ui.UIUtil;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptor.IMPORT_STATUS.MANDATORY;
import static com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptor.IMPORT_STATUS.UNLOADED;
import static com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptor.IMPORT_STATUS.UNUSED;
import static com.intellij.util.containers.ContainerUtil.newArrayList;
import static com.intellij.util.containers.ContainerUtil.newHashSet;

/**
 * @author Vlad Bozhenok <VladBozhenok@gmail.com>
 */
public class SelectHybrisImportedProjectsStep extends SelectImportedProjectsStep<HybrisModuleDescriptor>
    implements NonGuiSupport {

    final static int COLUMN_WIDTH = 300;

    private HybrisModuleDescriptor.IMPORT_STATUS selectionMode = MANDATORY;

    public SelectHybrisImportedProjectsStep(final WizardContext context) {
        super(context);

        this.fileChooser.addElementsMarkListener((ElementsChooser.ElementsMarkListener<HybrisModuleDescriptor>) (element, isMarked) -> {
            if (isMarked) {
                for (HybrisModuleDescriptor moduleDescriptor : element.getDependenciesPlainList()) {
                    if (fileChooser.isElementMarked(moduleDescriptor)) {
                        continue;
                    }

                    fileChooser.setElementMarked(moduleDescriptor, true);
                    if (selectionMode == MANDATORY) {
                        moduleDescriptor.setImportStatus(MANDATORY);
                    }
                }
            }
            fileChooser.repaint();
        });
    }

    @Override
    public void updateStep() {
        super.updateStep();
        selectionMode = MANDATORY;
        for (int index = 0; index < fileChooser.getElementCount(); index++) {
            final HybrisModuleDescriptor hybrisModuleDescriptor = fileChooser.getElementAt(index);
            if (hybrisModuleDescriptor.isPreselected()) {
                fileChooser.setElementMarked(hybrisModuleDescriptor, true);
                hybrisModuleDescriptor.setImportStatus(MANDATORY);
            }
        }
        selectionMode = UNUSED;
    }

    @Override
    @Nullable
    protected Icon getElementIcon(final HybrisModuleDescriptor item) {
        if (this.getContext().getHybrisProjectDescriptor().getAlreadyOpenedModules().contains(item)) {
            return AllIcons.Actions.Module;
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
        final Set<HybrisModuleDescriptor> duplicateModules = newHashSet();
        final Map<String, HybrisModuleDescriptor> uniqueModules = new HashMap<>();

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
        return getModuleNameAndPath(item);
    }

    @Override
    public boolean validate() throws ConfigurationException {
        validateCommon();

        if (this.fileChooser.getMarkedElements().isEmpty()) {
            throw new ConfigurationException(
                HybrisI18NBundleUtils.message("hybris.project.import.error.nothing.found.to.import"),
                HybrisI18NBundleUtils.message("hybris.project.import.error.unable.to.proceed")
            );
        }

        this.getContext().setList(this.fileChooser.getMarkedElements());

        return true;
    }

    public void onStepLeaving() {
        super.onStepLeaving();
        final List<HybrisModuleDescriptor> markedElements = newArrayList(this.fileChooser.getMarkedElements());
        final List<HybrisModuleDescriptor> allElements = newArrayList(markedElements);
        final Set<String> moduleDuplicateNames = allElements.stream()
                                                             .map(e -> e.getName())
                                                             .collect(Collectors.toSet());
        for (int index = 0; index < this.fileChooser.getElementCount(); index++) {
            final HybrisModuleDescriptor element = fileChooser.getElementAt(index);
            if (markedElements.contains(element)) {
                if (element.getImportStatus() != MANDATORY) {
                    element.setImportStatus(UNUSED);
                }
            } else if (!moduleDuplicateNames.contains(element.getName())) {
                moduleDuplicateNames.add(element.getName());
                element.setImportStatus(UNLOADED);
                allElements.add(element);
            }
        }

        try {
            this.getContext().setList(allElements);
        } catch (ConfigurationException e) {
            // no-op already validated
        }
    }


    private boolean validateCommon() throws ConfigurationException {
        final Set<HybrisModuleDescriptor> moduleDuplicates = this.calculateSelectedModuleDuplicates();
        final Collection<String> moduleDuplicateNames = newHashSet(moduleDuplicates.size());

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

        return true;
    }

    @Override
    public void nonGuiModeImport(final HybrisProjectSettings settings) throws ConfigurationException {
        selectionMode = MANDATORY;
        final List<HybrisModuleDescriptor> moduleToImport = new ArrayList<>();
        final Set<HybrisModuleDescriptor> moduleToCheck = new HashSet<>();
        for (HybrisModuleDescriptor hybrisModuleDescriptor : getContext().getList()) {
            if (hybrisModuleDescriptor.isPreselected()) {
                moduleToImport.add(hybrisModuleDescriptor);
                hybrisModuleDescriptor.setImportStatus(selectionMode);
                moduleToCheck.add(hybrisModuleDescriptor);
            }
        }
        resolveDependency(moduleToImport, moduleToCheck);

        selectionMode = UNUSED;
        final Set<String> unusedExtensionNameSet = settings.getUnusedExtensions();
        getContext()
            .getList()
            .stream()
            .filter(e -> unusedExtensionNameSet.contains(e.getName()))
            .forEach(e -> {
                moduleToImport.add(e);
                e.setImportStatus(selectionMode);
                moduleToCheck.add(e);
            });
        resolveDependency(moduleToImport, moduleToCheck);

        selectionMode = UNLOADED;
        final Set<String> moduleDuplicateNames = moduleToImport.stream()
                                                                .map(e -> e.getName())
                                                                .collect(Collectors.toSet());
        for (HybrisModuleDescriptor element : getContext().getList()) {
            if (!moduleToImport.contains(element) && !moduleDuplicateNames.contains(element.getName())) {
                moduleDuplicateNames.add(element.getName());
                element.setImportStatus(selectionMode);
                moduleToImport.add(element);
            }
        }

        try {
            this.getContext().setList(moduleToImport);
        } catch (ConfigurationException e) {
            // no-op already validated
        }
    }

    private void resolveDependency(
        final List<HybrisModuleDescriptor> moduleToImport,
        final Set<HybrisModuleDescriptor> moduleToCheck
    ) {
        while (!moduleToCheck.isEmpty()) {
            final HybrisModuleDescriptor currentModule = moduleToCheck.iterator().next();
            for (HybrisModuleDescriptor moduleDescriptor : currentModule.getDependenciesPlainList()) {
                if (!moduleToImport.contains(moduleDescriptor)) {
                    moduleToImport.add(moduleDescriptor);
                    moduleDescriptor.setImportStatus(selectionMode);
                    moduleToCheck.add(moduleDescriptor);
                }
            }
            moduleToCheck.remove(currentModule);
        }
    }

    /*
     * Aligned text to COLUMN_WIDTH. It is not precise by space pixel width (4pixels)
     */
    @NotNull
    private String getModuleNameAndPath(@NotNull final HybrisModuleDescriptor moduleDescriptor) {
        Validate.notNull(moduleDescriptor);

        final StringBuilder builder = new StringBuilder();
        builder.append(moduleDescriptor.getName());

        final Font font = getComponent().getFont();
        final BufferedImage img = UIUtil.createImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        final FontMetrics fm = img.getGraphics().getFontMetrics(font);

        final int currentWidth = fm.stringWidth(builder.toString());
        final int spaceWidth = fm.charWidth(' ');
        final int spaceCount = (COLUMN_WIDTH - currentWidth) / spaceWidth;

        for (int index = 0; index < spaceCount; index++) {
            builder.append(' ');
        }

        builder.append(" (");
        builder.append(moduleDescriptor.getRelativePath());
        builder.append(')');

        return builder.toString();
    }

    @Override
    protected boolean isElementEnabled(HybrisModuleDescriptor hybrisModuleDescriptor) {
        if (hybrisModuleDescriptor instanceof ConfigHybrisModuleDescriptor) {
            if (hybrisModuleDescriptor.isPreselected()) {
                return false;
            }
        }
        return super.isElementEnabled(hybrisModuleDescriptor);
    }
}
