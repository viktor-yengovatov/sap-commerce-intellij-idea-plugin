package com.intellij.idea.plugin.hybris.project;

import com.intellij.idea.plugin.hybris.project.settings.HybrisImportParameters;
import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import com.intellij.projectImport.ProjectImportBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created 5:20 PM 13 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public abstract class AbstractHybrisProjectImportBuilder extends ProjectImportBuilder<HybrisModuleDescriptor> {

    public abstract void setRootProjectDirectory(@NotNull final File directory);

    @NotNull
    public abstract HybrisImportParameters getProjectImportParameters();

}
