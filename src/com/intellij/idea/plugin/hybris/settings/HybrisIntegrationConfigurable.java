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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created 16:02 29 March 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisIntegrationConfigurable implements Configurable {

    protected final HybrisIntegrationSettingsForm settingsForm = new HybrisIntegrationSettingsForm();

    @Nls
    @Override
    public String getDisplayName() {
        return "Hybris Integration";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "Hybris Integration plugin configuration.";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        final HybrisIntegrationSettingsManager settingsManager = ApplicationManager.getApplication().getComponent(
            HybrisIntegrationSettingsManager.class
        );
        this.settingsForm.createComponent();
        this.settingsForm.setData(settingsManager.getHybrisIntegrationSettingsData());

        return this.settingsForm.getMainPanel();
    }

    @Override
    public boolean isModified() {
        final HybrisIntegrationSettingsManager settingsManager = ApplicationManager.getApplication().getComponent(
            HybrisIntegrationSettingsManager.class
        );

        return this.settingsForm.isModified(settingsManager.getHybrisIntegrationSettingsData());
    }

    @Override
    public void apply() throws ConfigurationException {
        final HybrisIntegrationSettingsManager settingsManager = ApplicationManager.getApplication().getComponent(
            HybrisIntegrationSettingsManager.class
        );

        this.settingsForm.getData(settingsManager.getHybrisIntegrationSettingsData());

        settingsManager.saveHybrisIntegrationSettingsData();
    }

    @Override
    public void reset() {
        final HybrisIntegrationSettingsManager settingsManager = ApplicationManager.getApplication().getComponent(
            HybrisIntegrationSettingsManager.class
        );

        this.settingsForm.setData(settingsManager.getHybrisIntegrationSettingsData());
    }

    @Override
    public void disposeUIResources() {
    }
}
