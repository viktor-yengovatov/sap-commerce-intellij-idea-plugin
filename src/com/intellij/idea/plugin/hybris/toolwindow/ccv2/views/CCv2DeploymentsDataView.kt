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

import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons
import com.intellij.idea.plugin.hybris.settings.CCv2Subscription
import com.intellij.idea.plugin.hybris.tools.ccv2.dto.CCv2Deployment
import com.intellij.idea.plugin.hybris.toolwindow.ccv2.CCv2Tab
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.RightGap
import com.intellij.ui.dsl.builder.RowLayout
import com.intellij.ui.dsl.builder.panel

object CCv2DeploymentsDataView : AbstractCCv2DataView<CCv2Deployment>() {

    override val tab: CCv2Tab
        get() = CCv2Tab.DEPLOYMENTS

    override fun dataPanel(data: Map<CCv2Subscription, Collection<CCv2Deployment>>): DialogPanel = if (data.isEmpty()) noDataPanel()
    else panel {
        data.forEach { (subscription, builds) ->
            collapsibleGroup(subscription.toString()) {
                if (builds.isEmpty()) {
                    noData()
                } else {
                    builds.forEach { deployment(it) }
                }
            }
                .expanded = true
        }
    }
        .let { scrollPanel(it) }

    private fun Panel.deployment(deployment: CCv2Deployment) {
        row {
            panel {
                row {
                    label(deployment.code)
                        .comment(deployment.buildCode)
                        .bold()
                }
            }.gap(RightGap.COLUMNS)

            panel {
                row {
                    label(deployment.envCode)
                        .comment("Environment")
                }
            }.gap(RightGap.COLUMNS)

            panel {
                row {
                    label(deployment.status)
                        .comment("Status")
                }
            }.gap(RightGap.COLUMNS)

            panel {
                row {
                    icon(deployment.strategy.icon)
                    label(deployment.strategy.title)
                        .comment("Strategy")
                }
            }.gap(RightGap.COLUMNS)

            panel {
                row {
                    icon(deployment.updateMode.icon)
                    label(deployment.updateMode.title)
                        .comment("Mode")
                }
            }.gap(RightGap.COLUMNS)

            panel {
                row {
                    icon(HybrisIcons.CCV2_DEPLOYMENT_CREATED_BY)
                    label(deployment.createdBy)
                        .comment("Created by")
                }
            }.gap(RightGap.COLUMNS)

            panel {
                row {
                    label(deployment.createdTimeFormatted)
                        .comment("Created time")
                }
            }.gap(RightGap.COLUMNS)

            panel {
                row {
                    label(deployment.deployedTimeFormatted)
                        .comment("Deployed time")
                }
            }

        }.layout(RowLayout.PARENT_GRID)
    }
}