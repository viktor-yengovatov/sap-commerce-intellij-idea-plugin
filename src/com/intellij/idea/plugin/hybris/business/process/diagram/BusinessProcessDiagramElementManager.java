/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
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

package com.intellij.idea.plugin.hybris.business.process.diagram;

import com.intellij.diagram.AbstractDiagramElementManager;
import com.intellij.diagram.presentation.DiagramState;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.SimpleColoredText;
import org.jetbrains.annotations.Nullable;

/**
 * Created 11:12 PM 31 January 2016.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class BusinessProcessDiagramElementManager extends AbstractDiagramElementManager<VirtualFile> {

    @Nullable
    @Override
    public VirtualFile findInDataContext(final DataContext dataContext) {
        return CommonDataKeys.VIRTUAL_FILE.getData(dataContext);
    }

    @Override
    public boolean isAcceptableAsNode(final Object o) {
        return o instanceof VirtualFile;
    }

    @Nullable
    @Override
    public String getElementTitle(final VirtualFile t) {
        return t.getName();
    }

    @Nullable
    @Override
    public SimpleColoredText getItemName(final Object o, final DiagramState diagramState) {
        return null;
    }

    @Override
    public String getNodeTooltip(final VirtualFile t) {
        return null;
    }
}
