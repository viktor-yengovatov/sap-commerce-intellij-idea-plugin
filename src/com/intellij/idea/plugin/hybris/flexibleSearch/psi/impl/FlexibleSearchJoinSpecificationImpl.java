// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchJoinCondition;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchJoinSpecification;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.*;

public class FlexibleSearchJoinSpecificationImpl extends ASTWrapperPsiElement
    implements FlexibleSearchJoinSpecification {

    public FlexibleSearchJoinSpecificationImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitJoinSpecification(this);
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
    public FlexibleSearchJoinCondition getJoinCondition() {
        return findNotNullChildByClass(FlexibleSearchJoinCondition.class);
    }

}
