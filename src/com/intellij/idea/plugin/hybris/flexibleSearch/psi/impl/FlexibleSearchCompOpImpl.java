/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2016 Alexander Bartash <AlexanderBartash@gmail.com>
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

// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchCompOp;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.EQUALS_OPERATOR;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.GREATER_THAN_OR_EQUALS_OPERATOR;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.LESS_THAN_OR_EQUALS_OPERATOR;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.NOT_EQUALS_OPERATOR;

public class FlexibleSearchCompOpImpl extends ASTWrapperPsiElement implements FlexibleSearchCompOp {

    public FlexibleSearchCompOpImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FlexibleSearchVisitor visitor) {
        visitor.visitCompOp(this);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FlexibleSearchVisitor) {
            accept((FlexibleSearchVisitor) visitor);
        } else {
            super.accept(visitor);
        }
    }

    @Override
    @Nullable
    public PsiElement getEqualsOperator() {
        return findChildByType(EQUALS_OPERATOR);
    }

    @Override
    @Nullable
    public PsiElement getGreaterThanOrEqualsOperator() {
        return findChildByType(GREATER_THAN_OR_EQUALS_OPERATOR);
    }

    @Override
    @Nullable
    public PsiElement getLessThanOrEqualsOperator() {
        return findChildByType(LESS_THAN_OR_EQUALS_OPERATOR);
    }

    @Override
    @Nullable
    public PsiElement getNotEqualsOperator() {
        return findChildByType(NOT_EQUALS_OPERATOR);
    }

}
