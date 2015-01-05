package com.intellij.idea.plugin.hybris.impex.util;

import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

/**
 * Created 22:43 01 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexPsiUtil {

    private ImpexPsiUtil() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static boolean isLineBreak(@Nullable final PsiElement psiElement) {
        return null != psiElement && null != psiElement.getNode() && ImpexTypes.CRLF == psiElement.getNode().getElementType();
    }

    public static boolean isString(@Nullable final PsiElement psiElement) {
        return isSingleString(psiElement) || isDoubleString(psiElement);
    }

    public static boolean isSingleString(@Nullable final PsiElement psiElement) {
        return null != psiElement && null != psiElement.getNode() && ImpexTypes.SINGLE_STRING == psiElement.getNode().getElementType();
    }

    public static boolean isDoubleString(@Nullable final PsiElement psiElement) {
        return null != psiElement && null != psiElement.getNode() && ImpexTypes.DOUBLE_STRING == psiElement.getNode().getElementType();
    }
}
