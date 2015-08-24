/*
 * This file is part of "Hybris Integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2015 Alexander Bartash <AlexanderBartash@gmail.com>
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

package com.intellij.idea.plugin.hybris.impex.folding;

import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created 22:34 01 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexFoldingDescriptor extends FoldingDescriptor {

    private final String placeholder;

    public ImpexFoldingDescriptor(@NotNull final PsiElement psiElement,
                                  @NotNull final FoldingGroup group) {
        super(
                psiElement.getNode(),
                new TextRange(
                        psiElement.getTextRange().getStartOffset(),
                        psiElement.getTextRange().getEndOffset()
                ),
                group
        );

        placeholder = ImpexFoldingPlaceholderBuilderFactory.getPlaceholderBuilder().getPlaceholder(psiElement);
    }

    @Nullable
    @Override
    public String getPlaceholderText() {
        return placeholder;
    }
}