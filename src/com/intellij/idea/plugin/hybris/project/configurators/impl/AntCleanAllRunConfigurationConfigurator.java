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

import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils;
import com.intellij.idea.plugin.hybris.project.configurators.RunConfigurationConfigurator;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisProjectDescriptor;
import com.intellij.lang.ant.config.AntBuildFile;
import com.intellij.lang.ant.config.AntBuildTarget;
import com.intellij.lang.ant.config.AntConfiguration;
import com.intellij.lang.ant.config.execution.AntRunConfiguration;
import com.intellij.lang.ant.config.execution.AntRunConfigurationType;
import com.intellij.lang.ant.config.impl.AntBeforeRunTask;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static com.intellij.idea.plugin.hybris.common.HybrisConstants.ANT_TASK_NAME_ALL;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.ANT_TASK_NAME_CLEAN;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.PLATFORM_EXTENSION_NAME;

public class AntCleanAllRunConfigurationConfigurator implements RunConfigurationConfigurator {

    @Override
    public void configure(
        final HybrisProjectDescriptor hybrisProjectDescriptor,
        @NotNull final Project project
    ) {

        final RunManagerImpl runManager = RunManagerImpl.getInstanceImpl(project);
        if (runManager == null) {
            return;
        }

        final AntRunConfigurationType antRunConfigurationType = ConfigurationTypeUtil.findConfigurationType(
            AntRunConfigurationType.class
        );

        final ConfigurationFactory configurationFactory = antRunConfigurationType.getConfigurationFactories()[0];
        final String configurationName = HybrisI18NBundleUtils.message(
            "hybris.project.import.run.configuration.ant.clean.all"
        );

        final RunnerAndConfigurationSettings runnerAndConfigurationSettings = runManager.createRunConfiguration(
            configurationName,
            configurationFactory
        );

        final AntConfiguration antConfiguration = AntConfiguration.getInstance(project);
        final AntBuildFile[] buildFiles = antConfiguration.getBuildFiles();
        final Optional<AntBuildFile> platformAntBuildFileOptional = Arrays
            .stream(buildFiles)
            .filter(buildFile -> PLATFORM_EXTENSION_NAME.equals(buildFile.getPresentableName()))
            .findFirst();

        if (!platformAntBuildFileOptional.isPresent()) {
            return;
        }

        final AntBuildFile platformAntBuildFile = platformAntBuildFileOptional.get();
        final AntBuildTarget antBuildTarget = platformAntBuildFile.getModel().findTarget(ANT_TASK_NAME_ALL);

        if (null == antBuildTarget || null == platformAntBuildFile.getVirtualFile()) {
            return;
        }

        final AntRunConfiguration antRunConfiguration = (AntRunConfiguration) runnerAndConfigurationSettings.getConfiguration();
        antRunConfiguration.acceptSettings(antBuildTarget);

        final AntBeforeRunTask antBeforeRunTask = new AntBeforeRunTask();
        antBeforeRunTask.setAntFileUrl(platformAntBuildFile.getVirtualFile().getUrl());
        antBeforeRunTask.setTargetName(ANT_TASK_NAME_CLEAN);
        antBeforeRunTask.setEnabled(true);

        runnerAndConfigurationSettings.setSingleton(true);
        runnerAndConfigurationSettings.setActivateToolWindowBeforeRun(true);

        runManager.addConfiguration(
            runnerAndConfigurationSettings,
            true,
            Collections.singletonList(antBeforeRunTask),
            false
        );
        runManager.setSelectedConfiguration(runnerAndConfigurationSettings);
    }
}
