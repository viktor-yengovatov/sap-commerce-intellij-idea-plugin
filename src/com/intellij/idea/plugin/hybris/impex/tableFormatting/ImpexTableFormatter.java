package com.intellij.idea.plugin.hybris.impex.tableFormatting;

import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.ImpexTable;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.Row;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

import static com.intellij.idea.plugin.hybris.impex.tableFormatting.util.ImpexTableFormatterConstants.PIPE;
import static com.intellij.idea.plugin.hybris.impex.tableFormatting.util.ImpexTableFormatterConstants.PIPE_COMMENT_START;


public class ImpexTableFormatter {

    private ImpexTableFormatter() {
    }

    public String format(final ImpexTable impexTable) {
        final String result = formatPipeTable(impexTable);
        if (result.contains("\r\n")) {
            // All documents inside IntelliJ IDEA always use the \n line separator. 
            // The correct line separator is put in when saving the files.
            return result.replaceAll("\\r\\n", "\n");
        }
        return result;
    }

    @NotNull
    private String formatPipeTable(ImpexTable table) {
        final int[] columnsMaxLength = calculateColumnsMaxLength(table);

        final StringBuilder buffer = new StringBuilder();
        for (Row row : table.rows()) {
            if (StringUtil.isEmptyOrSpaces(row.line())) {
                buffer.append(System.lineSeparator());
                continue;
            }
            appendFirstCommentedSign(buffer, row);
            int columnIndex = 0;
            for (ImpexTable.Cell cell : row.columns()) {
                int width = correctWidthForCommentedRow(columnsMaxLength[columnIndex], row, columnIndex);
                String formattedValue = padValue(width, cell.getValue());
                buffer.append(formattedValue);
                columnIndex++;
                appendInternalPipe(buffer, row, columnIndex);
            }
            if (row.hasTrailingPipe()) {
                appendLastPipe(buffer, row);
            }
            buffer.append(row.endOfLine());
        }
        return buffer.toString();
    }

    private int correctWidthForCommentedRow(int maxWidth, Row row, int columnIndex) {
        return maxWidth - (row.isCommented() && (columnIndex == 0 || columnIndex == row.size() - 1) ? 2 : 0);
    }

    private void appendLastPipe(StringBuilder buffer, Row row) {
        String pipe = Character.toString(PIPE);
        if (row.hasTrailingPipe()) {
            buffer.append(" ").append(pipe);
        }
    }

    private void appendFirstCommentedSign(StringBuilder buffer, Row row) {
        if (row.isCommented()) {
            buffer.append(PIPE_COMMENT_START);
        }
    }

    private void appendInternalPipe(StringBuilder buffer, Row row, int columnIndex) {
        if (columnIndex < row.size()) {
            buffer.append(" ").append(PIPE).append(" ");
        }
    }

    private String padValue(int width, String value) {
        if (width > 0) {
            return String.format("%-" + width + "s", value);
        } else {
            return "";
        }
    }

    private int[] calculateColumnsMaxLength(ImpexTable table) {
        int[] columnsMaxLength = new int[table.getColumnCount()];

        for (Row row : table.rows()) {
            int columnIndex = 0;
            for (ImpexTable.Cell cell : row.columns()) {
                int length = cellValueLength(row, columnIndex, cell);
                if (length > columnsMaxLength[columnIndex]) {
                    columnsMaxLength[columnIndex] = length;
                }
                columnIndex++;
            }
        }
        return columnsMaxLength;
    }

    private int cellValueLength(Row row, int columnIndex, ImpexTable.Cell cell) {
        return cell.getValue().length() +
               (row.isCommented() && (columnIndex == 0 || columnIndex == row.size() - 1) ? 2 : 0);
    }

    public static ImpexTableFormatter formatter() {
        return new ImpexTableFormatter();
    }

}
