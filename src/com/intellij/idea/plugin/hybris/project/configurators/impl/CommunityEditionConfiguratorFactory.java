/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2015 Alexander Bartash <AlexanderBartash@gmail.com>
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

import com.intellij.idea.plugin.hybris.project.configurators.CompilerOutputPathsConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.ConfiguratorFactory;
import com.intellij.idea.plugin.hybris.project.configurators.ContentRootConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.FacetConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.GroupModuleConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.LibRootsConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.ModulesDependenciesConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.SpringConfigurator;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.roots.IdeaModifiableModelsProvider;
import com.intellij.openapi.roots.ModifiableModelsProvider;

/**
 * Created by Martin Zdarsky (martin.zdarsky@hybris.com) on 18/08/15.
 */
public class CommunityEditionConfiguratorFactory implements ConfiguratorFactory {

    @Override
    public FacetConfigurator getFacetConfigurator() {
        return ServiceManager.getService(FacetConfiguratorAdapter.class);
    }

    @Override
    public SpringConfigurator getSpringConfigurator() {
        return ServiceManager.getService(SpringConfiguratorAdapter.class);
    }

    @Override
    public ModulesDependenciesConfigurator getModulesDependenciesConfigurator() {
        return ServiceManager.getService(ModulesDependenciesConfigurator.class);
    }

    @Override
    public CompilerOutputPathsConfigurator getCompilerOutputPathsConfigurator() {
        return ServiceManager.getService(CompilerOutputPathsConfigurator.class);
    }

    @Override
    public ContentRootConfigurator getContentRootConfigurator() {
        return ServiceManager.getService(ContentRootConfigurator.class);
    }

    @Override
    public LibRootsConfigurator getLibRootsConfigurator() {
        return ServiceManager.getService(LibRootsConfigurator.class);
    }

    @Override
    public ModifiableModelsProvider getModifiableModelsProvider() {
        return new IdeaModifiableModelsProvider();
    }

    @Override
    public GroupModuleConfigurator getGroupModuleConfigurator() {
        return ServiceManager.getService(GroupModuleConfigurator.class);
    }
}
