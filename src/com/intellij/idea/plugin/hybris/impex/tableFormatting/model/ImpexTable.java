package com.intellij.idea.plugin.hybris.impex.tableFormatting.model;

import java.util.List;

import static com.intellij.util.containers.ContainerUtil.newArrayList;

//TODO: make it immutable (use builder?)
public class ImpexTable {

    private List<Row> table;
    private int selectedRow = -1;
    private int selectedColumn = -1;

    public ImpexTable(List<Row> table) {
        this.table = newArrayList(table);
    }

    public int getRowCount() {
        return table.size();
    }

    public String getValue(int row, int column) {
        return table.get(row).get(column);
    }

    public Row[] rows() {
        return table.toArray(new Row[table.size()]);
    }

    public int getColumnCount() {
        return table.size() > 0 ? table.get(0).size() : 0;
    }

    public void addColumnBefore(int columnIndex) {
        for (Row row : table) {
            row.add(columnIndex, "");
        }
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }

    public int getSelectedColumn() {
        return selectedColumn;
    }

    public void setSelectedColumn(int selectedColumn) {
        this.selectedColumn = selectedColumn;
    }

    public boolean isRowCommented(int row) {
        return table.get(row).isCommented();
    }


    public static class Cell {

        private String value;

        public Cell(String value) {
            this.value = value != null ? value.trim() : "";
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
