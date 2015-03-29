package com.intellij.idea.plugin.hybris.impex.util;

import com.intellij.idea.plugin.hybris.impex.psi.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Contract;
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

    @Contract("null -> false")
    public static boolean isImpexParameters(@Nullable final PsiElement psiElement) {
        return psiElement instanceof ImpexParameters;
    }

    @Contract("null -> false")
    public static boolean isImpexAttribute(@Nullable final PsiElement psiElement) {
        return psiElement instanceof ImpexAttribute;
    }

    @Contract("null -> false")
    public static boolean isImpexModifiers(@Nullable final PsiElement psiElement) {
        return psiElement instanceof ImpexModifiers;
    }

    @Contract("null -> false")
    public static boolean isImpexValueGroup(@Nullable final PsiElement psiElement) {
        return psiElement instanceof ImpexValueGroup;
    }

    @Contract("null -> false")
    public static boolean isImpexValueLine(@Nullable final PsiElement psiElement) {
        return psiElement instanceof ImpexValueLine;
    }

    @Contract("null -> false")
    public static boolean isWhiteSpace(@Nullable final PsiElement psiElement) {
        return psiElement instanceof PsiWhiteSpace;
    }

    @Contract("null -> false")
    public static boolean isLineBreak(@Nullable final PsiElement psiElement) {
        return ImpexTypes.CRLF == getElementType(psiElement);
    }

    @Contract("null -> false")
    public static boolean isFieldValueSeparator(@Nullable final PsiElement psiElement) {
        return ImpexTypes.FIELD_VALUE_SEPARATOR == getElementType(psiElement);
    }

    @Contract("null -> false")
    public static boolean isString(@Nullable final PsiElement psiElement) {
        return isSingleString(psiElement) || isDoubleString(psiElement);
    }

    @Contract("null -> false")
    public static boolean isSingleString(@Nullable final PsiElement psiElement) {
        return ImpexTypes.SINGLE_STRING == getElementType(psiElement);
    }

    @Contract("null -> false")
    public static boolean isDoubleString(@Nullable final PsiElement psiElement) {
        return ImpexTypes.DOUBLE_STRING == getElementType(psiElement);
    }

    @Contract("null -> null;!null -> !null")
    public static IElementType getElementType(@Nullable final ASTNode node) {
        return node == null ? null : node.getElementType();
    }

    @Contract("null -> null;!null -> !null")
    public static IElementType getElementType(@Nullable final PsiElement element) {
        return element == null ? null : getElementType((ASTNode) element.getNode());
    }
}
