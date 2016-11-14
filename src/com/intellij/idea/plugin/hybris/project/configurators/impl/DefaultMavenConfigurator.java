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

import com.intellij.idea.plugin.hybris.project.configurators.MavenConfigurator;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisProjectDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.MavenModuleDescriptor;
import com.intellij.openapi.application.AccessToken;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.idea.maven.model.MavenConstants;
import org.jetbrains.idea.maven.model.MavenExplicitProfiles;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectsManager;
import org.jetbrains.idea.maven.wizards.MavenProjectBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.intellij.util.ui.UIUtil.invokeAndWaitIfNeeded;

/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 13/11/16.
 */
public class DefaultMavenConfigurator implements MavenConfigurator {

    private List<Module> originalModules;
    @Override
    public void configure(
        @NotNull final HybrisProjectDescriptor hybrisProjectDescriptor,
        @NotNull final Project project,
        @NotNull final List<MavenModuleDescriptor> mavenModules
    ) {
        if (mavenModules.isEmpty()) {
            return;
        }
        originalModules = Arrays.asList(ModuleManager.getInstance(project).getModules());

        final MavenProjectBuilder mavenProjectBuilder = new MavenProjectBuilder();
        final List<VirtualFile> pomList = mavenModules
            .stream()
            .map(e -> new File(e.getRootDirectory(), MavenConstants.POM_XML))
            .map(e -> VfsUtil.findFileByIoFile(e, true))
            .collect(Collectors.toList());
        mavenProjectBuilder.setFiles(pomList);

        if (!mavenProjectBuilder.setSelectedProfiles(MavenExplicitProfiles.NONE)) {
            return;
        }

        List<MavenProject> selectedProjects = new ArrayList<>();
        for (MavenProject mavenProject: mavenProjectBuilder.getList()) {
            final Optional<MavenModuleDescriptor> isPresent = mavenModules
                .stream()
                .filter(e -> e.getRootDirectory().getAbsolutePath().equals(mavenProject.getDirectory()))
                .findAny();
            if (isPresent.isPresent()) {
                selectedProjects.add(mavenProject);
            }
        }
        mavenProjectBuilder.setList(selectedProjects);
        invokeAndWaitIfNeeded((Runnable) () -> mavenProjectBuilder.commit(project));
    }


    @Override
    public void configurePostStartup(
        @NotNull final Project project,
        @NotNull final List<MavenModuleDescriptor> mavenModules,
        @Nullable final String[] rootGroup
    ) {
        final MavenProjectsManager projectManager = MavenProjectsManager.getInstance(project);
        projectManager.scheduleImportAndResolve();
        projectManager.waitForResolvingCompletion();
        projectManager.importProjects();
        final List<Module> newRootModules = Arrays
            .stream(ModuleManager.getInstance(project).getModules())
            .filter(e -> !originalModules.contains(e))
            .filter(e ->
                mavenModules
                    .stream()
                    .filter(m->m.getName().equals(e.getName()))
                    .findAny()
                    .isPresent()
            )
            .collect(Collectors.toList());

        if (rootGroup != null && rootGroup.length > 0) {
            moveMavenModulesToGroup(project, newRootModules, rootGroup);
        }
    }

    private void moveMavenModulesToGroup(
        final Project project,
        final List<Module> mavenModules,
        final String[] rootGroup
    ) {
        final ModifiableModuleModel modifiableModuleModel = ModuleManager.getInstance(project).getModifiableModel();

        for (Module module: mavenModules) {
            final String[] groupPath = modifiableModuleModel.getModuleGroupPath(module);
            modifiableModuleModel.setModuleGroupPath(module, ArrayUtils.addAll(rootGroup, groupPath));
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
