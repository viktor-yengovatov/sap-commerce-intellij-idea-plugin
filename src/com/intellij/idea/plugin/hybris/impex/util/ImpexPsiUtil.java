package com.intellij.idea.plugin.hybris.impex.util;

import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.util.PsiUtilCore.getElementType;

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
        return null != psiElement && ImpexTypes.CRLF == getElementType(psiElement);
    }

    public static boolean isString(@Nullable final PsiElement psiElement) {
        return null != psiElement && isSingleString(psiElement) || isDoubleString(psiElement);
    }

    public static boolean isSingleString(@Nullable final PsiElement psiElement) {
        return null != psiElement && ImpexTypes.SINGLE_STRING == getElementType(psiElement);
    }

    public static boolean isDoubleString(@Nullable final PsiElement psiElement) {
        return null != psiElement && ImpexTypes.DOUBLE_STRING == getElementType(psiElement);
    }
}
