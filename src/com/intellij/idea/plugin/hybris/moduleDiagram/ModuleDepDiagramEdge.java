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

package com.intellij.idea.plugin.hybris.moduleDiagram;

import com.intellij.diagram.DiagramEdgeBase;
import com.intellij.diagram.DiagramRelationshipInfo;
import org.jetbrains.annotations.NotNull;

/**
 * @author Eugene.Kudelevsky
 */
public class ModuleDepDiagramEdge extends DiagramEdgeBase<ModuleDepDiagramItem> {

    private int circularId = 0;

    public ModuleDepDiagramEdge(
        @NotNull final ModuleDepDiagramNode from,
        @NotNull final ModuleDepDiagramNode to,
        @NotNull final DiagramRelationshipInfo relationship
        ) {
        super(from, to, relationship);
    }

    public boolean isCircular() {
        return circularId > 0;
    }

    public int getCircularId() {
        return circularId;
    }

    public void setCircularId(final int circularId) {
        this.circularId = circularId;
    }
}
