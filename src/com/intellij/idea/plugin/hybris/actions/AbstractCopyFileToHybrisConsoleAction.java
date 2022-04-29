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

import com.intellij.execution.console.ConsoleExecutionEditor;
import com.intellij.execution.console.LanguageConsoleImpl;
import com.intellij.ide.projectView.ProjectView;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.impl.AbstractProjectViewPane;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils;
import com.intellij.idea.plugin.hybris.tools.remote.console.HybrisConsole;
import com.intellij.idea.plugin.hybris.tools.remote.console.HybrisConsoleToolWindowFactory;
import com.intellij.idea.plugin.hybris.tools.remote.console.view.HybrisConsolePanel;
import com.intellij.idea.plugin.hybris.tools.remote.console.view.HybrisConsolePanelView;
import com.intellij.idea.plugin.hybris.toolwindow.CopyFileToHybrisConsoleDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

import static com.intellij.idea.plugin.hybris.common.HybrisConstants.*;

public abstract class AbstractCopyFileToHybrisConsoleAction extends AnAction {

    public void performed(final Project project, final String consoleTitle, final String dialogTitle) {
        if (project != null) {
            final TreePath[] paths = getPaths(project);
            final StringBuilder query = new StringBuilder();
            for (final TreePath treePath : paths) {
                final DefaultMutableTreeNode lastPathNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
                final PsiFileNode file = (PsiFileNode) lastPathNode.getUserObject();
                query.append(file.getValue().getText()).append(System.lineSeparator());
            }
            final HybrisConsolePanel hybrisConsolePanel = getHybrisConsolePanel(project);
            final HybrisConsole hybrisConsole = hybrisConsolePanel.findConsole(consoleTitle);
            if (hybrisConsole != null) {
                if (!getTextFromHybrisConsole(project, hybrisConsole).isEmpty()) {
                    final CopyFileToHybrisConsoleDialog copyFileToHybrisConsoleDialog = new CopyFileToHybrisConsoleDialog(
                        project);
                    copyFileToHybrisConsoleDialog.setTitle(dialogTitle);
                    if (copyFileToHybrisConsoleDialog.showAndGet()) {
                        copyToHybrisConsole(project, consoleTitle, query.toString());
                    }
                } else {
                    copyToHybrisConsole(project, consoleTitle, query.toString());
                }
            }
        }
    }

    public boolean isRequiredFileExtension(
        @NotNull final Project project,
        final String fileExtension,
        final boolean oneFile
    ) {
        boolean isImpex = false;
        final List<String> fileExtensions = getFileExtensions(project);
        if (oneFile) {
            return fileExtensions.size() == 1 && fileExtensions.get(0).equals(fileExtension);
        } else {
            for (String extension : fileExtensions) {
                if (extension.equals(fileExtension)) {
                    isImpex = true;
                } else {
                    return false;
                }
            }
        }
        return isImpex;
    }

    private List<String> getFileExtensions(@NotNull final Project project) {
        final TreePath[] paths = getPaths(project);
        final List<String> names = new ArrayList<>();
        for (final TreePath treePath : paths) {
            final DefaultMutableTreeNode lastPathNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            final ProjectViewNode file = (ProjectViewNode) lastPathNode.getUserObject();
            final VirtualFile virtualFile = file.getVirtualFile();
            if (virtualFile != null && !virtualFile.isDirectory()) {
                names.add(file.getVirtualFile().getExtension());
            }
        }
        return names;
    }

    public TreePath[] getPaths(@NotNull final Project project) {
        final AbstractProjectViewPane currentProjectViewPane = ProjectView.getInstance(project)
                                                                          .getCurrentProjectViewPane();
        final TreePath[] selectionPaths = currentProjectViewPane.getSelectionPaths();
        return selectionPaths == null ? new TreePath[0] : selectionPaths;
    }

    public HybrisConsolePanel getHybrisConsolePanel(final Project project) {
        return HybrisConsolePanelView.Companion.getInstance(project).getConsolePanel();
    }

    public String getTextFromHybrisConsole(final Project project, final HybrisConsole hybrisConsole) {
        final LanguageConsoleImpl.Helper helper = new LanguageConsoleImpl.Helper(
            project,
            hybrisConsole.getVirtualFile()
        );
        final ConsoleExecutionEditor consoleExecutionEditor = new ConsoleExecutionEditor(helper);
        return consoleExecutionEditor.getDocument().getText();
    }

    public void copyToHybrisConsole(
        final Project project,
        final String consoleTitle,
        final String query
    ) {
        final HybrisConsolePanel hybrisConsolePanel = getHybrisConsolePanel(project);
        final HybrisConsole hybrisConsole = hybrisConsolePanel.findConsole(consoleTitle);
        if (hybrisConsole != null) {
            hybrisConsole.clear();
            hybrisConsole.setInputText(query);
            hybrisConsolePanel.setActiveConsole(hybrisConsole);
            final ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(
                HybrisConsoleToolWindowFactory.ID);
            if (toolWindow != null) {
                toolWindow.activate(null);
            }
        }
    }

    public String getDialogTitle(final String fileExtension) {
        return HybrisI18NBundleUtils.message(DIALOG_TITLE + fileExtension);
    }
}
