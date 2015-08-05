/*
 * This file is part of "Hybris Integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2015 Alexander Bartash <AlexanderBartash@gmail.com>
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

package com.intellij.idea.plugin.hybris.utils;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.EditorBundle;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

/**
 * Created 19:34 11 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public final class CommonIdeaUtils {

    private CommonIdeaUtils() throws IllegalAccessException {
        throw new IllegalAccessException("Should never be accessed.");
    }

    @Nullable
    public static Project getCurrentProject() {
        return CommonDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext());
    }

    public static boolean isTypingActionInProgress() {
        return StringUtils.equals(
                CommandProcessor.getInstance().getCurrentCommandName(),
                EditorBundle.message("typing.in.editor.command.name")
        );
    }
}
