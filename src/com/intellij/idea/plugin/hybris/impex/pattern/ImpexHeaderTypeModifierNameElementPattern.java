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

package com.intellij.idea.plugin.hybris.impex.pattern;

import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.filters.ElementFilter;
import com.intellij.psi.filters.position.FilterPattern;

/**
 * Created 22:29 14 May 2016
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexHeaderTypeModifierNameElementPattern implements ElementFilter {

    private static final ElementFilter INSTANCE = new ImpexHeaderTypeModifierNameElementPattern();
    private static final FilterPattern PATTERN_INSTANCE = new FilterPattern(INSTANCE);

    public static ElementFilter getInstance() {
        return INSTANCE;
    }

    public static FilterPattern getPatternInstance() {
        return PATTERN_INSTANCE;
    }

    protected ImpexHeaderTypeModifierNameElementPattern() {
    }

    @Override
    public boolean isAcceptable(final Object element, final PsiElement context) {
        return (context.getPrevSibling() != null)
               && (context.getPrevSibling().getPrevSibling() != null)
               && context.getPrevSibling()
                         .getPrevSibling()
                         .getNode()
                         .getElementType()
                         .equals(ImpexTypes.HEADER_TYPE);
    }

    @Override
    public boolean isClassAcceptable(final Class hintClass) {
        return true;
    }
}
