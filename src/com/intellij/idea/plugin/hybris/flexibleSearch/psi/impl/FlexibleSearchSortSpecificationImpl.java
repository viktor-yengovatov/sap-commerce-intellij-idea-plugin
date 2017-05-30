// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchNullOrdering;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchOrderingSpecification;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchSortKey;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchSortSpecification;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlexibleSearchSortSpecificationImpl extends ASTWrapperPsiElement
    implements FlexibleSearchSortSpecification {

    public FlexibleSearchSortSpecificationImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitSortSpecification(this);
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
    public FlexibleSearchNullOrdering getNullOrdering() {
        return findChildByClass(FlexibleSearchNullOrdering.class);
    }

    @Override
    @Nullable
    public FlexibleSearchOrderingSpecification getOrderingSpecification() {
        return findChildByClass(FlexibleSearchOrderingSpecification.class);
    }

    @Override
    @NotNull
    public FlexibleSearchSortKey getSortKey() {
        return findNotNullChildByClass(FlexibleSearchSortKey.class);
    }

}
