/*
 * Copyright 2015 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.idea.plugin.hybris.impex.folding.smart;

import com.intellij.idea.plugin.hybris.impex.ImpexConstants;
import com.intellij.idea.plugin.hybris.impex.folding.ImpexFoldingPlaceholderBuilder;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexAttribute;
import com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils;
import com.intellij.psi.PsiElement;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * Created 23:16 01 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class SmartImpexFoldingPlaceholderBuilder implements ImpexFoldingPlaceholderBuilder {

    public static final String IMPEX_PARAMETERS_PLACEHOLDER = "()";
    private static final Pattern QUOTES_PATTERN = Pattern.compile("[\"\']");

    @NotNull
    @Override
    public String getPlaceholder(@NotNull final PsiElement psiElement) {
        Validate.notNull(psiElement);

        if (ImpexPsiUtils.isImpexAttribute(psiElement)) {
            final ImpexAttribute attribute = (ImpexAttribute) psiElement;

            return this.getPlaceholder(attribute);
        }

        if (ImpexPsiUtils.isImpexParameters(psiElement)) {
            return IMPEX_PARAMETERS_PLACEHOLDER;
        }

        return psiElement.getText();
    }

    @NotNull
    @Contract(pure = true)
    private String getPlaceholder(@NotNull final ImpexAttribute impexAttribute) {
        Validate.notNull(impexAttribute);

        if (this.isUniqueAttribute(impexAttribute)) {

            return impexAttribute.getAnyAttributeName().getText();

        } else if (this.isDefaultAttribute(impexAttribute)) {

            return this.getValueText(impexAttribute);

        } else if (this.isLangAttribute(impexAttribute)) {

            return this.getValueText(impexAttribute);

        } else if (this.isDateFormatAttribute(impexAttribute)) {

            return this.getValueText(impexAttribute);

        } else if (this.isModeAttribute(impexAttribute)) {

            return this.getValueText(impexAttribute);

        } else if (this.isNumberFormatAttribute(impexAttribute)) {

            return this.getValueText(impexAttribute);

        } else if (this.isTranslatorAttribute(impexAttribute)) {

            return this.getClassNameFromValue(impexAttribute);

        } else if (this.isCellDecoratorAttribute(impexAttribute)) {

            return this.getClassNameFromValue(impexAttribute);

        } else if (this.isVirtualAttribute(impexAttribute)) {

            return impexAttribute.getAnyAttributeName().getText();

        } else if (this.isAllowNullAttribute(impexAttribute)) {

            return impexAttribute.getAnyAttributeName().getText();

        } else if (this.isForceWriteAttribute(impexAttribute)) {

            return impexAttribute.getAnyAttributeName().getText();

        } else if (this.isIgnoreNullAttribute(impexAttribute)) {

            return impexAttribute.getAnyAttributeName().getText();

        } else if (this.isIgnoreKeyCaseAttribute(impexAttribute)) {

            return impexAttribute.getAnyAttributeName().getText();
        }

        return StringUtils.EMPTY;
    }

    @NotNull
    @Contract(pure = true)
    private String getClassNameFromValue(final @NotNull ImpexAttribute impexAttribute) {
        Validate.notNull(impexAttribute);

        if (null == impexAttribute.getAnyAttributeValue() || (StringUtils.isBlank(impexAttribute.getAnyAttributeValue().getText()))) {
            return impexAttribute.getText();
        }

        final String clearedString = QUOTES_PATTERN.matcher(impexAttribute.getAnyAttributeValue().getText()).replaceAll("");

        final String className = StringUtils.substringAfterLast(clearedString, ".");

        return StringUtils.isBlank(className) ? impexAttribute.getAnyAttributeValue().getText() : className;
    }

    @NotNull
    @Contract(pure = true)
    private String getValueText(final @NotNull ImpexAttribute impexAttribute) {
        Validate.notNull(impexAttribute);

        if (null == impexAttribute.getAnyAttributeValue() || (StringUtils.isBlank(impexAttribute.getAnyAttributeValue().getText()))) {
            return impexAttribute.getText();
        }

        return impexAttribute.getAnyAttributeValue().getText();
    }

    @Contract(pure = true)
    private boolean isUniqueAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.isNameEqualsAndAndValueIsTrue(impexAttribute, ImpexConstants.Attributes.UNIQUE);
    }

    @Contract(pure = true)
    private boolean isVirtualAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.isNameEqualsAndAndValueIsTrue(impexAttribute, ImpexConstants.Attributes.VIRTUAL);
    }

    @Contract(pure = true)
    private boolean isAllowNullAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.isNameEqualsAndAndValueIsTrue(impexAttribute, ImpexConstants.Attributes.ALLOW_NULL);
    }

    @Contract(pure = true)
    private boolean isForceWriteAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.isNameEqualsAndAndValueIsTrue(impexAttribute, ImpexConstants.Attributes.FORCE_WRITE);
    }

    @Contract(pure = true)
    private boolean isIgnoreNullAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.isNameEqualsAndAndValueIsTrue(impexAttribute, ImpexConstants.Attributes.IGNORE_NULL);
    }

    @Contract(pure = true)
    private boolean isIgnoreKeyCaseAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.isNameEqualsAndAndValueIsTrue(impexAttribute, ImpexConstants.Attributes.IGNORE_KEY_CASE);
    }

    @Contract(pure = true)
    private boolean isDateFormatAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.quoteAwareStringEquals(ImpexConstants.Attributes.DATE_FORMAT, impexAttribute.getAnyAttributeName().getText());
    }

    @Contract(pure = true)
    private boolean isDefaultAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.quoteAwareStringEquals(ImpexConstants.Attributes.DEFAULT, impexAttribute.getAnyAttributeName().getText());
    }

    @Contract(pure = true)
    private boolean isLangAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.quoteAwareStringEquals(ImpexConstants.Attributes.LANG, impexAttribute.getAnyAttributeName().getText());
    }

    @Contract(pure = true)
    private boolean isTranslatorAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.quoteAwareStringEquals(ImpexConstants.Attributes.TRANSLATOR, impexAttribute.getAnyAttributeName().getText());
    }

    @Contract(pure = true)
    private boolean isNumberFormatAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.quoteAwareStringEquals(ImpexConstants.Attributes.NUMBER_FORMAT, impexAttribute.getAnyAttributeName().getText());
    }

    @Contract(pure = true)
    private boolean isModeAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.quoteAwareStringEquals(ImpexConstants.Attributes.MODE, impexAttribute.getAnyAttributeName().getText());
    }

    @Contract(pure = true)
    private boolean isCellDecoratorAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.quoteAwareStringEquals(ImpexConstants.Attributes.CELL_DECORATOR, impexAttribute.getAnyAttributeName().getText());
    }

    @Contract(pure = true)
    private boolean isNameEqualsAndAndValueIsTrue(@NotNull final ImpexAttribute impexAttribute,
                                                  @NotNull final String name) {
        Validate.notNull(impexAttribute);
        Validate.notNull(name);

        if (!this.quoteAwareStringEquals(name, impexAttribute.getAnyAttributeName().getText())) {
            return false;
        }

        if (null == impexAttribute.getAnyAttributeValue()) {
            return false;
        }

        final String value = impexAttribute.getAnyAttributeValue().getText();

        if (null == value) {
            return false;
        }

        final String trimmedValue = value.trim();

        return this.quoteAwareStringEquals("true", trimmedValue);
    }

    @Contract(pure = true)
    private boolean quoteAwareStringEquals(@Nullable final String quotedString, @Nullable final String value) {
        return !(null == quotedString ^ null == value)
                && (null == quotedString
                || quotedString.equals(value)
                || ("'" + quotedString + "'").equals(value)
                || ("\"" + quotedString + "\"").equals(value));

    }
}
