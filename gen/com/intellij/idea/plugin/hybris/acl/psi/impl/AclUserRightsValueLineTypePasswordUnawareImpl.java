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
package com.intellij.idea.plugin.hybris.acl.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.intellij.idea.plugin.hybris.acl.psi.AclTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.acl.psi.*;

public class AclUserRightsValueLineTypePasswordUnawareImpl extends ASTWrapperPsiElement implements AclUserRightsValueLineTypePasswordUnaware {

  public AclUserRightsValueLineTypePasswordUnawareImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AclVisitor visitor) {
    visitor.visitUserRightsValueLineTypePasswordUnaware(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AclVisitor) accept((AclVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<AclUserRightsValueGroupBlank> getUserRightsValueGroupBlankList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AclUserRightsValueGroupBlank.class);
  }

  @Override
  @Nullable
  public AclUserRightsValueGroupMemberOfGroups getUserRightsValueGroupMemberOfGroups() {
    return findChildByClass(AclUserRightsValueGroupMemberOfGroups.class);
  }

  @Override
  @NotNull
  public AclUserRightsValueGroupType getUserRightsValueGroupType() {
    return findNotNullChildByClass(AclUserRightsValueGroupType.class);
  }

  @Override
  @Nullable
  public AclUserRightsValueGroupUid getUserRightsValueGroupUid() {
    return findChildByClass(AclUserRightsValueGroupUid.class);
  }

}
