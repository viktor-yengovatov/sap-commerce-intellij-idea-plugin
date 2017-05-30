// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchCorrelationName;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchQuerySpecification;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchSubquery;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.*;

import java.util.List;

import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.LEFT_DOUBLE_BRACE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.RIGHT_DOUBLE_BRACE;

public class FlexibleSearchSubqueryImpl extends ASTWrapperPsiElement implements FlexibleSearchSubquery {

    public FlexibleSearchSubqueryImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitSubquery(this);
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
    public FlexibleSearchCorrelationName getCorrelationName() {
        return findChildByClass(FlexibleSearchCorrelationName.class);
    }

    @Override
    @NotNull
    public FlexibleSearchQuerySpecification getQuerySpecification() {
        return findNotNullChildByClass(FlexibleSearchQuerySpecification.class);
    }

    @Override
    @NotNull
    public List<FlexibleSearchSubquery> getSubqueryList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FlexibleSearchSubquery.class);
    }

    @Override
    @NotNull
    public PsiElement getLeftDoubleBrace() {
        return findNotNullChildByType(LEFT_DOUBLE_BRACE);
    }

    @Override
    @NotNull
    public PsiElement getRightDoubleBrace() {
        return findNotNullChildByType(RIGHT_DOUBLE_BRACE);
    }

}
