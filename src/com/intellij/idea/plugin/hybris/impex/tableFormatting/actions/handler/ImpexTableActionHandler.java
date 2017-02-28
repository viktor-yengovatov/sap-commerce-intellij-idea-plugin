package com.intellij.idea.plugin.hybris.impex.tableFormatting.actions.handler;

import com.intellij.idea.plugin.hybris.statistics.StatsCollector;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import org.jetbrains.annotations.Nullable;

import static com.intellij.idea.plugin.hybris.statistics.StatsCollector.ACTIONS.IMPEX_TABLE_FORMAT;

/**
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public abstract class ImpexTableActionHandler extends EditorActionHandler {
    final StatsCollector statsCollector = ServiceManager.getService(StatsCollector.class);

    @Override
    protected void doExecute(Editor editor, @Nullable Caret caret, final DataContext dataContext) {
        statsCollector.collectStat(IMPEX_TABLE_FORMAT);
        ApplicationManager.getApplication().runWriteAction(action(editor));
    }

    abstract protected Runnable action(Editor editor);
}
