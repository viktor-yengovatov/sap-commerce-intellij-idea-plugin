package com.intellij.idea.plugin.hybris.impex.assistance;

import com.intellij.codeInsight.highlighting.HighlightManager;
import com.intellij.idea.plugin.hybris.impex.psi.*;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.intellij.codeInsight.highlighting.HighlightUsagesHandler.highlightRanges;

/**
 * Created 12:42 05 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HeaderHighlightingCaretListener implements CaretListener, ApplicationComponent {

    private Map<Editor, ImpexFullHeaderParameter> highlightedBlocks = new ConcurrentHashMap<Editor, ImpexFullHeaderParameter>();

    @Override
    public void caretPositionChanged(final CaretEvent e) {
        if (null == e.getEditor().getProject()) {
            return;
        }

        final PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(e.getEditor(), e.getEditor().getProject());

        if (psiFile instanceof ImpexFile) {
            this.highlight(e.getEditor());
        }
    }

    @Override
    public void caretAdded(final CaretEvent e) {
    }

    @Override
    public void caretRemoved(final CaretEvent e) {
    }

    protected void highlight(final Editor editor) {

        final PsiElement psiElementUnderCaret = this.getPsiElementUnderCaret(editor);

        if (null == psiElementUnderCaret) {
            return;
        }

        final ImpexValueGroup valueGroup;
        if (psiElementUnderCaret instanceof ImpexValueGroup) {
            valueGroup = (ImpexValueGroup) psiElementUnderCaret;
        } else if (psiElementUnderCaret instanceof PsiWhiteSpace) {
            valueGroup = PsiTreeUtil.getPrevSiblingOfType(psiElementUnderCaret, ImpexValueGroup.class);
        } else {
            valueGroup = PsiTreeUtil.getParentOfType(psiElementUnderCaret, ImpexValueGroup.class);
        }

        if (null == valueGroup) {
            return;
        }

        final ImpexValueLine valueLine = PsiTreeUtil.getParentOfType(valueGroup, ImpexValueLine.class);
        final List<ImpexValueGroup> valueGroups = PsiTreeUtil.getChildrenOfTypeAsList(valueLine, ImpexValueGroup.class);

        int columnNumber = -1;
        int idx = 0;
        for (ImpexValueGroup group : valueGroups) {
            if (group == valueGroup) {
                columnNumber = idx;
                break;
            }
            idx++;
        }

        final ImpexHeaderLine impexHeaderLine = PsiTreeUtil.getPrevSiblingOfType(valueGroup.getParent(), ImpexHeaderLine.class);

        if (null == impexHeaderLine) {
            return;
        }

        final List<ImpexFullHeaderParameter> childrenOfType = PsiTreeUtil.getChildrenOfTypeAsList(impexHeaderLine, ImpexFullHeaderParameter.class);

        if (columnNumber >= childrenOfType.size()) {
            return;
        }

        if (!highlightedBlocks.isEmpty()) {
            final ImpexFullHeaderParameter impexFullHeaderParameter = highlightedBlocks.get(editor);
            if (null != impexFullHeaderParameter) {
                highlightRanges(
                        HighlightManager.getInstance(editor.getProject()),
                        editor,
                        EditorColorsManager.getInstance().getGlobalScheme().getAttributes(EditorColors.SEARCH_RESULT_ATTRIBUTES),
                        true,
                        Arrays.asList(impexFullHeaderParameter.getTextRange())
                );
            }
        }

        final ImpexFullHeaderParameter impexFullHeaderParameter = childrenOfType.get(columnNumber);
        highlightedBlocks.put(editor, impexFullHeaderParameter);

        highlightRanges(
                HighlightManager.getInstance(editor.getProject()),
                editor,
                EditorColorsManager.getInstance().getGlobalScheme().getAttributes(EditorColors.SEARCH_RESULT_ATTRIBUTES),
                false,
                Arrays.asList(impexFullHeaderParameter.getTextRange())
        );
    }

    @Nullable
    private PsiElement getPsiElementUnderCaret(final Editor editor) {

        if ((null == editor) || (null == editor.getProject())) {
            return null;
        }

        final PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, editor.getProject());

        if (psiFile != null) {
            return psiFile.findElementAt(editor.getCaretModel().getOffset());
        }

        return null;
    }

    @Override
    public void initComponent() {
        EditorFactory.getInstance().getEventMulticaster().addCaretListener(this);
    }

    @Override
    public void disposeComponent() {
        EditorFactory.getInstance().getEventMulticaster().removeCaretListener(this);
    }

    @NotNull
    @Override
    public String getComponentName() {
        return this.getClass().getName();
    }
}
