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

package com.intellij.idea.plugin.hybris.settings;

import com.google.common.collect.Lists;
import com.intellij.ide.util.PropertyName;

import java.util.List;

public class HybrisIntegrationSettingsData {

    public static final List<String> defaultJunkFileNames = Lists.newArrayList(
        ".classpath",
        ".directory",
        ".externalToolBuilders",
        ".idea",
        ".pmd",
        ".project",
        ".ruleset",
        ".settings",
        ".springBeans",
        "addonsrc",
        "beans.xsd",
        "bin",
        "classes",
        "commonwebsrc",
        "eclipsebin",
        "extensioninfo.xsd",
        "items.xsd",
        "platformhome.properties",
        "ruleset.xml",
        "testclasses"
    );

    @PropertyName("foldingEnabled")
    private boolean foldingEnabled = true;

    @PropertyName("useSmartFolding")
    private boolean useSmartFolding = true;

    @PropertyName("limitedSpringConfig")
    private boolean limitedSpringConfig = true;

    @PropertyName("groupModules")
    private boolean groupModules = true;

    @PropertyName("junkDirectoryList")
    private List<String> junkDirectoryList = defaultJunkFileNames;


    public HybrisIntegrationSettingsData() {
    }

    public boolean isFoldingEnabled() {
        return foldingEnabled;
    }

    public void setFoldingEnabled(final boolean foldingEnabled) {
        this.foldingEnabled = foldingEnabled;
    }

    public boolean isUseSmartFolding() {
        return useSmartFolding;
    }

    public void setUseSmartFolding(final boolean foldingEnabled) {
        this.useSmartFolding = foldingEnabled;
    }

    public boolean isLimitedSpringConfig() {
        return limitedSpringConfig;
    }

    public void setLimitedSpringConfig(final boolean limitedSpringConfig) {
        this.limitedSpringConfig = limitedSpringConfig;
    }

    public List<String> getJunkDirectoryList() {
        return junkDirectoryList;
    }

    public void setJunkDirectoryList(List<String> junkDirectoryList) {
        this.junkDirectoryList = junkDirectoryList;
    }

    public void setGroupModules(final boolean groupModules) {
        this.groupModules = groupModules;
    }

    public boolean isGroupModules() {
        return groupModules;
    }
}