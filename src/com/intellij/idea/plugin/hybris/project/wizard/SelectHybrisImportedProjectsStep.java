package com.intellij.idea.plugin.hybris.project.wizard;

import com.intellij.icons.AllIcons;
import com.intellij.ide.util.ElementsChooser;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.idea.plugin.hybris.project.HybrisProjectImportBuilder;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.projectImport.SelectImportedProjectsStep;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Vlad Bozhenok <vladbozhenok@gmail.com>
 */
public class SelectHybrisImportedProjectsStep extends SelectImportedProjectsStep<String> {

    Set<String> duplicateNames;

    public SelectHybrisImportedProjectsStep(WizardContext context) {
        super(context);
        fileChooser.addElementsMarkListener(new ElementsChooser.ElementsMarkListener<String>() {
            public void elementMarkChanged(final String element, final boolean isMarked) {
                duplicateNames = null;
                fileChooser.repaint();
            }
        });
    }

    private boolean isInConflict(final String item) {
        calcDuplicates();
        return fileChooser.getMarkedElements().contains(item) && duplicateNames.contains("conflict_name");
//        return fileChooser.getMarkedElements().contains(item) && duplicateNames.contains(EclipseProjectFinder.findProjectName(item));
    }

    private void calcDuplicates() {
        if (duplicateNames == null) {
            duplicateNames = new HashSet<String>();
            Set<String> usedNames = new HashSet<String>();
            for (String model : fileChooser.getMarkedElements()) {
//                final String projectName = EclipseProjectFinder.findProjectName(model);
                final String projectName = "conflict_name2";
                if (!usedNames.add(projectName)) {
                    duplicateNames.add(projectName);
                }
            }
        }
    }

    public HybrisProjectImportBuilder getContext() {
        return (HybrisProjectImportBuilder)getBuilder();
    }

    protected String getElementText(final String item) {
        StringBuilder stringBuilder = new StringBuilder();
//        final String projectName = EclipseProjectFinder.findProjectName(item);
        final String projectName = item;
        stringBuilder.append(projectName);
//        String relPath = PathUtil.getRelative(((HybrisProjectImportBuilder) getBuilder()).getParameters().root, item);
        if(!getContext().getParameters().projectsToConvert.contains(item)){
            getContext().getParameters().projectsToConvert.add(item);
        }

        String relPath=item+" <- REL_PATH";
        if (!relPath.equals(".") && !relPath.equals(projectName)) {
            stringBuilder.append(" (").append(relPath).append(")");
        }
        return stringBuilder.toString();
    }

    @Nullable
    protected Icon getElementIcon(final String item) {
        return isInConflict(item) ? AllIcons.Actions.Cancel : null;
    }

    @Override
    public void updateStep() {
        super.updateStep();
        duplicateNames = null;
    }

    public boolean validate() throws ConfigurationException {
//        calcDuplicates();
//        if (!duplicateNames.isEmpty()) {
//            throw new ConfigurationException("Duplicate names found:" + StringUtil.join(ArrayUtil.toStringArray(duplicateNames), ","), "Unable to proceed");
//        }
        return super.validate();
    }

}
