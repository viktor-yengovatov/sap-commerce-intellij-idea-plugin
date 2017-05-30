// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchGroupingColumnReference;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchValueExpression;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class FlexibleSearchGroupingColumnReferenceImpl extends ASTWrapperPsiElement
    implements FlexibleSearchGroupingColumnReference {

    public FlexibleSearchGroupingColumnReferenceImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitGroupingColumnReference(this);
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
    public FlexibleSearchValueExpression getValueExpression() {
        return findNotNullChildByClass(FlexibleSearchValueExpression.class);
    }

}
