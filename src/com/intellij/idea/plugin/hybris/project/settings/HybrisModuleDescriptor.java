package com.intellij.idea.plugin.hybris.project.settings;

import com.intellij.openapi.roots.ModifiableRootModel;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Set;

/**
 * Created 1:20 PM 14 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public interface HybrisModuleDescriptor extends Comparable<HybrisModuleDescriptor> {

    @NotNull
    String getModuleName();

    @NotNull
    File getModuleRootDirectory();

    @NotNull
    String getModuleRelativePath();

    @NotNull
    HybrisProjectDescriptor getRootProjectDescriptor();

    @NotNull
    File getIdeaModuleFile();

    @NotNull
    Set<String> getRequiredExtensionNames();

    @NotNull
    Set<HybrisModuleDescriptor> getDependenciesTree();

    void setDependenciesTree(@NotNull Set<HybrisModuleDescriptor> moduleDescriptors);

    @NotNull
    Set<HybrisModuleDescriptor> getDependenciesPlainList();

    void loadLibs(@NotNull final ModifiableRootModel modifiableRootModel);

}
