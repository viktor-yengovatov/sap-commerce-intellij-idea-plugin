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
import com.intellij.idea.plugin.hybris.project.configurators.GroupModuleConfigurator;
import com.intellij.idea.plugin.hybris.project.descriptors.ConfigHybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.CustomHybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.ExtHybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.PlatformHybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.RootModuleDescriptor;
import com.intellij.idea.plugin.hybris.settings.HybrisApplicationSettings;
import com.intellij.idea.plugin.hybris.settings.HybrisApplicationSettingsComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import static com.intellij.idea.plugin.hybris.settings.HybrisApplicationSettings.toIdeaGroup;

/**
 * Created by Martin Zdarsky (martin.zdarsky@hybris.com) on 24/08/15.
 */
public class DefaultGroupModuleConfigurator implements GroupModuleConfigurator {
    private static final Logger LOG = Logger.getInstance(DefaultGroupModuleConfigurator.class);

    private Set<HybrisModuleDescriptor> requiredHybrisModuleDescriptorList;
    private boolean groupModules;
    private String[] groupCustom;
    private String[] groupNonHybris;
    private String[] groupOtherCustom;
    private String[] groupHybris;
    private String[] groupPlatform;
    private String[] groupOtherHybris;

    public DefaultGroupModuleConfigurator() {
        readSettings();
    }

    @Override
    public void findDependencyModules(@NotNull final List<HybrisModuleDescriptor> modulesChosenForImport) {
        readSettings();
        if (!groupModules) {
            return;
        }
        requiredHybrisModuleDescriptorList = new HashSet<>();
        for (HybrisModuleDescriptor hybrisModuleDescriptor : modulesChosenForImport) {
            if (hybrisModuleDescriptor.isPreselected()) {
                requiredHybrisModuleDescriptorList.add(hybrisModuleDescriptor);
                requiredHybrisModuleDescriptorList.addAll(hybrisModuleDescriptor.getDependenciesPlainList());
            }
        }
    }

    @Override
    public void configure(
        @NotNull final ModifiableModuleModel modifiableModuleModel,
        @NotNull final Module module,
        @NotNull final HybrisModuleDescriptor moduleDescriptor
    ) {
        if (!groupModules) {
            return;
        }
        String[] groupNamePath = getGroupName(moduleDescriptor);
        modifiableModuleModel.setModuleGroupPath(module, groupNamePath);
    }

    @Nullable
    @Override
    public String[] getGroupName(@NotNull final HybrisModuleDescriptor moduleDescriptor) {
        String[] groupPathOverride = getLocalGroupPathOverride(moduleDescriptor);
        if (groupPathOverride != null) {
            return groupPathOverride.clone();
        }

        groupPathOverride = getGlobalGroupPathOverride(moduleDescriptor);
        if (groupPathOverride != null) {
            return groupPathOverride.clone();
        }

        String[] groupPath = getGroupPath(moduleDescriptor);
        if (groupPath == null) {
            return null;
        }
        return groupPath.clone();
    }

    private String[] getGlobalGroupPathOverride(final HybrisModuleDescriptor moduleDescriptor) {
        final ConfigHybrisModuleDescriptor configDescriptor = moduleDescriptor.getRootProjectDescriptor().getConfigHybrisModuleDescriptor();
        final File groupFile = new File(configDescriptor.getRootDirectory(), HybrisConstants.GROUP_OVERRIDE_FILENAME);
        return getGroupPathOverride(groupFile, moduleDescriptor.getName());
    }


    private String[] getLocalGroupPathOverride(final HybrisModuleDescriptor moduleDescriptor) {
        final File groupFile = new File(moduleDescriptor.getRootDirectory(), HybrisConstants.GROUP_OVERRIDE_FILENAME);
        final String[] pathOverride = getGroupPathOverride(groupFile, moduleDescriptor.getName());
        if (groupFile.exists() && pathOverride == null) {
            try (OutputStream out = new FileOutputStream(groupFile)) {
                Properties properties = new Properties();
                properties.setProperty(HybrisConstants.GROUP_OVERRIDE_KEY, "");
                properties.store(out, HybrisConstants.GROUP_OVERRIDE_COMMENTS);
            } catch (IOException e) {
                LOG.error("Cannot write " + HybrisConstants.GROUP_OVERRIDE_FILENAME + " for module " + moduleDescriptor.getName());
            }
        }
        return null;
    }

    private String[] getGroupPathOverride(final File groupFile, final String moduleName) {
        if (!groupFile.exists()) {
            return null;
        }
        String rawGroupText = null;
        Properties properties = new Properties();
        try (InputStream in = new FileInputStream(groupFile)) {
            properties.load(in);
        } catch (IOException e) {
            LOG.error("Cannot read " + HybrisConstants.GROUP_OVERRIDE_FILENAME + " for module " + moduleName);
            return null;
        }
        rawGroupText = properties.getProperty(HybrisConstants.GROUP_OVERRIDE_KEY);
        if (rawGroupText == null) {
            rawGroupText = properties.getProperty(moduleName + "." + HybrisConstants.GROUP_OVERRIDE_KEY);
        }
        return toIdeaGroup(rawGroupText);
    }

    private String[] getGroupPath(@NotNull final HybrisModuleDescriptor moduleDescriptor) {
        if (moduleDescriptor instanceof PlatformHybrisModuleDescriptor) {
            return groupPlatform;
        }
        if (moduleDescriptor instanceof ExtHybrisModuleDescriptor) {
            return groupPlatform;
        }
        if (moduleDescriptor instanceof ConfigHybrisModuleDescriptor) {
            return groupCustom;
        }
        if (moduleDescriptor instanceof RootModuleDescriptor) {
            return groupNonHybris;
        }
        if (moduleDescriptor instanceof CustomHybrisModuleDescriptor) {
            if (requiredHybrisModuleDescriptorList.contains(moduleDescriptor)) {
                return groupCustom;
            }
            return groupOtherCustom;
        }
        if (requiredHybrisModuleDescriptorList.contains(moduleDescriptor)) {
            return Stream.of(groupHybris, getExtDirectoryName(moduleDescriptor))
                         .flatMap(Stream::of)
                         .toArray(String[]::new);
        }
        return groupOtherHybris;
    }

    private String[] getExtDirectoryName(@NotNull final HybrisModuleDescriptor moduleDescriptor) {
        return new String[] {moduleDescriptor.getRootDirectory().getParentFile().getName()};
    }

    private void readSettings() {
        final HybrisApplicationSettings hybrisApplicationSettings = HybrisApplicationSettingsComponent.getInstance()
                                                                                                      .getState();
        groupModules = hybrisApplicationSettings.isGroupModules();
        groupCustom = toIdeaGroup(hybrisApplicationSettings.getGroupCustom());
        groupNonHybris = toIdeaGroup(hybrisApplicationSettings.getGroupNonHybris());
        groupOtherCustom = toIdeaGroup(hybrisApplicationSettings.getGroupOtherCustom());
        groupHybris = toIdeaGroup(hybrisApplicationSettings.getGroupHybris());
        groupOtherHybris = toIdeaGroup(hybrisApplicationSettings.getGroupOtherHybris());
        groupPlatform = toIdeaGroup(hybrisApplicationSettings.getGroupPlatform());
    }

}
