package com.intellij.idea.plugin.hybris.project;

import com.intellij.idea.plugin.hybris.util.HybrisI18NBundle;
import com.intellij.idea.plugin.hybris.util.HybrisIcons;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.packaging.artifacts.ModifiableArtifactModel;
import com.intellij.projectImport.ProjectImportBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * Created 8:58 PM 07 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisProjectImportBuilder extends ProjectImportBuilder<String> {

    @NotNull
    @Override
    public String getName() {
        return HybrisI18NBundle.message("hybris.project.name");
    }

    @Override
    public Icon getIcon() {
        return HybrisIcons.HYBRIS_ICON;
    }

    @Override
    public List<String> getList() {
        return null;
    }

    @Override
    public boolean isMarked(final String element) {
        return false;
    }

    @Override
    public void setList(final List<String> list) throws ConfigurationException {

    }

    @Override
    public void setOpenProjectSettingsAfter(final boolean on) {

    }

    @Nullable
    @Override
    public List<Module> commit(final Project project,
                               final ModifiableModuleModel model,
                               final ModulesProvider modulesProvider,
                               final ModifiableArtifactModel artifactModel) {
        return null;
    }
}
