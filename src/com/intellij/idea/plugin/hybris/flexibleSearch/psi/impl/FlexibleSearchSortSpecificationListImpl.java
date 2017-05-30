// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchSortSpecification;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchSortSpecificationList;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.*;

import java.util.List;

public class FlexibleSearchSortSpecificationListImpl extends ASTWrapperPsiElement
    implements FlexibleSearchSortSpecificationList {

    public FlexibleSearchSortSpecificationListImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitSortSpecificationList(this);
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
    public List<FlexibleSearchSortSpecification> getSortSpecificationList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FlexibleSearchSortSpecification.class);
    }

}
