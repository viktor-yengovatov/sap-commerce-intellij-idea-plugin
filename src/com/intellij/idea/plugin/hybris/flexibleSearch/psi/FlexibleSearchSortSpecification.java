// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FlexibleSearchSortSpecification extends PsiElement {

    @Nullable
    FlexibleSearchNullOrdering getNullOrdering();

    @Nullable
    FlexibleSearchOrderingSpecification getOrderingSpecification();

    @NotNull
    FlexibleSearchSortKey getSortKey();

}
