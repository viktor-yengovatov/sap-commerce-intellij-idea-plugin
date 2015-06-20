package com.intellij.idea.plugin.hybris.impex.formatting;

import com.intellij.formatting.Alignment;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexFile;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexValueGroup;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 12:55 01 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ColumnsAlignmentStrategy implements AlignmentStrategy {

    private final List<Alignment> alignments = new ArrayList<Alignment>();
    private int columnNumber = 0;

    public ColumnsAlignmentStrategy() {
    }

    @Override
    public Alignment getAlignment(@NotNull final ASTNode currentNode) {
        Validate.notNull(currentNode);

        final Alignment alignment;

        if (isNewColumn(currentNode)) {
            if (columnNumber >= alignments.size()) {
                alignment = Alignment.createAlignment(true, Alignment.Anchor.LEFT);
                alignments.add(alignment);
            } else {
                alignment = alignments.get(columnNumber);
            }
            columnNumber++;
        } else {
            alignment = Alignment.createAlignment();
        }

        return alignment;
    }

    @Override
    public void processNode(@NotNull final ASTNode currentNode) {
        Validate.notNull(currentNode);

        if (isStartOfTheFile(currentNode)) {
            columnNumber = 0;
            alignments.clear();
        } else {
            if (isNewLine(currentNode)) {
                columnNumber = 0;
            } else if (isHeaderLine(currentNode)) {
                alignments.clear();
            }
        }
    }

    @Contract(pure = true)
    private boolean isStartOfTheFile(@Nullable final ASTNode currentNode) {
        return null != currentNode && currentNode.getPsi() instanceof ImpexFile;
    }

    @Contract(pure = true)
    private boolean isNewLine(@Nullable final ASTNode currentNode) {
        return null != currentNode
                && ImpexTypes.VALUE_GROUP == currentNode.getElementType()
                && isStartOfValueLine(currentNode);
    }

    @Contract(pure = true)
    private boolean isStartOfValueLine(@Nullable final ASTNode currentNode) {
        return null != currentNode
                && PsiTreeUtil.findChildOfType(currentNode.getTreeParent().getPsi(), ImpexValueGroup.class) == currentNode.getPsi();
    }

    @Contract(pure = true)
    private boolean isNewColumn(@Nullable final ASTNode currentNode) {
        return null != currentNode && ImpexTypes.VALUE_GROUP == currentNode.getElementType();
    }

    @Contract(pure = true)
    private boolean isHeaderLine(@Nullable final ASTNode currentNode) {
        return null != currentNode && ImpexTypes.HEADER_LINE == currentNode.getElementType();
    }
}
