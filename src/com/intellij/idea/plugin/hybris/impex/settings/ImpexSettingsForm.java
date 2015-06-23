/*
 * Copyright 2015 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.idea.plugin.hybris.impex.settings;

import javax.swing.*;

/**
 * Created 21:58 29 March 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexSettingsForm {

    private JCheckBox enableFoldingCheckBox;
    private JCheckBox useSmartFoldingCheckBox;
    private JPanel mainPanel;

    public void setData(final ImpexSettingsData data) {
        enableFoldingCheckBox.setSelected(data.isFoldingEnabled());
        useSmartFoldingCheckBox.setSelected(data.isUseSmartFolding());
    }

    public void getData(final ImpexSettingsData data) {
        data.setFoldingEnabled(enableFoldingCheckBox.isSelected());
        data.setUseSmartFolding(useSmartFoldingCheckBox.isSelected());
    }

    public boolean isModified(final ImpexSettingsData data) {
        if (enableFoldingCheckBox.isSelected() != data.isFoldingEnabled()) {
            return true;
        }
        if (useSmartFoldingCheckBox.isSelected() != data.isUseSmartFolding()) {
            return true;
        }
        return false;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
