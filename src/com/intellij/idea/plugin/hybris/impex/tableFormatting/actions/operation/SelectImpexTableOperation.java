package com.intellij.idea.plugin.hybris.impex.tableFormatting.actions.operation;

import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableEditor;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.TableText;

/**
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public class SelectImpexTableOperation extends AbstractOperation {

    public SelectImpexTableOperation(final ImpexTableEditor editor) {
        super(editor);
    }

    @Override
    protected void perform() {
        final TableText tableText = getSelectedTable(editor);
        if (tableText.isNotEmpty()) {
            editor.setSelection(tableText.getRange());
        }
    }

}
