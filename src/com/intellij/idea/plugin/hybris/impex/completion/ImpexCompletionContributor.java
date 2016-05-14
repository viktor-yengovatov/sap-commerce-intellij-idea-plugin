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
import com.intellij.ide.DataManager;
import com.intellij.idea.plugin.hybris.impex.ImpexLanguage;
import com.intellij.idea.plugin.hybris.impex.completion.provider.AttributeModifierNameCompletionProvider;
import com.intellij.idea.plugin.hybris.impex.completion.provider.AttributeModifierValueCompletionProvider;
import com.intellij.idea.plugin.hybris.impex.completion.provider.ItemTypeAttributeNameCompletionProvider;
import com.intellij.idea.plugin.hybris.impex.completion.provider.ItemTypeCodeCompletionProvider;
import com.intellij.idea.plugin.hybris.impex.completion.provider.TypeModifierNameCompletionProvider;
import com.intellij.idea.plugin.hybris.impex.completion.provider.TypeModifierValueCompletionProvider;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.AsyncResult;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.filters.ElementFilter;
import com.intellij.psi.filters.position.FilterPattern;
import com.intellij.psi.impl.source.PsiExtensibleClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class ImpexCompletionContributor extends CompletionContributor {

    private static final Logger LOG = Logger.getInstance(ImpexCompletionContributor.class);

    protected static final Pattern DOUBLE_QUOTE_PATTERN = Pattern.compile("\"");

    public static Map<String, PsiClass> TYPE_CODES = new ConcurrentHashMap<String, PsiClass>();

    public ImpexCompletionContributor() {

        indexItemTypes(this.getProject());

        // case: header type modifier -> attribute_name
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                            .withLanguage(ImpexLanguage.getInstance())
                            .withElementType(ImpexTypes.ATTRIBUTE_NAME)
                            .and(IS_TYPE_MODIFIER),
            TypeModifierNameCompletionProvider.getInstance()
        );

        // case: header attribute's modifier name -> attribute_name
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                            .withLanguage(ImpexLanguage.getInstance())
                            .withElementType(ImpexTypes.ATTRIBUTE_NAME)
                            .andNot(IS_TYPE_MODIFIER),
            AttributeModifierNameCompletionProvider.getInstance()
        );

        // case: header type value -> attribute_value
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                            .withLanguage(ImpexLanguage.getInstance())
                            .and(IS_MODIFIER_VALUE)
                            .and(IS_TYPE_MODIFIER_VALUE),
            TypeModifierValueCompletionProvider.getInstance()
        );

        // case: header attribute's modifier value -> attribute_value
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                            .withLanguage(ImpexLanguage.getInstance())
                            .and(IS_MODIFIER_VALUE)
                            .andNot(IS_TYPE_MODIFIER_VALUE),
            AttributeModifierValueCompletionProvider.getInstance()
        );

        // case: itemtype-code
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                            .withLanguage(ImpexLanguage.getInstance())
                            .withElementType(ImpexTypes.HEADER_TYPE),
            ItemTypeCodeCompletionProvider.getInstance()
        );

        // case: item's attribute
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                            .withLanguage(ImpexLanguage.getInstance())
                            .withElementType(ImpexTypes.HEADER_PARAMETER_NAME),
            ItemTypeAttributeNameCompletionProvider.getInstance()
        );
    }

    public static void indexItemTypes(final Project project) {

        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            TYPE_CODES = new ConcurrentHashMap<String, PsiClass>();
                            final PsiClass itemRootClass = JavaPsiFacade.getInstance(project).findClass(
                                "de.hybris.platform.core.model.ItemModel",
                                GlobalSearchScope.allScope(project)
                            );

                            if (null == itemRootClass) {
                                return;
                            }

                            final Collection<PsiClass> inheritedClasses = ClassInheritorsSearch.search(itemRootClass)
                                                                                               .findAll();
                            // adding Item class itself
                            inheritedClasses.add(itemRootClass);

                            for (PsiClass itemClass : inheritedClasses) {
                                String typeCode = null;

                                // typecode -> the "hybris type"-uid within the typesystem of hybris
                                for (PsiField field : ((PsiExtensibleClass) itemClass).getOwnFields()) {
                                    if ("_TYPECODE".equals(field.getName())) {
                                        if (null != field.getInitializer()) {
                                            typeCode = DOUBLE_QUOTE_PATTERN.matcher(field.getInitializer().getText())
                                                                           .replaceAll("");
                                        }
                                    }
                                }
                                if (typeCode != null) {
                                    TYPE_CODES.put(typeCode, itemClass);
                                }
                            }

                        } catch (IndexNotReadyException e) {
                            LOG.warn(e);
                            showDumbModeNotification(project);
                        }
                    }
                });
            }
        };
        CommandProcessor.getInstance().executeCommand(project, runnable, "Indexing hybris typesystem", null);
    }

    private static void showDumbModeNotification(final Project project) {
        DumbService.getInstance(project).showDumbModeNotification(
            "[y]-typesystem information is not available during index update." +
            " Trigger 'Tools -> Index [y]-types' after IntelliJ-Index is build!"
        );
    }

    private Project getProject() {
        final AsyncResult<DataContext> dataContext = DataManager.getInstance().getDataContextFromFocus();
        return DataKeys.PROJECT.getData(dataContext.getResultSync());
    }

    private static final ElementPattern IS_TYPE_MODIFIER = new FilterPattern(new ElementFilter() {

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
    });

    private static final ElementPattern IS_TYPE_MODIFIER_VALUE = new FilterPattern(new ElementFilter() {

        @Override
        public boolean isAcceptable(final Object element, final PsiElement context) {
            return (context.getPrevSibling() != null)
                   && (context.getPrevSibling().getPrevSibling() != null)
                   && (context.getPrevSibling().getPrevSibling().getPrevSibling() != null)
                   && (context.getPrevSibling().getPrevSibling().getPrevSibling().getPrevSibling() != null)
                   && context.getPrevSibling()
                             .getPrevSibling()
                             .getPrevSibling()
                             .getPrevSibling()
                             .getNode()
                             .getElementType()
                             .equals(ImpexTypes.HEADER_TYPE);
        }

        @Override
        @Contract(value = "_ -> true", pure = true)
        public boolean isClassAcceptable(final Class hintClass) {
            return true;
        }
    });

    private static final ElementPattern IS_MODIFIER_VALUE = new FilterPattern(new ElementFilter() {

        @Override
        public boolean isAcceptable(final Object element, final PsiElement context) {
            return (context.getPrevSibling() != null)
                   && (context.getPrevSibling().getPrevSibling() != null)
                   && context.getPrevSibling()
                             .getPrevSibling()
                             .getNode()
                             .getElementType()
                             .equals(ImpexTypes.ATTRIBUTE_NAME);
        }

        @Override
        public boolean isClassAcceptable(final Class hintClass) {
            return true;
        }
    });
}