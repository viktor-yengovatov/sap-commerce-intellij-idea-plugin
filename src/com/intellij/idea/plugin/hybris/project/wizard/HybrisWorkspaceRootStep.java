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
