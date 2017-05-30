// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchCharacterSubstringFunction;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchCharacterValueFunction;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.*;

public class FlexibleSearchCharacterValueFunctionImpl extends ASTWrapperPsiElement
    implements FlexibleSearchCharacterValueFunction {

    public FlexibleSearchCharacterValueFunctionImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitCharacterValueFunction(this);
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
    public FlexibleSearchCharacterSubstringFunction getCharacterSubstringFunction() {
        return findNotNullChildByClass(FlexibleSearchCharacterSubstringFunction.class);
    }

}
