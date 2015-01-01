package com.intellij.idea.plugin.hybris.impex.formatting;

import com.intellij.formatting.Alignment;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

/**
 * Created 12:57 01 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public interface AlignmentStrategy {

    Alignment getAlignment(@NotNull ASTNode currentNode);

    void processNode(@NotNull ASTNode currentNode);
}
