package com.intellij.idea.plugin.hybris.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.DumbAware;

/**
 * @author Eugene.Kudelevsky
 */
public class HybrisToolsActionGroup extends DefaultActionGroup implements DumbAware {

    @Override
    public void update(final AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(ActionUtils.isHybrisContext(e));
    }
}
