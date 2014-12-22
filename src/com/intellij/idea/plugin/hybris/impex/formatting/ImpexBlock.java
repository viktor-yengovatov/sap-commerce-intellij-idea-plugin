package com.intellij.idea.plugin.hybris.impex.formatting;

import com.intellij.formatting.*;
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

        ASTNode currentNode = myNode.getFirstChildNode();

        int column = 0;
        List<Alignment> alignments = new ArrayList<Alignment>();

        while (currentNode != null) {
            if (currentNode.getElementType() == ImpexTypes.CRLF) {
                column = 0;
            }

            if (currentNode.getElementType() == ImpexTypes.HEADER_MODE_INSERT
                || currentNode.getElementType() == ImpexTypes.HEADER_MODE_REMOVE
                || currentNode.getElementType() == ImpexTypes.HEADER_MODE_UPDATE
                || currentNode.getElementType() == ImpexTypes.HEADER_MODE_INSERT_UPDATE) {
                alignments = new ArrayList<Alignment>();
            }

            if (currentNode.getElementType() != TokenType.WHITE_SPACE && currentNode.getElementType() != ImpexTypes.CRLF) {

                final Alignment alignment;

                if (currentNode.getElementType() == ImpexTypes.FIELD_VALUE_SEPARATOR) {
                    if (column >= alignments.size()) {
                        alignment = Alignment.createAlignment(true, Alignment.Anchor.LEFT);
                        alignments.add(alignment);
                    } else {
                        alignment = alignments.get(column);
                    }
                    column++;
                } else {
                    alignment = Alignment.createAlignment();
                }

                final Block block = new ImpexBlock(
                        currentNode,
                        Wrap.createWrap(WrapType.NONE, false),
                        alignment,
                        spacingBuilder);

                blocks.add(block);
            }

            currentNode = currentNode.getTreeNext();
        }

        return blocks;
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
