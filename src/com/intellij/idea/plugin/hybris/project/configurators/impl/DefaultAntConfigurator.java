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
import com.intellij.idea.plugin.hybris.project.configurators.AntConfigurator;
import com.intellij.idea.plugin.hybris.project.descriptors.CustomHybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.ExtHybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.PlatformHybrisModuleDescriptor;
import com.intellij.lang.ant.config.AntBuildFile;
import com.intellij.lang.ant.config.AntBuildFileBase;
import com.intellij.lang.ant.config.AntConfiguration;
import com.intellij.lang.ant.config.AntConfigurationBase;
import com.intellij.lang.ant.config.AntNoFileException;
import com.intellij.lang.ant.config.impl.AllJarsUnderDirEntry;
import com.intellij.lang.ant.config.impl.AntBuildFileImpl;
import com.intellij.lang.ant.config.impl.AntClasspathEntry;
import com.intellij.lang.ant.config.impl.AntInstallation;
import com.intellij.lang.ant.config.impl.BuildFileProperty;
import com.intellij.lang.ant.config.impl.SinglePathEntry;
import com.intellij.lang.ant.config.impl.TargetFilter;
import com.intellij.lang.ant.config.impl.configuration.EditPropertyContainer;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.rt.ant.execution.HybrisIdeaAntLogger;
import com.intellij.util.config.AbstractProperty;
import com.intellij.util.config.ListProperty;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 20/10/16.
 */
public class DefaultAntConfigurator implements AntConfigurator {

    public final List<String> desirablePlatformTargets = new ArrayList<>(asList("clean", "customize", "all", "deployment", "build"));
    public final List<String> desirableCustomTargets = new ArrayList<>(asList("build"));
    @Override
    public void configure(@NotNull final List<HybrisModuleDescriptor> allModules, @NotNull final Project project) {
        PlatformHybrisModuleDescriptor platformDescriptor = null;
        List<ExtHybrisModuleDescriptor> extHybrisModuleDescriptorList = new ArrayList<>();
        List<CustomHybrisModuleDescriptor> customHybrisModuleDescriptorList = new ArrayList<>();
        for (HybrisModuleDescriptor descriptor: allModules) {
            if (descriptor instanceof PlatformHybrisModuleDescriptor) {
                platformDescriptor = (PlatformHybrisModuleDescriptor) descriptor;
            }
            if (descriptor instanceof ExtHybrisModuleDescriptor) {
                extHybrisModuleDescriptorList.add((ExtHybrisModuleDescriptor) descriptor);
            }
            if (descriptor instanceof CustomHybrisModuleDescriptor) {
                customHybrisModuleDescriptorList.add((CustomHybrisModuleDescriptor) descriptor);
            }
        }
        if (platformDescriptor == null) {
            return;
        }
        final File platformDir = platformDescriptor.getRootDirectory();
        final List<AntClasspathEntry> classPaths = getAntClassPath(platformDir, extHybrisModuleDescriptorList);
        final AntInstallation antInstallation = createAntInstallation(platformDir);
        if (antInstallation == null) {
            return;
        }
        AntConfigurationBase.getInstance(project).setFilterTargets(true);
        final AntConfiguration antConfiguration = AntConfiguration.getInstance(project);
        registerAntInstallation(platformDir, platformDir, antInstallation, antConfiguration, classPaths, desirablePlatformTargets);
        customHybrisModuleDescriptorList.stream().forEach(
            e->registerAntInstallation(platformDir, e.getRootDirectory(), antInstallation, antConfiguration, classPaths, desirableCustomTargets)
        );

        final ToolWindowManager manager = ToolWindowManager.getInstance(project);
        final ToolWindow window = manager.getToolWindow("Ant Build");
        window.show(null);
    }

    private List<TargetFilter> getFilteredTargets(
        final AntConfiguration antConfiguration,
        final AntBuildFileBase antBuildFile,
        final List<String> desirableTargets
    ) {
        return Arrays.stream(antConfiguration.getModel(antBuildFile).getTargets())
                     .map(e->TargetFilter.fromTarget(e))
                     .peek(e->e.setVisible(desirableTargets.contains(e.getTargetName())))
                     .collect(Collectors.toList());
    }

    private void registerAntInstallation(
        final File platformDir,
        final File extensionDir,
        final AntInstallation antInstallation,
        final AntConfiguration antConfiguration,
        final List<AntClasspathEntry> classPaths,
        final List<String> desiredTargets
    ) {

        final AntBuildFileBase antBuildFile = findBuildFile(extensionDir, antConfiguration);
        if (antBuildFile == null) {
            return;
        }
        final List<TargetFilter> filterList = getFilteredTargets(antConfiguration, antBuildFile, desiredTargets);
        final AbstractProperty.AbstractPropertyContainer allOptions = antBuildFile.getAllOptions();
        final EditPropertyContainer editPropertyContainer = new EditPropertyContainer(allOptions);
        setAntProperties(antInstallation, editPropertyContainer, classPaths, platformDir, filterList);
        editPropertyContainer.apply();
    }

    private void setAntProperties(
        final AntInstallation antInstallation,
        final EditPropertyContainer editPropertyContainer,
        final List<AntClasspathEntry> classPaths,
        final File platformDir,
        final List<TargetFilter> filterList
    ) {
        AntBuildFileImpl.ADDITIONAL_CLASSPATH.set(editPropertyContainer, classPaths);
        AntBuildFileImpl.ANT_COMMAND_LINE_PARAMETERS.set(editPropertyContainer, "-l com.intellij.rt.ant.execution.HybrisIdeaAntLogger -logger com.intellij.rt.ant.execution.HybrisIdeaAntLogger");
        AntBuildFileImpl.TREE_VIEW.set(editPropertyContainer, false);
        AntBuildFileImpl.ANT_INSTALLATION.set(editPropertyContainer, antInstallation);
        AntBuildFileImpl.ANT_REFERENCE.set(editPropertyContainer, antInstallation.getReference());
        AntBuildFileImpl.RUN_WITH_ANT.set(editPropertyContainer, antInstallation);
        AntBuildFileImpl.MAX_HEAP_SIZE.set(editPropertyContainer, HybrisConstants.ANT_HEAP_SIZE_MB);
        AntBuildFileImpl.MAX_STACK_SIZE.set(editPropertyContainer, HybrisConstants.ANT_STACK_SIZE_MB);
        AntBuildFileImpl.RUN_IN_BACKGROUND.set(editPropertyContainer, false);
        AntBuildFileImpl.VERBOSE.set(editPropertyContainer, false);

        final ListProperty<BuildFileProperty> properties = AntBuildFileImpl.ANT_PROPERTIES;
        properties.clearList(editPropertyContainer);

        final BuildFileProperty platformHomeProperty = new BuildFileProperty();
        platformHomeProperty.setPropertyName(HybrisConstants.ANT_PLATFORM_HOME);
        platformHomeProperty.setPropertyValue(platformDir.getAbsolutePath());

        final BuildFileProperty antHomeProperty = new BuildFileProperty();
        antHomeProperty.setPropertyName(HybrisConstants.ANT_HOME);
        antHomeProperty.setPropertyValue(antInstallation.getHomeDir());

        final BuildFileProperty antOptsProperty = new BuildFileProperty();
        antOptsProperty.setPropertyName(HybrisConstants.ANT_OPTS);
        antOptsProperty.setPropertyValue(HybrisConstants.ANT_XMX+HybrisConstants.ANT_HEAP_SIZE_MB+" "+HybrisConstants.ANT_ENCODING);

        final List<BuildFileProperty> buildFileProperties = new ArrayList<>();
        buildFileProperties.add(platformHomeProperty);
        buildFileProperties.add(antHomeProperty);
        buildFileProperties.add(antOptsProperty);

        AntBuildFileImpl.ANT_PROPERTIES.set(editPropertyContainer, buildFileProperties);
        AntBuildFileImpl.TARGET_FILTERS.set(editPropertyContainer, filterList);
    }

    private List<AntClasspathEntry> getAntClassPath(
        final File platformDir,
        final List<ExtHybrisModuleDescriptor> extHybrisModuleDescriptorList
    ) {
        final List<AntClasspathEntry> classpath = new ArrayList<>();
        //brutal hack. Do not do this at home, kids!
        //we are hiding class in a classpath to confuse the classloader and pick our implementation
        final String entry = PathManager.getResourceRoot(
            HybrisIdeaAntLogger.class, "/" + HybrisIdeaAntLogger.class.getName().replace('.', '/') + ".class"
        );
        classpath.add(new SinglePathEntry(entry));
        //end of hack
        final File libDir = new File (platformDir, HybrisConstants.ANT_LIB_DIR);
        classpath.add(new AllJarsUnderDirEntry(libDir));
        final File platformLibDir = new File (platformDir, HybrisConstants.LIB_DIRECTORY);
        classpath.add(new AllJarsUnderDirEntry(platformLibDir));
        classpath.addAll(
            extHybrisModuleDescriptorList
                .parallelStream()
                .map(e->new AllJarsUnderDirEntry(new File (e.getRootDirectory(), HybrisConstants.LIB_DIRECTORY)))
                .collect(Collectors.toList())
        );
        return classpath;
    }

    private AntBuildFileBase findBuildFile(final File dir, final AntConfiguration antConfiguration) {
        final File buildxml = new File(dir, HybrisConstants.ANT_BUILD_XML);
        if (!buildxml.exists()) {
            return null;
        }

        final VirtualFile buildFile = VfsUtil.findFileByIoFile(buildxml, true);
        if (buildFile == null) {
            return null;
        }

        final AntBuildFile antBuildFile;
        try {
            antBuildFile = antConfiguration.addBuildFile(buildFile);
        } catch (AntNoFileException e) {
            return null;
        }

        if (antBuildFile instanceof AntBuildFileBase) {
            return (AntBuildFileBase)antBuildFile;
        }
        return null;
    }

    private AntInstallation createAntInstallation(final File platformDir) {
        final VirtualFile antFolder;
        try {
            antFolder = Files.find(
                Paths.get(platformDir.getAbsolutePath()), 1, (path, basicFileAttributes)
                    -> Files.isDirectory(path) && path.toFile().getName().matches("apache-ant.*")
            ).map(e -> VfsUtil.findFileByIoFile(e.toFile(), true)).findAny().orElse(null);
        } catch (IOException e) {
            return null;
        }
        if (antFolder == null) {
            return null;
        }

        try {
            return AntInstallation.fromHome(antFolder.getPresentableUrl());
        } catch (AntInstallation.ConfigurationException e) {
            return null;
        }
    }
}
