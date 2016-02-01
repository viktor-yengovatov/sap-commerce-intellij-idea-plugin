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

import com.intellij.diagram.DiagramColorManagerBase;
import com.intellij.diagram.DiagramEdge;
import com.intellij.ui.JBColor;

import java.awt.*;

/**
 * Created 11:11 PM 31 January 2016.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class BusinessProcessDiagramColorManager extends DiagramColorManagerBase {

    @Override
    public Color getEdgeColor(final DiagramEdge edge) {
        final String edgeType = edge.getRelationship().toString();

        if ("SOFT".equals(edgeType)) {
            return new JBColor(new Color(9, 128, 0), new Color(83, 128, 103));
        }

        if ("STRONG".equals(edgeType)) {
            return new JBColor(new Color(0, 26, 128), new Color(140, 177, 197));
        }

        return super.getEdgeColor(edge);
    }
}
