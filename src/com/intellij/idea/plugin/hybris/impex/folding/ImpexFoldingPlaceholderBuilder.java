package com.intellij.idea.plugin.hybris.impex.folding;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created 23:16 01 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public interface ImpexFoldingPlaceholderBuilder {

    @NotNull
    String getPlaceholder(@NotNull final PsiElement psiElement);
}
