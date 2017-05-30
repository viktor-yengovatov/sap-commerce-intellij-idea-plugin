// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchAggregateFunction;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchDerivedColumn;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchSelectSublist;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FlexibleSearchSelectSublistImpl extends ASTWrapperPsiElement implements FlexibleSearchSelectSublist {

    public FlexibleSearchSelectSublistImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitSelectSublist(this);
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
    public List<FlexibleSearchAggregateFunction> getAggregateFunctionList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FlexibleSearchAggregateFunction.class);
    }

    @Override
    @Nullable
    public FlexibleSearchDerivedColumn getDerivedColumn() {
        return findChildByClass(FlexibleSearchDerivedColumn.class);
    }

}
