/*
 * This file is part of "SAP Commerce Developers Toolset" plugin for IntelliJ IDEA.
 * Copyright (C) 2019-2024 EPAM Systems <hybrisideaplugin@epam.com> and contributors
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

package com.intellij.idea.plugin.hybris.toolwindow.ccv2.views

import com.intellij.idea.plugin.hybris.settings.CCv2Subscription
import com.intellij.idea.plugin.hybris.tools.ccv2.dto.CCv2DTO
import com.intellij.idea.plugin.hybris.toolwindow.ccv2.CCv2Tab
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.JBEmptyBorder

abstract class AbstractCCv2DataView<T : CCv2DTO> {

    abstract val tab: CCv2Tab
    abstract fun dataPanel(project: Project, data: Map<CCv2Subscription, Collection<T>>): DialogPanel

    fun fetchingInProgress(): DialogPanel = panel {
        row {
            label("Fetching ${tab.title} data, Please wait...")
                .align(Align.CENTER)
                .resizableColumn()
        }.resizableRow()
    }

    fun noDataPanel(): DialogPanel = panel {
        noData()
    }

    protected fun Panel.noData() {
        row {
            label("No ${tab.title} data available. Try re-fetching remote data...")
                .align(Align.CENTER)
                .resizableColumn()
        }.resizableRow()
    }

    protected fun scrollPanel(content: DialogPanel) = panel {
        row {
            scrollCell(content)
                .align(Align.FILL)
                .resizableColumn()
                .applyToComponent {
                    (this.parent.parent as? JBScrollPane)
                        ?.border = JBEmptyBorder(0)
                }

        }.resizableRow()
    }
}