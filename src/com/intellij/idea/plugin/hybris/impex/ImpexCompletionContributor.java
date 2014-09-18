package com.intellij.idea.plugin.hybris.impex;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.filters.ElementFilter;
import com.intellij.psi.filters.position.FilterPattern;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ImpexCompletionContributor extends CompletionContributor {

    private static final Map<String, List<String>> ATTRIBUTE_MODIFIER_TO_POSSIBLE_VALUES = new HashMap<String, List<String>>();
    private static final Map<String, List<String>> TYPE_MODIFIER_TO_POSSIBLE_VALUES = new HashMap<String, List<String>>();

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


    public ImpexCompletionContributor() {

        prepareModifierNameToValueMappings();

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
    }

    private void prepareModifierNameToValueMappings() {
        fillPossibleTypeValuesMapForTypeModifier();
        fillPossibleValuesMapForAttributeModifier();
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
}
