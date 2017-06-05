package com.intellij.idea.plugin.hybris.actions;

import com.intellij.idea.plugin.hybris.common.services.CommonIdeaService;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * @author Eugene.Kudelevsky
 */
public final class ActionUtils {

    private ActionUtils() {
    }

    public static boolean isHybrisContext(@NotNull final AnActionEvent e) {
        return isHybrisContext(e.getDataContext());
    }

    public static boolean isHybrisContext(@NotNull final DataContext dataContext) {
        final Project project = CommonDataKeys.PROJECT.getData(dataContext);
        return project != null && CommonIdeaService.getInstance().isHybrisProject(project);
    }
}
