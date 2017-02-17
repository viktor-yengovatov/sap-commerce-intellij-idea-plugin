package com.intellij.idea.plugin.hybris.impex.tableFormatting.actions;

import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public abstract class AbstractImpexTableFormatAction extends EditorAction {

    AbstractImpexTableFormatAction(EditorActionHandler defaultHandler) {
        super(defaultHandler);
    }

    @Override
    public void update(final AnActionEvent event) {
        super.update(event);

        if (ActionPlaces.isPopupPlace(event.getPlace())) {
            event.getPresentation().setVisible(event.getPresentation().isEnabled());
        }
    }

    @Override
    public void update(
        final Editor editor, final Presentation presentation, final DataContext dataContext
    ) {
        super.update(editor, presentation, dataContext);
        final VirtualFile file = dataContext.getData(CommonDataKeys.VIRTUAL_FILE);
        final boolean visible = file != null && file.getName().endsWith(".impex");
        presentation.setEnabledAndVisible(visible);
    }
}
