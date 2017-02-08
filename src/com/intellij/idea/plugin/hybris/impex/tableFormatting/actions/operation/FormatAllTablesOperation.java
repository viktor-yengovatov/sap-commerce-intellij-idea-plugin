package com.intellij.idea.plugin.hybris.impex.tableFormatting.actions.operation;

import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableEditor;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableParser;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.ImpexTable;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.Range;

import static com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableFormatter.formatter;
import static com.intellij.idea.plugin.hybris.impex.tableFormatting.util.ImpexTableUtil.detectTableIn;


/**
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public class FormatAllTablesOperation extends AbstractOperation {

    public FormatAllTablesOperation(final ImpexTableEditor editor) {
        super(editor);
    }

    @Override
    protected void perform() {
        formatNext(editor.getText(), editor.getText().length() - 1);
        editor.setSelection(new Range(0, 0));
    }

    private void formatNext(String text, int position) {
        if (position < 0) {
            return;
        }

        final Range tableRange = detectTableIn(text).around(position);
        if (tableRange.isNotEmpty()) {
            formatAndReplace(text, tableRange);
            formatNext(text, tableRange.getStart() - 1);
        } else {
            formatNext(text, text.lastIndexOf("\n", position) - 1);
        }
    }

    private void formatAndReplace(String text, Range tableRange) {
        final String textToFormat = text.substring(tableRange.getStart(), tableRange.getEnd());
        final ImpexTable impexTable = parse(textToFormat);
        if (impexTable.getRowCount() > 1) {
            String formattedText = formatter().format(impexTable);
            editor.replaceText(formattedText, tableRange);
        }
    }

    private ImpexTable parse(String textToFormat) {
        return new ImpexTableParser(textToFormat).parse();
    }
}
