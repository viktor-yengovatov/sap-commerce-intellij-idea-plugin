// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface FlexibleSearchComparisonPredicate extends PsiElement {

    @NotNull
    FlexibleSearchCompOp getCompOp();

    @NotNull
    List<FlexibleSearchRowValuePredicand> getRowValuePredicandList();

}
