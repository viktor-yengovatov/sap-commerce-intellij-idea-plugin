// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchCorrelationName;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchGeneralSetFunction;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchSetFunctionType;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchSetQuantifier;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchValueExpression;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.*;

public class FlexibleSearchGeneralSetFunctionImpl extends ASTWrapperPsiElement
    implements FlexibleSearchGeneralSetFunction {

    public FlexibleSearchGeneralSetFunctionImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitGeneralSetFunction(this);
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
    public FlexibleSearchCorrelationName getCorrelationName() {
        return findChildByClass(FlexibleSearchCorrelationName.class);
    }

    @Override
    @NotNull
    public FlexibleSearchSetFunctionType getSetFunctionType() {
        return findNotNullChildByClass(FlexibleSearchSetFunctionType.class);
    }

    @Override
    @Nullable
    public FlexibleSearchSetQuantifier getSetQuantifier() {
        return findChildByClass(FlexibleSearchSetQuantifier.class);
    }

    @Override
    @NotNull
    public FlexibleSearchValueExpression getValueExpression() {
        return findNotNullChildByClass(FlexibleSearchValueExpression.class);
    }

}
