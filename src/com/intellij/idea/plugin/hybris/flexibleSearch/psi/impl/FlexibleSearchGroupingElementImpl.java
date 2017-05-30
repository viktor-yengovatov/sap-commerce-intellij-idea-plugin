// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchGroupingElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchOrdinaryGroupingSet;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.*;

public class FlexibleSearchGroupingElementImpl extends ASTWrapperPsiElement implements FlexibleSearchGroupingElement {

    public FlexibleSearchGroupingElementImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitGroupingElement(this);
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
    public FlexibleSearchOrdinaryGroupingSet getOrdinaryGroupingSet() {
        return findNotNullChildByClass(FlexibleSearchOrdinaryGroupingSet.class);
    }

}
