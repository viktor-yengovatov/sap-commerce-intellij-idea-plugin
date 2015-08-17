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
import com.intellij.idea.plugin.hybris.project.settings.HybrisProjectDescriptor;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.IdeaModifiableModelsProvider;
import com.intellij.openapi.roots.ModifiableModelsProvider;
import com.intellij.spring.facet.SpringFacet;
import com.intellij.spring.facet.SpringFileSet;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by Martin Zdarsky (martin.zdarsky@hybris.com) on 10/08/15.
 */
public class DefaultSpringConfigurator implements SpringConfigurator {

    protected final ModifiableModelsProvider modifiableModelsProvider = new IdeaModifiableModelsProvider();

    @Override
    public void findSpringConfiguration(@NotNull final List<HybrisModuleDescriptor> modulesChosenForImport) {
        Validate.notNull(modulesChosenForImport);

        Map<String, HybrisModuleDescriptor> moduleDescriptorMap = new HashMap<String, HybrisModuleDescriptor>();
        for (HybrisModuleDescriptor moduleDescriptor: modulesChosenForImport) {
            moduleDescriptorMap.put(moduleDescriptor.getName(), moduleDescriptor);
        }
        for (HybrisModuleDescriptor moduleDescriptor: modulesChosenForImport) {
            processHybrisModule(moduleDescriptorMap, moduleDescriptor);
        }
    }

    @Override
    public void configureDependencies(final @NotNull HybrisProjectDescriptor hybrisProjectDescriptor,
                                      final @NotNull ModifiableModuleModel rootProjectModifiableModuleModel) {
        Validate.notNull(hybrisProjectDescriptor);
        Validate.notNull(rootProjectModifiableModuleModel);

        final List<Module> modules = Arrays.asList(rootProjectModifiableModuleModel.getModules());
        final Map<String, ModifiableFacetModel> modifiableFacetModelMap = new HashMap<String, ModifiableFacetModel>();
        final Map<String, Module> moduleMap = new HashMap<String, Module>();

        for (Module module : modules) {
            final ModifiableFacetModel modifiableFacetModel = modifiableModelsProvider.getFacetModifiableModel(module);
            modifiableFacetModelMap.put(module.getName(), modifiableFacetModel);
            moduleMap.put(module.getName(), module);
        }

        for (HybrisModuleDescriptor moduleDescriptor : hybrisProjectDescriptor.getModulesChosenForImport()) {
            configureFacetDependencies(moduleDescriptor, moduleMap, modifiableFacetModelMap);
        }
    }

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

        for (String dependsOnModuleName : moduleDescriptor.getRequiredExtensionNames()) {
            final SpringFileSet parentFileSet = getSpringFileSet(modifiableFacetModelMap, dependsOnModuleName);
            if (parentFileSet == null) {
                continue;
            }
            springFileSet.addDependency(parentFileSet);
        }
        commitFacetModel(moduleDescriptor, moduleMap, modifiableFacetModelMap);
    }

    protected SpringFileSet getSpringFileSet(@NotNull final Map<String, ModifiableFacetModel> modifiableFacetModelMap,
                                             @NotNull final String moduleName) {
        Validate.notNull(moduleName);
        Validate.notNull(modifiableFacetModelMap);

        final ModifiableFacetModel modifiableFacetModel = modifiableFacetModelMap.get(moduleName);
        if (modifiableFacetModel == null) {
            return null;
        }
        final SpringFacet springFacet = modifiableFacetModel.getFacetByType(SpringFacet.FACET_TYPE_ID);
        if (springFacet == null || springFacet.getFileSets().isEmpty()) {
            return null;
        }
        return springFacet.getFileSets().iterator().next();
    }

    protected void commitFacetModel(@NotNull final HybrisModuleDescriptor moduleDescriptor,
                                    @NotNull final Map<String, Module> moduleMap,
                                    @NotNull final Map<String, ModifiableFacetModel> modifiableFacetModelMap) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(moduleMap);
        Validate.notNull(modifiableFacetModelMap);

        final Module module = moduleMap.get(moduleDescriptor.getName());
        final ModifiableFacetModel modifiableFacetModel = modifiableFacetModelMap.get(moduleDescriptor.getName());

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                modifiableModelsProvider.commitFacetModifiableModel(module, modifiableFacetModel);
            }
        });
    }

    protected void processHybrisModule(@NotNull final Map<String, HybrisModuleDescriptor> moduleDescriptorMap,
                                     @NotNull final HybrisModuleDescriptor moduleDescriptor) {
        Validate.notNull(moduleDescriptorMap);
        Validate.notNull(moduleDescriptor);

        Properties projectProperties = new Properties();

        File propFile = new File(moduleDescriptor.getRootDirectory(), HybrisConstants.PROJECT_PROPERTIES);
        try {
            FileInputStream fis = new FileInputStream(propFile);
            projectProperties.load(fis);
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        final File resourcesDir = new File (moduleDescriptor.getRootDirectory(), HybrisConstants.RESOURCES_DIRECTORY);

        for (String key: projectProperties.stringPropertyNames()) {
            if (   key.endsWith(HybrisConstants.APPLICATION_CONTEXT_SPRING_FILES)
                || key.endsWith(HybrisConstants.ADDITIONAL_WEB_SPRING_CONFIG_FILES)
                || key.endsWith(HybrisConstants.GLOBAL_CONTEXT_SPRING_FILES)) {
                String moduleName = key.substring(0, key.indexOf("."));
                // relevantModule can be different to a moduleDescriptor. e.g. addon concept
                final HybrisModuleDescriptor relevantModule = moduleDescriptorMap.get(moduleName);
                if (relevantModule != null) {
                    String rawFile = projectProperties.getProperty(key);
                    if (rawFile == null) {
                        continue;
                    }
                    for (String file: rawFile.split(",")) {
                        File springFile;
                        if (file.startsWith("classpath:")) {
                            springFile = new File(resourcesDir, file.substring("classpath:".length(), file.length()));
                        } else {
                            springFile = new File(resourcesDir, file);
                        }
                        if (springFile.exists()) {
                            relevantModule.addSpringFile(springFile.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }
}
