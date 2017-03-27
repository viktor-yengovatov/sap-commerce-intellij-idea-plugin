// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface FlexibleSearchJoinedTable extends PsiElement {

  @NotNull
  FlexibleSearchJoinSpecification getJoinSpecification();

  @Nullable
  FlexibleSearchJoinType getJoinType();

  @Nullable
  FlexibleSearchJoinedTable getJoinedTable();

  @Nullable
  FlexibleSearchTablePrimary getTablePrimary();

  @NotNull
  FlexibleSearchTableReference getTableReference();

}
