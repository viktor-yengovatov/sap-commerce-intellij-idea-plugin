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

package com.intellij.idea.plugin.hybris.flexibleSearch.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons;
import com.intellij.idea.plugin.hybris.flexibleSearch.FlexibleSearchLanguage;
import com.intellij.idea.plugin.hybris.flexibleSearch.completion.provider.FSKeywordCompletionProvider;
import com.intellij.idea.plugin.hybris.flexibleSearch.completion.provider.FSKeywords;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes;
import com.intellij.idea.plugin.hybris.impex.ImpexLanguage;
import com.intellij.idea.plugin.hybris.impex.completion.provider.ImpexHeaderAttributeModifierNameCompletionProvider;
import com.intellij.idea.plugin.hybris.impex.completion.provider.ImpexHeaderAttributeModifierValueCompletionProvider;
import com.intellij.idea.plugin.hybris.impex.completion.provider.ImpexHeaderItemTypeAttributeNameCompletionProvider;
import com.intellij.idea.plugin.hybris.impex.completion.provider.ImpexHeaderItemTypeCodeCompletionProvider;
import com.intellij.idea.plugin.hybris.impex.completion.provider.ImpexHeaderTypeModifierNameCompletionProvider;
import com.intellij.idea.plugin.hybris.impex.completion.provider.ImpexHeaderTypeModifierValueCompletionProvider;
import com.intellij.idea.plugin.hybris.impex.completion.provider.ImpexKeywordCompletionProvider;
import com.intellij.idea.plugin.hybris.impex.completion.provider.ImpexMacrosCompletionProvider;
import com.intellij.idea.plugin.hybris.impex.constants.ImpexKeywords;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexFullHeaderParameter;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexFullHeaderType;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexModifiers;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class FlexibleSearchCompletionContributor extends CompletionContributor {

    private static final Logger LOG = Logger.getInstance(FlexibleSearchCompletionContributor.class);

    public FlexibleSearchCompletionContributor() {
        // keywords
        extend(
            CompletionType.BASIC,
            psiElement().withLanguage(FlexibleSearchLanguage.getInstance()), 
            new FSKeywordCompletionProvider(FSKeywords.keywords(), (keyword) ->
                LookupElementBuilder.create(keyword)
                                    .withCaseSensitivity(false)
                                    .withIcon(AllIcons.Nodes.Function))
        );


    }
}