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

package com.intellij.idea.plugin.hybris.flexibleSearch.file.actions;

import com.intellij.idea.plugin.hybris.actions.AbstractCopyFileToHybrisConsoleAction;
import com.intellij.idea.plugin.hybris.actions.ActionUtils;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import static com.intellij.idea.plugin.hybris.common.HybrisConstants.*;

public class CopyFlexibleSearchFileAction extends AbstractCopyFileToHybrisConsoleAction {

    @Override
    public void update(@NotNull final AnActionEvent e) {
        final Project project = e.getProject();
        if (project != null) {
            e.getPresentation().setEnabledAndVisible(ActionUtils.isHybrisContext(project) && isRequiredFileExtension(
                project, FLEXIBLE_SEARCH_FILE_EXTENSION, true));
        }
    }

    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        final Project project = e.getProject();
        if (project != null) {
            performed(project, FLEXIBLE_SEARCH_CONSOLE_TITLE, getDialogTitle(FLEXIBLE_SEARCH_FILE_EXTENSION));
        }
    }
}
