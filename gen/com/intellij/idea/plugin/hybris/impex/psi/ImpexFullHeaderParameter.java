// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.impex.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ImpexFullHeaderParameter extends PsiElement {

  @NotNull
  ImpexAnyHeaderParameterName getAnyHeaderParameterName();

  @NotNull
  List<ImpexModifiers> getModifiersList();

  @NotNull
  List<ImpexParameters> getParametersList();

}
