package com.intellij.idea.plugin.hybris.impex.tableFormatting.actions.operation;

import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableEditor;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.Range;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.TableText;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.FoldRegion;
import com.intellij.openapi.editor.FoldingModel;
import org.jetbrains.annotations.NotNull;

import static com.intellij.idea.plugin.hybris.impex.tableFormatting.util.ImpexTableUtil.detectTableIn;

public abstract class AbstractOperation implements Runnable {

    protected final ImpexTableEditor editor;

    AbstractOperation(final ImpexTableEditor editor) {
        this.editor = editor;
    }

    @Override
    final public void run() {
        unfoldAllCode();
        perform();
    }

    private void unfoldAllCode() {
        final Editor ideaEditor = editor.getIdeaEditor();
        if (ideaEditor != null) {
            ideaEditor.getFoldingModel().runBatchFoldingOperation(() -> {
                final FoldingModel foldingModel = ideaEditor.getFoldingModel();
                final FoldRegion[] allFoldRegions = foldingModel.getAllFoldRegions();
                for (final FoldRegion foldRegion : allFoldRegions) {
                    ideaEditor.getFoldingModel().removeFoldRegion(foldRegion);
                }
            });
        }
    }

    protected abstract void perform();

    final TableText getSelectedTable(final @NotNull ImpexTableEditor editor) {
        final TableText tableText = editor.getSelectedText();
        if (tableText.isEmpty()) {
            String text = editor.getText();
            int caretPosition = editor.getCaretPosition();
            Range range = detectTableIn(text).around(caretPosition);
            return new TableText(text.substring(range.getStart(), range.getEnd()), range);
        }
        return tableText;
    }
}
