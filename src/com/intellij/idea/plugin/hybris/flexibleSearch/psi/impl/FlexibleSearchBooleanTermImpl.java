// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchBooleanFactor;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchBooleanTerm;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.*;

import java.util.List;

public class FlexibleSearchBooleanTermImpl extends ASTWrapperPsiElement implements FlexibleSearchBooleanTerm {

    public FlexibleSearchBooleanTermImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitBooleanTerm(this);
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
    public FlexibleSearchBooleanFactor getBooleanFactor() {
        return findNotNullChildByClass(FlexibleSearchBooleanFactor.class);
    }

    @Override
    @NotNull
    public List<FlexibleSearchBooleanTerm> getBooleanTermList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FlexibleSearchBooleanTerm.class);
    }

}
