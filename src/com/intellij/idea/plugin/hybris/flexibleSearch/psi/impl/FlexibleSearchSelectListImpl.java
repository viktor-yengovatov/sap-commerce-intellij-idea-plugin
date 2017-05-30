// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchSelectList;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchSelectSublist;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FlexibleSearchSelectListImpl extends ASTWrapperPsiElement implements FlexibleSearchSelectList {

    public FlexibleSearchSelectListImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitSelectList(this);
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
    public List<FlexibleSearchSelectSublist> getSelectSublistList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FlexibleSearchSelectSublist.class);
    }

}
