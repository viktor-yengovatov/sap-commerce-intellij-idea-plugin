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

package com.intellij.idea.plugin.hybris.tools.remote.console.impl

import com.intellij.execution.console.ConsoleHistoryController
import com.intellij.execution.console.ConsoleRootType
import com.intellij.idea.plugin.hybris.common.HybrisConstants
import com.intellij.idea.plugin.hybris.flexibleSearch.FlexibleSearchLanguage
import com.intellij.idea.plugin.hybris.tools.remote.console.HybrisConsole
import com.intellij.idea.plugin.hybris.tools.remote.http.impex.HybrisHttpResult
import com.intellij.openapi.project.Project
import java.awt.BorderLayout
import javax.swing.JPanel

class HybrisFlexibleSearchConsole(project: Project) : HybrisConsole(project, HybrisConstants.FLEXIBLE_SEARCH_CONSOLE_TITLE, FlexibleSearchLanguage.getInstance()) {


    object MyConsoleRootType : ConsoleRootType("hybris.flexible.search.shell", null)

    private val panel = JPanel()

    init {
        createUI()
        ConsoleHistoryController(MyConsoleRootType, "hybris.flexible.search.shell", this).install()
    }

    private fun createUI() {
        add(panel, BorderLayout.NORTH)
        isEditable = true
    }

    override fun execute(query: String): HybrisHttpResult {
        return HybrisHttpResult.HybrisHttpResultBuilder.createResult().build()
    }
}