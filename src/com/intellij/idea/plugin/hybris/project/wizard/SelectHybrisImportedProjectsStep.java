package com.intellij.idea.plugin.hybris.project.wizard;

import com.intellij.icons.AllIcons;
import com.intellij.ide.util.ElementsChooser;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.idea.plugin.hybris.project.HybrisProjectImportBuilder;
import com.intellij.idea.plugin.hybris.project.utils.HybrisProjectFinderUtils;
import com.intellij.idea.plugin.hybris.utils.VirtualFileSystemUtils;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.projectImport.SelectImportedProjectsStep;
import com.intellij.util.ArrayUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Vlad Bozhenok <vladbozhenok@gmail.com>
 */
public class SelectHybrisImportedProjectsStep extends SelectImportedProjectsStep<String> {

    protected Set<String> duplicateNames;

    public SelectHybrisImportedProjectsStep(final WizardContext context) {
        super(context);

        this.fileChooser.addElementsMarkListener(new ElementsChooser.ElementsMarkListener<String>() {

            public void elementMarkChanged(final String element, final boolean isMarked) {
                duplicateNames = null;
                fileChooser.repaint();
            }
        });
    }

    private boolean isInConflict(final String item) {
        this.calcDuplicates();

        return this.fileChooser.getMarkedElements().contains(item)
            && this.duplicateNames.contains(HybrisProjectFinderUtils.findProjectName(item));
    }

    private void calcDuplicates() {
        if (null == this.duplicateNames) {
            this.duplicateNames = new HashSet<String>();

            final Collection<String> usedNames = new HashSet<String>();

            for (String model : this.fileChooser.getMarkedElements()) {

                final String projectName = HybrisProjectFinderUtils.findProjectName(model);

                if (!usedNames.add(projectName)) {
                    this.duplicateNames.add(projectName);
                }
            }
        }
    }

    public HybrisProjectImportBuilder getContext() {
        return (HybrisProjectImportBuilder) this.getBuilder();
    }

    protected String getElementText(final String item) {
        final StringBuilder stringBuilder = new StringBuilder();

        final String projectName = HybrisProjectFinderUtils.findProjectName(item);
        stringBuilder.append(projectName);

        final String relPath = VirtualFileSystemUtils.getRelative(
            ((HybrisProjectImportBuilder) this.getBuilder()).getParameters().root, item
        );

        if (!this.getContext().getParameters().projectsToConvert.contains(item)) {
            this.getContext().getParameters().projectsToConvert.add(item);
        }

        if (!relPath.equals(".") && !relPath.equals(projectName)) {
            stringBuilder.append(" (").append(relPath).append(")");
        }
        return stringBuilder.toString();
    }

    @Nullable
    protected Icon getElementIcon(final String item) {
        return this.isInConflict(item) ? AllIcons.Actions.Cancel : null;
    }

    @Override
    public void updateStep() {
        super.updateStep();

        this.duplicateNames = null;
    }

    public boolean validate() throws ConfigurationException {
        this.calcDuplicates();

        if (!this.duplicateNames.isEmpty()) {
            throw new ConfigurationException(
                "Duplicate names found:" + StringUtil.join(ArrayUtil.toStringArray(this.duplicateNames), ","),
                "Unable to proceed"
            );
        }

        return super.validate();
    }
}
