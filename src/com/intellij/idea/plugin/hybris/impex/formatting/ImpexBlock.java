/*
 * Copyright 2015 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.idea.plugin.hybris.impex.formatting;

import com.intellij.formatting.*;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexFile;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.lang.ASTNode;
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
        return ((ImpexFile) myNode.getPsi().getContainingFile()).getAlignmentStrategy();
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
