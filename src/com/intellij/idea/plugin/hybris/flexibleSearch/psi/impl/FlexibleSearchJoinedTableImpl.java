// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchJoinSpecification;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchJoinType;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchJoinedTable;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTablePrimary;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTableReference;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlexibleSearchJoinedTableImpl extends ASTWrapperPsiElement implements FlexibleSearchJoinedTable {

    public FlexibleSearchJoinedTableImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitJoinedTable(this);
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
    public FlexibleSearchJoinSpecification getJoinSpecification() {
        return findChildByClass(FlexibleSearchJoinSpecification.class);
    }

    @Override
    @Nullable
    public FlexibleSearchJoinType getJoinType() {
        return findChildByClass(FlexibleSearchJoinType.class);
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

    @Override
    @Nullable
    public FlexibleSearchTableReference getTableReference() {
        return findChildByClass(FlexibleSearchTableReference.class);
    }

}
