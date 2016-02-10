/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2015 Alexander Bartash <AlexanderBartash@gmail.com>
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

package com.intellij.idea.plugin.hybris.project.configurators;

import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.settings.HybrisApplicationSettingsComponent;
import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.vfs.VfsUtilCore;
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
    public void configure(@NotNull final ModifiableRootModel modifiableRootModel,
                          @NotNull final HybrisModuleDescriptor moduleDescriptor) {
        Validate.notNull(modifiableRootModel);
        Validate.notNull(moduleDescriptor);

        final CompilerModuleExtension compilerModuleExtension = modifiableRootModel.getModuleExtension(
            CompilerModuleExtension.class
        );

        final File outputDirectory;

        if (isJRebelOutputPath()) {

            final File webOutputDir = new File(moduleDescriptor.getRootDirectory(),
                                               HybrisConstants.WEB_COMPILER_OUTPUT_PATH);
            final File backofficeOutputDir = new File(moduleDescriptor.getRootDirectory(),
                                                      HybrisConstants.BACKOFFICE_COMPILER_OUTPUT_PATH);

            if (webOutputDir.exists()) {
                outputDirectory = webOutputDir;
            } else if (backofficeOutputDir.exists()) {
                outputDirectory = backofficeOutputDir;
            } else {
                outputDirectory = new File(moduleDescriptor.getRootDirectory(), HybrisConstants.JAVA_COMPILER_OUTPUT_PATH);
            }
        } else {
            outputDirectory = new File(moduleDescriptor.getRootDirectory(), HybrisConstants.COMPILER_OUTPUT_PATH);
        }

        compilerModuleExtension.setCompilerOutputPath(VfsUtilCore.pathToUrl(outputDirectory.getAbsolutePath()));
        compilerModuleExtension.setCompilerOutputPathForTests(VfsUtilCore.pathToUrl(outputDirectory.getAbsolutePath()));

        compilerModuleExtension.setExcludeOutput(true);
        compilerModuleExtension.inheritCompilerOutputPath(false);
    }

    private boolean isJRebelOutputPath() {
        return HybrisApplicationSettingsComponent.getInstance().getState().isJRebelOutputPath();
    }
}
