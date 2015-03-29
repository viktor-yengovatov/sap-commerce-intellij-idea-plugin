package com.intellij.idea.plugin.hybris.impex.folding.smart;

import com.intellij.idea.plugin.hybris.impex.ImpexConstants;
import com.intellij.idea.plugin.hybris.impex.folding.ImpexFoldingPlaceholderBuilder;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexAttribute;
import com.intellij.idea.plugin.hybris.impex.util.ImpexPsiUtil;
import com.intellij.psi.PsiElement;
import org.apache.commons.lang.StringUtils;
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

        if (ImpexPsiUtil.isImpexAttribute(psiElement)) {
            final ImpexAttribute attribute = (ImpexAttribute) psiElement;

            return this.getPlaceholder(attribute);
        }

        if (ImpexPsiUtil.isImpexParameters(psiElement)) {
            return IMPEX_PARAMETERS_PLACEHOLDER;
        }

        return psiElement.getText();
    }

    @NotNull
    private String getPlaceholder(@NotNull final ImpexAttribute impexAttribute) {
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
    private String getClassNameFromValue(final @NotNull ImpexAttribute impexAttribute) {
        if (null == impexAttribute.getAnyAttributeValue() || (StringUtils.isBlank(impexAttribute.getAnyAttributeValue().getText()))) {
            return impexAttribute.getText();
        }

        final String clearedString = QUOTES_PATTERN.matcher(impexAttribute.getAnyAttributeValue().getText()).replaceAll("");

        final String className = StringUtils.substringAfterLast(clearedString, ".");

        return StringUtils.isBlank(className) ? impexAttribute.getAnyAttributeValue().getText() : className;
    }

    @NotNull
    private String getValueText(final @NotNull ImpexAttribute impexAttribute) {
        if (null == impexAttribute.getAnyAttributeValue() || (StringUtils.isBlank(impexAttribute.getAnyAttributeValue().getText()))) {
            return impexAttribute.getText();
        }

        return impexAttribute.getAnyAttributeValue().getText();
    }


    private boolean isUniqueAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.isNameEqualsAndAndValueIsTrue(impexAttribute, ImpexConstants.Attributes.UNIQUE);
    }

    private boolean isVirtualAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.isNameEqualsAndAndValueIsTrue(impexAttribute, ImpexConstants.Attributes.VIRTUAL);
    }

    private boolean isAllowNullAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.isNameEqualsAndAndValueIsTrue(impexAttribute, ImpexConstants.Attributes.ALLOW_NULL);
    }

    private boolean isForceWriteAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.isNameEqualsAndAndValueIsTrue(impexAttribute, ImpexConstants.Attributes.FORCE_WRITE);
    }

    private boolean isIgnoreNullAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.isNameEqualsAndAndValueIsTrue(impexAttribute, ImpexConstants.Attributes.IGNORE_NULL);
    }

    private boolean isIgnoreKeyCaseAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.isNameEqualsAndAndValueIsTrue(impexAttribute, ImpexConstants.Attributes.IGNORE_KEY_CASE);
    }

    private boolean isDateFormatAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.quoteAwareStringEquals(ImpexConstants.Attributes.DATE_FORMAT, impexAttribute.getAnyAttributeName().getText());
    }

    private boolean isDefaultAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.quoteAwareStringEquals(ImpexConstants.Attributes.DEFAULT, impexAttribute.getAnyAttributeName().getText());
    }

    private boolean isLangAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.quoteAwareStringEquals(ImpexConstants.Attributes.LANG, impexAttribute.getAnyAttributeName().getText());
    }

    private boolean isTranslatorAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.quoteAwareStringEquals(ImpexConstants.Attributes.TRANSLATOR, impexAttribute.getAnyAttributeName().getText());
    }

    private boolean isNumberFormatAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.quoteAwareStringEquals(ImpexConstants.Attributes.NUMBER_FORMAT, impexAttribute.getAnyAttributeName().getText());
    }

    private boolean isModeAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.quoteAwareStringEquals(ImpexConstants.Attributes.MODE, impexAttribute.getAnyAttributeName().getText());
    }

    private boolean isCellDecoratorAttribute(@NotNull final ImpexAttribute impexAttribute) {
        return this.quoteAwareStringEquals(ImpexConstants.Attributes.CELL_DECORATOR, impexAttribute.getAnyAttributeName().getText());
    }


    private boolean isNameEqualsAndAndValueIsTrue(@NotNull final ImpexAttribute impexAttribute,
                                                  @NotNull final String name) {
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

    private boolean quoteAwareStringEquals(@Nullable final String quotedString, @Nullable final String value) {
        if (null == quotedString ^ null == value) {
            return false;
        }

        if (null == quotedString) {
            return true;
        }

        return quotedString.equals(value) || ("'" + quotedString + "'").equals(value) || ("\"" + quotedString + "\"").equals(value);
    }
}
