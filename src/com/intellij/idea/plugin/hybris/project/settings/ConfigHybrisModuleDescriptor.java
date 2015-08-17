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
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;
import java.util.List;
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
    public String getName() {
        return HybrisConstants.CONFIG_EXTENSION_NAME;
    }

    @NotNull
    @Override
    public Set<String> getRequiredExtensionNames() {
        return Collections.emptySet();
    }

    @NotNull
    @Override
    public List<JavaLibraryDescriptor> getLibraryDescriptors() {
        return Collections.<JavaLibraryDescriptor>singletonList(new DefaultJavaLibraryDescriptor(
            new File(this.getRootDirectory(), HybrisConstants.CONFIG_LICENCE_DIRECTORY), true
        ));
    }

    @Override
    public boolean isPreselected() {
        return getRelativePath().endsWith(File.separator+"config") && !getRelativePath().contains("bin"+File.separator);
    }
}
