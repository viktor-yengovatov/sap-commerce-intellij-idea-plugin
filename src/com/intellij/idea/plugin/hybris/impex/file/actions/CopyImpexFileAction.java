package com.intellij.idea.plugin.hybris.impex.file.actions;

import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.idea.plugin.hybris.actions.AbstractCopyFileToHybrisConsoleAction;
import com.intellij.idea.plugin.hybris.actions.ActionUtils;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class CopyImpexFileAction extends AbstractCopyFileToHybrisConsoleAction {

    @Override
    public void update(@NotNull final AnActionEvent e) {
        final DataContext dataContext = e.getDataContext();
        e.getPresentation()
         .setEnabledAndVisible(ActionUtils.isHybrisContext(dataContext) && isRequiredFileExtension(
             dataContext, ".impex", false));
    }

    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        final TreePath[] files = (TreePath[]) getFiles(e.getDataContext());
        final StringBuilder query = new StringBuilder();
        for (final TreePath treePath : files) {
            final DefaultMutableTreeNode lastPathNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            final PsiFileNode file = (PsiFileNode) lastPathNode.getUserObject();
            query.append(file.getValue().getText()).append('\n');
        }
        copyToHybrisConsole(e.getProject(), "Hybris Impex Console", query.toString());
    }
}
