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

import com.google.common.collect.Sets;
import com.intellij.idea.plugin.hybris.project.exceptions.HybrisConfigurationException;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created 3:55 PM 13 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class PlatformHybrisModuleDescriptor extends AbstractHybrisModuleDescriptor {

    public PlatformHybrisModuleDescriptor(@NotNull final File moduleRootDirectory,
                                          @NotNull final HybrisProjectDescriptor rootProjectDescriptor
    ) throws HybrisConfigurationException {
        super(moduleRootDirectory, rootProjectDescriptor);
    }

    @NotNull
    @Override
    public String getName() {
        return HybrisConstants.PLATFORM_EXTENSION_NAME;
    }

    @NotNull
    @Override
    public Set<String> getRequiredExtensionNames() {
        final File extDirectory = new File(this.getRootDirectory(), HybrisConstants.PLATFORM_EXTENSIONS_DIRECTORY_NAME);

        final Set<String> platformDependencies = Sets.newHashSet(
            HybrisConstants.CONFIG_EXTENSION_NAME
        );

        if (extDirectory.isDirectory()) {
            final File[] files = extDirectory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);

            if (null != files) {
                for (File file : files) {
                    platformDependencies.add(file.getName());
                }
            }
        }

        return Collections.unmodifiableSet(platformDependencies);
    }

    @NotNull
    @Override
    public List<JavaLibraryDescriptor> getLibraryDescriptors() {
        final List<JavaLibraryDescriptor> moduleDescriptors = new ArrayList<JavaLibraryDescriptor>();

        final File resourcesDirectory = new File(this.getRootDirectory(), HybrisConstants.RESOURCES_DIRECTORY);
        final File[] resourcesInnerDirectories = resourcesDirectory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);

        for (File resourcesInnerDirectory : resourcesInnerDirectories) {

            moduleDescriptors.add(new DefaultJavaLibraryDescriptor(
                new File(resourcesInnerDirectory, HybrisConstants.LIB_DIRECTORY), true
            ));

            moduleDescriptors.add(new DefaultJavaLibraryDescriptor(
                new File(resourcesInnerDirectory, HybrisConstants.BIN_DIRECTORY), true
            ));
        }

        moduleDescriptors.add(new DefaultJavaLibraryDescriptor(
            new File(getRootDirectory(), HybrisConstants.PL_BOOTSTRAP_LIB_DIRECTORY), true
        ));

        moduleDescriptors.add(new DefaultJavaLibraryDescriptor(
            new File(getRootDirectory(), HybrisConstants.PL_TOMCAT_BIN_DIRECTORY)
        ));

        moduleDescriptors.add(new DefaultJavaLibraryDescriptor(
            new File(getRootDirectory(), HybrisConstants.PL_TOMCAT_LIB_DIRECTORY), true
        ));

        return Collections.unmodifiableList(moduleDescriptors);
    }

    @Override
    public boolean isPreselected() {
        return true;
    }
}
