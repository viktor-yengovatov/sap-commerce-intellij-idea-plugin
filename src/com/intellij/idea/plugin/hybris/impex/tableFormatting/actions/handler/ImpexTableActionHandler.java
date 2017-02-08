package com.intellij.idea.plugin.hybris.impex.tableFormatting.actions.handler;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import org.jetbrains.annotations.Nullable;

/**
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public abstract class ImpexTableActionHandler extends EditorActionHandler {

    @Override
    protected void doExecute(Editor editor, @Nullable Caret caret, final DataContext dataContext) {
        ApplicationManager.getApplication().runWriteAction(action(editor));
    }

    abstract protected Runnable action(Editor editor);
}
