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

import com.intellij.idea.plugin.hybris.utils.HybrisI18NBundleUtils;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.AddEditDeleteListPanel;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.components.JBLabel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

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
    private JPanel junkDirectoriesPanel;
    private JLabel impexLabel;
    private JLabel projectImportLabel;
    private JCheckBox groupModulesCheckBox;

    private JunkListPanel junkListPanel;

    public void setData(final HybrisIntegrationSettingsData data) {
        enableFoldingCheckBox.setSelected(data.isFoldingEnabled());
        useSmartFoldingCheckBox.setSelected(data.isUseSmartFolding());
        limitedSpringConfigComboBox.setSelected(data.isLimitedSpringConfig());
        junkListPanel.setJunkDirectoryList(data.getJunkDirectoryList());
        groupModulesCheckBox.setSelected(data.isGroupModules());
    }

    public void getData(final HybrisIntegrationSettingsData data) {
        data.setFoldingEnabled(enableFoldingCheckBox.isSelected());
        data.setUseSmartFolding(useSmartFoldingCheckBox.isSelected());
        data.setLimitedSpringConfig(limitedSpringConfigComboBox.isSelected());
        data.setJunkDirectoryList(junkListPanel.getJunkDirectoryList());
        data.setGroupModules(groupModulesCheckBox.isSelected());
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
        if (!junkListPanel.getJunkDirectoryList().equals(data.getJunkDirectoryList())) {
            return true;
        }
        return false;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
        impexLabel = new JBLabel();
        impexLabel.setBorder(IdeBorderFactory.createTitledBorder(HybrisI18NBundleUtils.message("hybris.import.settings.impex.title"), false));
        projectImportLabel = new JBLabel();
        projectImportLabel.setBorder(IdeBorderFactory.createTitledBorder(HybrisI18NBundleUtils.message("hybris.import.settings.project.title")));
        junkListPanel = new JunkListPanel("hybris.import.settings.junk.directory.name", new ArrayList<String>());
        junkDirectoriesPanel = junkListPanel;
    }

    private static class JunkListPanel extends AddEditDeleteListPanel<String> {

        public JunkListPanel(final String title, final java.util.List<String> initialList) {
            super(HybrisI18NBundleUtils.message(title), initialList);
        }

        public void setJunkDirectoryList(@Nullable java.util.List<String> itemList) {
            myListModel.clear();
            for (String itemToAdd: itemList) {
                super.addElement(itemToAdd);
            }
        }

        @Nullable
        @Override
        protected String findItemToAdd() {
            return showEditDialog("", "hybris.import.settings.junk.directory.popup.add.title", "hybris.import.settings.junk.directory.popup.add.text");
        }

        @Nullable
        @Override
        protected String editSelectedItem(@NotNull final String item) {
            return showEditDialog(item, "hybris.import.settings.junk.directory.popup.edit.title", "hybris.import.settings.junk.directory.popup.edit.text");
        }

        @Nullable
        private String showEditDialog(@NotNull final String initialValue, @NotNull final String title, @NotNull final String message) {
            return Messages.showInputDialog(this, HybrisI18NBundleUtils.message(message), HybrisI18NBundleUtils.message(title), Messages.getQuestionIcon(), initialValue, new InputValidatorEx() {
                @Override
                public boolean checkInput(@NotNull String inputString) {
                    return !StringUtil.isEmpty(inputString);
                }

                @Override
                public boolean canClose(@NotNull String inputString) {
                    return !StringUtil.isEmpty(inputString) && (!myListModel.contains(inputString) || initialValue.equals(inputString));
                }

                @Nullable
                @Override
                public String getErrorText(@NotNull String inputString) {
                    if (!checkInput(inputString)) {
                        return "directory name string cannot be empty";
                    }
                    if (!canClose(inputString)) {
                        return "duplicities are not allowed (nor make any sense)";
                    }
                    return null;
                }
            });
        }

        @NotNull
        public java.util.List<String> getJunkDirectoryList() {
            return (java.util.List<String>) Collections.list(myListModel.elements());
        }
    }
}
