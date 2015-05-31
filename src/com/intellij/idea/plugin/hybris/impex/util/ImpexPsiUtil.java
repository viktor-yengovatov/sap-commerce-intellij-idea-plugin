package com.intellij.idea.plugin.hybris.impex.util;

import com.intellij.idea.plugin.hybris.impex.psi.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilBase;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created 22:43 01 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexPsiUtil {

    private ImpexPsiUtil() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    @Contract(value = "null -> false", pure = true)
    public static boolean isImpexParameters(@Nullable final PsiElement psiElement) {
        return psiElement instanceof ImpexParameters;
    }

    @Contract(value = "null -> false", pure = true)
    public static boolean isImpexAttribute(@Nullable final PsiElement psiElement) {
        return psiElement instanceof ImpexAttribute;
    }

    @Contract(value = "null -> false", pure = true)
    public static boolean isImpexModifiers(@Nullable final PsiElement psiElement) {
        return psiElement instanceof ImpexModifiers;
    }

    @Contract(value = "null -> false", pure = true)
    public static boolean isImpexValueGroup(@Nullable final PsiElement psiElement) {
        return psiElement instanceof ImpexValueGroup;
    }

    @Contract(value = "null -> false", pure = true)
    public static boolean isImpexValueLine(@Nullable final PsiElement psiElement) {
        return psiElement instanceof ImpexValueLine;
    }

    @Contract(value = "null -> false", pure = true)
    public static boolean isWhiteSpace(@Nullable final PsiElement psiElement) {
        return psiElement instanceof PsiWhiteSpace;
    }

    @Contract(value = "null -> false", pure = true)
    public static boolean isLineBreak(@Nullable final PsiElement psiElement) {
        return ImpexTypes.CRLF == CommonPsiUtils.getNullSafeElementType(psiElement);
    }

    @Contract(value = "null -> false", pure = true)
    public static boolean isFieldValueSeparator(@Nullable final PsiElement psiElement) {
        return ImpexTypes.FIELD_VALUE_SEPARATOR == CommonPsiUtils.getNullSafeElementType(psiElement);
    }

    @Contract(value = "null -> false", pure = true)
    public static boolean isString(@Nullable final PsiElement psiElement) {
        return isSingleString(psiElement) || isDoubleString(psiElement);
    }

    @Contract(value = "null -> false", pure = true)
    public static boolean isSingleString(@Nullable final PsiElement psiElement) {
        return ImpexTypes.SINGLE_STRING == CommonPsiUtils.getNullSafeElementType(psiElement);
    }

    @Contract(value = "null -> false", pure = true)
    public static boolean isDoubleString(@Nullable final PsiElement psiElement) {
        return ImpexTypes.DOUBLE_STRING == CommonPsiUtils.getNullSafeElementType(psiElement);
    }

    @Contract(value = "null -> false", pure = true)
    public static boolean isImpexFullHeaderParameter(@Nullable final PsiElement psiElement) {
        return ImpexTypes.FULL_HEADER_PARAMETER == CommonPsiUtils.getNullSafeElementType(psiElement);
    }

    @Contract(pure = false)
    public static PsiElement getHeaderOfValueGroupUnderCaret(@NotNull final Editor editor) {

        final PsiElement psiElementUnderCaret = PsiUtilBase.getElementAtCaret(editor);
        if (null == psiElementUnderCaret) {
            return null;
        }

        final ImpexValueGroup valueGroup = ImpexPsiUtil.getClosestSelectedValueGroupFromTheSameLine(psiElementUnderCaret);
        if (null != valueGroup) {

            final PsiElement header = ImpexPsiUtil.getHeaderForValueGroup(valueGroup);
            if (null != header) {

                return header;
            }
        }

        return null;
    }

    // TODO: Becomes to complex, refactor
    @Nullable
    @Contract(pure = true)
    public static ImpexValueGroup getClosestSelectedValueGroupFromTheSameLine(@Nullable final PsiElement psiElementUnderCaret) {
        if (null == psiElementUnderCaret) {
            return null;
        }

        if (isImpexValueGroup(psiElementUnderCaret)) {

            return (ImpexValueGroup) psiElementUnderCaret;

        } else if (isFieldValueSeparator(psiElementUnderCaret)) {

            final ImpexValueGroup valueGroup = PsiTreeUtil.getParentOfType(psiElementUnderCaret, ImpexValueGroup.class);
            if (null != valueGroup) {
                return PsiTreeUtil.getPrevSiblingOfType(valueGroup, ImpexValueGroup.class);
            }

        } else if (isWhiteSpace(psiElementUnderCaret)) {

            ImpexValueGroup valueGroup = PsiTreeUtil.getParentOfType(psiElementUnderCaret, ImpexValueGroup.class);

            if (null == valueGroup) {
                valueGroup = PsiTreeUtil.getPrevSiblingOfType(psiElementUnderCaret, ImpexValueGroup.class);
            }

            if (null == valueGroup) {
                valueGroup = skipAllExceptLineBreaksAndGetImpexValueGroup(psiElementUnderCaret);
            }

            return valueGroup;

        } else if (isLineBreak(psiElementUnderCaret)) {

            return skipAllExceptLineBreaksAndGetImpexValueGroup(psiElementUnderCaret);

        } else {
            return PsiTreeUtil.getParentOfType(psiElementUnderCaret, ImpexValueGroup.class);
        }

        return null;
    }

    @Nullable
    @Contract(pure = true)
    public static PsiElement getHeaderForValueGroup(@Nullable final ImpexValueGroup valueGroup) {
        if (null == valueGroup) {
            return null;
        }

        final int columnNumber = ImpexPsiUtil.getColumnNumberForValueGroup(valueGroup);

        if (columnNumber < 0) {
            return null;
        }

        final ImpexValueLine impexValueLine = PsiTreeUtil.getParentOfType(valueGroup, ImpexValueLine.class);
        if (null == impexValueLine) {
            return null;
        }

        final ImpexHeaderLine impexHeaderLine = PsiTreeUtil.getPrevSiblingOfType(impexValueLine, ImpexHeaderLine.class);
        if (null == impexHeaderLine) {
            return null;
        }

        final ImpexFullHeaderParameter header = ImpexPsiUtil.getImpexFullHeaderParameterFromHeaderLineByNumber(columnNumber, impexHeaderLine);

        if (null == header) {
            return ImpexPsiUtil.getHeaderParametersSeparatorFromHeaderLineByNumber(columnNumber, impexHeaderLine);
        } else {
            return header;
        }
    }

    @Nullable
    @Contract(pure = true)
    public static ImpexValueGroup skipAllExceptLineBreaksAndGetImpexValueGroup(
            @NotNull final PsiElement psiElement
    ) {
        Validate.notNull(psiElement);

        if (isLineBreak(psiElement.getPrevSibling())) {
            return null;
        }

        PsiElement prevSibling = psiElement.getPrevSibling();
        while (!isImpexValueLine(prevSibling)) {
            if (null == prevSibling || isLineBreak(prevSibling)) {
                return null;
            }

            prevSibling = prevSibling.getPrevSibling();
        }

        if (!isImpexValueLine(prevSibling)) {
            return null;
        }

        return PsiTreeUtil.getParentOfType(PsiTreeUtil.lastChild(prevSibling), ImpexValueGroup.class);
    }

    @Contract(pure = true)
    public static int getColumnNumberForValueGroup(@NotNull final ImpexValueGroup valueGroup) {
        Validate.notNull(valueGroup);

        final ImpexValueLine valueLine = PsiTreeUtil.getParentOfType(valueGroup, ImpexValueLine.class);
        final List<ImpexValueGroup> valueGroups = PsiTreeUtil.getChildrenOfTypeAsList(valueLine, ImpexValueGroup.class);

        int columnNumber = 0;

        for (ImpexValueGroup group : valueGroups) {
            if (group == valueGroup) {
                return columnNumber;
            }

            columnNumber++;
        }

        return -1;
    }

    @Nullable
    @Contract(pure = true)
    public static ImpexFullHeaderParameter getImpexFullHeaderParameterFromHeaderLineByNumber(
            final int columnNumber,
            @NotNull final ImpexHeaderLine impexHeaderLine
    ) {
        Validate.isTrue(columnNumber >= 0);
        Validate.notNull(impexHeaderLine);

        final PsiElement columnSeparator = getHeaderParametersSeparatorFromHeaderLineByNumber(columnNumber, impexHeaderLine);
        if (null == columnSeparator) {
            return null;
        }

        final PsiElement nextSibling = CommonPsiUtils.getNextNonWhitespaceElement(columnSeparator);

        if (ImpexPsiUtil.isImpexFullHeaderParameter(nextSibling)) {
            return (ImpexFullHeaderParameter) nextSibling;
        }

        return null;
    }

    @Nullable
    @Contract(pure = true)
    public static PsiElement getHeaderParametersSeparatorFromHeaderLineByNumber(
            final int columnNumber,
            @NotNull final ImpexHeaderLine impexHeaderLine
    ) {
        Validate.isTrue(columnNumber >= 0);
        Validate.notNull(impexHeaderLine);

        final List<PsiElement> parameterSeparators = CommonPsiUtils.findChildrenByIElementType(
                impexHeaderLine, ImpexTypes.PARAMETERS_SEPARATOR
        );

        if (columnNumber >= parameterSeparators.size()) {
            return null;
        }

        return parameterSeparators.get(columnNumber);
    }
}
