package com.intellij.idea.plugin.hybris.project.wizard;

import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.idea.plugin.hybris.project.HybrisProjectImportBuilder;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VfsUtilCore;
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
            "title text", "", null, FileChooserDescriptorFactory.createSingleFolderDescriptor()
        );
    }

    @Override
    public JComponent getComponent() {
        return this.rootPanel;
    }

    @Override
    public void updateDataModel() {

    }

    public void updateStep() {
        this.projectsRootChooser.setText(this.getBuilder().getFileToImport().replace('/', File.separatorChar));
        this.projectsRootChooser.getTextField().selectAll();
    }

    public boolean validate() throws ConfigurationException {
        return super.validate() && this.getContext().setRootDirectory(this.projectsRootChooser.getText());
    }

    public HybrisProjectImportBuilder getContext() {
        return (HybrisProjectImportBuilder) this.getBuilder();
    }

}
