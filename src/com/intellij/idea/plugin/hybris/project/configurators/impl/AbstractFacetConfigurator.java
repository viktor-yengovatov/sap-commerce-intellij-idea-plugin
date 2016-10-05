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

import com.intellij.facet.ModifiableFacetModel;
import com.intellij.idea.plugin.hybris.project.configurators.FacetConfigurator;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableModelsProvider;
import com.intellij.openapi.roots.ModifiableRootModel;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

/**
 * Created 8:40 PM 13 February 2016.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public abstract class AbstractFacetConfigurator implements FacetConfigurator {

    @Override
    public void configure(@NotNull final ModifiableFacetModel modifiableFacetModel,
                          @NotNull final HybrisModuleDescriptor moduleDescriptor,
                          @NotNull final Module javaModule,
                          @NotNull final ModifiableRootModel modifiableRootModel,
                          @NotNull final ModifiableModelsProvider modifiableModelsProvider) {
        Validate.notNull(modifiableFacetModel);
        Validate.notNull(moduleDescriptor);
        Validate.notNull(javaModule);
        Validate.notNull(modifiableRootModel);
        Validate.notNull(modifiableModelsProvider);

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                configureInner(modifiableFacetModel, moduleDescriptor, javaModule, modifiableRootModel);
                modifiableModelsProvider.commitFacetModifiableModel(javaModule, modifiableFacetModel);
            }
        });
    }

    protected abstract void configureInner(@NotNull ModifiableFacetModel modifiableFacetModel,
                                           @NotNull HybrisModuleDescriptor moduleDescriptor,
                                           @NotNull Module javaModule,
                                           @NotNull ModifiableRootModel modifiableRootModel);
}
