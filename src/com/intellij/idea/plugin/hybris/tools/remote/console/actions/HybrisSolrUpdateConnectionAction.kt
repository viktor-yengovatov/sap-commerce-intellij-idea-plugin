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

package com.intellij.idea.plugin.hybris.tools.remote.console.actions

import com.intellij.codeInsight.lookup.LookupManager
import com.intellij.icons.AllIcons.Actions.Refresh
import com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils
import com.intellij.idea.plugin.hybris.tools.remote.console.SolrConsole
import com.intellij.idea.plugin.hybris.tools.remote.console.view.HybrisTabs
import com.intellij.idea.plugin.hybris.tools.remote.http.SolrHttpClient
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import javax.swing.DefaultComboBoxModel

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
class HybrisSolrUpdateConnectionAction(private val tabbedPane: HybrisTabs) : AnAction(
        HybrisI18NBundleUtils.message("action.console.hybris.solr.connection.update.message.text"),
        HybrisI18NBundleUtils.message("action.console.hybris.solr.connection.update.message.title"),
        Refresh) {

    override fun actionPerformed(e: AnActionEvent) {
        val solrConsole = tabbedPane.activeConsole()
        if (solrConsole is SolrConsole) {
            val project = e.project!!
            val model = DefaultComboBoxModel(SolrHttpClient.getInstance(project).listOfCores(project))
            solrConsole.coresComboBox.model = model
        }
    }

    override fun update(e: AnActionEvent) {
        val editor = tabbedPane.activeConsole().consoleEditor
        val lookup = LookupManager.getActiveLookup(editor)

        e.presentation.isEnabled = (lookup == null || !lookup.isCompletion) && tabbedPane.activeConsole() is SolrConsole

        e.presentation.isVisible = tabbedPane.activeConsole() is SolrConsole
    }

}