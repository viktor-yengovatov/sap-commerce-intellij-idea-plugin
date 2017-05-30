// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchBooleanPrimary;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchBooleanTest;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTruthValue;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlexibleSearchBooleanTestImpl extends ASTWrapperPsiElement implements FlexibleSearchBooleanTest {

    public FlexibleSearchBooleanTestImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitBooleanTest(this);
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
    public FlexibleSearchBooleanPrimary getBooleanPrimary() {
        return findNotNullChildByClass(FlexibleSearchBooleanPrimary.class);
    }

    @Override
    @Nullable
    public FlexibleSearchTruthValue getTruthValue() {
        return findChildByClass(FlexibleSearchTruthValue.class);
    }

}
