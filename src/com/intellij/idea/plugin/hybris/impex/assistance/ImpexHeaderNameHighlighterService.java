package com.intellij.idea.plugin.hybris.impex.assistance;

import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Created 19:46 11 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public interface ImpexHeaderNameHighlighterService {

    @Contract(pure = false)
    void highlightCurrentHeader(@NotNull Editor editor);

    @Contract(pure = false)
    void releaseEditorData(@NotNull Editor editor);
}
