package com.intellij.idea.plugin.hybris.project;

import com.intellij.idea.plugin.hybris.project.settings.HybrisProjectImportParameters;
import com.intellij.projectImport.ProjectImportBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Created 5:20 PM 13 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public abstract class AbstractHybrisProjectImportBuilder extends ProjectImportBuilder<String> {

    public abstract void setRootProjectAbsolutePath(@NotNull final String rootProjectAbsolutePath);

    public abstract boolean isRootDirectorySet();

    @NotNull
    public abstract HybrisProjectImportParameters getProjectImportParameters();

}
