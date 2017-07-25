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

package com.intellij.idea.plugin.hybris.common.services.impl;

import com.intellij.idea.plugin.hybris.common.services.CommonIdeaService;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisProjectDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.PlatformHybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.settings.HybrisApplicationSettings;
import com.intellij.idea.plugin.hybris.settings.HybrisApplicationSettingsComponent;
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettings;
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettingsComponent;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.EditorBundle;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created 10:24 PM 10 February 2016.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class DefaultCommonIdeaService implements CommonIdeaService {

    protected final CommandProcessor commandProcessor;

    public DefaultCommonIdeaService(@NotNull final CommandProcessor commandProcessor) {
        Validate.notNull(commandProcessor);

        this.commandProcessor = commandProcessor;
    }

    @Override
    public boolean isTypingActionInProgress() {
        return StringUtils.equals(
            this.commandProcessor.getCurrentCommandName(), EditorBundle.message("typing.in.editor.command.name")
        );
    }

    @Override
    @NotNull
    public Optional<String> getHybrisDirectory(@NotNull final Project project) {
        Validate.notNull(project);

        return Optional.ofNullable(HybrisProjectSettingsComponent.getInstance(project))
                       .map(HybrisProjectSettingsComponent::getState)
                       .map(HybrisProjectSettings::getHybrisDirectory);
    }

    @Override
    @NotNull
    public Optional<String> getCustomDirectory(@NotNull final Project project) {
        Validate.notNull(project);

        return Optional.ofNullable(HybrisProjectSettingsComponent.getInstance(project))
                       .map(HybrisProjectSettingsComponent::getState)
                       .map(HybrisProjectSettings::getCustomDirectory);
    }

    @Override
    public boolean isHybrisProject(@NotNull final Project project) {
        return HybrisProjectSettingsComponent.getInstance(project).getState().isHybrisProject();
    }

    @Override
    public boolean isOutDatedHybrisProject(@NotNull final Project project) {
        final HybrisProjectSettings hybrisProjectSettings = HybrisProjectSettingsComponent.getInstance(project)
                                                                                          .getState();
        final String version = hybrisProjectSettings.getImportedByVersion();
        if (version == null) {
            return true;
        }
        final String[] versionParts = version.split("\\.");
        if (versionParts.length < 2) {
            return true;
        }
        final String majorVersion = versionParts[0];
        final String minorVersion = versionParts[1];
        try {
            final int majorVersionNumber = Integer.valueOf(majorVersion);
            final int minorVersionNumber = Integer.valueOf(minorVersion);
            final int versionNumber = majorVersionNumber * 10 + minorVersionNumber;
            return versionNumber < 64;
        } catch (NumberFormatException nfe) {
            return true;
        }
    }

    @Override
    public boolean isPotentiallyHybrisProject(@NotNull final Project project) {
        final Module[] modules = ModuleManager.getInstance(project).getModules();
        if (modules.length == 0) {
            return false;
        }
        final ArrayList<String> moduleNames = Arrays.stream(modules)
                                                    .map(Module::getName)
                                                    .collect(Collectors.toCollection(ArrayList::new));

        final Collection<String> acceleratorNames = Arrays.asList("*cockpits", "*core", "*facades", "*storefront");
        if (matchAllModuleNames(acceleratorNames, moduleNames)) {
            return true;
        }
        final Collection<String> webservicesNames = Arrays.asList("*hmc", "hmc", "platform");
        if (matchAllModuleNames(webservicesNames, moduleNames)) {
            return true;
        }
        return false;
    }

    @Override
    public PlatformHybrisModuleDescriptor getPlatformDescriptor(final HybrisProjectDescriptor hybrisProjectDescriptor) {
        return (PlatformHybrisModuleDescriptor) hybrisProjectDescriptor
            .getFoundModules()
            .stream()
            .filter(e -> e instanceof PlatformHybrisModuleDescriptor)
            .findAny()
            .orElse(null);
    }

    @Override
    public boolean shouldShowPermissionToSendStatisticsDialog() {
        final HybrisApplicationSettings settings = HybrisApplicationSettingsComponent.getInstance().getState();
        return !settings.isAllowedSendingPlainStatistics() && !settings.isDevelopmentMode();
    }

    private boolean matchAllModuleNames(
        @NotNull final Collection<String> namePaterns,
        @NotNull final Collection<String> moduleNames
    ) {
        return namePaterns.stream()
                          .allMatch(pattern -> matchModuleName(pattern, moduleNames));
    }

    private boolean matchModuleName(@NotNull final String pattern, final Collection<String> moduleNames) {
        String regex = ("\\Q" + pattern + "\\E").replace("*", "\\E.*\\Q");
        return moduleNames.stream()
                          .parallel()
                          .filter(p -> p.matches(regex))
                          .findAny()
                          .isPresent();
    }
}