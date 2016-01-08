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
import com.intellij.idea.plugin.hybris.project.settings.HybrisProjectDescriptor;
import com.intellij.idea.plugin.hybris.project.tasks.SearchHybrisDistributionDirectoryTaskModalWindow;
import com.intellij.idea.plugin.hybris.project.utils.Processor;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import com.intellij.idea.plugin.hybris.utils.HybrisI18NBundleUtils;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.projectImport.ProjectImportWizardStep;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.Validate;

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
    private TextFieldWithBrowseButton sourceCodeZipFilesInChooser;
    private JTextField projectNameTextField;
    private JCheckBox importOotbModulesInReadOnlyModeCheckBox;
    private volatile TextFieldWithBrowseButton hybrisDistributionDirectoryFilesInChooser;
    private volatile TextFieldWithBrowseButton customExtensionsDirectoryFilesInChooser;

    public HybrisWorkspaceRootStep(final WizardContext context) {
        super(context);

        this.storeModuleFilesInChooser.addBrowseFolderListener(
            HybrisI18NBundleUtils.message("hybris.project.import.select.directory.where.new.idea.module.files.will.be.stored"),
            "",
            null,
            FileChooserDescriptorFactory.createSingleFolderDescriptor()
        );

        this.storeModuleFilesInCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                storeModuleFilesInChooser.setEnabled(((JCheckBox) e.getSource()).isSelected());
            }
        });

        this.sourceCodeZipFilesInChooser.addBrowseFolderListener(
            HybrisI18NBundleUtils.message("hybris.import.label.select.hybris.src.file"),
            "",
            null,
            FileChooserDescriptorFactory.createSingleFileDescriptor()
        );

        this.hybrisDistributionDirectoryFilesInChooser.addBrowseFolderListener(
            HybrisI18NBundleUtils.message("hybris.import.label.select.hybris.distribution.directory"),
            "",
            null,
            FileChooserDescriptorFactory.createSingleFolderDescriptor()
        );

        this.customExtensionsDirectoryFilesInChooser.addBrowseFolderListener(
            HybrisI18NBundleUtils.message("hybris.import.label.select.custom.extensions.directory"),
            "",
            null,
            FileChooserDescriptorFactory.createSingleFolderDescriptor()
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

        this.getContext().getHybrisProjectDescriptor().setImportOotbModulesInReadOnlyMode(
            this.importOotbModulesInReadOnlyModeCheckBox.isSelected()
        );

        this.getContext().getHybrisProjectDescriptor().setSourceCodeZip(
            new File(this.sourceCodeZipFilesInChooser.getText())
        );

        this.getWizardContext().setProjectName(this.projectNameTextField.getText());

        this.getContext().getHybrisProjectDescriptor().setHybrisDistributionDirectory(
            new File(this.hybrisDistributionDirectoryFilesInChooser.getText())
        );

        this.getContext().getHybrisProjectDescriptor().setCustomExtensionsDirectory(
            new File(this.customExtensionsDirectoryFilesInChooser.getText())
        );
    }

    @Override
    public void updateStep() {
        this.storeModuleFilesInChooser.setText(
            new File(
                this.getBuilder().getFileToImport(), HybrisConstants.DEFAULT_DIRECTORY_NAME_FOR_IDEA_MODULE_FILES
            ).getAbsolutePath()
        );

        this.projectNameTextField.setText(getWizardContext().getProjectName());

        final HybrisProjectDescriptor hybrisProjectDescriptor = this.getContext().getHybrisProjectDescriptor();

        this.importOotbModulesInReadOnlyModeCheckBox.setSelected(
            hybrisProjectDescriptor.isImportOotbModulesInReadOnlyMode()
        );

        if (StringUtils.isBlank(this.hybrisDistributionDirectoryFilesInChooser.getText())) {

            ProgressManager.getInstance().run(new SearchHybrisDistributionDirectoryTaskModalWindow(
                new File(this.getBuilder().getFileToImport()), new Processor<String>() {

                @Override
                public void process(final String parameter) {
                    hybrisDistributionDirectoryFilesInChooser.setText(parameter);
                }
            }));
        }

        if (StringUtils.isNotBlank(this.hybrisDistributionDirectoryFilesInChooser.getText())) {

            if (StringUtils.isBlank(this.customExtensionsDirectoryFilesInChooser.getText())) {

                this.customExtensionsDirectoryFilesInChooser.setText(
                    new File(
                        this.hybrisDistributionDirectoryFilesInChooser.getText(),
                        HybrisConstants.CUSTOM_MODULES_DIRECTORY_RELATIVE_PATH
                    ).getAbsolutePath()
                );
            }
        }
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


        if (StringUtils.isBlank(this.hybrisDistributionDirectoryFilesInChooser.getText())) {
            throw new ConfigurationException(
                HybrisI18NBundleUtils.message("hybris.import.wizard.validation.hybris.distribution.directory.empty")
            );
        }

        if (!new File(this.hybrisDistributionDirectoryFilesInChooser.getText()).isDirectory()) {
            throw new ConfigurationException(
                HybrisI18NBundleUtils.message("hybris.import.wizard.validation.hybris.distribution.directory.does.not.exist"));
        }

        if (!StringUtils.startsWith(this.hybrisDistributionDirectoryFilesInChooser.getText(), this.getBuilder().getFileToImport())) {
            throw new ConfigurationException(
                HybrisI18NBundleUtils.message("hybris.import.wizard.validation.hybris.distribution.directory.is.outside.of.project.root.directory"));
        }


        if (StringUtils.isBlank(this.customExtensionsDirectoryFilesInChooser.getText())) {
            throw new ConfigurationException(
                HybrisI18NBundleUtils.message("hybris.import.wizard.validation.custom.extensions.directory.empty"));
        }

        if (!new File(this.customExtensionsDirectoryFilesInChooser.getText()).isDirectory()) {
            throw new ConfigurationException(
                HybrisI18NBundleUtils.message("hybris.import.wizard.validation.custom.extensions.directory.does.not.exist"));
        }

        if (!StringUtils.startsWith(this.customExtensionsDirectoryFilesInChooser.getText(), this.getBuilder().getFileToImport())) {
            throw new ConfigurationException(
                HybrisI18NBundleUtils.message("hybris.import.wizard.validation.custom.extensions.directory.is.outside.of.project.root.directory"));
        }

        return true;
    }

    public AbstractHybrisProjectImportBuilder getContext() {
        return (AbstractHybrisProjectImportBuilder) this.getBuilder();
    }

}
