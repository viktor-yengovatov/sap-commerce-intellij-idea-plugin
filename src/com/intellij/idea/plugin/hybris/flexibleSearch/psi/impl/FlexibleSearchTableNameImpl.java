// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTableName;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.references.TypeNameMixin;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.TABLE_NAME_IDENTIFIER;

public class FlexibleSearchTableNameImpl extends TypeNameMixin implements FlexibleSearchTableName {

    public FlexibleSearchTableNameImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitTableName(this);
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
    public PsiElement getTableNameIdentifier() {
        return findNotNullChildByType(TABLE_NAME_IDENTIFIER);
    }

}
