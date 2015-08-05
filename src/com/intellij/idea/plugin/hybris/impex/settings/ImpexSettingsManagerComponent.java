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

package com.intellij.idea.plugin.hybris.impex.settings;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Created 19:39 29 March 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexSettingsManagerComponent implements ApplicationComponent, ImpexSettingsManager {

    protected final ImpexSettingsData settingsData = new ImpexSettingsData();

    @Override
    public void initComponent() {
        PropertiesComponent.getInstance().loadFields(this.settingsData);
    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return this.getClass().getName();
    }

    @NotNull
    @Override
    public ImpexSettingsData getImpexSettingsData() {
        return this.settingsData;
    }

    @Override
    public void saveImpexSettingsData() {
        PropertiesComponent.getInstance().saveFields(this.settingsData);
    }
}
