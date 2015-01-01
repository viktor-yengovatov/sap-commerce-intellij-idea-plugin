package com.intellij.idea.plugin.hybris.impex.folding;

import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created 22:34 01 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexFoldingDescriptor extends FoldingDescriptor {

    private final String placeholder;
    private static final ImpexFoldingPlaceholderBuilder placeholderBuilder = new DefaultImpexFoldingPlaceholderBuilder();

    public ImpexFoldingDescriptor(@NotNull final PsiElement psiElement,
                                  @NotNull final FoldingGroup group) {
        super(
                psiElement.getNode(),
                new TextRange(
                        psiElement.getTextRange().getStartOffset(),
                        psiElement.getTextRange().getEndOffset()
                ),
                group
        );

        placeholder = placeholderBuilder.getPlaceholder(psiElement);
    }

    @Nullable
    @Override
    public String getPlaceholderText() {
        return placeholder;
    }
}