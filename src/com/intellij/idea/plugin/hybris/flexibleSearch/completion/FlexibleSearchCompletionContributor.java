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
import com.intellij.idea.plugin.hybris.completion.provider.ItemTypeCodeCompletionProvider;
import com.intellij.idea.plugin.hybris.flexibleSearch.FlexibleSearchLanguage;
import com.intellij.idea.plugin.hybris.flexibleSearch.completion.provider.FSKeywordCompletionProvider;
import com.intellij.idea.plugin.hybris.flexibleSearch.completion.provider.FSKeywords;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchFromClause;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchOrderByClause;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchSetQuantifier;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchWhereClause;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.tree.TokenSet;

import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.SELECT_LIST;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.TABLE_NAME_IDENTIFIER;
import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.util.containers.ContainerUtil.newHashSet;

public class FlexibleSearchCompletionContributor extends CompletionContributor {

    private static final Logger LOG = Logger.getInstance(FlexibleSearchCompletionContributor.class);

    public FlexibleSearchCompletionContributor() {
        // keywords
        extend(
            CompletionType.BASIC,
            psiElement()
                .withLanguage(FlexibleSearchLanguage.getInstance())
                .andNot(psiElement().inside(FlexibleSearchFromClause.class))
                .andNot(psiElement().inside(FlexibleSearchWhereClause.class))
                /*.andNot(psiElement().inside(psiElement(COLUMN_REFERENCE_IDENTIFIER)))*/,
            new FSKeywordCompletionProvider(FSKeywords.topLevelKeywords(), (keyword) ->
                LookupElementBuilder.create(keyword)
                                    .withCaseSensitivity(false)
                                    .withIcon(AllIcons.Nodes.Function))
        );

        extend(
            CompletionType.BASIC,
            psiElement()
                .withElementType(TokenSet.create(TABLE_NAME_IDENTIFIER))
                .withLanguage(FlexibleSearchLanguage.getInstance()),
            ItemTypeCodeCompletionProvider.getInstance()
        );

        extend(
            CompletionType.BASIC,
            psiElement()
                .afterLeaf(psiElement().withElementType(TokenSet.create(TABLE_NAME_IDENTIFIER)))
                .withLanguage(FlexibleSearchLanguage.getInstance()),
            new FSKeywordCompletionProvider(newHashSet("AS"), (keyword) ->
                LookupElementBuilder.create(keyword)
                                    .withCaseSensitivity(false)
                                    .withIcon(AllIcons.Nodes.Function))
        );

        extend(
            CompletionType.BASIC,
            psiElement()
                .inside(FlexibleSearchSetQuantifier.class)
                .withLanguage(FlexibleSearchLanguage.getInstance()),
            new FSKeywordCompletionProvider(newHashSet("DISTINCT", "COUNT"), (keyword) ->
                LookupElementBuilder.create(keyword)
                                    .withCaseSensitivity(false)
                                    .withIcon(AllIcons.Nodes.Method))
        );

        extend(
            CompletionType.BASIC,
            psiElement()
                .inside(psiElement(SELECT_LIST))
                .withLanguage(FlexibleSearchLanguage.getInstance()),
            new FSKeywordCompletionProvider(newHashSet("*"), (keyword) ->
                LookupElementBuilder.create(keyword)
                                    .bold()
                                    .withCaseSensitivity(false)
                                    .withIcon(AllIcons.Nodes.Static))
        );

        extend(
            CompletionType.BASIC,
            psiElement()
                .inside(psiElement(FlexibleSearchFromClause.class))
                .withLanguage(FlexibleSearchLanguage.getInstance()),
            new FSKeywordCompletionProvider(FSKeywords.joinKeywords(), (keyword) ->
                LookupElementBuilder.create(keyword)
                                    .bold()
                                    .withCaseSensitivity(false)
                                    .withIcon(AllIcons.Nodes.Static))
        );

        extend(
            CompletionType.BASIC,
            psiElement()
                .inside(psiElement(FlexibleSearchOrderByClause.class))
                .withLanguage(FlexibleSearchLanguage.getInstance()),
            new FSKeywordCompletionProvider(FSKeywords.orderKeywords(), (keyword) ->
                LookupElementBuilder.create(keyword)
                                    .bold()
                                    .withCaseSensitivity(false)
                                    .withIcon(AllIcons.Nodes.Static))
        );

    }
}