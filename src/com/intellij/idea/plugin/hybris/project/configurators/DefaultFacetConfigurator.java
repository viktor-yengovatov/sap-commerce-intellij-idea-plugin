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

import com.intellij.facet.*;
import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.spring.facet.SpringFacet;
import com.intellij.spring.facet.SpringFileSet;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created by Martin Zdarsky (martin.zdarsky@hybris.com) on 7/08/15.
 */
public class DefaultFacetConfigurator implements FacetConfigurator {

    @Override
    public void configure(@NotNull final ModifiableFacetModel modifiableFacetModel,
                          @NotNull final HybrisModuleDescriptor moduleDescriptor,
                          @NotNull final Module javaModule) {

        Validate.notNull(javaModule);
        Validate.notNull(modifiableFacetModel);
        Validate.notNull(moduleDescriptor);

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                configureInner(modifiableFacetModel, moduleDescriptor, javaModule);
            }
        });
    }

    protected void configureInner(@NotNull final ModifiableFacetModel modifiableFacetModel,
                                  @NotNull final HybrisModuleDescriptor moduleDescriptor,
                                  @NotNull final Module javaModule) {

        configureSpringFacet(javaModule, modifiableFacetModel, moduleDescriptor);
        configureWebFacet(javaModule, modifiableFacetModel, moduleDescriptor);

        modifiableFacetModel.commit();
    }


    private void configureSpringFacet(@NotNull final Module javaModule,
                                      @NotNull final ModifiableFacetModel modifiableFacetModel,
                                      @NotNull final HybrisModuleDescriptor moduleDescriptor) {
        Validate.notNull(javaModule);
        Validate.notNull(modifiableFacetModel);
        Validate.notNull(moduleDescriptor);

        SpringFacet springFacet = modifiableFacetModel.getFacetByType(SpringFacet.FACET_TYPE_ID);

        if (springFacet == null) {
            FacetType springFacetType = FacetTypeRegistry.getInstance().findFacetType(SpringFacet.FACET_TYPE_ID);
            springFacet = (SpringFacet) springFacetType.createFacet(javaModule, SpringFacet.FACET_TYPE_ID.toString(),
                                                                    springFacetType.createDefaultConfiguration(), null);
            modifiableFacetModel.addFacet(springFacet);
        } else {
            springFacet.removeFileSets();
        }

        final String facetId = moduleDescriptor.getName() + SpringFacet.FACET_TYPE_ID.toString();
        final SpringFileSet springFileSet = springFacet.addFileSet(facetId, facetId);
        for (String springFile: moduleDescriptor.getSpringFileSet()) {
            final VirtualFile vf = VfsUtil.findFileByIoFile(new File(springFile), true);
            springFileSet.addFile(vf);
        }
    }

    private void configureWebFacet(@NotNull final Module javaModule,
                                   @NotNull final ModifiableFacetModel modifiableFacetModel,
                                   @NotNull final HybrisModuleDescriptor moduleDescriptor) {
        Validate.notNull(javaModule);
        Validate.notNull(modifiableFacetModel);
        Validate.notNull(moduleDescriptor);

        FacetType webFacetType = FacetTypeRegistry.getInstance().findFacetType(WebFacet.ID);
        WebFacet webFacet = (WebFacet) webFacetType.createFacet(javaModule, WebFacet.ID.toString(), webFacetType.createDefaultConfiguration(), null);
        final VirtualFile vf = VfsUtil.findFileByIoFile(new File(moduleDescriptor.getRootDirectory(), HybrisConstants.WEB_ROOT), true);
        webFacet.addWebRoot(vf, "/");
        modifiableFacetModel.addFacet(webFacet);
    }

}
