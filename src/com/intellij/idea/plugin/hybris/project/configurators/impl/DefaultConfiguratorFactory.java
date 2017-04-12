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

import com.intellij.idea.plugin.hybris.project.configurators.AntConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.CompilerOutputPathsConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.ConfiguratorFactory;
import com.intellij.idea.plugin.hybris.project.configurators.ContentRootConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.EclipseConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.FacetConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.GroupModuleConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.JavadocModuleConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.LibRootsConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.MavenConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.ModuleSettingsConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.ModulesDependenciesConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.RunConfigurationConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.SpringConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.VersionControlSystemConfigurator;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisProjectDescriptor;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.roots.IdeaModifiableModelsProvider;
import com.intellij.openapi.roots.ModifiableModelsProvider;
import com.intellij.openapi.util.BuildNumber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.intellij.idea.plugin.hybris.common.services.CommonIdeaService.IDEA_2016_2_BASELINE_VERSION;

/**
 * Created by Martin Zdarsky (martin.zdarsky@hybris.com) on 18/08/15.
 */
public class DefaultConfiguratorFactory implements ConfiguratorFactory {

    @NotNull
    @Override
    public List<FacetConfigurator> getFacetConfigurators() {
        final FacetConfigurator springFacetConfigurator = ServiceManager.getService(SpringFacetConfigurator.class);
        final FacetConfigurator webFacetConfigurator = ServiceManager.getService(WebFacetConfigurator.class);

        final List<FacetConfigurator> facetConfigurators = new ArrayList<FacetConfigurator>(2);

        if (null != springFacetConfigurator) {
            facetConfigurators.add(springFacetConfigurator);
        }

        if (null != webFacetConfigurator) {
            facetConfigurators.add(webFacetConfigurator);
        }

        return facetConfigurators;
    }

    @NotNull
    @Override
    public SpringConfigurator getSpringConfigurator() {
        final BuildNumber buildNumber = ApplicationInfo.getInstance().getBuild();

        if (buildNumber.getBaselineVersion() < IDEA_2016_2_BASELINE_VERSION) {
            final SpringConfigurator springConfigurator = ServiceManager.getService(
                NoInheritanceSpringConfigurator.class
            );

            return (null == springConfigurator) ? new DummySpringConfigurator() : springConfigurator;
        }

        final SpringConfigurator springConfigurator = ServiceManager.getService(
            DefaultSpringConfigurator.class
        );

        return (null == springConfigurator) ? new DummySpringConfigurator() : springConfigurator;
    }

    @NotNull
    @Override
    public ModulesDependenciesConfigurator getModulesDependenciesConfigurator() {
        return ServiceManager.getService(ModulesDependenciesConfigurator.class);
    }

    @NotNull
    @Override
    public CompilerOutputPathsConfigurator getCompilerOutputPathsConfigurator() {
        return ServiceManager.getService(CompilerOutputPathsConfigurator.class);
    }

    @NotNull
    @Override
    public ContentRootConfigurator getRegularContentRootConfigurator() {
        return ServiceManager.getService(RegularContentRootConfigurator.class);
    }

    @NotNull
    @Override
    public ContentRootConfigurator getReadOnlyContentRootConfigurator() {
        return ServiceManager.getService(ReadOnlyContentRootConfigurator.class);
    }

    @NotNull
    @Override
    public LibRootsConfigurator getLibRootsConfigurator() {
        return ServiceManager.getService(LibRootsConfigurator.class);
    }

    @NotNull
    @Override
    public ModifiableModelsProvider getModifiableModelsProvider() {
        return new IdeaModifiableModelsProvider();
    }

    @NotNull
    @Override
    public GroupModuleConfigurator getGroupModuleConfigurator() {
        return ServiceManager.getService(GroupModuleConfigurator.class);
    }

    @NotNull
    @Override
    public JavadocModuleConfigurator getJavadocModuleConfigurator() {
        return ServiceManager.getService(JavadocModuleConfigurator.class);
    }

    @NotNull
    @Override
    public ModuleSettingsConfigurator getModuleSettingsConfigurator() {
        return ServiceManager.getService(ModuleSettingsConfigurator.class);
    }

    @NotNull
    @Override
    public VersionControlSystemConfigurator getVersionControlSystemConfigurator() {
        return ServiceManager.getService(VersionControlSystemConfigurator.class);
    }

    @NotNull
    @Override
    public RunConfigurationConfigurator getDebugRunConfigurationConfigurator() {
        return ServiceManager.getService(DebugRunConfigurationConfigurator.class);
    }

    @Nullable
    @Override
    public RunConfigurationConfigurator getJUnitRunConfigurationConfigurator() {
        return ServiceManager.getService(JUnitRunConfigurationConfigurator.class);
    }

    @Nullable
    @Override
    public AntConfigurator getAntConfigurator() {
        return ServiceManager.getService(AntConfigurator.class);
    }

    @Nullable
    @Override
    public MavenConfigurator getMavenConfigurator() {
        return ServiceManager.getService(MavenConfigurator.class);
    }

    @Nullable
    @Override
    public EclipseConfigurator getEclipseConfigurator() {
        return ServiceManager.getService(EclipseConfigurator.class);
    }

    protected static class DummySpringConfigurator implements SpringConfigurator {

        @Override
        public void findSpringConfiguration(@NotNull final List<HybrisModuleDescriptor> modulesChosenForImport) {

        }

        @Override
        public void configureDependencies(
            @NotNull final HybrisProjectDescriptor hybrisProjectDescriptor,
            @NotNull final ModifiableModuleModel rootProjectModifiableModuleModel
        ) {

        }
    }
}
