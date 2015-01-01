package com.intellij.idea.plugin.hybris.impex.formatting;

import com.intellij.formatting.Alignment;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 12:55 01 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ColumnsAlignmentStrategy implements AlignmentStrategy {

    private int columnNumber = 0;
    private final List<Alignment> alignments = new ArrayList<Alignment>();

    public ColumnsAlignmentStrategy() {
    }

    @Override
    public Alignment getAlignment(@NotNull final ASTNode currentNode) {

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
        if (isNewLine(currentNode)) {
            columnNumber = 0;
        } else if (isStartOfNewBlock(currentNode)) {
            alignments.clear();
        }
    }

    private boolean isNewLine(final ASTNode currentNode) {
        return ImpexTypes.CRLF == currentNode.getElementType();
    }

    private boolean isNewColumn(@NotNull final ASTNode currentNode) {
        return ImpexTypes.FIELD_VALUE_SEPARATOR == currentNode.getElementType();
    }

    private boolean isStartOfNewBlock(@NotNull final ASTNode currentNode) {
        return ImpexTypes.HEADER_MODE_INSERT == currentNode.getElementType()
               || ImpexTypes.HEADER_MODE_REMOVE == currentNode.getElementType()
               || ImpexTypes.HEADER_MODE_UPDATE == currentNode.getElementType()
               || ImpexTypes.HEADER_MODE_INSERT_UPDATE == currentNode.getElementType();
    }
}
