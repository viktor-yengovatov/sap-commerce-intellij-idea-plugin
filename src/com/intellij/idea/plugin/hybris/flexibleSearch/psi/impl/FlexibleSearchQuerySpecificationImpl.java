// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchQuerySpecification;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchSelectList;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchSetQuantifier;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTableExpression;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.*;

public class FlexibleSearchQuerySpecificationImpl extends ASTWrapperPsiElement
    implements FlexibleSearchQuerySpecification {

    public FlexibleSearchQuerySpecificationImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitQuerySpecification(this);
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
    public FlexibleSearchSelectList getSelectList() {
        return findChildByClass(FlexibleSearchSelectList.class);
    }

    @Override
    @Nullable
    public FlexibleSearchSetQuantifier getSetQuantifier() {
        return findChildByClass(FlexibleSearchSetQuantifier.class);
    }

    @Override
    @Nullable
    public FlexibleSearchTableExpression getTableExpression() {
        return findChildByClass(FlexibleSearchTableExpression.class);
    }

}
