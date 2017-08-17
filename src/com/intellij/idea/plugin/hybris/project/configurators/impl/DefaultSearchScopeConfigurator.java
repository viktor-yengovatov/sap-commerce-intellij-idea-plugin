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

import com.intellij.find.FindSettings;
import com.intellij.idea.plugin.hybris.project.configurators.SearchScopeConfigurator;
import com.intellij.idea.plugin.hybris.settings.HybrisApplicationSettingsComponent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.scope.packageSet.FilePatternPackageSet;
import com.intellij.psi.search.scope.packageSet.NamedScope;
import com.intellij.psi.search.scope.packageSet.NamedScopeManager;
import com.intellij.psi.search.scope.packageSet.UnionPackageSet;

import java.util.Arrays;

import static com.intellij.idea.plugin.hybris.common.HybrisConstants.SEARCH_SCOPE_GROUP_PREFIX;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.SEARCH_SCOPE_Y_PREFIX;

/**
 * Created by Martin Zdarsky-Jones on 04/07/2017.
 */
public class DefaultSearchScopeConfigurator implements SearchScopeConfigurator {

    @Override
    public void configure(final Project project) {
        final String customGroupName = HybrisApplicationSettingsComponent.getInstance().getState().getGroupCustom();
        final String commerceGroupName = HybrisApplicationSettingsComponent.getInstance().getState().getGroupHybris();
        final String nonHybrisGroupName = HybrisApplicationSettingsComponent.getInstance()
                                                                            .getState()
                                                                            .getGroupNonHybris();
        final String platformGroupName = HybrisApplicationSettingsComponent.getInstance().getState().getGroupPlatform();
        NamedScope customScope = null;
        NamedScope platformScope = null;
        NamedScope commerceScope = null;
        NamedScope hybrisScope = null;

        if (groupExists(project, customGroupName)) {
            customScope = addScope(project, customGroupName);
        }
        if (groupExists(project, platformGroupName)) {
            platformScope = addScope(project, platformGroupName);
        }
        if (groupExists(project, commerceGroupName)) {
            commerceScope = addScope(project, commerceGroupName);
        }
        if (platformScope != null && commerceScope != null) {
            hybrisScope = addScope(project, platformGroupName, commerceGroupName);
        }
        if (groupExists(project, nonHybrisGroupName)) {
            addScope(project, nonHybrisGroupName);
        }

        NamedScope defaultScope = customScope != null ? customScope : hybrisScope != null ? hybrisScope : platformScope;
        FindSettings.getInstance().setCustomScope(defaultScope.getName());
        FindSettings.getInstance().setDefaultScopeName(defaultScope.getName());
    }

    private boolean groupExists(final Project project, final String groupName) {
        final Module[] modules = ModuleManager.getInstance(project).getModules();
        return Arrays.stream(modules)
                     .map(module -> ModuleManager.getInstance(project).getModuleGroupPath(module))
                     .filter(groupPath -> groupPath != null && groupPath.length > 0 && groupPath[0].equals(groupName))
                     .findAny()
                     .isPresent();
    }

    private NamedScope addScope(final Project project, final String groupName) {
        final FilePatternPackageSet filePatternPackageSet = new FilePatternPackageSet(
            SEARCH_SCOPE_GROUP_PREFIX + groupName,
            "*//*"
        );
        final NamedScope scope = new NamedScope(SEARCH_SCOPE_Y_PREFIX + " " + groupName, filePatternPackageSet);
        NamedScopeManager.getInstance(project).addScope(scope);
        return scope;
    }

    private NamedScope addScope(final Project project, final String firstGroupName, String secondGroupName) {
        final FilePatternPackageSet firstFilePatternPackageSet = new FilePatternPackageSet(
            SEARCH_SCOPE_GROUP_PREFIX + firstGroupName,
            "*//*"
        );
        final FilePatternPackageSet secondFilePatternPackageSet = new FilePatternPackageSet(
            SEARCH_SCOPE_GROUP_PREFIX + secondGroupName,
            "*//*"
        );
        final UnionPackageSet unionPackageSet = new UnionPackageSet(
            firstFilePatternPackageSet,
            secondFilePatternPackageSet
        );
        final NamedScope scope = new NamedScope(
            SEARCH_SCOPE_Y_PREFIX + " " + firstGroupName + " " + secondGroupName,
            unionPackageSet
        );
        NamedScopeManager.getInstance(project).addScope(scope);
        return scope;
    }
}
