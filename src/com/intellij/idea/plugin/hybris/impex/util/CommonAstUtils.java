package com.intellij.idea.plugin.hybris.impex.util;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * Created 4:23 PM 31 May 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class CommonAstUtils {

    private CommonAstUtils() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    @Contract(pure = true)
    public static IElementType getNullSafeElementType(@Nullable final ASTNode node) {
        return node == null ? null : node.getElementType();
    }

}
