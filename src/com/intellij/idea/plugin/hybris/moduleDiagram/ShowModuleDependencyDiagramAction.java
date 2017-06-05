package com.intellij.idea.plugin.hybris.moduleDiagram;

import com.intellij.diagram.DiagramProvider;
import com.intellij.idea.plugin.hybris.actions.ActionUtils;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.uml.core.actions.ShowDiagram;
import org.jetbrains.annotations.Nullable;

/**
 * @author Eugene.Kudelevsky
 */
public class ShowModuleDependencyDiagramAction extends ShowDiagram {

    @Override
    public void update(final AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(ActionUtils.isHybrisContext(e));
    }

    @Nullable
    @Override
    public DiagramProvider getProvider(final AnActionEvent e) {
        return new ModuleDepDiagramProvider();
    }
}
