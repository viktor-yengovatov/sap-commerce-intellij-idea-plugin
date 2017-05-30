// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchGroupingColumnReference;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchGroupingColumnReferenceList;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FlexibleSearchGroupingColumnReferenceListImpl extends ASTWrapperPsiElement
    implements FlexibleSearchGroupingColumnReferenceList {

    public FlexibleSearchGroupingColumnReferenceListImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitGroupingColumnReferenceList(this);
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
    public List<FlexibleSearchGroupingColumnReference> getGroupingColumnReferenceList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FlexibleSearchGroupingColumnReference.class);
    }

}
