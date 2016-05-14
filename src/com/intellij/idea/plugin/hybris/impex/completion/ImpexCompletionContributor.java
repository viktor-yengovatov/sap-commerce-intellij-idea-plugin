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

package com.intellij.idea.plugin.hybris.impex.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.idea.plugin.hybris.impex.ImpexLanguage;
import com.intellij.idea.plugin.hybris.impex.completion.provider.ImpexHeaderAttributeModifierNameCompletionProvider;
import com.intellij.idea.plugin.hybris.impex.completion.provider.ImpexHeaderAttributeModifierValueCompletionProvider;
import com.intellij.idea.plugin.hybris.impex.completion.provider.ImpexHeaderItemTypeAttributeNameCompletionProvider;
import com.intellij.idea.plugin.hybris.impex.completion.provider.ImpexHeaderItemTypeCodeCompletionProvider;
import com.intellij.idea.plugin.hybris.impex.completion.provider.ImpexHeaderTypeModifierNameCompletionProvider;
import com.intellij.idea.plugin.hybris.impex.completion.provider.ImpexHeaderTypeModifierValueCompletionProvider;
import com.intellij.idea.plugin.hybris.impex.pattern.ImpexHeaderAttributeModifierNameElementPattern;
import com.intellij.idea.plugin.hybris.impex.pattern.ImpexHeaderTypeModifierNameElementPattern;
import com.intellij.idea.plugin.hybris.impex.pattern.ImpexHeaderTypeModifierValueElementPattern;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.patterns.PlatformPatterns;

public class ImpexCompletionContributor extends CompletionContributor {

    private static final Logger LOG = Logger.getInstance(ImpexCompletionContributor.class);

    public ImpexCompletionContributor() {
        // case: header type modifier -> attribute_name
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                            .withLanguage(ImpexLanguage.getInstance())
                            .withElementType(ImpexTypes.ATTRIBUTE_NAME)
                            .and(ImpexHeaderTypeModifierNameElementPattern.getPatternInstance()),
            ImpexHeaderTypeModifierNameCompletionProvider.getInstance()
        );

        // case: header attribute's modifier name -> attribute_name
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                            .withLanguage(ImpexLanguage.getInstance())
                            .withElementType(ImpexTypes.ATTRIBUTE_NAME)
                            .andNot(ImpexHeaderTypeModifierNameElementPattern.getPatternInstance()),
            ImpexHeaderAttributeModifierNameCompletionProvider.getInstance()
        );

        // case: header type value -> attribute_value
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                            .withLanguage(ImpexLanguage.getInstance())
                            .and(ImpexHeaderAttributeModifierNameElementPattern.getPatternInstance())
                            .and(ImpexHeaderTypeModifierValueElementPattern.getPatternInstance()),
            ImpexHeaderTypeModifierValueCompletionProvider.getInstance()
        );

        // case: header attribute's modifier value -> attribute_value
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                            .withLanguage(ImpexLanguage.getInstance())
                            .and(ImpexHeaderAttributeModifierNameElementPattern.getPatternInstance())
                            .andNot(ImpexHeaderTypeModifierValueElementPattern.getPatternInstance()),
            ImpexHeaderAttributeModifierValueCompletionProvider.getInstance()
        );

        // case: itemtype-code
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                            .withLanguage(ImpexLanguage.getInstance())
                            .withElementType(ImpexTypes.HEADER_TYPE),
            ImpexHeaderItemTypeCodeCompletionProvider.getInstance()
        );

        // case: item's attribute
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                            .withLanguage(ImpexLanguage.getInstance())
                            .withElementType(ImpexTypes.HEADER_PARAMETER_NAME),
            ImpexHeaderItemTypeAttributeNameCompletionProvider.getInstance()
        );
    }
}
