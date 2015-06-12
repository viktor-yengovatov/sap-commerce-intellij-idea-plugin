package com.intellij.idea.plugin.hybris.project.wizard;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.idea.plugin.hybris.project.HybrisProjectImportBuilder;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.projectImport.ProjectImportWizardStep;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vlad Bozhenok <vladbozhenok@gmail.com>
 */
public class HybrisWorkspaceRootStep extends ProjectImportWizardStep {

    private JPanel rootPanel;
    private TextFieldWithBrowseButton projectsRootChooser;
    private HybrisProjectImportBuilder.Parameters parameters;

    public HybrisWorkspaceRootStep(final WizardContext context) {
        super(context);
        projectsRootChooser.addBrowseFolderListener("title text", "", null,
                FileChooserDescriptorFactory.createSingleFolderDescriptor());
//        final List<String> roots = new ArrayList<String>();
//        roots.add("root item");
//        getParameters().workspace = roots;
    }

    @Override
    public JComponent getComponent() {
        return this.rootPanel;
    }

    @Override
    public void updateDataModel() {

    }

    public void updateStep() {
        String path = getBuilder().getFileToImport();
//        if (path == null) {
//            if (getWizardContext().isProjectFileDirectorySet() || !PropertiesComponent.getInstance().isValueSet(_ECLIPSE_PROJECT_DIR)) {
//                path = getWizardContext().getProjectFileDirectory();
//            }
//            else {
//                path = PropertiesComponent.getInstance().getValue(_ECLIPSE_PROJECT_DIR);
//            }
//        }
        projectsRootChooser.setText(path.replace('/', File.separatorChar));
        projectsRootChooser.getTextField().selectAll();
    }

    public boolean validate() throws ConfigurationException {
        return super.validate() && getContext().setRootDirectory(projectsRootChooser.getText());
    }

    public HybrisProjectImportBuilder getContext() {
        return (HybrisProjectImportBuilder)getBuilder();
    }

    public HybrisProjectImportBuilder.Parameters getParameters() {
        if (parameters == null) {
            parameters = ((HybrisProjectImportBuilder)getBuilder()).getParameters();
        }
        return parameters;
    }

}