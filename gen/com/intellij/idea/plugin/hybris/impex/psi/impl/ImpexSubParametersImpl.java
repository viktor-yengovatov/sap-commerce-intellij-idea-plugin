// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.impex.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes.*;
import com.intellij.idea.plugin.hybris.impex.psi.*;

public class ImpexSubParametersImpl extends ImpexParametersImpl implements ImpexSubParameters {

  public ImpexSubParametersImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ImpexVisitor) ((ImpexVisitor)visitor).visitSubParameters(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ImpexParameter> getParameterList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ImpexParameter.class);
  }

}
