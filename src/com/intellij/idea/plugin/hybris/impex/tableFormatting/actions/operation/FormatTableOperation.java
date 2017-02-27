package com.intellij.idea.plugin.hybris.impex.tableFormatting.actions.operation;

import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTable;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableEditor;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableFormatter;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;

/**
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public class FormatTableOperation extends AbstractOperation {


    public FormatTableOperation(final ImpexTableEditor editor) {
        super(editor);
    }

    @Override
    protected void perform() {
        final Pair<PsiElement, PsiElement> table = getSelectedTable(editor);
        if (table == null || table.first == null || table.second == null) {
            return;
        }
        final ImpexTable formattedTable = ImpexTableFormatter.format(table);
        editor.getIdeaEditor().getDocument().replaceString(
            formattedTable.getStartOffset(),
            formattedTable.getEndOffset(),
            formattedTable.getContent()
        );
    }


}
