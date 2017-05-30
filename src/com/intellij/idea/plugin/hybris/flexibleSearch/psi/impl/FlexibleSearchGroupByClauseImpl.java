// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchGroupByClause;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchGroupingElementList;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchSetQuantifier;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlexibleSearchGroupByClauseImpl extends ASTWrapperPsiElement implements FlexibleSearchGroupByClause {

    public FlexibleSearchGroupByClauseImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitGroupByClause(this);
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
    public FlexibleSearchGroupingElementList getGroupingElementList() {
        return findNotNullChildByClass(FlexibleSearchGroupingElementList.class);
    }

    @Override
    @Nullable
    public FlexibleSearchSetQuantifier getSetQuantifier() {
        return findChildByClass(FlexibleSearchSetQuantifier.class);
    }

}
