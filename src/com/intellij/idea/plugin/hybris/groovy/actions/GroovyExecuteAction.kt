/*
 * This file is part of "SAP Commerce Developers Toolset" plugin for Intellij IDEA.
 * Copyright (C) 2014-2016 Alexander Bartash <AlexanderBartash@gmail.com>
 * Copyright (C) 2019-2023 EPAM Systems <hybrisideaplugin@epam.com> and contributors
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
package com.intellij.idea.plugin.hybris.groovy.actions

import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons

class GroovyExecuteInCommitModeOnAction : AbstractGroovyExecuteAction(
    "Execute Groovy Script<br/>Commit Mode <strong><font color='#57965C'>ON</font></strong>",
    "Execute Groovy Script on a remote SAP Commerce instance",
    HybrisIcons.CONSOLE_EXECUTE,
    true
)
class GroovyExecuteInCommitModeOffAction : AbstractGroovyExecuteAction(
    "Execute Groovy Script<br/>Commit Mode <strong><font color='#C75450'>OFF</font></strong>",
    "Execute Groovy Script on a remote SAP Commerce instance",
    HybrisIcons.CONSOLE_EXECUTE_COMMIT_MODE_OFF,
    false
)
