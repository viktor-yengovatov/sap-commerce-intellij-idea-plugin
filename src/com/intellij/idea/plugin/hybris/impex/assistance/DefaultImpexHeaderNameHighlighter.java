package com.intellij.idea.plugin.hybris.impex.assistance;

import com.intellij.codeInsight.highlighting.HighlightManager;
import com.intellij.codeInsight.highlighting.HighlightUsagesHandler;
import com.intellij.idea.plugin.hybris.impex.ImpexLanguage;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexFullHeaderParameter;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexHeaderLine;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexValueGroup;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexValueLine;
import com.intellij.lang.Language;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.intellij.idea.plugin.hybris.impex.util.ImpexPsiUtil.*;

public class DefaultImpexHeaderNameHighlighter implements ImpexHeaderNameHighlighter {

    final Map<Editor, PsiElement> highlightedBlocks = new ConcurrentHashMap<Editor, PsiElement>();

    public DefaultImpexHeaderNameHighlighter() {
    }

    @Override
    public void highlightCurrentHeader(@NotNull final Editor editor) {
        final Project project = editor.getProject();

        if (null == project) {
            return;
        }

        if (project.isDisposed()) {
            return;
        }

        final Language languageInEditor = PsiUtilBase.getLanguageInEditor(editor, project);

        if (languageInEditor instanceof ImpexLanguage) {
            this.highlightHeaderOfValueUnderCaret(editor);
        }
    }

    @Override
    public void releaseEditorData(@NotNull final Editor editor) {
        this.highlightedBlocks.remove(editor);
    }

    protected void highlightHeaderOfValueUnderCaret(@NotNull final Editor editor) {

        final PsiElement psiElementUnderCaret = PsiUtilBase.getElementAtCaret(editor);
        if (null == psiElementUnderCaret) {
            return;
        }

        final ImpexValueGroup valueGroup = this.getSelectedValueGroup(psiElementUnderCaret);
        if (null != valueGroup) {

            final ImpexFullHeaderParameter impexFullHeaderParameter = this.getHeaderForValueGroup(valueGroup);
            if (null != impexFullHeaderParameter) {

                this.highlightArea(editor, impexFullHeaderParameter);
                return;
            }
        }

        this.clearHighlightedArea(editor);
    }

    @Nullable
    @Contract("null -> null")
    // TODO: Becomes to complex, refactor
    protected ImpexValueGroup getSelectedValueGroup(@Nullable final PsiElement psiElementUnderCaret) {
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
                valueGroup = this.skipAllExceptLineBreaksAndGetImpexValueGroup(psiElementUnderCaret);
            }

            return valueGroup;

        } else if (isLineBreak(psiElementUnderCaret)) {

            return this.skipAllExceptLineBreaksAndGetImpexValueGroup(psiElementUnderCaret);

        } else {
            return PsiTreeUtil.getParentOfType(psiElementUnderCaret, ImpexValueGroup.class);
        }

        return null;
    }

    private ImpexValueGroup skipAllExceptLineBreaksAndGetImpexValueGroup(final PsiElement psiElementUnderCaret) {
        if (isLineBreak(psiElementUnderCaret.getPrevSibling())) {
            return null;
        }

        PsiElement prevSibling = psiElementUnderCaret.getPrevSibling();
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

    @Nullable
    @Contract("null -> null")
    protected ImpexFullHeaderParameter getHeaderForValueGroup(@Nullable final ImpexValueGroup valueGroup) {
        if (null == valueGroup) {
            return null;
        }

        final int columnNumber = this.calculateColumnNumberForValueGroup(valueGroup);

        if (columnNumber >= 0) {

            final ImpexValueLine impexValueLine = PsiTreeUtil.getParentOfType(valueGroup, ImpexValueLine.class);
            if (null != impexValueLine) {

                final ImpexHeaderLine impexHeaderLine = PsiTreeUtil.getPrevSiblingOfType(impexValueLine, ImpexHeaderLine.class);
                if (null != impexHeaderLine) {

                    final List<ImpexFullHeaderParameter> childrenOfType = PsiTreeUtil.getChildrenOfTypeAsList(
                            impexHeaderLine, ImpexFullHeaderParameter.class
                    );

                    if (columnNumber < childrenOfType.size()) {
                        return childrenOfType.get(columnNumber);
                    }
                }
            }
        }

        return null;
    }

    protected int calculateColumnNumberForValueGroup(@NotNull final ImpexValueGroup valueGroup) {
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

    protected void highlightArea(@NotNull final Editor editor,
                                 @NotNull final PsiElement impexFullHeaderParameter) {
        if (isAlreadyHighlighted(editor, impexFullHeaderParameter)) {
            return;
        }

        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                final PsiElement currentHighlightedElement = highlightedBlocks.remove(editor);
                if (null != currentHighlightedElement) {
                    modifyHighlightedArea(editor, currentHighlightedElement, true);
                }

                highlightedBlocks.put(editor, impexFullHeaderParameter);
                modifyHighlightedArea(editor, impexFullHeaderParameter, false);
            }
        });
    }

    protected boolean isAlreadyHighlighted(@NotNull final Editor editor,
                                           @Nullable final PsiElement impexFullHeaderParameter) {
        return this.highlightedBlocks.get(editor) == impexFullHeaderParameter;
    }

    // TODO: Maybe it is better to clear the whole header line
    protected void clearHighlightedArea(@NotNull final Editor editor) {

        if (!highlightedBlocks.isEmpty()) {
            final PsiElement impexFullHeaderParameter = highlightedBlocks.remove(editor);

            if (null != impexFullHeaderParameter) {
                ApplicationManager.getApplication().invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        modifyHighlightedArea(editor, impexFullHeaderParameter, true);
                    }
                });
            }
        }
    }

    protected void modifyHighlightedArea(@NotNull final Editor editor,
                                         @NotNull final PsiElement impexFullHeaderParameter,
                                         final boolean clear) {
        if (null == editor.getProject()) {
            return;
        }

        if (editor.getProject().isDisposed()) {
            return;
        }

        HighlightUsagesHandler.highlightRanges(
                HighlightManager.getInstance(editor.getProject()),
                editor,
                EditorColorsManager.getInstance().getGlobalScheme().getAttributes(EditorColors.SEARCH_RESULT_ATTRIBUTES),
                clear,
                Collections.singletonList(impexFullHeaderParameter.getTextRange())
        );
    }
}