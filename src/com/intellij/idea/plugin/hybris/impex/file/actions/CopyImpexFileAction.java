package com.intellij.idea.plugin.hybris.impex.file.actions;

import com.intellij.idea.plugin.hybris.actions.AbstractCopyFileToHybrisConsoleAction;
import com.intellij.idea.plugin.hybris.actions.ActionUtils;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import static com.intellij.idea.plugin.hybris.common.HybrisConstants.*;

public class CopyImpexFileAction extends AbstractCopyFileToHybrisConsoleAction {

    @Override
    public void update(@NotNull final AnActionEvent e) {
        final Project project = e.getProject();
        if (project != null) {
            e.getPresentation()
             .setEnabledAndVisible(ActionUtils.isHybrisContext(project) && isRequiredFileExtension(
                 project, IMPEX_FILE_EXTENSION, false));
        }
    }

    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        final Project project = e.getProject();
        if (project != null) {
            performed(project, IMPEX_CONSOLE_TITLE, getDialogTitle(IMPEX_FILE_EXTENSION));
        }
    }
}
