// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchCharacterLikePredicate;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchLikePredicate;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.*;

public class FlexibleSearchLikePredicateImpl extends ASTWrapperPsiElement implements FlexibleSearchLikePredicate {

    public FlexibleSearchLikePredicateImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitLikePredicate(this);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FlexibleSearchVisitor) {
            accept((FlexibleSearchVisitor) visitor);
        } else {
            super.accept(visitor);
        }
    }

    @Override
    @NotNull
    public FlexibleSearchCharacterLikePredicate getCharacterLikePredicate() {
        return findNotNullChildByClass(FlexibleSearchCharacterLikePredicate.class);
    }

}
