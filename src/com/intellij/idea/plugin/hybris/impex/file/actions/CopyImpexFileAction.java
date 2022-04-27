package com.intellij.idea.plugin.hybris.impex.file.actions;

import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.idea.plugin.hybris.actions.AbstractCopyFileToHybrisConsoleAction;
import com.intellij.idea.plugin.hybris.actions.ActionUtils;
import com.intellij.idea.plugin.hybris.tools.remote.console.HybrisConsole;
import com.intellij.idea.plugin.hybris.tools.remote.console.view.HybrisConsolePanel;
import com.intellij.idea.plugin.hybris.toolwindow.CopyFileToHybrisConsoleDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class CopyImpexFileAction extends AbstractCopyFileToHybrisConsoleAction {

    private static final String IMPEX_CONSOLE_TITLE = "Hybris Impex Console";

    @Override
    public void update(@NotNull final AnActionEvent e) {
        final DataContext dataContext = e.getDataContext();
        e.getPresentation()
         .setEnabledAndVisible(ActionUtils.isHybrisContext(dataContext) && isRequiredFileExtension(
             dataContext, ".impex", false));
    }

    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        final Project project = e.getProject();
        final TreePath[] files = (TreePath[]) getFiles(e.getDataContext());
        final StringBuilder query = new StringBuilder();
        for (final TreePath treePath : files) {
            final DefaultMutableTreeNode lastPathNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            final PsiFileNode file = (PsiFileNode) lastPathNode.getUserObject();
            query.append(file.getValue().getText()).append('\n');
        }
        final HybrisConsolePanel hybrisConsolePanel = getHybrisConsolePanel(project);
        final HybrisConsole hybrisConsole = hybrisConsolePanel.findConsole(IMPEX_CONSOLE_TITLE);
        if (hybrisConsole != null) {
            if (!getTextFromHybrisConsole(project, hybrisConsole).isEmpty()) {
                final CopyFileToHybrisConsoleDialog copyFileToHybrisConsoleDialog = new CopyFileToHybrisConsoleDialog(
                    project);
                copyFileToHybrisConsoleDialog.setTitle("Impex Console");
                if (copyFileToHybrisConsoleDialog.showAndGet()) {
                    copyToHybrisConsole(project, IMPEX_CONSOLE_TITLE, query.toString());
                }
            } else {
                copyToHybrisConsole(project, IMPEX_CONSOLE_TITLE, query.toString());
            }
        }
    }
}
