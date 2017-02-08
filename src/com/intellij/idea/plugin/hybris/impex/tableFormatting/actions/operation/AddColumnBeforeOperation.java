package com.intellij.idea.plugin.hybris.impex.tableFormatting.actions.operation;

import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableEditor;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableParser;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.ImpexTable;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.TableText;

import static com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableFormatter.formatter;

public class AddColumnBeforeOperation extends AbstractOperation {


    public AddColumnBeforeOperation(final ImpexTableEditor editor) {
        super(editor);
    }

    @Override
    protected void perform() {
        TableText tableText = getSelectedTable(editor);
        if (tableText.isNotEmpty()) {
            ImpexTable impexTable = parseTable(tableText, editor.getCaretPosition());
            impexTable.addColumnBefore(impexTable.getSelectedColumn());
            String formattedText = formatter().format(impexTable);
            editor.replaceText(formattedText, tableText.getRange());
        }
    }

    private ImpexTable parseTable(TableText tableText, int caretPosition) {
        return new ImpexTableParser(tableText.getText())
            .detectingCellByPosition(caretPosition - tableText.getRange().getStart())
            .parse();
    }

}
