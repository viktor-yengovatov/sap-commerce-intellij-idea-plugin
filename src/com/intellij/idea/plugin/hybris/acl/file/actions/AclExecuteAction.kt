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

package com.intellij.idea.plugin.hybris.acl.file.actions

import com.intellij.idea.plugin.hybris.acl.file.AclFileType
import com.intellij.idea.plugin.hybris.actions.AbstractExecuteAction
import com.intellij.idea.plugin.hybris.common.HybrisConstants
import com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils
import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread

class AclExecuteAction : AbstractExecuteAction(
    AclFileType.defaultExtension,
    HybrisConstants.CONSOLE_TITLE_IMPEX
) {

    init {
        with(templatePresentation) {
            text = HybrisI18NBundleUtils.message("hybris.acl.actions.execute_query")
            description = HybrisI18NBundleUtils.message("hybris.acl.actions.execute_query.description")
            icon = HybrisIcons.Console.Actions.EXECUTE
        }
    }

    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}