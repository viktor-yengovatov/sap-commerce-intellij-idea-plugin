// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FlexibleSearchTableExpression extends PsiElement {

    @NotNull
    FlexibleSearchFromClause getFromClause();

    @Nullable
    FlexibleSearchGroupByClause getGroupByClause();

    @Nullable
    FlexibleSearchOrderByClause getOrderByClause();

    @Nullable
    FlexibleSearchWhereClause getWhereClause();

}
