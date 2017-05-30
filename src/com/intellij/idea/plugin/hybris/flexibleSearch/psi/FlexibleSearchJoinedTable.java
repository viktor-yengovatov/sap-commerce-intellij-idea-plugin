// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.*;

public interface FlexibleSearchJoinedTable extends PsiElement {

    @Nullable
    FlexibleSearchJoinSpecification getJoinSpecification();

    @Nullable
    FlexibleSearchJoinType getJoinType();

    @Nullable
    FlexibleSearchJoinedTable getJoinedTable();

    @Nullable
    FlexibleSearchTablePrimary getTablePrimary();

    @Nullable
    FlexibleSearchTableReference getTableReference();

}
