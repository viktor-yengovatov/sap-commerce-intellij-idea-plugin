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
import com.intellij.idea.plugin.hybris.tools.remote.console.HybrisConsole
import com.intellij.idea.plugin.hybris.tools.remote.console.SolrConsole
import com.intellij.idea.plugin.hybris.tools.remote.http.HybrisHacHttpClient
import com.intellij.idea.plugin.hybris.tools.remote.http.impex.HybrisHttpResult
import com.intellij.idea.plugin.hybris.tools.remote.http.solr.SolrHttpClient
import com.intellij.idea.plugin.hybris.tools.remote.http.solr.SolrQueryObject
import com.intellij.openapi.fileTypes.PlainTextLanguage
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.components.JBLabel
import org.apache.commons.collections4.CollectionUtils
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.Insets
import java.util.*
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel
import javax.swing.border.EmptyBorder

class HybrisSolrSearchConsole(project: Project) : HybrisConsole(project, HybrisConstants.SOLR_SEARCH_CONSOLE_TITLE, PlainTextLanguage.INSTANCE), SolrConsole {

    object MyConsoleRootType : ConsoleRootType("hybris.solr.search.shell", null)

    private val panel = JPanel(FlowLayout(FlowLayout.LEFT, 0, 0))

    private val coresLabel = JBLabel("Select core: ")
    override val coresComboBox = ComboBox(CollectionComboBoxModel(retrieveListOfCores()), 270)

    private val maxRowsLabel = JBLabel("Rows: ")
    private val maxRowsSpinner = JSpinner(SpinnerNumberModel(10, 1, 500, 1))

    private val labelInsets = Insets(0, 10, 0, 1)

    init {
        createUI()
        ConsoleHistoryController(MyConsoleRootType, "hybris.solr.search.shell", this).install()
    }

    private fun createUI() {
        initCoresElements()
        initMaxRowsElements()

        add(panel, BorderLayout.NORTH)
        isEditable = true
        prompt = "q="
    }

    private fun initCoresElements() {
        coresLabel.border = EmptyBorder(labelInsets)
        panel.add(coresLabel)
        panel.add(coresComboBox)
    }

    private fun initMaxRowsElements() {
        maxRowsLabel.border = EmptyBorder(labelInsets)
        panel.add(maxRowsLabel)
        panel.add(maxRowsSpinner)
    }

    override fun printDefaultText() {
        this.setInputText("*:*")
    }

    override fun onSelection() {
        if (coresComboBox.selectedItem == null) {
            val cores = retrieveListOfCores()
            if (CollectionUtils.isNotEmpty(cores)) {
                coresComboBox.model = CollectionComboBoxModel(cores)
            }
        }
    }

    private fun retrieveListOfCores() = SolrHttpClient.getInstance(project).listOfCores(project).toList()

    override fun execute(query: String): HybrisHttpResult {
        return HybrisHacHttpClient.getInstance(project).executeSolrSearch(project, buildSolrQueryObject(query))
    }

    private fun buildSolrQueryObject(query: String): Optional<SolrQueryObject> {
        return Optional.ofNullable(coresComboBox.selectedItem)
                .map { it as String }
                .map { SolrQueryObject(query, it, maxRowsSpinner.value as Int) }
    }
}