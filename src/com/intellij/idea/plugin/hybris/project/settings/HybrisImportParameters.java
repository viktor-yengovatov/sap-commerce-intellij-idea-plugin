package com.intellij.idea.plugin.hybris.project.settings;

import com.intellij.idea.plugin.hybris.project.utils.Processor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Created 1:48 PM 14 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public interface HybrisImportParameters {

    @Nullable
    Project getProject();

    @NotNull
    List<HybrisModuleDescriptor> getFoundModules();

    @NotNull
    List<HybrisModuleDescriptor> getModulesChosenForImport();

    void setModulesChosenForImport(@NotNull List<HybrisModuleDescriptor> moduleDescriptors);

    @NotNull
    Set<HybrisModuleDescriptor> getAlreadyOpenedModules();

    @Nullable
    File getRootDirectory();

    void setRootDirectoryAndScanForModules(@Nullable File rootDirectory,
                                           @Nullable Processor<File> progressListenerProcessor,
                                           @Nullable Processor<List<File>> errorsProcessor);

    boolean isOpenProjectSettingsAfterImport();

    void setOpenProjectSettingsAfterImport(boolean openProjectSettingsAfterImport);
}
