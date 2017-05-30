// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchCharacterSubstringFunction;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchStringValueExpression;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.*;

import java.util.List;

public class FlexibleSearchCharacterSubstringFunctionImpl extends ASTWrapperPsiElement
    implements FlexibleSearchCharacterSubstringFunction {

    public FlexibleSearchCharacterSubstringFunctionImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitCharacterSubstringFunction(this);
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
    public FlexibleSearchCharacterSubstringFunction getCharacterSubstringFunction() {
        return findChildByClass(FlexibleSearchCharacterSubstringFunction.class);
    }

    @Override
    @NotNull
    public List<FlexibleSearchStringValueExpression> getStringValueExpressionList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FlexibleSearchStringValueExpression.class);
    }

}
