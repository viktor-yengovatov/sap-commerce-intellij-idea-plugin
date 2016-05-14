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

package com.intellij.idea.plugin.hybris.impex.completion.provider;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexFullHeaderType;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexHeaderLine;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import static com.intellij.idea.plugin.hybris.impex.completion.ImpexCompletionContributor.TYPE_CODES;

/**
 * Created 22:13 14 May 2016
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ItemTypeAttributeNameCompletionProvider extends CompletionProvider<CompletionParameters> {

    protected static final Pattern SET_PATTERN = Pattern.compile("set");

    private static final CompletionProvider<CompletionParameters> INSTANCE = new ItemTypeAttributeNameCompletionProvider();

    public static CompletionProvider<CompletionParameters> getInstance() {
        return INSTANCE;
    }

    protected ItemTypeAttributeNameCompletionProvider() {
    }

    @Override
    public void addCompletions(
        @NotNull final CompletionParameters parameters,
        final ProcessingContext context,
        @NotNull final CompletionResultSet result
    ) {
        final PsiElement headerElement = calculateHeaderElementOfAttribute(parameters.getPosition());
        fillAllTypeFieldsCompletionResultsSet(headerElement, result);
    }

    private static void fillAllTypeFieldsCompletionResultsSet(
        final PsiElement headerElement,
        final CompletionResultSet resultSet
    ) {
        if (headerElement != null) {
            final PsiClass type = TYPE_CODES.get(headerElement.getText());

            if (type != null) {
                for (PsiMethod method : type.getAllMethods()) {
                    if (method.getName().startsWith("set")) {
                        String attribute = SET_PATTERN.matcher(method.getName()).replaceFirst("");
                        attribute = attribute.substring(0, 1).toLowerCase() + attribute.substring(1);

                        final LookupElementBuilder element = LookupElementBuilder
                            .create(attribute)
                            .withStrikeoutness(method.isDeprecated()) // marks deprecation
                            .withTypeText(
                                method.getParameterList().getParameters()[0].getTypeElement()
                                                                            .getType()
                                                                            .getPresentableText(),
                                true
                            );

                        resultSet.addElement(element);
                    }
                }
            }
        }
    }

    @Contract("null -> null")
    private static PsiElement calculateHeaderElementOfAttribute(final PsiElement psiElement) {
        if (null == psiElement || null == psiElement.getNode()) {
            return null;
        }

        final ImpexHeaderLine impexHeaderLine = PsiTreeUtil.getParentOfType(psiElement, ImpexHeaderLine.class);
        if (null == impexHeaderLine) {
            return null;
        }

        final ImpexFullHeaderType impexFullHeaderType = PsiTreeUtil.findChildOfType(
            impexHeaderLine,
            ImpexFullHeaderType.class
        );
        if (null == impexFullHeaderType) {
            return null;
        }

        PsiElement child = impexFullHeaderType.getFirstChild();
        while ((null != child)
               && (null != child.getNode())
               && !ImpexTypes.HEADER_TYPE.equals(child.getNode().getElementType())) {

            child = child.getNextSibling();
        }

        if ((null != child)
            && (null != child.getNode())
            && ImpexTypes.HEADER_TYPE.equals(child.getNode().getElementType())) {

            return child;
        }

        return null;
    }
}
