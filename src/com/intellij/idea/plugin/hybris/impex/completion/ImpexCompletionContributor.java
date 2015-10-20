/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
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

package com.intellij.idea.plugin.hybris.impex.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.ide.DataManager;
import com.intellij.idea.plugin.hybris.impex.ImpexLanguage;
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
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.psi.filters.ElementFilter;
import com.intellij.psi.filters.position.FilterPattern;
import com.intellij.psi.impl.source.PsiExtensibleClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ImpexCompletionContributor extends CompletionContributor {

    private static final Logger LOG = Logger.getInstance(ImpexCompletionContributor.class);

    public static final Map<String, List<String>> ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES = new HashMap<String, List<String>>();
    public static final Map<String, List<String>> TYPE_MODIFIER_TO_POSSIBLE_VALUES = new HashMap<String, List<String>>();

    // list see https://wiki.hybris.com/pages/viewpage.action?title=ImpEx+Syntax&spaceKey=release5
    // values of hybris API 5.2.0
    private static final String TYPE_MODIFIER_BATCHMODE = "batchmode";
    private static final String TYPE_MODIFIER_CACHEUNIQUE = "cacheUnique";
    private static final String TYPE_MODIFIER_PROCESSOR = "processor";
    private static final String TYPE_MODIFIER_IMPEXLEGACYMODE = "impex.legacy.mode";

    private static final String ATTRIBUTE_MODIFIER_ALIAS = "alias";
    private static final String ATTRIBUTE_MODIFIER_ALLOWNULL = "allownull";
    private static final String ATTRIBUTE_MODIFIER_CELLDECORATOR = "cellDecorator";
    private static final String ATTRIBUTE_MODIFIER_COLLECTIONDELIMITER = "collection-delimiter";
    private static final String ATTRIBUTE_MODIFIER_DATEFORMAT = "dateformat";
    private static final String ATTRIBUTE_MODIFIER_DEFAULT = "default";
    private static final String ATTRIBUTE_MODIFIER_FORCEWRITE = "forceWrite";
    private static final String ATTRIBUTE_MODIFIER_IGNOREKEYCASE = "ignoreKeyCase";
    private static final String ATTRIBUTE_MODIFIER_IGNORENULL = "ignorenull";
    private static final String ATTRIBUTE_MODIFIER_KEY2VALUEDELIMITER = "key2value-delimiter ";
    private static final String ATTRIBUTE_MODIFIER_LANG = "lang";
    private static final String ATTRIBUTE_MODIFIER_MAPDELIMITER = "map-delimiter";
    private static final String ATTRIBUTE_MODIFIER_MODE = "mode";
    private static final String ATTRIBUTE_MODIFIER_NUMBERFORMAT = "numberformat";
    private static final String ATTRIBUTE_MODIFIER_PATHDELIMITER = "path-delimiter";
    private static final String ATTRIBUTE_MODIFIER_POS = "pos";
    private static final String ATTRIBUTE_MODIFIER_TRANSLATOR = "translator";
    private static final String ATTRIBUTE_MODIFIER_UNIQUE = "unique";
    private static final String ATTRIBUTE_MODIFIER_VIRTUAL = "virtual";
    public static Hashtable<String, PsiClass> TYPECODES = new Hashtable<String, PsiClass>();

    public ImpexCompletionContributor() {

        prepareModifierNameToValueMappings();

        // case: header type modifier -> attribute_name
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement().withLanguage(ImpexLanguage.INSTANCE).withElementType(ImpexTypes.ATTRIBUTE_NAME).and(isTypeModifier),
                new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        for (String key : TYPE_MODIFIER_TO_POSSIBLE_VALUES.keySet()) {
                            resultSet.addElement(LookupElementBuilder.create(key));
                        }
                    }
                }
        );

        // case: header attribute's modifier name -> attribute_name
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement().withLanguage(ImpexLanguage.INSTANCE).withElementType(ImpexTypes.ATTRIBUTE_NAME).andNot(isTypeModifier),
                new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        for (String key : ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES.keySet()) {
                            resultSet.addElement(LookupElementBuilder.create(key));
                        }
                    }
                }
        );

        // case: header type value -> attribute_value
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement().withLanguage(ImpexLanguage.INSTANCE).and(isModifierValue).and(isTypeModifierValue),
                new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        if (parameters.getPosition().getPrevSibling() != null
                                && parameters.getPosition().getPrevSibling().getPrevSibling() != null) {
                            final String modifierName = parameters.getPosition().getPrevSibling().getPrevSibling().getText();
                            List<String> possibleValueList = TYPE_MODIFIER_TO_POSSIBLE_VALUES.get(modifierName);
                            // the list is null when a modifier is not found in the definition
                            if (possibleValueList != null) {
                                for (String possibleValue : possibleValueList) {
                                    resultSet.addElement(LookupElementBuilder.create(possibleValue));
                                }
                            } else {
                                // show error message when not defined within hybris API
                                Notifications.Bus.notify(new Notification(ImpexLanguage.INSTANCE.getDisplayName(), "possible error in your impex", "You typed an unknown hybris-TYPE-modifier with name '" + modifierName + "'.", NotificationType.WARNING));
                            }
                        }
                    }
                }
        );

        // case: header attribute's modifier value -> attribute_value
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement().withLanguage(ImpexLanguage.INSTANCE).and(isModifierValue).andNot(isTypeModifierValue),
                new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        if (parameters.getPosition().getPrevSibling() != null
                                && parameters.getPosition().getPrevSibling().getPrevSibling() != null) {
                            String modifierName = parameters.getPosition().getPrevSibling().getPrevSibling().getText();
                            List<String> possibleValueList = ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES.get(modifierName);
                            // the list is null when a modifier is not found in the definition
                            if (possibleValueList != null) {
                                for (String possibleValue : possibleValueList) {
                                    resultSet.addElement(LookupElementBuilder.create(possibleValue));
                                }
                            } else {
                                // show error message when not defined within hybris API
                                Notifications.Bus.notify(new Notification(ImpexLanguage.INSTANCE.getDisplayName(), "possible error in your impex", "You typed an unknown hybris-ATTRIBUTE-modifier with name '" + modifierName + "'.", NotificationType.WARNING));
                            }
                        }
                    }
                }
        );

        // case: itemtype-code
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement().withLanguage(ImpexLanguage.INSTANCE).withElementType(ImpexTypes.HEADER_TYPE),
                new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        for (String typecode : TYPECODES.keySet()) {
                            resultSet.addElement(LookupElementBuilder.create(typecode));
                        }
                    }
                }
        );

        // case: item's attribute
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement().withLanguage(ImpexLanguage.INSTANCE).withElementType(ImpexTypes.HEADER_PARAMETER_NAME),
                new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        PsiElement headerElement = calculateHeaderElementOfAttribute(parameters.getPosition());
                        fillAllTypeFieldsCompletionResultsSet(headerElement, resultSet);
                    }
                }
        );
    }

    public static void fillAllTypeFieldsCompletionResultsSet(PsiElement headerElement, CompletionResultSet resultSet) {
        if (headerElement != null) {
            PsiClass type = TYPECODES.get(headerElement.getText());

            if (type != null) {
                for (PsiMethod method : type.getAllMethods()) {
                    if (method.getName().startsWith("set")) {
                        String attribute = method.getName().replaceFirst("set", "");
                        attribute = attribute.substring(0, 1).toLowerCase() + attribute.substring(1);
                        resultSet.addElement(LookupElementBuilder.create(attribute)
                                .withStrikeoutness(method.isDeprecated()) // marks deprecation
                                .withTypeText(method.getParameterList().getParameters()[0].getTypeElement().getType().getPresentableText(), true));
                    }
                }
            }
        }
    }

    public static PsiMethod getTypeFieldFor(PsiElement headerElement, String fieldCode) {
        if (headerElement != null) {
            PsiClass type = TYPECODES.get(headerElement.getText());

            if (type != null) {
                for (PsiMethod method : type.getAllMethods()) {
                    if (method.getName().startsWith("set") && method.getName().toLowerCase().endsWith(fieldCode.toLowerCase())) {
                        return method;
                    }
                }
            }
        }
        return null;
    }

    @Contract("null -> null")
    public static PsiElement calculateHeaderElementOfAttribute(PsiElement psiElement) {
        if (null == psiElement || null == psiElement.getNode()) {
            return null;
        }

        final ImpexHeaderLine impexHeaderLine = PsiTreeUtil.getParentOfType(psiElement, ImpexHeaderLine.class);
        if (null == impexHeaderLine) {
            return null;
        }

        final ImpexFullHeaderType impexFullHeaderType = PsiTreeUtil.findChildOfType(impexHeaderLine, ImpexFullHeaderType.class);
        if (null == impexFullHeaderType) {
            return null;
        }

        PsiElement child = impexFullHeaderType.getFirstChild();
        while (null != child && null != child.getNode() && !ImpexTypes.HEADER_TYPE.equals(child.getNode().getElementType())) {
            child = child.getNextSibling();
        }

        if (null != child && null != child.getNode() && ImpexTypes.HEADER_TYPE.equals(child.getNode().getElementType())) {
            return child;
        }

        return null;
    }

    private void prepareModifierNameToValueMappings() {
        fillPossibleTypeValuesMapForTypeModifier();
        fillPossibleValuesMapForAttributeModifier();
        indexItemTypes(getProject());
    }

    public static void indexItemTypes(final Project project) {

        Runnable runnable = new Runnable() {
            public void run() {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    public void run() {
                        try {
                            TYPECODES = new Hashtable<String, PsiClass>();
                            PsiClass itemRootClass = JavaPsiFacade.getInstance(project).findClass("de.hybris.platform.core.model.ItemModel", GlobalSearchScope.allScope(project));

                            if (null == itemRootClass) return;

                            Collection<PsiClass> inheritedClasses = ClassInheritorsSearch.search(itemRootClass).findAll();
                            // adding Item class itself
                            inheritedClasses.add(itemRootClass);

                            for (PsiClass itemClass : inheritedClasses) {
                                String typeCode = null;

                                // typecode -> the "hybris type"-uid within the typesystem of hybris
                                for (PsiField field : ((PsiExtensibleClass) itemClass).getOwnFields()) {
                                    if ("_TYPECODE".equals(field.getName())) {
                                        if (null != field.getInitializer()) {
                                            typeCode = field.getInitializer().getText().replaceAll("\"", "");
                                        }
                                    }
                                }
                                if (typeCode != null) {
                                    TYPECODES.put(typeCode, itemClass);
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
        DumbService.getInstance(project).showDumbModeNotification("[y]-typesystem information is not available during index update. Trigger 'Tools -> Index [y]-types' after IntelliJ-Index is build!");
    }

    private Project getProject() {
        AsyncResult<DataContext> dataContext = DataManager.getInstance().getDataContextFromFocus();
        return DataKeys.PROJECT.getData(dataContext.getResultSync());
    }

    private void fillPossibleTypeValuesMapForTypeModifier() {
        List<String> booleanValue = Arrays.asList("true", "false");
        TYPE_MODIFIER_TO_POSSIBLE_VALUES.put(TYPE_MODIFIER_BATCHMODE, booleanValue);
        TYPE_MODIFIER_TO_POSSIBLE_VALUES.put(TYPE_MODIFIER_CACHEUNIQUE, booleanValue);
        TYPE_MODIFIER_TO_POSSIBLE_VALUES.put(TYPE_MODIFIER_IMPEXLEGACYMODE, booleanValue);

        // no suggestions available
        TYPE_MODIFIER_TO_POSSIBLE_VALUES.put(TYPE_MODIFIER_PROCESSOR, new ArrayList<String>());
    }

    private void fillPossibleValuesMapForAttributeModifier() {
        List<String> booleanValue = Arrays.asList("true", "false");
        ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES.put(ATTRIBUTE_MODIFIER_UNIQUE, booleanValue);
        ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES.put(ATTRIBUTE_MODIFIER_ALLOWNULL, booleanValue);
        ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES.put(ATTRIBUTE_MODIFIER_FORCEWRITE, booleanValue);
        ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES.put(ATTRIBUTE_MODIFIER_IGNOREKEYCASE, booleanValue);
        ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES.put(ATTRIBUTE_MODIFIER_IGNORENULL, booleanValue);
        ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES.put(ATTRIBUTE_MODIFIER_VIRTUAL, booleanValue);
        ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES.put(ATTRIBUTE_MODIFIER_MODE, Arrays.asList("append", "remove"));

        // no suggestions available
        ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES.put(ATTRIBUTE_MODIFIER_KEY2VALUEDELIMITER, new ArrayList<String>());
        ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES.put(ATTRIBUTE_MODIFIER_ALIAS, new ArrayList<String>());
        ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES.put(ATTRIBUTE_MODIFIER_CELLDECORATOR, new ArrayList<String>());
        ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES.put(ATTRIBUTE_MODIFIER_COLLECTIONDELIMITER, new ArrayList<String>());
        ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES.put(ATTRIBUTE_MODIFIER_DATEFORMAT, new ArrayList<String>());
        ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES.put(ATTRIBUTE_MODIFIER_DEFAULT, new ArrayList<String>());
        ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES.put(ATTRIBUTE_MODIFIER_LANG, new ArrayList<String>());
        ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES.put(ATTRIBUTE_MODIFIER_MAPDELIMITER, new ArrayList<String>());
        ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES.put(ATTRIBUTE_MODIFIER_NUMBERFORMAT, new ArrayList<String>());
        ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES.put(ATTRIBUTE_MODIFIER_PATHDELIMITER, new ArrayList<String>());
        ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES.put(ATTRIBUTE_MODIFIER_POS, new ArrayList<String>());
        ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES.put(ATTRIBUTE_MODIFIER_TRANSLATOR, new ArrayList<String>());
    }

    FilterPattern isTypeModifier = new FilterPattern(new ElementFilter() {
        public boolean isAcceptable(Object element, PsiElement context) {
            return context.getPrevSibling() != null
                    && context.getPrevSibling().getPrevSibling() != null
                    && context.getPrevSibling().getPrevSibling().getNode().getElementType().equals(ImpexTypes.HEADER_TYPE);
        }

        public boolean isClassAcceptable(Class hintClass) {
            return true;
        }
    });

    FilterPattern isTypeModifierValue = new FilterPattern(new ElementFilter() {
        public boolean isAcceptable(Object element, PsiElement context) {
            return context.getPrevSibling() != null
                    && context.getPrevSibling().getPrevSibling() != null
                    && context.getPrevSibling().getPrevSibling().getPrevSibling() != null
                    && context.getPrevSibling().getPrevSibling().getPrevSibling().getPrevSibling() != null
                    && context.getPrevSibling().getPrevSibling().getPrevSibling().getPrevSibling().getNode().getElementType().equals(ImpexTypes.HEADER_TYPE);
        }

        @Contract(value = "_ -> true", pure = true)
        public boolean isClassAcceptable(Class hintClass) {
            return true;
        }
    });

    FilterPattern isModifierValue = new FilterPattern(new ElementFilter() {
        public boolean isAcceptable(Object element, PsiElement context) {
            return context.getPrevSibling() != null
                    && context.getPrevSibling().getPrevSibling() != null
                    && context.getPrevSibling().getPrevSibling().getNode().getElementType().equals(ImpexTypes.ATTRIBUTE_NAME);
        }

        public boolean isClassAcceptable(Class hintClass) {
            return true;
        }
    });
}