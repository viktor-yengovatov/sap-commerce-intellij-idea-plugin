// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.impex.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexMacroDeclaration;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexMacroNameDec;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexMacroUsageDec;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexMacroValueDec;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ImpexMacroDeclarationImpl extends ASTWrapperPsiElement implements ImpexMacroDeclaration {

  public ImpexMacroDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ImpexVisitor visitor) {
    visitor.visitMacroDeclaration(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ImpexVisitor) accept((ImpexVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public ImpexMacroNameDec getMacroNameDec() {
    return findNotNullChildByClass(ImpexMacroNameDec.class);
  }

  @Override
  @Nullable
  public ImpexMacroUsageDec getMacroUsageDec() {
    return findChildByClass(ImpexMacroUsageDec.class);
  }

  @Override
  @Nullable
  public ImpexMacroValueDec getMacroValueDec() {
    return findChildByClass(ImpexMacroValueDec.class);
  }

}
