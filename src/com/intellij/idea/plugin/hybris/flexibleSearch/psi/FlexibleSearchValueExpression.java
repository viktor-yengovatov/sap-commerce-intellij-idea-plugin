// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface FlexibleSearchValueExpression extends PsiElement {

  @Nullable
  FlexibleSearchColumnLocalization getColumnLocalization();

  @Nullable
  FlexibleSearchColumnReference getColumnReference();

  @Nullable
  FlexibleSearchParameterReference getParameterReference();

  @Nullable
  PsiElement getLeftBrace();

  @Nullable
  PsiElement getNumber();

  @Nullable
  PsiElement getRightBrace();

}
