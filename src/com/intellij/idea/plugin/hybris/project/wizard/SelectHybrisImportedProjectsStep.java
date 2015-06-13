package com.intellij.idea.plugin.hybris.project.wizard;

import com.intellij.icons.AllIcons;
import com.intellij.ide.util.ElementsChooser;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.idea.plugin.hybris.project.AbstractHybrisProjectImportBuilder;
import com.intellij.idea.plugin.hybris.project.settings.HybrisProjectImportParameters;
import com.intellij.idea.plugin.hybris.project.utils.HybrisProjectUtils;
import com.intellij.idea.plugin.hybris.utils.VirtualFileSystemUtils;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.projectImport.SelectImportedProjectsStep;
import com.intellij.util.ArrayUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
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

            @Override
            public void elementMarkChanged(final String element, final boolean isMarked) {
                duplicateNames = null;
                fileChooser.repaint();
            }
        });
    }

    private boolean isInConflict(final String item) {
        this.calcDuplicates();

        return this.fileChooser.getMarkedElements().contains(item)
            && this.duplicateNames.contains(HybrisProjectUtils.findProjectName(item));
    }

    private void calcDuplicates() {
        if (null == this.duplicateNames) {
            this.duplicateNames = new HashSet<String>();

            final Collection<String> usedNames = new HashSet<String>();

            for (String model : this.fileChooser.getMarkedElements()) {

                final String projectName = HybrisProjectUtils.findProjectName(model);

                if (!usedNames.add(projectName)) {
                    this.duplicateNames.add(projectName);
                }
            }
        }
    }

    @Override
    public AbstractHybrisProjectImportBuilder getContext() {
        return (AbstractHybrisProjectImportBuilder) this.getBuilder();
    }

    @Override
    protected String getElementText(final String item) {
        final StringBuilder stringBuilder = new StringBuilder();

        final String projectName = HybrisProjectUtils.findProjectName(item);
        stringBuilder.append(projectName);

        final HybrisProjectImportParameters projectImportParameters = this.getContext().getProjectImportParameters();

        final String relPath = VirtualFileSystemUtils.getRelative(projectImportParameters.getRoot(), item);

        if (null == projectImportParameters.getProjectsPathsToConvert()) {
            projectImportParameters.setProjectsPathsToConvert(new ArrayList<String>());
        }

        if (!projectImportParameters.getProjectsPathsToConvert().contains(item)) {
            projectImportParameters.getProjectsPathsToConvert().add(item);
        }

        if (!relPath.equals(".") && !relPath.equals(projectName)) {
            stringBuilder.append(" (").append(relPath).append(')');
        }

        return stringBuilder.toString();
    }

    @Override
    @Nullable
    protected Icon getElementIcon(final String item) {
        return this.isInConflict(item) ? AllIcons.Actions.Cancel : null;
    }

    @Override
    public void updateStep() {
        super.updateStep();

        this.duplicateNames = null;
    }

    @Override
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
