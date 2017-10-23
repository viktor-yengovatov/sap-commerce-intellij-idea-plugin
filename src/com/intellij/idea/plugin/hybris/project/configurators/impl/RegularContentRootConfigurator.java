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

package com.intellij.idea.plugin.hybris.project.configurators.impl;

import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import com.intellij.idea.plugin.hybris.project.configurators.ContentRootConfigurator;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptor;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.vfs.VfsUtil;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.java.JavaResourceRootType;
import org.jetbrains.jps.model.java.JavaSourceRootType;
import org.jetbrains.jps.model.java.JpsJavaExtensionService;

import java.io.File;
import java.util.Arrays;

import static com.intellij.idea.plugin.hybris.common.HybrisConstants.ACCELERATOR_ADDON_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.ACCELERATOR_STOREFRONT_COMMONS_EXTENSION_NAME;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.ADDON_SRC_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.BACK_OFFICE_MODULE_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.BOWER_COMPONENTS_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.CLASSES_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.COMMON_WEB_MODULE_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.COMMON_WEB_SRC_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.ECLIPSE_BIN_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.EXTERNAL_TOOL_BUILDERS_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.GEN_SRC_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.HAC_MODULE_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.HMC_MODULE_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.JS_TARGET_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.NODE_MODULES_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.PLATFORM_BOOTSTRAP_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.PLATFORM_MODEL_CLASSES_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.PLATFORM_TOMCAT_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.RESOURCES_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.SETTINGS_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.SRC_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.TEST_CLASSES_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.TEST_SRC_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.WEB_INF_CLASSES_DIRECTORY;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.WEB_MODULE_DIRECTORY;
import static com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptorType.CUSTOM;

/**
 * Created 2:07 AM 15 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class RegularContentRootConfigurator implements ContentRootConfigurator {

    @Override
    public void configure(
        @NotNull final ModifiableRootModel modifiableRootModel,
        @NotNull final HybrisModuleDescriptor moduleDescriptor
    ) {
        Validate.notNull(modifiableRootModel);
        Validate.notNull(moduleDescriptor);

        final ContentEntry contentEntry = modifiableRootModel.addContentEntry(VfsUtil.pathToUrl(
            moduleDescriptor.getRootDirectory().getAbsolutePath()
        ));


        this.configureCommonRoots(moduleDescriptor, contentEntry);
        if (moduleDescriptor.getRequiredExtensionNames().contains(HybrisConstants.HMC_EXTENSION_NAME)) {
            this.configureAdditionalRoots(
                moduleDescriptor,
                HMC_MODULE_DIRECTORY,
                contentEntry,
                moduleDescriptor.getRootDirectory()
            );
        }
        this.configureAdditionalRoots(
            moduleDescriptor,
            HAC_MODULE_DIRECTORY,
            contentEntry,
            moduleDescriptor.getRootDirectory()
        );
        this.configureWebRoots(moduleDescriptor, contentEntry, moduleDescriptor.getRootDirectory());
        this.configureCommonWebRoots(moduleDescriptor, contentEntry);
        this.configureAcceleratorAddonRoots(moduleDescriptor, contentEntry);
        this.configureBackOfficeRoots(moduleDescriptor, contentEntry);
        this.configurePlatformRoots(moduleDescriptor, contentEntry);
    }

    protected void configureCommonRoots(
        @NotNull final HybrisModuleDescriptor moduleDescriptor,
        @NotNull final ContentEntry contentEntry
    ) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(contentEntry);

        // https://hybris-integration.atlassian.net/browse/IIP-354
        // Do not register acceleratorstorefrontcommons/src as source root because it references not existent class
        // GeneratedAcceleratorstorefrontcommonsConstants and it breaks compilation from Intellij
        if (!ACCELERATOR_STOREFRONT_COMMONS_EXTENSION_NAME.equals(moduleDescriptor.getName())) {
            final File srcDirectory = new File(moduleDescriptor.getRootDirectory(), SRC_DIRECTORY);
            contentEntry.addSourceFolder(
                VfsUtil.pathToUrl(srcDirectory.getAbsolutePath()),
                JavaSourceRootType.SOURCE
            );
        }

        final File genSrcDirectory = new File(moduleDescriptor.getRootDirectory(), GEN_SRC_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(genSrcDirectory.getAbsolutePath()),
            JavaSourceRootType.SOURCE,
            JpsJavaExtensionService.getInstance().createSourceRootProperties("", true)
        );

        final File testSrcDirectory = new File(moduleDescriptor.getRootDirectory(), TEST_SRC_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(testSrcDirectory.getAbsolutePath()),
            JavaSourceRootType.TEST_SOURCE
        );

        final File resourcesDirectory = new File(moduleDescriptor.getRootDirectory(), RESOURCES_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(resourcesDirectory.getAbsolutePath()),
            JavaResourceRootType.RESOURCE
        );

        excludeCommonNeedlessDirs(contentEntry, moduleDescriptor);
    }

    protected void excludeCommonNeedlessDirs(
        final ContentEntry contentEntry,
        final HybrisModuleDescriptor moduleDescriptor
    ) {
        excludeSubDirectories(contentEntry, moduleDescriptor.getRootDirectory(), Arrays.asList(
            EXTERNAL_TOOL_BUILDERS_DIRECTORY,
            SETTINGS_DIRECTORY,
            CLASSES_DIRECTORY,
            TEST_CLASSES_DIRECTORY,
            ECLIPSE_BIN_DIRECTORY,
            NODE_MODULES_DIRECTORY,
            BOWER_COMPONENTS_DIRECTORY,
            JS_TARGET_DIRECTORY
        ));
    }

    private void excludeSubDirectories(
        @NotNull final ContentEntry contentEntry,
        @NotNull final File dir,
        @NotNull final Iterable<String> names
    ) {
        for (String subDirName : names) {
            excludeDirectory(contentEntry, new File(dir, subDirName));
        }
    }

    private static void excludeDirectory(@NotNull final ContentEntry contentEntry, @NotNull final File dir) {
        contentEntry.addExcludeFolder(VfsUtil.pathToUrl(dir.getAbsolutePath()));
    }

    protected void configureAdditionalRoots(
        @NotNull final HybrisModuleDescriptor moduleDescriptor,
        @NotNull final String directoryName,
        @NotNull final ContentEntry contentEntry,
        @NotNull final File parentDirectory
    ) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(directoryName);
        Validate.notNull(contentEntry);
        Validate.notNull(parentDirectory);

        final File additionalModuleDirectory = new File(parentDirectory, directoryName);
        if (!additionalModuleDirectory.exists() || additionalModuleDirectory.isFile()) {
            return;
        }

        final File additionalSrcDirectory = new File(additionalModuleDirectory, SRC_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(additionalSrcDirectory.getAbsolutePath()),
            JavaSourceRootType.SOURCE
        );

        final File additionalResourcesDirectory = new File(additionalModuleDirectory, RESOURCES_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(additionalResourcesDirectory.getAbsolutePath()),
            JavaResourceRootType.RESOURCE
        );
        excludeDirectory(contentEntry, new File(additionalModuleDirectory, CLASSES_DIRECTORY));
    }

    protected void configureWebRoots(
        @NotNull final HybrisModuleDescriptor moduleDescriptor,
        @NotNull final ContentEntry contentEntry,
        @NotNull final File parentDirectory
    ) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(contentEntry);
        Validate.notNull(parentDirectory);

        final File webModuleDirectory = new File(parentDirectory, WEB_MODULE_DIRECTORY);
        this.configureWebModuleRoots(moduleDescriptor, contentEntry, webModuleDirectory);
    }

    protected void configureCommonWebRoots(
        @NotNull final HybrisModuleDescriptor moduleDescriptor,
        @NotNull final ContentEntry contentEntry
    ) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(contentEntry);

        final File commonWebModuleDirectory = new File(
            moduleDescriptor.getRootDirectory(), COMMON_WEB_MODULE_DIRECTORY
        );

        this.configureWebModuleRoots(moduleDescriptor, contentEntry, commonWebModuleDirectory);
    }

    protected void configureAcceleratorAddonRoots(
        @NotNull final HybrisModuleDescriptor moduleDescriptor,
        @NotNull final ContentEntry contentEntry
    ) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(contentEntry);

        final File commonWebModuleDirectory = new File(
            moduleDescriptor.getRootDirectory(), ACCELERATOR_ADDON_DIRECTORY
        );
        this.configureWebRoots(moduleDescriptor, contentEntry, commonWebModuleDirectory);
        this.configureAdditionalRoots(moduleDescriptor, HMC_MODULE_DIRECTORY, contentEntry, commonWebModuleDirectory);
    }

    protected void configureBackOfficeRoots(
        @NotNull final HybrisModuleDescriptor moduleDescriptor,
        @NotNull final ContentEntry contentEntry
    ) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(contentEntry);

        final File backOfficeModuleDirectory = new File(
            moduleDescriptor.getRootDirectory(), BACK_OFFICE_MODULE_DIRECTORY
        );

        final File backOfficeSrcDirectory = new File(backOfficeModuleDirectory, SRC_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(backOfficeSrcDirectory.getAbsolutePath()),
            JavaSourceRootType.SOURCE
        );

        final File backOfficeTestSrcDirectory = new File(backOfficeModuleDirectory, TEST_SRC_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(backOfficeTestSrcDirectory.getAbsolutePath()),
            JavaSourceRootType.TEST_SOURCE
        );

        final File hmcResourcesDirectory = new File(backOfficeModuleDirectory, RESOURCES_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(hmcResourcesDirectory.getAbsolutePath()),
            JavaResourceRootType.RESOURCE
        );
        excludeDirectory(contentEntry, new File(backOfficeModuleDirectory, CLASSES_DIRECTORY));
    }

    protected void configurePlatformRoots(
        @NotNull final HybrisModuleDescriptor moduleDescriptor,
        @NotNull final ContentEntry contentEntry
    ) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(contentEntry);

        if (!HybrisConstants.PLATFORM_EXTENSION_NAME.equalsIgnoreCase(moduleDescriptor.getName())) {
            return;
        }
        final File rootDirectory = moduleDescriptor.getRootDirectory();
        final File platformBootstrapDirectory = new File(rootDirectory, PLATFORM_BOOTSTRAP_DIRECTORY);

        if (!moduleDescriptor.getRootProjectDescriptor().isImportOotbModulesInReadOnlyMode()) {

            final File platformBootstrapResourcesDirectory = new File(platformBootstrapDirectory, RESOURCES_DIRECTORY);
            contentEntry.addSourceFolder(
                VfsUtil.pathToUrl(platformBootstrapResourcesDirectory.getAbsolutePath()),
                JavaResourceRootType.RESOURCE
            );
        }

        excludeDirectory(contentEntry, new File(platformBootstrapDirectory, PLATFORM_MODEL_CLASSES_DIRECTORY));
        excludeDirectory(contentEntry, new File(rootDirectory, PLATFORM_TOMCAT_DIRECTORY));
        contentEntry.addExcludePattern("apache-ant-*");
    }

    protected void configureWebModuleRoots(
        @NotNull final HybrisModuleDescriptor moduleDescriptor,
        final @NotNull ContentEntry contentEntry,
        final File webModuleDirectory
    ) {
        Validate.notNull(moduleDescriptor);

        final File webSrcDirectory = new File(webModuleDirectory, SRC_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(webSrcDirectory.getAbsolutePath()),
            JavaSourceRootType.SOURCE
        );

        final File webGenSrcDirectory = new File(webModuleDirectory, GEN_SRC_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(webGenSrcDirectory.getAbsolutePath()),
            JavaSourceRootType.SOURCE,
            JpsJavaExtensionService.getInstance().createSourceRootProperties("", true)
        );

        final File webTestSrcDirectory = new File(webModuleDirectory, TEST_SRC_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(webTestSrcDirectory.getAbsolutePath()),
            JavaSourceRootType.TEST_SOURCE
        );

        excludeSubDirectories(contentEntry, webModuleDirectory, Arrays.asList(
            ADDON_SRC_DIRECTORY, TEST_CLASSES_DIRECTORY, COMMON_WEB_SRC_DIRECTORY
        ));

        configureWebInf(contentEntry, moduleDescriptor, webModuleDirectory);
    }

    protected void configureWebInf(
        final ContentEntry contentEntry,
        final HybrisModuleDescriptor moduleDescriptor,
        final File webModuleDirectory
    ) {
        final File rootDirectory = moduleDescriptor.getRootDirectory();

        if (moduleDescriptor.getDescriptorType() == CUSTOM) {
            excludeDirectory(contentEntry, new File(rootDirectory, WEB_INF_CLASSES_DIRECTORY));
        } else {
            final File webSrcDirectory = new File(webModuleDirectory, SRC_DIRECTORY);

            if (webSrcDirectory.exists()) {
                excludeDirectory(contentEntry, new File(rootDirectory, WEB_INF_CLASSES_DIRECTORY));
            }
        }
    }
}
