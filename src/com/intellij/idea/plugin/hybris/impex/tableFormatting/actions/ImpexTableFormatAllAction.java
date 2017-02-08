package com.intellij.idea.plugin.hybris.impex.tableFormatting.actions;

import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableEditor;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.actions.handler.ImpexTableActionHandler;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.actions.operation.FormatAllTablesOperation;
import com.intellij.openapi.editor.Editor;

/**
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public class ImpexTableFormatAllAction extends AbstractImpexTableFormatAction {

    public ImpexTableFormatAllAction() {
        super(new ImpexTableActionHandler() {

            @Override
            protected Runnable action(Editor editor) {
                return new FormatAllTablesOperation(new ImpexTableEditor(editor));
            }
        });
    }
}
