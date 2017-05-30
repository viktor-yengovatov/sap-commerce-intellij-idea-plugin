// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchCorrelationName;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTableName;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTablePrimary;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlexibleSearchTablePrimaryImpl extends ASTWrapperPsiElement implements FlexibleSearchTablePrimary {

    public FlexibleSearchTablePrimaryImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitTablePrimary(this);
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
    public FlexibleSearchTableName getTableName() {
        return findNotNullChildByClass(FlexibleSearchTableName.class);
    }

}
