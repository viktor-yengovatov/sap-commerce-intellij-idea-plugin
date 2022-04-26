/*
 * This file is part of "SAP Commerce Developers Toolset" plugin for Intellij IDEA.
 * Copyright (C) 2019 EPAM Systems <hybrisideaplugin@epam.com>
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

package com.intellij.idea.plugin.hybris.actions;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.ide.projectView.impl.AbstractProjectViewPane;
import com.intellij.idea.plugin.hybris.tools.remote.console.HybrisConsole;
import com.intellij.idea.plugin.hybris.tools.remote.console.view.HybrisConsolePanel;
import com.intellij.idea.plugin.hybris.tools.remote.console.view.HybrisConsolePanelView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractCopyFileToHybrisConsoleAction extends AnAction {

    public static boolean isRequiredFileExtension(
        @NotNull final DataContext dataContext,
        final String fileExtension,
        final boolean oneFile
    ) {
        boolean isImpex = false;
        final List<String> filesName = getFilesName(dataContext);
        if (oneFile) {
            return filesName.size() == 1 && filesName.get(0).contains(fileExtension);
        } else {
            for (String name : filesName) {
                if (name.contains(fileExtension)) {
                    isImpex = true;
                } else {
                    return false;
                }
            }
        }
        return isImpex;
    }

    private static List<String> getFilesName(@NotNull final DataContext dataContext) {
        final Object[] files = getFiles(dataContext);
        final List<String> names = new ArrayList<>();
        if (files != null) {
            for (final Object file : files) {
                final TreePath treePath = (TreePath) file;
                names.add(treePath.getLastPathComponent().toString());
            }
        }
        return names;
    }

    public static Object[] getFiles(@NotNull final DataContext dataContext) {
        final Project project = CommonDataKeys.PROJECT.getData(dataContext);
        if (project != null) {
            final AbstractProjectViewPane currentProjectViewPane = ProjectView.getInstance(project)
                                                                              .getCurrentProjectViewPane();
            return currentProjectViewPane.getSelectionPaths();
        }
        return new Object[0];
    }

    public static void copyToHybrisConsole(final Project project, final String consoleTitle, final String query) {
        final HybrisConsolePanel hybrisConsolePanel = HybrisConsolePanelView.Companion.getInstance(Objects.requireNonNull(
            project)).getConsolePanel();
        final HybrisConsole hybrisConsole = hybrisConsolePanel.findConsole(consoleTitle);
        if (hybrisConsole != null) {
            hybrisConsole.clear();
            hybrisConsole.setInputText(query);
            hybrisConsolePanel.setActiveConsole(hybrisConsole);
        }
    }
}
