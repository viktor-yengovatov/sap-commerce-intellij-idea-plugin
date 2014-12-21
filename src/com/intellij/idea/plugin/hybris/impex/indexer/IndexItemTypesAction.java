package com.intellij.idea.plugin.hybris.impex.indexer;

import com.intellij.idea.plugin.hybris.impex.completion.ImpexCompletionContributor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class IndexItemTypesAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        ImpexCompletionContributor.indexItemTypes(anActionEvent.getProject());
    }
}
