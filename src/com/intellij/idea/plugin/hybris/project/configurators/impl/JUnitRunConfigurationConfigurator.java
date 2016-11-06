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
import com.intellij.execution.junit.JUnitConfiguration;
import com.intellij.execution.junit.JUnitConfigurationType;
import com.intellij.idea.plugin.hybris.project.configurators.RunConfigurationConfigurator;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisProjectDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.PlatformHybrisModuleDescriptor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;

/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 17/10/2016.
 */
public class JUnitRunConfigurationConfigurator implements RunConfigurationConfigurator {

    @Override
    public void configure(@NotNull final HybrisProjectDescriptor hybrisProjectDescriptor, @NotNull final Project project) {

        final RunManagerImpl runManager = RunManagerImpl.getInstanceImpl(project);
        if (runManager == null) {
            return;
        }

        configureJUnitTemplateOnly(runManager, hybrisProjectDescriptor);
    }

    private void configureJUnitTemplateOnly(final RunManagerImpl runManager, final HybrisProjectDescriptor hybrisProjectDescriptor) {
        final String platformRootDirectoryPath = getPlatformRootDirectoryPath(hybrisProjectDescriptor);
        if (platformRootDirectoryPath == null) {
            return;
        }
        final JUnitConfigurationType configurationType = ConfigurationTypeUtil.findConfigurationType(JUnitConfigurationType.class);
        final ConfigurationFactory configurationFactory = configurationType.getConfigurationFactories()[0];
        final RunnerAndConfigurationSettings template = runManager.getConfigurationTemplate(configurationFactory);
        final JUnitConfiguration runConfiguration = (JUnitConfiguration) template.getConfiguration();
        runConfiguration.setVMParameters("-ea -Dplatformhome=" + platformRootDirectoryPath);
        runManager.setBeforeRunTasks(runConfiguration, Collections.emptyList(), false);
    }

    private String getPlatformRootDirectoryPath(final HybrisProjectDescriptor hybrisProjectDescriptor) {
        return hybrisProjectDescriptor
            .getModulesChosenForImport()
            .stream()
            .filter(e -> e instanceof PlatformHybrisModuleDescriptor)
            .map(HybrisModuleDescriptor::getRootDirectory)
            .map(File::getPath)
            .findAny()
            .orElse(null);
    }
}
