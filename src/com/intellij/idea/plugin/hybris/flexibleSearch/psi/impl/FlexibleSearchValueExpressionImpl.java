// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchColumnReference;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchLang;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchValueExpression;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.NUMBER;

public class FlexibleSearchValueExpressionImpl extends ASTWrapperPsiElement implements FlexibleSearchValueExpression {

    public FlexibleSearchValueExpressionImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitValueExpression(this);
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
    public FlexibleSearchColumnReference getColumnReference() {
        return findChildByClass(FlexibleSearchColumnReference.class);
    }

    @Override
    @Nullable
    public FlexibleSearchLang getLang() {
        return findChildByClass(FlexibleSearchLang.class);
    }

    @Override
    @Nullable
    public PsiElement getNumber() {
        return findChildByType(NUMBER);
    }

}
