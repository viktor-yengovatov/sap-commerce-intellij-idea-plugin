package com.intellij.idea.plugin.hybris.impex.folding.simple;

import com.intellij.idea.plugin.hybris.impex.folding.ImpexFoldingPlaceholderBuilder;
import com.intellij.psi.PsiElement;
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

        return this.getFirstAndLastCharacters(psiElement);
    }

    @NotNull
    private String getFirstAndLastCharacters(@NotNull final PsiElement psiElement) {
        final String text = psiElement.getText();

        if (text.length() < 2) {
            return psiElement.getText();
        }

        return new StringBuilder()
                .append(text.charAt(0))
                .append(text.charAt(text.length() - 1))
                .toString();
    }
}
