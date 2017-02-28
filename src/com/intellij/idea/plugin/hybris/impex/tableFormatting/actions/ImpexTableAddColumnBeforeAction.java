package com.intellij.idea.plugin.hybris.impex.tableFormatting.actions;

import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableEditor;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.actions.handler.ImpexTableActionHandler;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.actions.operation.AddColumnBeforeOperation;
import com.intellij.idea.plugin.hybris.statistics.StatsCollector;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;

/**
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public class ImpexTableAddColumnBeforeAction extends AbstractImpexTableFormatAction {

    public ImpexTableAddColumnBeforeAction() {
        super(new ImpexTableActionHandler() {
            @Override
            protected Runnable action(Editor editor) {
                return new AddColumnBeforeOperation(new ImpexTableEditor(editor));
            }
        });
    }
}
