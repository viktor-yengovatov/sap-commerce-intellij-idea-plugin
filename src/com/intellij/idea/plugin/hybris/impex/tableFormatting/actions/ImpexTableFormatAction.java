package com.intellij.idea.plugin.hybris.impex.tableFormatting.actions;

import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableEditor;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.actions.handler.ImpexTableActionHandler;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.actions.operation.FormatTableOperation;
import com.intellij.openapi.editor.Editor;

public class ImpexTableFormatAction extends AbstractImpexTableFormatAction {

    public ImpexTableFormatAction() {
        super(new ImpexTableActionHandler() {
            @Override
            protected Runnable action(Editor editor) {
                return new FormatTableOperation(new ImpexTableEditor(editor));
            }
        });
    }

}
