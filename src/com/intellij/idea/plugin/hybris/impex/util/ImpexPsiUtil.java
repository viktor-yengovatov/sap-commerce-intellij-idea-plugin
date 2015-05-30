package com.intellij.idea.plugin.hybris.impex.util;

import com.intellij.idea.plugin.hybris.impex.psi.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.intellij.util.containers.ContainerUtil.isEmpty;

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

    @Contract("null -> false")
    public static boolean isImpexFullHeaderParameter(@Nullable final PsiElement psiElement) {
        return ImpexTypes.FULL_HEADER_PARAMETER == getElementType(psiElement);
    }

    @Contract("null -> null;!null -> !null")
    public static IElementType getElementType(@Nullable final ASTNode node) {
        return node == null ? null : node.getElementType();
    }

    @Contract("null -> null;!null -> !null")
    public static IElementType getElementType(@Nullable final PsiElement element) {
        return element == null ? null : getElementType((ASTNode) element.getNode());
    }

    @Nullable
    public static PsiElement getNextNonWhitespaceElement(@NotNull final PsiElement element) {
        PsiElement nextSibling = element.getNextSibling();

        while (null != nextSibling && ImpexPsiUtil.isWhiteSpace(nextSibling)) {
            nextSibling = nextSibling.getNextSibling();
        }

        return nextSibling;
    }

    @NotNull
    public static <T extends PsiElement> List<T> findChildrenByIElementType(@NotNull final PsiElement element,
                                                                            @NotNull final IElementType elementType) {
        List<T> result = Collections.emptyList();
        ASTNode child = element.getNode().getFirstChildNode();

        while (child != null) {
            if (elementType == child.getElementType()) {
                if (isEmpty(result)) {
                    result = new ArrayList<T>();
                }
                result.add((T) child.getPsi());
            }
            child = child.getTreeNext();
        }

        return result;
    }

    @Nullable
    public static PsiElement getHeaderParametersSeparatorByNumber(
            final int columnNumber,
            @NotNull final ImpexHeaderLine impexHeaderLine
    ) {
        final List<PsiElement> parameterSeparators = ImpexPsiUtil.findChildrenByIElementType(
                impexHeaderLine, ImpexTypes.PARAMETERS_SEPARATOR
        );

        if (columnNumber >= parameterSeparators.size()) {
            return null;
        }

        return parameterSeparators.get(columnNumber);
    }

    @Nullable
    public static ImpexFullHeaderParameter getImpexFullHeaderParameterByNumber(
            final int columnNumber,
            @NotNull final ImpexHeaderLine impexHeaderLine
    ) {
        final PsiElement columnSeparator = getHeaderParametersSeparatorByNumber(columnNumber, impexHeaderLine);
        if (null == columnSeparator) {
            return null;
        }

        final PsiElement nextSibling = ImpexPsiUtil.getNextNonWhitespaceElement(columnSeparator);

        if (ImpexPsiUtil.isImpexFullHeaderParameter(nextSibling)) {
            return (ImpexFullHeaderParameter) nextSibling;
        }

        return null;
    }
}
