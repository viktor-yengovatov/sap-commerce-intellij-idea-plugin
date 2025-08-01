/*
 * This file is part of "SAP Commerce Developers Toolset" plugin for IntelliJ IDEA.
 * Copyright (C) 2019-2025 EPAM Systems <hybrisideaplugin@epam.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * ----------------------------------------------------------------
 */
package com.intellij.idea.plugin.hybris.impex.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes.*;
import com.intellij.idea.plugin.hybris.impex.psi.*;
import java.util.Collection;

public class ImpexUserRightsImpl extends ImpExUserRightsMixin implements ImpexUserRights {

  public ImpexUserRightsImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ImpexVisitor visitor) {
    visitor.visitUserRights(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ImpexVisitor) accept((ImpexVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ImpexUserRightsEnd getUserRightsEnd() {
    return findChildByClass(ImpexUserRightsEnd.class);
  }

  @Override
  @Nullable
  public ImpexUserRightsHeaderLine getUserRightsHeaderLine() {
    return findChildByClass(ImpexUserRightsHeaderLine.class);
  }

  @Override
  @NotNull
  public ImpexUserRightsStart getUserRightsStart() {
    return findNotNullChildByClass(ImpexUserRightsStart.class);
  }

  @Override
  @NotNull
  public List<ImpexUserRightsValueLine> getUserRightsValueLineList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ImpexUserRightsValueLine.class);
  }

}
