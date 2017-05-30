// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchGroupingColumnReference;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchGroupingColumnReferenceList;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchOrdinaryGroupingSet;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.*;

public class FlexibleSearchOrdinaryGroupingSetImpl extends ASTWrapperPsiElement
    implements FlexibleSearchOrdinaryGroupingSet {

    public FlexibleSearchOrdinaryGroupingSetImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitOrdinaryGroupingSet(this);
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
    public FlexibleSearchGroupingColumnReference getGroupingColumnReference() {
        return findChildByClass(FlexibleSearchGroupingColumnReference.class);
    }

    @Override
    @Nullable
    public FlexibleSearchGroupingColumnReferenceList getGroupingColumnReferenceList() {
        return findChildByClass(FlexibleSearchGroupingColumnReferenceList.class);
    }

}
