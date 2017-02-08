package com.intellij.idea.plugin.hybris.impex.tableFormatting.model;

/**
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public class TableText {
    private final String text;
    private final Range range;

    public TableText(String text, Range range) {
        this.text = text;
        this.range = range;
    }

    public String getText() {
        return text;
    }

    public Range getRange() {
        return range;
    }

    public boolean isNotEmpty() {
        return range.isNotEmpty();
    }

    public boolean isEmpty() {
        return range.isEmpty();
    }
}
