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
