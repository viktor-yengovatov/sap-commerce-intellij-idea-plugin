/*
 * Copyright 2015 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.idea.plugin.hybris.project.configurators;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.settings.HybrisProjectDescriptor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.*;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created 12:22 AM 25 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class DefaultModulesDependenciesConfigurator implements ModulesDependenciesConfigurator {

    protected final ModifiableModelsProvider modifiableModelsProvider = new IdeaModifiableModelsProvider();

    @Override
    public void configure(final @NotNull HybrisProjectDescriptor hybrisProjectDescriptor,
                          final @NotNull ModifiableModuleModel rootProjectModifiableModuleModel) {
        Validate.notNull(hybrisProjectDescriptor);
        Validate.notNull(rootProjectModifiableModuleModel);

        final List<Module> modules = Arrays.asList(rootProjectModifiableModuleModel.getModules());
        final Collection<ModifiableRootModel> modifiableRootModels = new ArrayList<ModifiableRootModel>();

        for (Module module : modules) {
            modifiableRootModels.add(this.modifiableModelsProvider.getModuleModifiableModel(module));
        }

        for (HybrisModuleDescriptor moduleDescriptor : hybrisProjectDescriptor.getModulesChosenForImport()) {
            final ModifiableRootModel modifiableRootModel = Iterables.find(
                modifiableRootModels,
                new FindModifiableRootModelByName(moduleDescriptor.getName())
            );

            this.configureModuleDependencies(moduleDescriptor, modifiableRootModel, modifiableRootModels);
        }
    }

    protected void configureModuleDependencies(@NotNull final HybrisModuleDescriptor moduleDescriptor,
                                               @NotNull final ModifiableRootModel modifiableRootModel,
                                               @NotNull final Collection<ModifiableRootModel> modifiableRootModels) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(modifiableRootModel);
        Validate.notNull(modifiableRootModels);

        for (String dependsOnModuleName : moduleDescriptor.getRequiredExtensionNames()) {
            final Optional<ModifiableRootModel> dependsOnModifiableRootModelOptional = Iterables.tryFind(
                modifiableRootModels,
                new FindModifiableRootModelByName(dependsOnModuleName)
            );

            final ModuleOrderEntry moduleOrderEntry = (dependsOnModifiableRootModelOptional.isPresent())
                ? modifiableRootModel.addModuleOrderEntry(dependsOnModifiableRootModelOptional.get().getModule())
                : modifiableRootModel.addInvalidModuleEntry(dependsOnModuleName);

            moduleOrderEntry.setExported(true);
            moduleOrderEntry.setScope(DependencyScope.COMPILE);
        }

        this.commitModule(modifiableRootModel);
    }

    protected void commitModule(@NotNull final ModifiableRootModel modifiableRootModel) {
        Validate.notNull(modifiableRootModel);

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                modifiableModelsProvider.commitModuleModifiableModel(modifiableRootModel);
            }
        });
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
