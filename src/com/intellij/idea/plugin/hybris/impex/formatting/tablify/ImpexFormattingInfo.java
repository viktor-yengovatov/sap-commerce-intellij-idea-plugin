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

package com.intellij.idea.plugin.hybris.impex.formatting.tablify;

import com.intellij.formatting.SpacingBuilder;
import com.intellij.idea.plugin.hybris.impex.formatting.ImpexCodeStyleSettings;
import com.intellij.lang.ASTNode;
import com.intellij.psi.codeStyle.CodeStyleSettings;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
public class ImpexFormattingInfo {

    private Map<ASTNode, Map<Integer, ImpexColumnInfo<ASTNode>>> infoColumnMap;

    private Map<ASTNode, ImpexColumnInfo<ASTNode>> reverseMap;

    public SpacingBuilder getSpacingBuilder() {
        return spacingBuilder;
    }

    private SpacingBuilder spacingBuilder;

    public ImpexCodeStyleSettings getImpexCodeStyleSettings() {
        return codeStyleSettings.getCustomSettings(ImpexCodeStyleSettings.class);
    }

    private CodeStyleSettings codeStyleSettings;

    public ImpexFormattingInfo(
        final CodeStyleSettings codeStyleSettings,
        final SpacingBuilder spacingBuilder,
        final Map<ASTNode, Map<Integer, ImpexColumnInfo<ASTNode>>> infoColumnMap
    ) {
        this.infoColumnMap = infoColumnMap;
        this.spacingBuilder = spacingBuilder;
        this.codeStyleSettings = codeStyleSettings;
        buildReverseMap();
    }

    private void buildReverseMap() {
        reverseMap = new HashMap<>();

        for (ImpexColumnInfo<ASTNode> columnInfo : infoColumnMap.values()
                                                                .stream()
                                                                .flatMap(it -> it.values().stream())
                                                                .collect(toList())) {
            for (ASTNode node : columnInfo.getElements()) {
                reverseMap.put(node, columnInfo);
            }
        }
    }

    public ImpexColumnInfo<ASTNode> getColumnInfo(final ASTNode node) {
        return reverseMap.get(node);
    }
}
