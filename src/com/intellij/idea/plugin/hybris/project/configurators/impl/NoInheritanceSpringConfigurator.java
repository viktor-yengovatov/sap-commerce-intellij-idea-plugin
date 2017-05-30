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
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.settings.HybrisApplicationSettingsComponent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.pointers.VirtualFilePointer;
import com.intellij.spring.facet.SpringFileSet;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Created by Martin Zdarsky (martin.zdarsky@hybris.com) on 14/08/15.
 *
 * This class is created due to a Idea bug IDEA-143901
 * https://youtrack.jetbrains.com/issue/IDEA-143901
 */
public class NoInheritanceSpringConfigurator extends DefaultSpringConfigurator {

    @Override
    protected void configureFacetDependencies(@NotNull final HybrisModuleDescriptor moduleDescriptor,
                                              @NotNull final Map<String, Module> moduleMap,
                                              @NotNull final Map<String, ModifiableFacetModel> modifiableFacetModelMap) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(moduleMap);
        Validate.notNull(modifiableFacetModelMap);

        final SpringFileSet springFileSet = getSpringFileSet(modifiableFacetModelMap, moduleDescriptor.getName());
        if (springFileSet == null) {
            return;
        }

        if (moduleDescriptor.isPreselected() || !isLimitedSpringConfig()) {
            for (HybrisModuleDescriptor parentModuleDescriptor : moduleDescriptor.getDependenciesPlainList()) {
                final SpringFileSet parentFileSet = getSpringFileSet(modifiableFacetModelMap, parentModuleDescriptor.getName());
                if (parentFileSet == null) {
                    continue;
                }

                for (VirtualFilePointer filePointer : parentFileSet.getXmlFiles()) {
                    springFileSet.addFile(filePointer.getFile());
                }
            }
        }
        commitFacetModel(moduleDescriptor, moduleMap, modifiableFacetModelMap);
    }

    private boolean isLimitedSpringConfig() {
        return HybrisApplicationSettingsComponent.getInstance().getState().isLimitedSpringConfig();
    }
}
