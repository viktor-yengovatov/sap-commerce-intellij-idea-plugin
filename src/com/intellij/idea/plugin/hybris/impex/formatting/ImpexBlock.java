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

package com.intellij.idea.plugin.hybris.impex.formatting;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.common.AbstractBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 22:21 21 December 2014
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexBlock extends AbstractBlock {

    private final SpacingBuilder spacingBuilder;

    protected ImpexBlock(@NotNull final ASTNode node,
                         @Nullable final Wrap wrap,
                         @Nullable final Alignment alignment,
                         @NotNull final SpacingBuilder spacingBuilder) {

        super(node, wrap, alignment);

        this.spacingBuilder = spacingBuilder;
    }

    @Override
    protected List<Block> buildChildren() {
        final List<Block> blocks = new ArrayList<Block>();

        final AlignmentStrategy alignmentStrategy = getAlignmentStrategy();
        alignmentStrategy.processNode(myNode);

        ASTNode currentNode = myNode.getFirstChildNode();

        while (null != currentNode) {

            // Unpack 'Value Line' as columns will not be aligned if they do not share the same parent
            if (ImpexTypes.VALUE_LINE == currentNode.getElementType()) {
                currentNode = currentNode.getFirstChildNode();
            }

            alignmentStrategy.processNode(currentNode);

            if (isNotWhitespaceOrNewLine(currentNode)) {

                final Block block = new ImpexBlock(
                        currentNode,
                        null,
                        alignmentStrategy.getAlignment(currentNode),
                        spacingBuilder);

                blocks.add(block);
            }

            // Unpack Value Line
            if (isEndOfValueLine(currentNode)) {
                currentNode = currentNode.getTreeParent().getTreeNext();
            } else {
                currentNode = currentNode.getTreeNext();
            }
        }

        return blocks;
    }

    private boolean isEndOfValueLine(final ASTNode currentNode) {
        return null == currentNode.getTreeNext() && ImpexTypes.VALUE_LINE == currentNode.getTreeParent().getElementType();
    }

    private AlignmentStrategy getAlignmentStrategy() {
        return ServiceManager.getService(AlignmentStrategy.class);
    }

    private boolean isNotWhitespaceOrNewLine(final ASTNode currentNode) {
        return TokenType.WHITE_SPACE != currentNode.getElementType()
               && ImpexTypes.CRLF != currentNode.getElementType();
    }

    @Override
    public Indent getIndent() {
        return Indent.getNoneIndent();
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
