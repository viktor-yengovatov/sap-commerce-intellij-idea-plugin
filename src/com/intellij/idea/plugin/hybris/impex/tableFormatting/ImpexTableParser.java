package com.intellij.idea.plugin.hybris.impex.tableFormatting;

import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.ImpexTable;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.Row;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.Row.RowBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.intellij.idea.plugin.hybris.impex.tableFormatting.model.DelimitersCount.delimitersCountIn;
import static com.intellij.idea.plugin.hybris.impex.tableFormatting.util.ImpexTableUtil.isHeaderLine;
import static com.intellij.util.containers.ContainerUtil.newArrayList;

/**
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public class ImpexTableParser {

    static final String WIN_EOF = "\r\n";
    static final String LINUX_EOF = "\n";

    private List<Row> tableRows = newArrayList();

    private int maxRowSize = 0;

    private int caretPosition = -1;

    private int selectedRow = -1;

    private Character delimiter;

    private int selectedColumn = -1;

    private int currentLineStartIndex;

    private String notFormattedText;

    public ImpexTableParser(String notFormattedText) {
        this.notFormattedText = notFormattedText;
    }

    public ImpexTableParser detectingCellByPosition(int caretPosition) {
        this.caretPosition = caretPosition;
        return this;
    }

    public ImpexTable parse() {
        parseText();
        ImpexTable impexTable = new ImpexTable(tableRows);
        impexTable.setSelectedRow(selectedRow);
        impexTable.setSelectedColumn(selectedColumn);
        return impexTable;
    }

    private void parseText() {

        delimiter = detectDelimiter(notFormattedText);
        LineSplitter lineSplitter = new LineSplitter(notFormattedText);

        for (String line : lineSplitter) {
            boolean rowWithCaret = false;
            if (caretPosition >= lineSplitter.currentLineStartIndex() && caretPosition <= lineSplitter.currentLineEndIndex()) {
                selectedRow = lineSplitter.currentLineIndex();
                currentLineStartIndex = lineSplitter.currentLineStartIndex();
                rowWithCaret = true;
            }
            parseLine(line, lineSplitter.getEndOfLine(), rowWithCaret);
        }

        normalizeRows();
    }

    private void normalizeRows() {
        for (Row row : tableRows) {
            for (int i = row.size(); i < maxRowSize; i++) {
                row.add("");
            }
        }
    }

    private Character detectDelimiter(String text) {
        return delimitersCountIn(text).mostFrequent();
    }

    private void parseLine(String line, String endOfLine, boolean rowWithCaret) {
        Row row = splitForColumns(line, rowWithCaret, endOfLine);
        rememberMaxLength(row.size());
        tableRows.add(row);
    }

    private Row splitForColumns(String line, boolean rowWithCaret, String endOfLine) {
        List<ImpexTable.Cell> cells = new ArrayList<>();
        int rowCaretPosition = caretPosition - currentLineStartIndex;


        if (line.trim().toLowerCase().startsWith(";")) {
            line = new StringBuilder().append(" ").append(line).toString();
        }

        final ColumnSplitter tableRow = new ColumnSplitter(line, delimiter);

        int columnIndex = 0;
        for (final String value : tableRow) {
            if (columnIndex == 0 && isHeaderLine(line)) {
                if (line.trim().toLowerCase().startsWith(";")) {
                    cells.add(new ImpexTable.Cell(""));
                }
                columnIndex++;
                continue;
            }

            cells.add(new ImpexTable.Cell(value));
            if (rowWithCaret) {
                if (rowCaretPosition >= tableRow.currentColumnStartIndex() && rowCaretPosition <= tableRow.currentColumnEndIndex()) {
                    selectedColumn = tableRow.currentColumnIndex();
                } else if (tableRow.currentColumnIndex() == 0 && rowCaretPosition < tableRow.currentColumnStartIndex()) {
                    selectedColumn = 0;
                }
            }
        }
        if (rowWithCaret && selectedColumn == -1) {
            selectedColumn = cells.size();
        }

        final RowBuilder rowBuilder =
            RowBuilder.newInstance(cells, endOfLine)
                      .withIndentation(tableRow.getIndentetion())
                      .hasLeadingPipe(tableRow.hasLeadingPipe())
                      .isCommented(tableRow.isCommented())
                      .hasTrailingPipe(tableRow.hasTrailingPipe());

        return rowBuilder.build();
    }

    private void rememberMaxLength(int size) {
        if (size > maxRowSize) {
            maxRowSize = size;
        }
    }
}