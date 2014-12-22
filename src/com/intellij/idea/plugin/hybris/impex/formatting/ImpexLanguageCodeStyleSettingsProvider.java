package com.intellij.idea.plugin.hybris.impex.formatting;

import com.intellij.idea.plugin.hybris.impex.ImpexLanguage;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import org.jetbrains.annotations.NotNull;

/**
 * Created 23:46 21 December 2014
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {

    @NotNull
    @Override
    public Language getLanguage() {
        return ImpexLanguage.INSTANCE;
    }

    @Override
    public void customizeSettings(@NotNull final CodeStyleSettingsCustomizable consumer,
                                  @NotNull final SettingsType settingsType) {

        if (SettingsType.SPACING_SETTINGS == settingsType) {

            consumer.showCustomOption(
                    ImpexCodeStyleSettings.class,
                    "SPACE_AFTER_FIELD_VALUE_SEPARATOR",
                    "After field value separator",
                    CodeStyleSettingsCustomizable.SPACES_AROUND_OPERATORS
            );

            consumer.showCustomOption(
                    ImpexCodeStyleSettings.class,
                    "SPACE_BEFORE_FIELD_VALUE_SEPARATOR",
                    "Before field value separator",
                    CodeStyleSettingsCustomizable.SPACES_AROUND_OPERATORS
            );

            consumer.showCustomOption(
                    ImpexCodeStyleSettings.class,
                    "SPACE_AFTER_PARAMETERS_SEPARATOR",
                    "After parameters separator",
                    CodeStyleSettingsCustomizable.SPACES_AROUND_OPERATORS
            );

            consumer.showCustomOption(
                    ImpexCodeStyleSettings.class,
                    "SPACE_BEFORE_PARAMETERS_SEPARATOR",
                    "Before parameters separator",
                    CodeStyleSettingsCustomizable.SPACES_AROUND_OPERATORS
            );

            consumer.showCustomOption(
                    ImpexCodeStyleSettings.class,
                    "SPACE_AFTER_ATTRIBUTE_SEPARATOR",
                    "After attribute separator",
                    CodeStyleSettingsCustomizable.SPACES_AROUND_OPERATORS
            );

            consumer.showCustomOption(
                    ImpexCodeStyleSettings.class,
                    "SPACE_BEFORE_ATTRIBUTE_SEPARATOR",
                    "Before attribute separator",
                    CodeStyleSettingsCustomizable.SPACES_AROUND_OPERATORS
            );

            consumer.showCustomOption(
                    ImpexCodeStyleSettings.class,
                    "SPACE_AFTER_FIELD_LIST_ITEM_SEPARATOR",
                    "After list item separator",
                    CodeStyleSettingsCustomizable.SPACES_AROUND_OPERATORS
            );

            consumer.showCustomOption(
                    ImpexCodeStyleSettings.class,
                    "SPACE_BEFORE_FIELD_LIST_ITEM_SEPARATOR",
                    "Before list item separator",
                    CodeStyleSettingsCustomizable.SPACES_AROUND_OPERATORS
            );

        } else if (settingsType == SettingsType.BLANK_LINES_SETTINGS) {

            consumer.showStandardOptions("KEEP_BLANK_LINES_IN_CODE");
        }
    }

    @Override
    public String getCodeSample(@NotNull final SettingsType settingsType) {
        return "# Comment\n" +
               "$lang=en\n" +
               "$contentCatalog=projectContentCatalog\n" +
               "$contentCV=catalogVersion(CatalogVersion.catalog(Catalog.id[default=$contentCatalog]),CatalogVersion.version[default='Staged'])[default=$contentCatalog:Staged]\n" +
               "$macro=qwe;qwe,qwe,;qwe\n" +
               "\n" +
               "#% impex.setLocale( Locale.GERMAN );\n" +
               "\n" +
               "INSERT_UPDATE SomeType;$contentCV[unique=true][map-delimiter = |][dateformat = yyyy-MM-dd HH:mm:ss];uid[unique=true];title[lang=$lang]\n" +
               "Subtype;;account;\"Your Account\"\n" +
               ";;<ignore>;\"Add/Edit Address\"\n" +
               ";;key -> vaue | key -> vaue;\"Address Book\"\n" +
               ";;value1,value2,value3;12345;com.domain.Class; qwe : asd\n" +
               "\n" +
               "INSERT Address[impex.legacy.mode = true, batchmode = true];firstname;owner( Principal.uid | AbstractOrder.code )\n" +
               ";Hans;admin\n" +
               "\n" +
               "UPDATE Address;firstname;owner( Principal.uid | AbstractOrder.code );&docId\n" +
               ";Hans;admin;id\n" +
               "\n" +
               "remove Address;firstname;owner( Principal.uid | AbstractOrder.code )\n" +
               ";Hans;admin\n" +
               "\n" +
               "INSERT_UPDATE Media@media[translator=de.hybris.platform.impex.jalo.media.MediaDataTranslator];mime[default='image/png']\n" +
               ";;$contentResource/images/logo.png\n" +
               "\n" +
               "@@@@@\n";
    }
}
