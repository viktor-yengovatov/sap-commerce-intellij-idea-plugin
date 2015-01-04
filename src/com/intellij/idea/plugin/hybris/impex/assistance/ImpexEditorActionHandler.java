package com.intellij.idea.plugin.hybris.impex.assistance;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nullable;

/**
 * Created 01:29 02 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexEditorActionHandler extends EditorActionHandler {

//    @Override
//    protected boolean isEnabledForCaret(final Editor editor, final Caret caret, final DataContext dataContext) {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled(final Editor editor, final DataContext dataContext) {
//        return true;
//    }

    @Override
    protected void doExecute(final Editor editor, final Caret caret, final DataContext dataContext) {
        super.doExecute(editor, caret, dataContext);

        final PsiElement psiElementUnderCaret = this.getPsiElementUnderCaret(dataContext);
    }

    @Nullable
    private PsiElement getPsiElementUnderCaret(final DataContext dataContext) {
        final PsiFile psiFile = DataKeys.PSI_FILE.getData(dataContext);
        final Editor editor = DataKeys.EDITOR.getData(dataContext);

        if ((psiFile != null) && (editor != null)) {
            return psiFile.findElementAt(editor.getCaretModel().getOffset());
        }

        return null;
    }
}
