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

package com.intellij.idea.plugin.hybris.flexibleSearch.formatting;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.formatting.WrapType;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.common.AbstractBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class FlexibleSearchBlock extends AbstractBlock {

    private final SpacingBuilder spacingBuilder;

    protected FlexibleSearchBlock(
        @NotNull final ASTNode node,
        @Nullable final Wrap wrap,
        @Nullable final Alignment alignment,
        @NotNull final SpacingBuilder spacingBuilder
    ) {

        super(node, wrap, alignment);

        this.spacingBuilder = spacingBuilder;
    }

    @Override
    protected List<Block> buildChildren() {
        List<Block> blocks = new ArrayList<>();
        ASTNode child = myNode.getFirstChildNode();
        while (child != null) {
            if (child.getElementType() != TokenType.WHITE_SPACE) {

                Block block = new FlexibleSearchBlock(
                    child,
                    Wrap.createWrap(WrapType.NONE, false),
                    null,
                    spacingBuilder
                );

                blocks.add(block);
                
            }
            child = child.getTreeNext();
        }
        return blocks;
    }

    @Nullable
    @Override
    public Alignment getAlignment() {
        if (this.getNode().getElementType() == FlexibleSearchTypes.QUERY_SPECIFICATION) {
            Alignment.createAlignment(true, Alignment.Anchor.LEFT);
        }
        return super.getAlignment();
    }

    @Override
    public Indent getIndent() {
        if (this.getNode().getElementType() == FlexibleSearchTypes.LEFT_DOUBLE_BRACE){
            return Indent.getNormalIndent();
        }
        return Indent.getSmartIndent(Indent.Type.NONE);
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable final Block child1, @NotNull final Block child2) {
        return spacingBuilder.getSpacing(this, child1, child2);
    }

    @Override
    public boolean isLeaf() {
        return myNode.getFirstChildNode() == null;
    }
}
