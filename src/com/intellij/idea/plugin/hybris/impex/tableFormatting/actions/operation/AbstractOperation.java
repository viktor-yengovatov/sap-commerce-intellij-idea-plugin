package com.intellij.idea.plugin.hybris.impex.tableFormatting.actions.operation;

import com.intellij.idea.plugin.hybris.impex.psi.ImpexHeaderLine;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexRootMacroUsage;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexValueLine;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableEditor;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.getNextSiblingOfAnyType;
import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.isHeaderLine;
import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.isLineBreak;
import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.isUserRightsMacros;
import static com.intellij.psi.util.PsiTreeUtil.getNextSiblingOfType;
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static com.intellij.psi.util.PsiTreeUtil.getPrevSiblingOfType;
import static com.intellij.psi.util.PsiUtilBase.getElementAtCaret;

/**
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public abstract class AbstractOperation implements Runnable {

    protected final ImpexTableEditor editor;

    AbstractOperation(final ImpexTableEditor editor) {
        this.editor = editor;
    }

    @Override
    final public void run() {
        perform();
    }

    protected abstract void perform();

    @Nullable
    final Pair<PsiElement, PsiElement> getSelectedTable(final @NotNull ImpexTableEditor editor) {
        final PsiElement elementAtCaret = getElementAtCaret(editor.getIdeaEditor());


        final PsiElement valueLineAt;
        if (elementAtCaret != null) {
            if (getNextSiblingOfType(elementAtCaret, ImpexValueLine.class) != null) {
                valueLineAt = elementAtCaret.getNextSibling();
            } else if (getPrevSiblingOfType(elementAtCaret, ImpexValueLine.class) != null) {
                valueLineAt = elementAtCaret.getPrevSibling();
            } else {
                valueLineAt = getParentOfType(
                    elementAtCaret,
                    ImpexValueLine.class
                );

            }
        } else {
            return null;
        }

        if (valueLineAt == null) {
            return null;
        }

        final PsiElement headerLine = scanFirstLine(elementAtCaret, valueLineAt);

        final PsiElement lastValueLine = scanLastLine(valueLineAt, headerLine);

        return Pair.create(headerLine, lastValueLine);
    }

    @Nullable
    private PsiElement scanFirstLine(
        @NotNull final PsiElement elementAtCaret,
        @NotNull final PsiElement valueLineAt
    ) {
        final PsiElement headerLine;

        if (isHeaderLine(elementAtCaret)) {
            headerLine = elementAtCaret;
        } else if (isUserRightsMacros(elementAtCaret)) {
            headerLine = elementAtCaret;
        } else if (getParentOfType(elementAtCaret, ImpexRootMacroUsage.class) != null &&
                   isUserRightsMacros(getParentOfType(elementAtCaret, ImpexRootMacroUsage.class))) {
            headerLine = getParentOfType(elementAtCaret, ImpexRootMacroUsage.class);
        } else if (getParentOfType(elementAtCaret, ImpexHeaderLine.class) != null) {
            headerLine = getParentOfType(elementAtCaret, ImpexHeaderLine.class);
        } else {
            if (isHeaderLine(getPrevSiblingOfType(valueLineAt, ImpexHeaderLine.class))) {
                headerLine = getPrevSiblingOfType(
                    valueLineAt,
                    ImpexHeaderLine.class
                );
            } else if (isUserRightsMacros(getPrevSiblingOfType(valueLineAt, ImpexRootMacroUsage.class))) {
                headerLine = getPrevSiblingOfType(
                    valueLineAt,
                    ImpexRootMacroUsage.class
                );
            } else {
                return null;
            }
        }
        return headerLine;
    }

    @Nullable
    private PsiElement scanLastLine(final PsiElement valueLineAt, final PsiElement headerLine) {
        final PsiElement secondHeaderLine = getNextSiblingOfAnyType(
            valueLineAt,
            ImpexRootMacroUsage.class,
            ImpexHeaderLine.class
        );

        if (secondHeaderLine == null) {
            PsiElement lastValueLine = valueLineAt != null ? valueLineAt.getNextSibling() : headerLine.getNextSibling();
            boolean flag = true;
            while (lastValueLine != null && lastValueLine.getNextSibling() != null && flag) {
                if (isLineBreak(lastValueLine) && isLineBreak(lastValueLine.getNextSibling())) {
                    flag = false;
                } else {
                    lastValueLine = lastValueLine.getNextSibling();
                }
            }

            if (lastValueLine == null) {
                lastValueLine = valueLineAt;
            }

            return lastValueLine;
        }

        if (secondHeaderLine instanceof ImpexRootMacroUsage && secondHeaderLine.getText().startsWith("$END")) {
            return secondHeaderLine;
        }

        return getPrevSiblingOfType(
            secondHeaderLine,
            ImpexValueLine.class
        );
    }

}
