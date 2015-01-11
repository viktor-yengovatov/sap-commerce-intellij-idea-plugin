package com.intellij.idea.plugin.hybris.impex.util;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.EditorBundle;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

/**
 * Created 19:34 11 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class IdeaUtil {

    @Nullable
    public static Project getCurrentProject() {
        return (Project) CommonDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext());
    }

    public static boolean isTypingActionInProgress() {
        return StringUtils.equals(
                CommandProcessor.getInstance().getCurrentCommandName(),
                EditorBundle.message("typing.in.editor.command.name")
        );
    }
}
