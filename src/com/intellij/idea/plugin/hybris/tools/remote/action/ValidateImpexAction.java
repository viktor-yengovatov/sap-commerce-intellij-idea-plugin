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

package com.intellij.idea.plugin.hybris.tools.remote.action;

import com.intellij.idea.plugin.hybris.tools.remote.console.ExecuteImpexConsole;
import com.intellij.idea.plugin.hybris.tools.remote.http.ImpexHttpResult;
import com.intellij.idea.plugin.hybris.tools.remote.http.ValidateImpexHttpClient;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
public class ValidateImpexAction extends AnAction {

    @Override
    public void actionPerformed(final AnActionEvent e) {
        final Editor editor = CommonDataKeys.EDITOR.getData(e.getDataContext());
        if (editor != null) {
            final SelectionModel selectionModel = editor.getSelectionModel();
            final ValidateImpexHttpClient client = new ValidateImpexHttpClient();
            final ImpexHttpResult impexHttpResult = client.validateImpex(selectionModel.getSelectedText());

            ExecuteImpexConsole.getInstance().show(impexHttpResult, e.getProject());
        }
    }
}
