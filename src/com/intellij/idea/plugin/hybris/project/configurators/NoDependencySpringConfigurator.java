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

import com.intellij.facet.ModifiableFacetModel;
import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
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
public class NoDependencySpringConfigurator extends DefaultSpringConfigurator {

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

        for (HybrisModuleDescriptor parentModuleDescriptor : moduleDescriptor.getDependenciesPlainList()) {
            final SpringFileSet parentFileSet = getSpringFileSet(modifiableFacetModelMap, parentModuleDescriptor.getName());
            if (parentFileSet == null) {
                continue;
            }

            for (VirtualFilePointer filePointer: parentFileSet.getXmlFiles()) {
                springFileSet.addFile(filePointer.getFile());
            }
        }
        commitFacetModel(moduleDescriptor, moduleMap, modifiableFacetModelMap);
    }
}
