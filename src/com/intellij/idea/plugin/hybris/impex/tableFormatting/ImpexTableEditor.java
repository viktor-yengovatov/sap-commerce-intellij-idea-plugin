package com.intellij.idea.plugin.hybris.impex.tableFormatting;

import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.Range;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.TableText;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import org.jetbrains.annotations.Nullable;

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

    public TableText getSelectedText() {
        return new TableText(
            getSelectionModel().getSelectedText(),
            new Range(getSelectionModel().getSelectionStart(), getSelectionModel().getSelectionEnd())
        );
    }


    public String getText() {
        return editor.getDocument().getText();
    }


    public int getCaretPosition() {
        return editor.getCaretModel().getOffset();
    }


    public void replaceText(String newText, Range tableRange) {
        getSelectionModel().setSelection(tableRange.getStart(), tableRange.getEnd());
        editor.getDocument().replaceString(tableRange.getStart(), tableRange.getEnd(), newText);
    }


    public void setSelection(Range range) {
        getSelectionModel().setSelection(range.getStart(), range.getEnd());
    }

    @Nullable
    public Editor getIdeaEditor() {
        return editor;
    }
}
