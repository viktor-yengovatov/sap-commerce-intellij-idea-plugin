// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchBooleanPredicand;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchBooleanPrimary;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchPredicate;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.*;

public class FlexibleSearchBooleanPrimaryImpl extends ASTWrapperPsiElement implements FlexibleSearchBooleanPrimary {

    public FlexibleSearchBooleanPrimaryImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitBooleanPrimary(this);
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
    public FlexibleSearchBooleanPredicand getBooleanPredicand() {
        return findChildByClass(FlexibleSearchBooleanPredicand.class);
    }

    @Override
    @Nullable
    public FlexibleSearchPredicate getPredicate() {
        return findChildByClass(FlexibleSearchPredicate.class);
    }

}
