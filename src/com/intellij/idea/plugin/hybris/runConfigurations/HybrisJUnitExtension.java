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

package com.intellij.idea.plugin.hybris.runConfigurations;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.RunConfigurationExtension;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.ParametersList;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.junit.JUnitConfiguration;
import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptorType;
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettings;
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettingsComponent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * @author Eugene.Kudelevsky
 */
public class HybrisJUnitExtension extends RunConfigurationExtension {

    private static final String HYBRIS_DATA_DIR_ENV = "HYBRIS_DATA_DIR";

    @Override
    public <T extends RunConfigurationBase> void updateJavaParameters(
        final T configuration, final JavaParameters params, final RunnerSettings runnerSettings
    ) throws ExecutionException {
        if (runnerSettings != null || !isApplicableFor(configuration)) {
            return;
        }
        final Project project = configuration.getProject();
        final ParametersList vmParameters = params.getVMParametersList();

        if (!vmParameters.hasParameter("-ea")) {
            vmParameters.add("-ea");
        }
        if (vmParameters.getParameters().stream().noneMatch(param -> param.startsWith("-Dplatformhome="))) {
            final VirtualFile platformRootDirectory = findPlatformRootDirectory(project);

            if (platformRootDirectory != null) {
                vmParameters.add("-Dplatformhome=" + platformRootDirectory.getPath());
            }
        }
        if (!params.getEnv().containsKey(HYBRIS_DATA_DIR_ENV)) {
            final HybrisProjectSettings settings = HybrisProjectSettingsComponent.getInstance(project).getState();

            final String hybrisDataDirPath = FileUtil.toCanonicalPath(
                project.getBasePath() + '/' + settings.getHybrisDirectory() + "/data");

            if (hybrisDataDirPath != null) {
                params.addEnv(HYBRIS_DATA_DIR_ENV, hybrisDataDirPath);
            }
        }
    }

    @Nullable
    private static VirtualFile findPlatformRootDirectory(@NotNull final Project project) {
        final Module platformModule =
            Arrays.stream(ModuleManager.getInstance(project).getModules())
                  .filter(module -> HybrisModuleDescriptor.getDescriptorType(module) == HybrisModuleDescriptorType.PLATFORM)
                  .findAny()
                  .orElse(null);

        return platformModule == null ? null
            : Arrays.stream(ModuleRootManager.getInstance(platformModule).getContentRoots())
                    .filter(vFile -> vFile.findChild(HybrisConstants.EXTENSIONS_XML) != null)
                    .findAny()
                    .orElse(null);
    }

    @Override
    protected boolean isApplicableFor(@NotNull final RunConfigurationBase configuration) {
        if (!(configuration instanceof JUnitConfiguration)) {
            return false;
        }
        final Project project = configuration.getProject();
        final HybrisProjectSettings settings = HybrisProjectSettingsComponent.getInstance(project).getState();
        return settings.isHybrisProject();
    }
}
