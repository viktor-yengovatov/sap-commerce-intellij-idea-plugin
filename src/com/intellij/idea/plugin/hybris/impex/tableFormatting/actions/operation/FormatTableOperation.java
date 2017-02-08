package com.intellij.idea.plugin.hybris.impex.tableFormatting.actions.operation;

import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableEditor;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableParser;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.ImpexTable;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.TableText;

import static com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableFormatter.formatter;

/**
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public class FormatTableOperation extends AbstractOperation {


    public FormatTableOperation(final ImpexTableEditor editor) {
        super(editor);
    }

    protected void perform() {
        final TableText tableText = getSelectedTable(editor);
        if (tableText.isNotEmpty()) {
            ImpexTable impexTable = new ImpexTableParser(tableText.getText()).parse();
            String formattedText = formatter().format(impexTable);
            editor.replaceText(formattedText, tableText.getRange());
        }
    }

}
