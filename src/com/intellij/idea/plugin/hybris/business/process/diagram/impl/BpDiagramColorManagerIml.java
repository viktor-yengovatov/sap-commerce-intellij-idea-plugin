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

package com.intellij.idea.plugin.hybris.business.process.diagram.impl;

import com.intellij.diagram.DiagramEdge;
import com.intellij.idea.plugin.hybris.business.process.diagram.BpDiagramColorManager;
import com.intellij.ui.JBColor;

import java.awt.*;

/**
 * Created 11:11 PM 31 January 2016.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class BpDiagramColorManagerIml extends BpDiagramColorManager {

    @Override
    public Color getEdgeColor(final DiagramEdge edge) {
        final String edgeType = edge.getRelationship().toString();

        if ("OK".equalsIgnoreCase(edgeType)) {
            return new JBColor(
                new Color(9, 128, 0),
                new Color(9, 128, 0)
            );
        }

        if ("NOK".equalsIgnoreCase(edgeType)) {
            return new JBColor(
                new Color(161, 49, 42),
                new Color(161, 49, 42)
            );
        }

        return new JBColor(
            new Color(71, 71, 71),
            new Color(155, 158, 161)
        );
    }
}
