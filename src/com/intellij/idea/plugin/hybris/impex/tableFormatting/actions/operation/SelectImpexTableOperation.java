package com.intellij.idea.plugin.hybris.impex.tableFormatting.actions.operation;

import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableEditor;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.util.Range;

/**
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public class SelectImpexTableOperation extends AbstractOperation {

    public SelectImpexTableOperation(final ImpexTableEditor editor) {
        super(editor);
    }

    @Override
    protected void perform() {
        final Pair<PsiElement, PsiElement> table = getSelectedTable(editor);
        if (table != null) {
            final int startOffset = table.first.getTextRange().getStartOffset();
            final int endOffset = table.second.getTextRange().getEndOffset();
            editor.setSelection(new Range<>(startOffset, endOffset));
        }
    }

}
