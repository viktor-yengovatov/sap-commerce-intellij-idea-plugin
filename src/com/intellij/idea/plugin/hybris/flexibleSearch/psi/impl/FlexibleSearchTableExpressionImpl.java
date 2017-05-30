// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchFromClause;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchGroupByClause;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchOrderByClause;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTableExpression;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchWhereClause;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.*;

public class FlexibleSearchTableExpressionImpl extends ASTWrapperPsiElement implements FlexibleSearchTableExpression {

    public FlexibleSearchTableExpressionImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitTableExpression(this);
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
    public FlexibleSearchFromClause getFromClause() {
        return findNotNullChildByClass(FlexibleSearchFromClause.class);
    }

    @Override
    @Nullable
    public FlexibleSearchGroupByClause getGroupByClause() {
        return findChildByClass(FlexibleSearchGroupByClause.class);
    }

    @Override
    @Nullable
    public FlexibleSearchOrderByClause getOrderByClause() {
        return findChildByClass(FlexibleSearchOrderByClause.class);
    }

    @Override
    @Nullable
    public FlexibleSearchWhereClause getWhereClause() {
        return findChildByClass(FlexibleSearchWhereClause.class);
    }

}
