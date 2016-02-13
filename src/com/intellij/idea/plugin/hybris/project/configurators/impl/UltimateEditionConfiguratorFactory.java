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

import com.intellij.idea.plugin.hybris.project.configurators.ConfiguratorFactory;
import com.intellij.idea.plugin.hybris.project.configurators.FacetConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.SpringConfigurator;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.util.BuildNumber;

/**
 * Created by Martin Zdarsky (martin.zdarsky@hybris.com) on 18/08/15.
 */
public class UltimateEditionConfiguratorFactory extends CommunityEditionConfiguratorFactory implements ConfiguratorFactory {

    // bug IDEA-143901 was fixed in 144.1948
    public static final int IDEA_143901_FIX_BASELINE_VERSION = 144;

    @Override
    public FacetConfigurator getFacetConfigurator() {
        return ServiceManager.getService(DefaultFacetConfigurator.class);
    }

    @Override
    public SpringConfigurator getSpringConfigurator() {
        final BuildNumber buildNumber = ApplicationInfo.getInstance().getBuild();

        if (buildNumber.getBaselineVersion() < IDEA_143901_FIX_BASELINE_VERSION) {
            return ServiceManager.getService(NoInheritanceSpringConfigurator.class);
        }

        return ServiceManager.getService(DefaultSpringConfigurator.class);
    }
}
