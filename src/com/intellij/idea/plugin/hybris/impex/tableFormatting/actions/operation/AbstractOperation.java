package com.intellij.idea.plugin.hybris.impex.tableFormatting.actions.operation;

import com.intellij.idea.plugin.hybris.impex.psi.ImpexHeaderLine;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexValueLine;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableEditor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.FoldRegion;
import com.intellij.openapi.editor.FoldingModel;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.isHeaderLine;
import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.isLineBreak;
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
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
            if (elementAtCaret.getNextSibling() instanceof ImpexValueLine) {
                valueLineAt = elementAtCaret.getNextSibling();
            } else if (elementAtCaret.getPrevSibling() instanceof ImpexValueLine) {
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


        final ImpexHeaderLine secondHeaderLine = PsiTreeUtil.getNextSiblingOfType(
            valueLineAt,
            ImpexHeaderLine.class
        );

        final ImpexHeaderLine headerLine;
        if (isHeaderLine(elementAtCaret)) {
            headerLine = (ImpexHeaderLine) elementAtCaret;
        } else if (getParentOfType(elementAtCaret, ImpexHeaderLine.class) != null) {
            headerLine = getParentOfType(elementAtCaret, ImpexHeaderLine.class);
        } else {
            headerLine = PsiTreeUtil.getPrevSiblingOfType(
                valueLineAt,
                ImpexHeaderLine.class
            );
        }

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

            return Pair.create(headerLine, lastValueLine);
        }

        final ImpexValueLine lastValueLine = PsiTreeUtil.getPrevSiblingOfType(
            secondHeaderLine,
            ImpexValueLine.class
        );

        return Pair.create(headerLine, lastValueLine);
    }
}
