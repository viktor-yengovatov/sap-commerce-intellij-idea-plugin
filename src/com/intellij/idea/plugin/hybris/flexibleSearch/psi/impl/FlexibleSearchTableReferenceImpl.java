// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchJoinedTable;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTablePrimary;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTableReference;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlexibleSearchTableReferenceImpl extends ASTWrapperPsiElement implements FlexibleSearchTableReference {

    public FlexibleSearchTableReferenceImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitTableReference(this);
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
    public FlexibleSearchJoinedTable getJoinedTable() {
        return findChildByClass(FlexibleSearchJoinedTable.class);
    }

    @Override
    @Nullable
    public FlexibleSearchTablePrimary getTablePrimary() {
        return findChildByClass(FlexibleSearchTablePrimary.class);
    }

}
