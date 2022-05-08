package com.intellij.idea.plugin.hybris.impex.file.actions;

import com.intellij.idea.plugin.hybris.actions.ActionUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import static com.intellij.idea.plugin.hybris.actions.CopyFileToHybrisConsoleUtils.copySelectedFilesToHybrisConsole;
import static com.intellij.idea.plugin.hybris.actions.CopyFileToHybrisConsoleUtils.isRequiredMultipleFileExtension;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.*;

public class CopyImpexFileAction extends AnAction {

    @Override
    public void update(@NotNull final AnActionEvent event) {
        Project project = event.getProject();
        if (project != null) {
            event.getPresentation()
                 .setEnabledAndVisible(ActionUtils.isHybrisContext(project) && isRequiredMultipleFileExtension(
                     project, IMPEX_FILE_EXTENSION));
        }
    }

    @Override
    public void actionPerformed(@NotNull final AnActionEvent event) {
        Project project = event.getProject();
        if (project != null) {
            copySelectedFilesToHybrisConsole(project, IMPEX_CONSOLE_TITLE, IMPEX_FILE_EXTENSION);
        }
    }
}
