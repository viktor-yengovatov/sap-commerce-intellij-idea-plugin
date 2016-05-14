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
import com.intellij.idea.plugin.hybris.impex.ImpexLanguage;
import com.intellij.idea.plugin.hybris.impex.constants.modifier.ImpexModifier;
import com.intellij.idea.plugin.hybris.impex.constants.modifier.TypeModifier;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * Created 22:12 14 May 2016
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexHeaderTypeModifierValueCompletionProvider extends CompletionProvider<CompletionParameters> {

    private static final CompletionProvider<CompletionParameters> INSTANCE = new ImpexHeaderTypeModifierValueCompletionProvider();

    public static CompletionProvider<CompletionParameters> getInstance() {
        return INSTANCE;
    }

    protected ImpexHeaderTypeModifierValueCompletionProvider() {
    }

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
                    ImpexLanguage.getInstance().getDisplayName(),
                    "possible error in your impex",
                    "You typed an unknown hybris-TYPE-modifier with name '" + modifierName + "'.",
                    NotificationType.WARNING
                ));
            }
        }
    }
}
