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
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.ide.DataManager;
import com.intellij.idea.plugin.hybris.impex.ImpexLanguage;
import com.intellij.idea.plugin.hybris.impex.constants.modifier.AttributeModifier;
import com.intellij.idea.plugin.hybris.impex.constants.modifier.ImpexModifier;
import com.intellij.idea.plugin.hybris.impex.constants.modifier.TypeModifier;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexFullHeaderType;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexHeaderLine;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
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
import com.intellij.psi.PsiMethod;
import com.intellij.psi.filters.ElementFilter;
import com.intellij.psi.filters.position.FilterPattern;
import com.intellij.psi.impl.source.PsiExtensibleClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class ImpexCompletionContributor extends CompletionContributor {

    private static final Logger LOG = Logger.getInstance(ImpexCompletionContributor.class);

    protected static final Pattern DOUBLE_QUOTE_PATTERN = Pattern.compile("\"");
    protected static final Pattern SET_PATTERN = Pattern.compile("set");

    private static Map<String, PsiClass> TYPE_CODES = new ConcurrentHashMap<String, PsiClass>();

    public ImpexCompletionContributor() {

        indexItemTypes(this.getProject());

        // case: header type modifier -> attribute_name
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                            .withLanguage(ImpexLanguage.INSTANCE)
                            .withElementType(ImpexTypes.ATTRIBUTE_NAME)
                            .and(IS_TYPE_MODIFIER),

            new CompletionProvider<CompletionParameters>() {

                @Override
                public void addCompletions(
                    @NotNull final CompletionParameters parameters,
                    final ProcessingContext context,
                    @NotNull final CompletionResultSet result
                ) {
                    for (ImpexModifier impexModifier : TypeModifier.values()) {
                        result.addElement(LookupElementBuilder.create(impexModifier.getModifierName()));
                    }
                }
            }
        );

        // case: header attribute's modifier name -> attribute_name
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                            .withLanguage(ImpexLanguage.INSTANCE)
                            .withElementType(ImpexTypes.ATTRIBUTE_NAME)
                            .andNot(IS_TYPE_MODIFIER),

            new CompletionProvider<CompletionParameters>() {

                @Override
                public void addCompletions(
                    @NotNull final CompletionParameters parameters,
                    final ProcessingContext context,
                    @NotNull final CompletionResultSet result
                ) {

                    for (ImpexModifier impexModifier : AttributeModifier.values()) {
                        result.addElement(LookupElementBuilder.create(impexModifier.getModifierName()));
                    }
                }
            }
        );

        // case: header type value -> attribute_value
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                            .withLanguage(ImpexLanguage.INSTANCE)
                            .and(IS_MODIFIER_VALUE)
                            .and(IS_TYPE_MODIFIER_VALUE),

            new CompletionProvider<CompletionParameters>() {

                @Override
                public void addCompletions(
                    @NotNull final CompletionParameters parameters,
                    final ProcessingContext context,
                    @NotNull final CompletionResultSet result
                ) {

                    if ((parameters.getPosition().getPrevSibling() != null)
                        && (parameters.getPosition().getPrevSibling().getPrevSibling() != null)) {

                        final String modifierName = parameters.getPosition()
                                                              .getPrevSibling()
                                                              .getPrevSibling()
                                                              .getText();
                        final ImpexModifier impexModifier = TypeModifier.getByModifierName(modifierName);

                        // the list is null when a modifier is not found in the definition
                        if ((null != impexModifier) && !impexModifier.getModifierValues().isEmpty()) {
                            for (String possibleValue : impexModifier.getModifierValues()) {
                                result.addElement(LookupElementBuilder.create(possibleValue));
                            }
                        } else {
                            // show error message when not defined within hybris API
                            Notifications.Bus.notify(new Notification(
                                ImpexLanguage.INSTANCE.getDisplayName(),
                                "possible error in your impex",
                                "You typed an unknown hybris-TYPE-modifier with name '" + modifierName + "'.",
                                NotificationType.WARNING
                            ));
                        }
                    }
                }
            }
        );

        // case: header attribute's modifier value -> attribute_value
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                            .withLanguage(ImpexLanguage.INSTANCE)
                            .and(IS_MODIFIER_VALUE)
                            .andNot(IS_TYPE_MODIFIER_VALUE),

            new CompletionProvider<CompletionParameters>() {

                @Override
                public void addCompletions(
                    @NotNull final CompletionParameters parameters,
                    final ProcessingContext context,
                    @NotNull final CompletionResultSet result
                ) {

                    if (parameters.getPosition().getPrevSibling() != null
                        && parameters.getPosition().getPrevSibling().getPrevSibling() != null) {

                        final String modifierName = parameters.getPosition()
                                                              .getPrevSibling()
                                                              .getPrevSibling()
                                                              .getText();
                        final ImpexModifier impexModifier = AttributeModifier.getByModifierName(modifierName);

                        // the list is null when a modifier is not found in the definition
                        if ((null != impexModifier) && !impexModifier.getModifierValues().isEmpty()) {
                            for (String possibleValue : impexModifier.getModifierValues()) {
                                result.addElement(LookupElementBuilder.create(possibleValue));
                            }
                        } else {
                            // show error message when not defined within hybris API
                            Notifications.Bus.notify(new Notification(
                                ImpexLanguage.INSTANCE.getDisplayName(),
                                "possible error in your impex",
                                "You typed an unknown hybris-ATTRIBUTE-modifier with name '" + modifierName + "'.",
                                NotificationType.WARNING
                            ));
                        }
                    }
                }
            }
        );

        // case: itemtype-code
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                            .withLanguage(ImpexLanguage.INSTANCE)
                            .withElementType(ImpexTypes.HEADER_TYPE),

            new CompletionProvider<CompletionParameters>() {

                @Override
                public void addCompletions(
                    @NotNull final CompletionParameters parameters,
                    final ProcessingContext context,
                    @NotNull final CompletionResultSet result
                ) {

                    for (String typecode : TYPE_CODES.keySet()) {
                        result.addElement(LookupElementBuilder.create(typecode));
                    }
                }
            }
        );

        // case: item's attribute
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                            .withLanguage(ImpexLanguage.INSTANCE)
                            .withElementType(ImpexTypes.HEADER_PARAMETER_NAME),

            new CompletionProvider<CompletionParameters>() {

                @Override
                public void addCompletions(
                    @NotNull final CompletionParameters parameters,
                    final ProcessingContext context,
                    @NotNull final CompletionResultSet result
                ) {
                    final PsiElement headerElement = calculateHeaderElementOfAttribute(parameters.getPosition());
                    fillAllTypeFieldsCompletionResultsSet(headerElement, result);
                }
            }
        );
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
            "[y]-typesystem information is not available during index update. Trigger 'Tools -> Index [y]-types' after IntelliJ-Index is build!");
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