/*
 * This file is part of "Hybris Integration" plugin for Intellij IDEA.
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

package com.intellij.idea.plugin.hybris.project.configurators;

import com.intellij.openapi.roots.IdeaModifiableModelsProvider;
import com.intellij.openapi.roots.ModifiableModelsProvider;

/**
 * Created by Martin Zdarsky (martin.zdarsky@hybris.com) on 18/08/15.
 */
public class CommunityEditionConfiguratorFactory implements ConfiguratorFactory {

    @Override
    public FacetConfigurator getFacetConfigurator() {
        return new FacetConfiguratorAdapter();
    }

    @Override
    public SpringConfigurator getSpringConfigurator() {
        return new SpringConfiguratorAdapter();
    }

    @Override
    public ModulesDependenciesConfigurator getModulesDependenciesConfigurator() {
        return new DefaultModulesDependenciesConfigurator();
    }

    @Override
    public CompilerOutputPathsConfigurator getCompilerOutputPathsConfigurator() {
        return new DefaultCompilerOutputPathsConfigurator();
    }

    @Override
    public ContentRootConfigurator getContentRootConfigurator() {
        return new HybrisContentRootConfigurator();
    }

    @Override
    public LibRootsConfigurator getLibRootsConfigurator() {
        return new DefaultLibRootsConfigurator();
    }

    @Override
    public ModifiableModelsProvider getModifiableModelsProvider() {
        return new IdeaModifiableModelsProvider();
    }
}
