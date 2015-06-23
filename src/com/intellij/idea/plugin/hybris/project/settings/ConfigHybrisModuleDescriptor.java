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

import com.intellij.idea.plugin.hybris.project.exceptions.HybrisConfigurationException;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import com.intellij.idea.plugin.hybris.utils.LibUtils;
import com.intellij.openapi.roots.ModifiableRootModel;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;
import java.util.Set;

/**
 * Created 3:55 PM 13 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ConfigHybrisModuleDescriptor extends AbstractHybrisModuleDescriptor {

    public ConfigHybrisModuleDescriptor(@NotNull final File moduleRootDirectory,
                                        @NotNull final HybrisProjectDescriptor rootProjectDescriptor
    ) throws HybrisConfigurationException {
        super(moduleRootDirectory, rootProjectDescriptor);
    }

    @NotNull
    @Override
    public String getModuleName() {
        return HybrisConstants.CONFIG_EXTENSION_NAME;
    }

    @NotNull
    @Override
    public Set<String> getRequiredExtensionNames() {
        return Collections.emptySet();
    }

    @Override
    public void loadLibs(@NotNull final ModifiableRootModel modifiableRootModel) {
        final File configLicenceDirectory = new File(
            getModuleRootDirectory(), HybrisConstants.CONFIG_LICENCE_DIRECTORY
        );

        LibUtils.addJarFolderToModuleLibs(modifiableRootModel, configLicenceDirectory, true);
    }
}
