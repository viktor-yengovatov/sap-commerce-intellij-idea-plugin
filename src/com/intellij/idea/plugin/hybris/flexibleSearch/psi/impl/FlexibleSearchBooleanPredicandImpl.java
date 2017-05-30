// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchBooleanPredicand;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchBooleanValueExpression;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchColumnReference;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.*;

public class FlexibleSearchBooleanPredicandImpl extends ASTWrapperPsiElement implements FlexibleSearchBooleanPredicand {

    public FlexibleSearchBooleanPredicandImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitBooleanPredicand(this);
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
    public FlexibleSearchBooleanValueExpression getBooleanValueExpression() {
        return findChildByClass(FlexibleSearchBooleanValueExpression.class);
    }

    @Override
    @Nullable
    public FlexibleSearchColumnReference getColumnReference() {
        return findChildByClass(FlexibleSearchColumnReference.class);
    }

}
