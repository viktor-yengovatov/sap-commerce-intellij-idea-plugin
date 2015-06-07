package com.intellij.idea.plugin.hybris.project;

import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.idea.plugin.hybris.project.settings.ImportFromHybrisControl;
import com.intellij.openapi.externalSystem.model.DataNode;
import com.intellij.openapi.externalSystem.model.ProjectSystemId;
import com.intellij.openapi.externalSystem.model.project.ProjectData;
import com.intellij.openapi.externalSystem.service.project.manage.ProjectDataManager;
import com.intellij.openapi.externalSystem.service.project.wizard.AbstractExternalProjectImportBuilder;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;

/**
 * Created 8:58 PM 07 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisProjectImportBuilder extends AbstractExternalProjectImportBuilder<ImportFromHybrisControl> {

    public HybrisProjectImportBuilder(@NotNull final ProjectDataManager projectDataManager,
                                      @NotNull final ImportFromHybrisControl control,
                                      @NotNull final ProjectSystemId externalSystemId) {
        super(projectDataManager, control, externalSystemId);
    }

    @Override
    protected void doPrepare(@NotNull final WizardContext context) {

    }

    @Override
    protected void beforeCommit(@NotNull final DataNode<ProjectData> dataNode, @NotNull final Project project) {

    }

    @NotNull
    @Override
    protected File getExternalProjectConfigToUse(@NotNull final File file) {
        return null;
    }

    @Override
    protected void applyExtraSettings(@NotNull final WizardContext context) {

    }

    @NotNull
    @Override
    public String getName() {
        return null;
    }

    @Override
    public Icon getIcon() {
        return null;
    }
}
