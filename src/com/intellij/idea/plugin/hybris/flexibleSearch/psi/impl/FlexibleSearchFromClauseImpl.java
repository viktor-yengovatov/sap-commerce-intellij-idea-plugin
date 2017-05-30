// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchFromClause;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchSubquery;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTableReferenceList;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.*;

public class FlexibleSearchFromClauseImpl extends ASTWrapperPsiElement implements FlexibleSearchFromClause {

    public FlexibleSearchFromClauseImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitFromClause(this);
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
    public FlexibleSearchSubquery getSubquery() {
        return findChildByClass(FlexibleSearchSubquery.class);
    }

    @Override
    @Nullable
    public FlexibleSearchTableReferenceList getTableReferenceList() {
        return findChildByClass(FlexibleSearchTableReferenceList.class);
    }

}
