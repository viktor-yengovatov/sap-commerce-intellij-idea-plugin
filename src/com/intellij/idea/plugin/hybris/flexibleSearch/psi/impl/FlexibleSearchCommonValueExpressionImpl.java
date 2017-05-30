// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchCommonValueExpression;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchStringValueExpression;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class FlexibleSearchCommonValueExpressionImpl extends ASTWrapperPsiElement
    implements FlexibleSearchCommonValueExpression {

    public FlexibleSearchCommonValueExpressionImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitCommonValueExpression(this);
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
    public FlexibleSearchStringValueExpression getStringValueExpression() {
        return findNotNullChildByClass(FlexibleSearchStringValueExpression.class);
    }

}
