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

package com.intellij.idea.plugin.hybris.tools.remote.console.impl

import com.intellij.execution.console.ConsoleHistoryController
import com.intellij.execution.console.ConsoleRootType
import com.intellij.idea.plugin.hybris.common.HybrisConstants
import com.intellij.idea.plugin.hybris.statistics.StatsCollector
import com.intellij.idea.plugin.hybris.tools.remote.console.HybrisConsole
import com.intellij.idea.plugin.hybris.tools.remote.http.HybrisHacHttpClient
import com.intellij.idea.plugin.hybris.tools.remote.http.impex.HybrisHttpResult
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import org.jetbrains.plugins.groovy.GroovyLanguage
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class HybrisGroovyConsole(project: Project) : HybrisConsole(project, HybrisConstants.GROOVY_CONSOLE_TITLE, GroovyLanguage) {

    override fun collectStatistics() {
        StatsCollector.getInstance().collectStat(StatsCollector.ACTIONS.GROOVY_CONSOLE)
    }

    object MyConsoleRootType : ConsoleRootType("hybris.groovy.shell", null)

    private val panel = JPanel(FlowLayout(FlowLayout.LEFT, 0, 0))
    private val commitCheckbox = JBCheckBox()
    private val commitLabel = JBLabel("Commit mode: ")

    init {
        createUI()
        ConsoleHistoryController(MyConsoleRootType, "hybris.groovy.shell", this).install()
    }

    private fun createUI() {
        commitLabel.border = EmptyBorder(0, 10, 0, 3)
        commitCheckbox.border = EmptyBorder(0, 0, 0, 5)
        panel.add(commitLabel)
        panel.add(commitCheckbox)
        add(panel, BorderLayout.NORTH)
        isEditable = true
    }

    override fun execute(query: String): HybrisHttpResult {
        return HybrisHacHttpClient.getInstance(project).executeGroovyScript(project, query, commitCheckbox.isSelected)
    }
}