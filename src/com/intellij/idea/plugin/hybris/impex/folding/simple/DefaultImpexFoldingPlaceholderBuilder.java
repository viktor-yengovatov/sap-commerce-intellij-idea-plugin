package com.intellij.idea.plugin.hybris.impex.folding.simple;

import com.intellij.idea.plugin.hybris.impex.folding.ImpexFoldingPlaceholderBuilder;
import com.intellij.psi.PsiElement;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Created 23:16 01 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class DefaultImpexFoldingPlaceholderBuilder implements ImpexFoldingPlaceholderBuilder {

    @NotNull
    @Override
    public String getPlaceholder(@NotNull final PsiElement psiElement) {
        Validate.notNull(psiElement);

        return this.getFirstAndLastCharacters(psiElement);
    }

    @NotNull
    @Contract(pure = true)
    private String getFirstAndLastCharacters(@NotNull final PsiElement psiElement) {
        Validate.notNull(psiElement);

        final String text = psiElement.getText();

        if (text.length() < 2) {
            return psiElement.getText();
        }

        return String.valueOf(text.charAt(0)) + text.charAt(text.length() - 1);
    }
}
