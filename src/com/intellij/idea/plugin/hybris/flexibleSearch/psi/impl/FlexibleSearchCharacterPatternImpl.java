// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchCharacterPattern;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchStringValueFunction;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.*;

import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.STRING;

public class FlexibleSearchCharacterPatternImpl extends ASTWrapperPsiElement implements FlexibleSearchCharacterPattern {

    public FlexibleSearchCharacterPatternImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitCharacterPattern(this);
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
    public FlexibleSearchStringValueFunction getStringValueFunction() {
        return findChildByClass(FlexibleSearchStringValueFunction.class);
    }

    @Override
    @Nullable
    public PsiElement getString() {
        return findChildByType(STRING);
    }

}
