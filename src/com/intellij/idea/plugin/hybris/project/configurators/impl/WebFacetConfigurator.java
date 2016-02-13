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

import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.FacetType;
import com.intellij.facet.FacetTypeRegistry;
import com.intellij.facet.ModifiableFacetModel;
import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.openapi.module.Module;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

/**
 * Created 8:33 PM 13 February 2016.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class WebFacetConfigurator extends AbstractFacetConfigurator {

    @Override
    protected void configureInner(@NotNull final ModifiableFacetModel modifiableFacetModel,
                                  @NotNull final HybrisModuleDescriptor moduleDescriptor,
                                  @NotNull final Module javaModule) {
        Validate.notNull(javaModule);
        Validate.notNull(modifiableFacetModel);
        Validate.notNull(moduleDescriptor);

        WebFacet webFacet = modifiableFacetModel.getFacetByType(WebFacet.ID);

        if (webFacet == null) {
            final FacetType<WebFacet, FacetConfiguration> webFacetType = FacetTypeRegistry.getInstance().findFacetType(
                WebFacet.ID
            );

            webFacet = webFacetType.createFacet(
                javaModule, WebFacet.ID.toString(), webFacetType.createDefaultConfiguration(), null
            );

            modifiableFacetModel.addFacet(webFacet);
        } else {
            webFacet.removeAllWebRoots();
        }
    }
}
