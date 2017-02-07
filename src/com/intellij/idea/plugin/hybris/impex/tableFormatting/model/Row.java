package com.intellij.idea.plugin.hybris.impex.tableFormatting.model;

import com.google.common.base.Joiner;

import java.util.List;

import static com.intellij.util.containers.ContainerUtil.newArrayList;

/**
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public class Row {

    List<ImpexTable.Cell> cells;
    private String endOfLine;
    private boolean commented;
    private String indentation;
    private boolean leadingPipe;
    private boolean trailingPipe;

    private Row(List<ImpexTable.Cell> columns, String endOfLine) {
        this.endOfLine = endOfLine != null ? endOfLine : "";
        cells = newArrayList(columns);
    }

    public String line() {
        return Joiner.on(" ").skipNulls().join(columns());
    }

    public int size() {
        return cells.size();
    }

    public void add(String value) {
        cells.add(new ImpexTable.Cell(value));
    }

    public void add(int column, String value) {
        cells.add(column, new ImpexTable.Cell(value));
    }

    public String get(int column) {
        return cells.get(column).getValue();
    }

    public ImpexTable.Cell[] columns() {
        return cells.toArray(new ImpexTable.Cell[cells.size()]);
    }

    public String endOfLine() {
        return endOfLine;
    }

    public boolean isCommented() {
        return commented;
    }

    private void setCommented(boolean commented) {
        this.commented = commented;
    }

    public String getIndentation() {
        return indentation;
    }

    private void setIndentation(String indentation) {
        this.indentation = indentation;
    }

    public boolean hasLeadingPipe() {
        return leadingPipe;
    }

    public boolean hasTrailingPipe() {
        return trailingPipe;
    }

    private void setLeadingPipe(boolean leadingPipe) {
        this.leadingPipe = leadingPipe;
    }

    private void setTrailingPipe(boolean trailingPipe) {
        this.trailingPipe = trailingPipe;
    }


    public static class RowBuilder {

        private Row row;

        public static RowBuilder newInstance(List<ImpexTable.Cell> cells, String endOfLine) {
            return new RowBuilder(new Row(cells, endOfLine));
        }

        private RowBuilder(Row row) {
            this.row = row;
            row.setIndentation("");
            row.setLeadingPipe(true);
            row.setTrailingPipe(true);
        }
        
        public RowBuilder withIndentation(String indentation) {
            row.setIndentation(indentation);
            return this;
        }

        public Row build() {
            return row;
        }

        public RowBuilder hasLeadingPipe(final boolean hasLeadingPipe) {
            row.setLeadingPipe(hasLeadingPipe);
            return this;
        }

        public RowBuilder isCommented(final boolean isCommented) {
            row.setCommented(isCommented);
            return this;
        }

        public RowBuilder hasTrailingPipe(final boolean hasTrailingPipe) {
            row.setTrailingPipe(hasTrailingPipe);
            return this;
        }
    }

}
