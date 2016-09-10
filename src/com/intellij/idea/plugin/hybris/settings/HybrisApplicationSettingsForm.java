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

package com.intellij.idea.plugin.hybris.settings;

import com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils;
import com.intellij.idea.plugin.hybris.project.configurators.impl.DefaultConfiguratorFactory;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.AddEditDeleteListPanel;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.CheckBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created 21:58 29 March 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisApplicationSettingsForm {

    private JCheckBox enableFoldingCheckBox;
    private JCheckBox useSmartFoldingCheckBox;
    private JCheckBox limitedSpringConfigCheckBox;
    private JPanel mainPanel;
    private JPanel junkDirectoriesPanel;
    private JLabel impexLabel;
    private JLabel projectImportLabel;
    private JCheckBox groupModulesCheckBox;
    private JTextField groupCustomTextField;
    private JTextField groupCustomUnusedTextField;
    private JTextField groupHybrisTextField;
    private JTextField groupHybrisUnusedTextField;
    private JCheckBox hideEmptyMiddleFoldersCheckBox;
    private JLabel projectTreeViewSettingsLabel;
    private JCheckBox defaultPlatformInReadOnly;
    private JTextField groupPlatformTextField;
    private JCheckBox createBackwardCyclicDependenciesForAddOns;

    private JunkListPanel junkListPanel;

    public void setData(final HybrisApplicationSettings data) {
        enableFoldingCheckBox.setSelected(data.isFoldingEnabled());
        useSmartFoldingCheckBox.setSelected(data.isUseSmartFolding());
        limitedSpringConfigCheckBox.setSelected(data.isLimitedSpringConfig());
        junkListPanel.setJunkDirectoryList(data.getJunkDirectoryList());
        groupModulesCheckBox.setSelected(data.isGroupModules());
        groupCustomTextField.setText(data.getGroupCustom());
        groupCustomUnusedTextField.setText(data.getGroupOtherCustom());
        groupHybrisTextField.setText(data.getGroupHybris());
        groupHybrisUnusedTextField.setText(data.getGroupOtherHybris());
        groupPlatformTextField.setText(data.getGroupPlatform());
        hideEmptyMiddleFoldersCheckBox.setSelected(data.isHideEmptyMiddleFolders());
        defaultPlatformInReadOnly.setSelected(data.isDefaultPlatformInReadOnly());
        createBackwardCyclicDependenciesForAddOns.setSelected(data.isCreateBackwardCyclicDependenciesForAddOns());
    }

    public void getData(final HybrisApplicationSettings data) {
        data.setFoldingEnabled(enableFoldingCheckBox.isSelected());
        data.setUseSmartFolding(useSmartFoldingCheckBox.isSelected());
        data.setLimitedSpringConfig(limitedSpringConfigCheckBox.isSelected());
        data.setJunkDirectoryList(junkListPanel.getJunkDirectoryList());
        data.setGroupModules(groupModulesCheckBox.isSelected());
        data.setGroupCustom(groupCustomTextField.getText());
        data.setGroupOtherCustom(groupCustomUnusedTextField.getText());
        data.setGroupHybris(groupHybrisTextField.getText());
        data.setGroupOtherHybris(groupHybrisUnusedTextField.getText());
        data.setGroupPlatform(groupPlatformTextField.getText());
        data.setHideEmptyMiddleFolders(hideEmptyMiddleFoldersCheckBox.isSelected());
        data.setDefaultPlatformInReadOnly(defaultPlatformInReadOnly.isSelected());
        data.setCreateBackwardCyclicDependenciesForAddOns(createBackwardCyclicDependenciesForAddOns.isSelected());
    }

    public boolean isModified(final HybrisApplicationSettings data) {
        if (enableFoldingCheckBox.isSelected() != data.isFoldingEnabled()) {
            return true;
        }
        if (useSmartFoldingCheckBox.isSelected() != data.isUseSmartFolding()) {
            return true;
        }
        if (limitedSpringConfigCheckBox.isSelected() != data.isLimitedSpringConfig()) {
            return true;
        }
        if (!junkListPanel.getJunkDirectoryList().equals(data.getJunkDirectoryList())) {
            return true;
        }
        if (groupModulesCheckBox.isSelected() != data.isGroupModules()) {
            return true;
        }
        if (!StringUtil.equals(groupCustomTextField.getText(),data.getGroupCustom())) {
            return true;
        }
        if (!StringUtil.equals(groupCustomUnusedTextField.getText(),data.getGroupOtherCustom())) {
            return true;
        }
        if (!StringUtil.equals(groupHybrisTextField.getText(),data.getGroupHybris())) {
            return true;
        }
        if (!StringUtil.equals(groupHybrisUnusedTextField.getText(),data.getGroupOtherHybris())) {
            return true;
        }
        if (!StringUtil.equals(groupPlatformTextField.getText(),data.getGroupPlatform())) {
            return true;
        }
        if (hideEmptyMiddleFoldersCheckBox.isSelected() != data.isHideEmptyMiddleFolders()) {
            return true;
        }
        if (defaultPlatformInReadOnly.isSelected() != data.isDefaultPlatformInReadOnly()) {
            return true;
        }
        if (createBackwardCyclicDependenciesForAddOns.isSelected() != data.isCreateBackwardCyclicDependenciesForAddOns()) {
            return true;
        }
        return false;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
        final int baselineVersion = ApplicationInfo.getInstance().getBuild().getBaselineVersion();
        final boolean boxVisible = baselineVersion < DefaultConfiguratorFactory.IDEA_2016_2_BASELINE_VERSION;
        limitedSpringConfigCheckBox = new JCheckBox();
        limitedSpringConfigCheckBox.setVisible(boxVisible);
        impexLabel = new JBLabel();
        impexLabel.setBorder(IdeBorderFactory.createTitledBorder(HybrisI18NBundleUtils.message("hybris.import.settings.impex.title"), false));
        projectImportLabel = new JBLabel();
        projectImportLabel.setBorder(IdeBorderFactory.createTitledBorder(HybrisI18NBundleUtils.message("hybris.import.settings.project.title")));
        junkListPanel = new JunkListPanel("hybris.import.settings.junk.directory.name", new ArrayList<String>());
        junkDirectoriesPanel = junkListPanel;

        projectTreeViewSettingsLabel = new JBLabel();
        projectTreeViewSettingsLabel.setBorder(IdeBorderFactory.createTitledBorder(HybrisI18NBundleUtils.message("hybris.project.view.tree.settings")));
    }

    public void createComponent() {
        groupModulesCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent changeEvent) {
                groupCustomTextField.setEnabled(groupModulesCheckBox.isSelected());
                groupCustomUnusedTextField.setEnabled(groupModulesCheckBox.isSelected());
                groupHybrisTextField.setEnabled(groupModulesCheckBox.isSelected());
                groupHybrisUnusedTextField.setEnabled(groupModulesCheckBox.isSelected());
                groupPlatformTextField.setEnabled(groupModulesCheckBox.isSelected());
            }
        });
    }

    private static class JunkListPanel extends AddEditDeleteListPanel<String> {

        private static final long serialVersionUID = -6339262026248471671L;

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
