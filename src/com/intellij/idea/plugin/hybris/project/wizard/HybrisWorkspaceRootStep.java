/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2015 Alexander Bartash <AlexanderBartash@gmail.com>
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

import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.idea.plugin.hybris.project.AbstractHybrisProjectImportBuilder;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import com.intellij.idea.plugin.hybris.utils.HybrisI18NBundleUtils;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.projectImport.ProjectImportWizardStep;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author Vlad Bozhenok <VladBozhenok@gmail.com>
 */
public class HybrisWorkspaceRootStep extends ProjectImportWizardStep {

    private JPanel rootPanel;
    private TextFieldWithBrowseButton storeModuleFilesInChooser;
    private JCheckBox storeModuleFilesInCheckBox;
    private JLabel storeModuleFilesInLabel;
    private TextFieldWithBrowseButton sourceCodeZipFilesInChooser;
    private JLabel sourceCodeZip;
    private JTextField projectNameTextField;

    public HybrisWorkspaceRootStep(final WizardContext context) {
        super(context);

        this.storeModuleFilesInChooser.addBrowseFolderListener(
            HybrisI18NBundleUtils.message("hybris.project.import.select.directory.where.new.idea.module.files.will.be.stored"),
            HybrisI18NBundleUtils.message("hybris.project.import.select.directory.where.new.idea.module.files.will.be.stored"),
            null,
            FileChooserDescriptorFactory.createSingleFolderDescriptor()
        );

        this.storeModuleFilesInCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(@NotNull final ActionEvent e) {
                storeModuleFilesInChooser.setEnabled(((JCheckBox) e.getSource()).isSelected());
            }
        });

        this.sourceCodeZipFilesInChooser.addBrowseFolderListener(
            HybrisI18NBundleUtils.message("hybris.import.label.select.hybris.src.file"),
            "",
            null,
            FileChooserDescriptorFactory.createSingleFileDescriptor()
        );

    }

    @Override
    public JComponent getComponent() {
        return this.rootPanel;
    }

    @Override
    public void updateDataModel() {
        this.getContext().setRootProjectDirectory(new File(this.getContext().getFileToImport()));

        if (this.storeModuleFilesInCheckBox.isSelected()) {
            this.getContext().getHybrisProjectDescriptor().setModulesFilesDirectory(
                new File(this.storeModuleFilesInChooser.getText())
            );
        }

        this.getContext().getHybrisProjectDescriptor().setSourceCodeZip(
            new File(this.sourceCodeZipFilesInChooser.getText())
        );

        getWizardContext().setProjectName(projectNameTextField.getText());
    }

    @Override
    public void updateStep() {
        this.storeModuleFilesInChooser.setText(
            new File(
                this.getBuilder().getFileToImport(), HybrisConstants.DEFAULT_DIRECTORY_NAME_FOR_IDEA_MODULE_FILES
            ).getAbsolutePath()
        );
        projectNameTextField.setText(getWizardContext().getProjectName());
    }

    @Override
    public boolean validate() throws ConfigurationException {
        if (!super.validate()) {
            return false;
        }

        if (this.storeModuleFilesInCheckBox.isSelected()) {
            final File moduleFilesDirectory = new File(this.storeModuleFilesInChooser.getText());
            if (!moduleFilesDirectory.exists()) {
                return moduleFilesDirectory.mkdirs();
            }
        }

        return true;
    }

    public AbstractHybrisProjectImportBuilder getContext() {
        return (AbstractHybrisProjectImportBuilder) this.getBuilder();
    }

}
