// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchCharacterLikePredicate;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchCharacterPattern;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchRowValuePredicand;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchValueExpression;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.*;

public class FlexibleSearchCharacterLikePredicateImpl extends ASTWrapperPsiElement
    implements FlexibleSearchCharacterLikePredicate {

    public FlexibleSearchCharacterLikePredicateImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitCharacterLikePredicate(this);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FlexibleSearchVisitor) {
            accept((FlexibleSearchVisitor) visitor);
        } else {
            super.accept(visitor);
        }
    }

    @Override
    @Nullable
    public FlexibleSearchCharacterPattern getCharacterPattern() {
        return findChildByClass(FlexibleSearchCharacterPattern.class);
    }

    @Override
    @NotNull
    public FlexibleSearchRowValuePredicand getRowValuePredicand() {
        return findNotNullChildByClass(FlexibleSearchRowValuePredicand.class);
    }

    @Override
    @Nullable
    public FlexibleSearchValueExpression getValueExpression() {
        return findChildByClass(FlexibleSearchValueExpression.class);
    }

}
