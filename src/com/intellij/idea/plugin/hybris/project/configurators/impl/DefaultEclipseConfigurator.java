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

import com.intellij.idea.plugin.hybris.project.configurators.EclipseConfigurator;
import com.intellij.idea.plugin.hybris.project.descriptors.EclipseModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisProjectDescriptor;
import com.intellij.openapi.application.AccessToken;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.idea.eclipse.importWizard.EclipseImportBuilder;

import java.util.List;
import java.util.stream.Collectors;

import static com.intellij.util.ui.UIUtil.invokeAndWaitIfNeeded;

/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 15/11/16.
 */
public class DefaultEclipseConfigurator implements EclipseConfigurator {

    @Override
    public void configure(
        @NotNull final HybrisProjectDescriptor hybrisProjectDescriptor,
        @NotNull final Project project,
        @NotNull final List<EclipseModuleDescriptor> eclipseModules,
        @NotNull final String[] eclipseRootGroup
    ) {
        if (eclipseModules.isEmpty()) {
            return;
        }
        final EclipseImportBuilder eclipseImportBuilder = new EclipseImportBuilder();
        final List<String> projectList = eclipseModules
            .stream()
            .map(e -> e.getRootDirectory())
            .map(e -> e.getPath())
            .collect(Collectors.toList());
        eclipseImportBuilder.getParameters().converterOptions.commonModulesDirectory=
            hybrisProjectDescriptor.getModulesFilesDirectory().getPath();
        eclipseImportBuilder.setList(projectList);
        invokeAndWaitIfNeeded((Runnable) () -> {
            final List<Module> newRootModules = eclipseImportBuilder.commit(project);
            if (eclipseRootGroup != null && eclipseRootGroup.length > 0) {
                moveEclipseModulesToGroup(project, newRootModules, eclipseRootGroup);
            }}
        );

    }

    private void moveEclipseModulesToGroup(
        @NotNull final Project project,
        @NotNull final List<Module> eclipseModules,
        @NotNull final String[] eclipseRootGroup
    ) {
        final ModifiableModuleModel modifiableModuleModel = ModuleManager.getInstance(project).getModifiableModel();

        for (Module module: eclipseModules) {
            final String[] groupPath = modifiableModuleModel.getModuleGroupPath(module);
            modifiableModuleModel.setModuleGroupPath(module, ArrayUtils.addAll(eclipseRootGroup, groupPath));
        }
        AccessToken token = null;
        try {
            token = ApplicationManager.getApplication().acquireWriteActionLock(getClass());
            modifiableModuleModel.commit();
        } finally {
            token.finish();
        }
    }
}
