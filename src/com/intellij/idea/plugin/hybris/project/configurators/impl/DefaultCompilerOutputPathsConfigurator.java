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
import com.intellij.idea.plugin.hybris.project.configurators.CompilerOutputPathsConfigurator;
import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.vfs.VfsUtilCore;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created 12:18 AM 25 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class DefaultCompilerOutputPathsConfigurator implements CompilerOutputPathsConfigurator {

    @Override
    public void configure(
        @NotNull final ModifiableRootModel modifiableRootModel,
        @NotNull final HybrisModuleDescriptor moduleDescriptor
    ) {
        Validate.notNull(modifiableRootModel);
        Validate.notNull(moduleDescriptor);

        final CompilerModuleExtension compilerModuleExtension = modifiableRootModel.getModuleExtension(
            CompilerModuleExtension.class
        );

        final File outputDirectory = this.getCompilerOutputPath(moduleDescriptor);

        compilerModuleExtension.setCompilerOutputPath(VfsUtilCore.pathToUrl(outputDirectory.getAbsolutePath()));
        compilerModuleExtension.setCompilerOutputPathForTests(VfsUtilCore.pathToUrl(outputDirectory.getAbsolutePath()));

        compilerModuleExtension.setExcludeOutput(true);
        compilerModuleExtension.inheritCompilerOutputPath(false);
    }

    @NotNull
    protected File getCompilerOutputPath(final @NotNull HybrisModuleDescriptor moduleDescriptor) {
        Validate.notNull(moduleDescriptor);

        final File webOutputPath = this.getWebCompilerOutputPath(moduleDescriptor);

        if (webOutputPath.isDirectory()) {
            if (this.shouldUseFakeOutputPath(moduleDescriptor)) {
                return this.getFakeWebCompilerOutputPath(moduleDescriptor);
            }

            return webOutputPath;
        }

        final File backofficeOutputPath = this.getBackofficeCompilerOutputPath(moduleDescriptor);

        if (backofficeOutputPath.isDirectory()) {
            if (this.shouldUseFakeOutputPath(moduleDescriptor)) {
                return this.getFakeBackofficeCompilerOutputPath(moduleDescriptor);
            }

            return backofficeOutputPath;
        }

        if (this.shouldUseFakeOutputPath(moduleDescriptor)) {
            return this.getFakeJavaCompilerOutputPath(moduleDescriptor);
        }

        return this.getJavaCompilerOutputPath(moduleDescriptor);
    }

    /**
     * When we configure IDEA's output paths matching Hybris output paths IDEA can not compile the project cleanly
     * probably because of some weird issue with caching or indexing inside IDEA. But if we specify separate output
     * paths for IDEA everything works fine. At the same time we need IDEA's output paths be matching Hybris output
     * paths to support jRebel. Since users modify only their custom modules we can use fake output paths for all OOTB
     * extensions.
     *
     * @param moduleDescriptor {@link HybrisModuleDescriptor} of the module.
     *
     * @return {@code true} if fake output paths should be used for the given module, otherwise {@code false}.
     */
    protected boolean shouldUseFakeOutputPath(final @NotNull HybrisModuleDescriptor moduleDescriptor) {
        Validate.notNull(moduleDescriptor);

        final Boolean readOnlyMode = BooleanUtils.isTrue(
            moduleDescriptor.getRootProjectDescriptor().isImportOotbModulesInReadOnlyMode()
        );

        return readOnlyMode && !moduleDescriptor.isInCustomDir();
    }

    @NotNull
    protected File getJavaCompilerOutputPath(final @NotNull HybrisModuleDescriptor moduleDescriptor) {
        Validate.notNull(moduleDescriptor);

        return new File(
            moduleDescriptor.getRootDirectory(),
            HybrisConstants.JAVA_COMPILER_OUTPUT_PATH
        );
    }

    @NotNull
    protected File getFakeJavaCompilerOutputPath(final @NotNull HybrisModuleDescriptor moduleDescriptor) {
        Validate.notNull(moduleDescriptor);

        return new File(
            moduleDescriptor.getRootDirectory(),
            HybrisConstants.JAVA_COMPILER_FAKE_OUTPUT_PATH
        );
    }

    @NotNull
    protected File getBackofficeCompilerOutputPath(final @NotNull HybrisModuleDescriptor moduleDescriptor) {
        Validate.notNull(moduleDescriptor);

        return new File(
            moduleDescriptor.getRootDirectory(),
            HybrisConstants.BACKOFFICE_COMPILER_OUTPUT_PATH
        );
    }

    @NotNull
    protected File getFakeBackofficeCompilerOutputPath(final @NotNull HybrisModuleDescriptor moduleDescriptor) {
        Validate.notNull(moduleDescriptor);

        return new File(
            moduleDescriptor.getRootDirectory(),
            HybrisConstants.BACKOFFICE_COMPILER_FAKE_OUTPUT_PATH
        );
    }

    @NotNull
    protected File getWebCompilerOutputPath(final @NotNull HybrisModuleDescriptor moduleDescriptor) {
        Validate.notNull(moduleDescriptor);

        return new File(
            moduleDescriptor.getRootDirectory(),
            HybrisConstants.WEB_COMPILER_OUTPUT_PATH
        );
    }

    @NotNull
    protected File getFakeWebCompilerOutputPath(final @NotNull HybrisModuleDescriptor moduleDescriptor) {
        Validate.notNull(moduleDescriptor);

        return new File(
            moduleDescriptor.getRootDirectory(),
            HybrisConstants.WEB_COMPILER_FAKE_OUTPUT_PATH
        );
    }
}
