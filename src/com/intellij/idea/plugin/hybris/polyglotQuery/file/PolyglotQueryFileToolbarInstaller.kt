/*
 * This file is part of "SAP Commerce Developers Toolset" plugin for IntelliJ IDEA.
 * Copyright (C) 2019-2025 EPAM Systems <hybrisideaplugin@epam.com> and contributors
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

package com.intellij.idea.plugin.hybris.polyglotQuery.file

import com.intellij.idea.plugin.hybris.startup.event.AbstractHybrisFileToolbarInstaller
import com.intellij.openapi.components.Service
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.project.Project
import com.intellij.util.application

@Service
class PolyglotQueryFileToolbarInstaller : AbstractHybrisFileToolbarInstaller(
    "hybris.pgq.console",
    "hybris.pgq.toolbar.left",
    "hybris.pgq.toolbar.right",
    PolyglotQueryFileType
) {

    companion object {
        fun getInstance(): PolyglotQueryFileToolbarInstaller = application.getService(PolyglotQueryFileToolbarInstaller::class.java)
    }

    override fun isToolbarEnabled(project: Project, editor: EditorEx) = true
}