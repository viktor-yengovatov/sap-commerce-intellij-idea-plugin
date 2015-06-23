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

import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.idea.plugin.hybris.project.AbstractHybrisProjectImportBuilder;
import com.intellij.idea.plugin.hybris.utils.HybrisI18NBundleUtils;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.projectImport.ProjectImportWizardStep;

import javax.swing.*;
import java.io.File;

/**
 * @author Vlad Bozhenok <vladbozhenok@gmail.com>
 */
public class HybrisWorkspaceRootStep extends ProjectImportWizardStep {

    private JPanel rootPanel;
    private TextFieldWithBrowseButton projectsRootChooser;

    public HybrisWorkspaceRootStep(final WizardContext context) {
        super(context);

        this.projectsRootChooser.addBrowseFolderListener(
            HybrisI18NBundleUtils.message("hybris.project.import.select.project.root.directory"),
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

    }

    @Override
    public void updateStep() {
        this.projectsRootChooser.setText(this.getBuilder().getFileToImport().replace('/', File.separatorChar));
        this.projectsRootChooser.getTextField().selectAll();
    }

    @Override
    public boolean validate() throws ConfigurationException {
        if (super.validate()) {
            this.getContext().setRootProjectDirectory(new File(this.projectsRootChooser.getText()));

            return !this.getContext()
                        .getHybrisProjectDescriptor()
                        .getFoundModules()
                        .isEmpty();
        }

        return false;
    }

    public AbstractHybrisProjectImportBuilder getContext() {
        return (AbstractHybrisProjectImportBuilder) this.getBuilder();
    }

}
