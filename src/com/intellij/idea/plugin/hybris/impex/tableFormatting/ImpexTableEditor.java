package com.intellij.idea.plugin.hybris.impex.tableFormatting;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import org.jetbrains.annotations.NotNull;

/**
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public class ImpexTableEditor {

    protected final Editor editor;

    public ImpexTableEditor(Editor editor) {
        this.editor = editor;
    }

    private SelectionModel getSelectionModel() {
        return editor.getSelectionModel();
    }

    public String getText() {
        return editor.getDocument().getText();
    }


    public void setSelection(final com.intellij.util.Range<Integer> range) {
        getSelectionModel().setSelection(range.getFrom(), range.getTo());
    }

    @NotNull
    public Editor getIdeaEditor() {
        return editor;
    }
}
