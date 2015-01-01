package com.intellij.idea.plugin.hybris.impex.folding;

import com.intellij.idea.plugin.hybris.impex.psi.ImpexModifiersBlock;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexParametersBlock;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiElementFilter;
import org.jetbrains.annotations.Nullable;

import static com.intellij.idea.plugin.hybris.impex.util.ImpexPsiUtil.isLineBreak;

/**
 * Created 22:40 01 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class FoldingBlocksFilter implements PsiElementFilter {

    @Override
    public boolean isAccepted(@Nullable final PsiElement eachElement) {
        return null != eachElement && (isFoldable(eachElement) && isNotFoldableParent(eachElement));
    }

    private boolean isFoldable(@Nullable final PsiElement element) {
        if (null == element) {
            return false;
        }

        return element instanceof ImpexModifiersBlock
               || element instanceof ImpexParametersBlock
               || isLineBreak(element);
    }

    private boolean isNotFoldableParent(@Nullable final PsiElement element) {
        if (null == element) {
            return false;
        }

        PsiElement parent = element.getParent();
        while (null != parent) {
            if (isFoldable(parent)) {
                return false;
            }

            parent = parent.getParent();
        }

        return true;
    }
}