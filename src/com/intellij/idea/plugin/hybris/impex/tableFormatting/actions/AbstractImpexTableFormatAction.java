package com.intellij.idea.plugin.hybris.impex.tableFormatting.actions;

import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;

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
}
