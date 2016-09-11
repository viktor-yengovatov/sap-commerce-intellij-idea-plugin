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

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.intellij.idea.plugin.hybris.project.configurators.ModulesDependenciesConfigurator;
import com.intellij.idea.plugin.hybris.project.settings.ExtHybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.settings.HybrisProjectDescriptor;
import com.intellij.idea.plugin.hybris.project.settings.OotbHybrisModuleDescriptor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.DependencyScope;
import com.intellij.openapi.roots.IdeaModifiableModelsProvider;
import com.intellij.openapi.roots.ModifiableModelsProvider;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleOrderEntry;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created 12:22 AM 25 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class DefaultModulesDependenciesConfigurator implements ModulesDependenciesConfigurator {

    private static final Logger LOG = Logger.getInstance(DefaultModulesDependenciesConfigurator.class);
    protected final ModifiableModelsProvider modifiableModelsProvider = new IdeaModifiableModelsProvider();

    @Override
    public void configure(final @NotNull HybrisProjectDescriptor hybrisProjectDescriptor,
                          final @NotNull ModifiableModuleModel rootProjectModifiableModuleModel) {
        Validate.notNull(hybrisProjectDescriptor);
        Validate.notNull(rootProjectModifiableModuleModel);

        final List<Module> modules = Arrays.asList(rootProjectModifiableModuleModel.getModules());
        final Collection<ModifiableRootModel> modifiableRootModels = modules
            .stream()
            .map(this.modifiableModelsProvider::getModuleModifiableModel)
            .collect(Collectors.toCollection(ArrayList::new));

        final Set<HybrisModuleDescriptor> extModules = hybrisProjectDescriptor
            .getModulesChosenForImport()
            .stream()
            .filter(moduleDescriptor -> moduleDescriptor instanceof ExtHybrisModuleDescriptor)
            .collect(Collectors.toSet());

        for (HybrisModuleDescriptor moduleDescriptor : hybrisProjectDescriptor.getModulesChosenForImport()) {
            final ModifiableRootModel modifiableRootModel = Iterables.find(
                modifiableRootModels,
                new FindModifiableRootModelByName(moduleDescriptor.getName())
            );

            this.configureModuleDependencies(moduleDescriptor, modifiableRootModel, modifiableRootModels, extModules);
        }
    }

    protected void configureModuleDependencies(@NotNull final HybrisModuleDescriptor moduleDescriptor,
                                               @NotNull final ModifiableRootModel modifiableRootModel,
                                               @NotNull final Collection<ModifiableRootModel> modifiableRootModels,
                                               @NotNull final Set<HybrisModuleDescriptor> extModules) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(modifiableRootModel);
        Validate.notNull(modifiableRootModels);
        Validate.notNull(extModules);

        for (HybrisModuleDescriptor dependency : moduleDescriptor.getDependenciesTree()) {
            if (moduleDescriptor instanceof OotbHybrisModuleDescriptor) {
                if (extModules.contains(dependency)) {
                    continue;
                }
            }

            final Optional<ModifiableRootModel> dependsOnModifiableRootModelOptional = Iterables.tryFind(
                modifiableRootModels,
                new FindModifiableRootModelByName(dependency.getName())
            );

            final ModuleOrderEntry moduleOrderEntry = (dependsOnModifiableRootModelOptional.isPresent())
                ? modifiableRootModel.addModuleOrderEntry(dependsOnModifiableRootModelOptional.get().getModule())
                : modifiableRootModel.addInvalidModuleEntry(dependency.getName());

            moduleOrderEntry.setExported(true);
            moduleOrderEntry.setScope(DependencyScope.COMPILE);
        }

        this.commitModule(modifiableRootModel);
    }

    protected void commitModule(@NotNull final ModifiableRootModel modifiableRootModel) {
        Validate.notNull(modifiableRootModel);

        ApplicationManager.getApplication().runWriteAction(
            () -> modifiableModelsProvider.commitModuleModifiableModel(modifiableRootModel)
        );
    }

    private static class FindModifiableRootModelByName implements Predicate<ModifiableRootModel> {

        private final String name;

        public FindModifiableRootModelByName(@NotNull final String name) {
            Validate.notEmpty(name);

            this.name = name;
        }

        @Override
        public boolean apply(@Nullable final ModifiableRootModel t) {
            return null != t && this.name.equalsIgnoreCase(t.getModule().getName());
        }
    }
}
