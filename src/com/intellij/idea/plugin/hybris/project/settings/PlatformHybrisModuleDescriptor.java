/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2016 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.intellij.idea.plugin.hybris.project.settings;

import com.google.common.collect.Sets;
import com.intellij.idea.plugin.hybris.project.exceptions.HybrisConfigurationException;
import com.intellij.idea.plugin.hybris.common.HybrisConstants;
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
            new File(getRootDirectory(), HybrisConstants.PL_BOOTSTRAP_LIB_DIRECTORY),
            new File(getRootDirectory(), HybrisConstants.PL_BOOTSTRAP_GEN_SRC_DIRECTORY),
            true
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
