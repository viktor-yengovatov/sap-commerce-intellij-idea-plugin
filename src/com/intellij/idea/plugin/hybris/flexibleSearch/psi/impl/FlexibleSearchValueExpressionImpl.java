// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchColumnLocalization;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchColumnReference;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchParameterReference;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchValueExpression;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.LEFT_BRACE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.NUMBER;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.RIGHT_BRACE;

public class FlexibleSearchValueExpressionImpl extends ASTWrapperPsiElement implements FlexibleSearchValueExpression {

  public FlexibleSearchValueExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull FlexibleSearchVisitor visitor) {
    visitor.visitValueExpression(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof FlexibleSearchVisitor) accept((FlexibleSearchVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public FlexibleSearchColumnLocalization getColumnLocalization() {
    return findChildByClass(FlexibleSearchColumnLocalization.class);
  }

  @Override
  @Nullable
  public FlexibleSearchColumnReference getColumnReference() {
    return findChildByClass(FlexibleSearchColumnReference.class);
  }

  @Override
  @Nullable
  public FlexibleSearchParameterReference getParameterReference() {
    return findChildByClass(FlexibleSearchParameterReference.class);
  }

  @Override
  @Nullable
  public PsiElement getLeftBrace() {
    return findChildByType(LEFT_BRACE);
  }

  @Override
  @Nullable
  public PsiElement getNumber() {
    return findChildByType(NUMBER);
  }

  @Override
  @Nullable
  public PsiElement getRightBrace() {
    return findChildByType(RIGHT_BRACE);
  }

}
