// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface FlexibleSearchPredicate extends PsiElement {

    @Nullable
    FlexibleSearchBetweenPredicate getBetweenPredicate();

    @Nullable
    FlexibleSearchCompOp getCompOp();

    @Nullable
    FlexibleSearchExistsPredicate getExistsPredicate();

    @Nullable
    FlexibleSearchLikePredicate getLikePredicate();

    @Nullable
    FlexibleSearchNullPredicate getNullPredicate();

    @NotNull
    List<FlexibleSearchRowValuePredicand> getRowValuePredicandList();

}
