package com.intellij.idea.plugin.hybris.impex.util;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.intellij.util.containers.ContainerUtil.isEmpty;

/**
 * Created 4:20 PM 31 May 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class CommonPsiUtils {

    @Nullable
    @Contract(pure = true)
    public static IElementType getNullSafeElementType(@Nullable final PsiElement element) {
        return element == null ? null : CommonAstUtils.getNullSafeElementType(element.getNode());
    }

    @Nullable
    @Contract(pure = true)
    public static PsiElement getNextNonWhitespaceElement(@NotNull final PsiElement element) {
        PsiElement nextSibling = element.getNextSibling();

        while (null != nextSibling && ImpexPsiUtil.isWhiteSpace(nextSibling)) {
            nextSibling = nextSibling.getNextSibling();
        }

        return nextSibling;
    }

    @NotNull
    @Contract(pure = true)
    public static List<PsiElement> findChildrenByIElementType(@NotNull final PsiElement element,
                                                              @NotNull final IElementType elementType) {
        List<PsiElement> result = Collections.emptyList();
        ASTNode child = element.getNode().getFirstChildNode();

        while (child != null) {
            if (elementType == child.getElementType()) {
                if (isEmpty(result)) {
                    result = new ArrayList<PsiElement>();
                }
                result.add(child.getPsi());
            }
            child = child.getTreeNext();
        }

        return result;
    }

}
