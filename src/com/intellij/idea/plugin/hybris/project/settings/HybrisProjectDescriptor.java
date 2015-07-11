/*
 * Copyright 2015 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
public interface HybrisProjectDescriptor {

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

    void clear();

    @Nullable
    File getModulesFilesDirectory();

    void setModulesFilesDirectory(@Nullable File modulesFilesDirectory);

    void setRootDirectoryAndScanForModules(@NotNull File rootDirectory,
                                           @Nullable Processor<File> progressListenerProcessor,
                                           @Nullable Processor<List<File>> errorsProcessor);

    boolean isOpenProjectSettingsAfterImport();

    void setOpenProjectSettingsAfterImport(boolean openProjectSettingsAfterImport);
}
