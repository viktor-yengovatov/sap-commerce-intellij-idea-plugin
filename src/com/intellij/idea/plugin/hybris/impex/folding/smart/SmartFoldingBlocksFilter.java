package com.intellij.idea.plugin.hybris.impex.folding.smart;

import com.intellij.idea.plugin.hybris.impex.folding.ImpexFoldingPlaceholderBuilderFactory;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexAttribute;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexParameters;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiElementFilter;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import static com.intellij.idea.plugin.hybris.impex.util.ImpexPsiUtil.isLineBreak;

/**
 * Created 22:40 01 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class SmartFoldingBlocksFilter implements PsiElementFilter {

    @Override
    public boolean isAccepted(@Nullable final PsiElement eachElement) {
        return null != eachElement && (isFoldable(eachElement) && isNotFoldableParent(eachElement));
    }

    @Contract(pure = true)
    private boolean isFoldable(@Nullable final PsiElement element) {
        return null != element
               && this.isSupportedType(element)
               && (isLineBreak(element) || this.isNotBlankPlaceholder(element));
    }

    @Contract(pure = true)
    private boolean isNotBlankPlaceholder(final @Nullable PsiElement element) {
        return (null != element) && !StringUtils.isBlank(
                ImpexFoldingPlaceholderBuilderFactory.getPlaceholderBuilder().getPlaceholder(element)
        );
    }

    @Contract(pure = true)
    private boolean isSupportedType(final @Nullable PsiElement element) {
        return element instanceof ImpexAttribute
               || element instanceof ImpexParameters
               || isLineBreak(element);
    }

    @Contract(pure = true)
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