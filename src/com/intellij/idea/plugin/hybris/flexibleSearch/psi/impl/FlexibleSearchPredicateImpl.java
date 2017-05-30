// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchBetweenPredicate;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchCompOp;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchExistsPredicate;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchLikePredicate;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchNullPredicate;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchPredicate;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchRowValuePredicand;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.*;

import java.util.List;

public class FlexibleSearchPredicateImpl extends ASTWrapperPsiElement implements FlexibleSearchPredicate {

    public FlexibleSearchPredicateImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitPredicate(this);
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
    public FlexibleSearchBetweenPredicate getBetweenPredicate() {
        return findChildByClass(FlexibleSearchBetweenPredicate.class);
    }

    @Override
    @Nullable
    public FlexibleSearchCompOp getCompOp() {
        return findChildByClass(FlexibleSearchCompOp.class);
    }

    @Override
    @Nullable
    public FlexibleSearchExistsPredicate getExistsPredicate() {
        return findChildByClass(FlexibleSearchExistsPredicate.class);
    }

    @Override
    @Nullable
    public FlexibleSearchLikePredicate getLikePredicate() {
        return findChildByClass(FlexibleSearchLikePredicate.class);
    }

    @Override
    @Nullable
    public FlexibleSearchNullPredicate getNullPredicate() {
        return findChildByClass(FlexibleSearchNullPredicate.class);
    }

    @Override
    @NotNull
    public List<FlexibleSearchRowValuePredicand> getRowValuePredicandList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FlexibleSearchRowValuePredicand.class);
    }

}
