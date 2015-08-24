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

import javax.swing.*;

/**
 * Created 21:58 29 March 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisIntegrationSettingsForm {

    private JCheckBox enableFoldingCheckBox;
    private JCheckBox useSmartFoldingCheckBox;
    private JCheckBox limitedSpringConfigComboBox;
    private JPanel mainPanel;

    public void setData(final HybrisIntegrationSettingsData data) {
        enableFoldingCheckBox.setSelected(data.isFoldingEnabled());
        useSmartFoldingCheckBox.setSelected(data.isUseSmartFolding());
        limitedSpringConfigComboBox.setSelected(data.isLimitedSpringConfig());
    }

    public void getData(final HybrisIntegrationSettingsData data) {
        data.setFoldingEnabled(enableFoldingCheckBox.isSelected());
        data.setUseSmartFolding(useSmartFoldingCheckBox.isSelected());
        data.setLimitedSpringConfig(limitedSpringConfigComboBox.isSelected());
    }

    public boolean isModified(final HybrisIntegrationSettingsData data) {
        if (enableFoldingCheckBox.isSelected() != data.isFoldingEnabled()) {
            return true;
        }
        if (useSmartFoldingCheckBox.isSelected() != data.isUseSmartFolding()) {
            return true;
        }
        if (limitedSpringConfigComboBox.isSelected() != data.isLimitedSpringConfig()) {
            return true;
        }
        return false;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
