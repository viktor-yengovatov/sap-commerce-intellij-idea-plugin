package com.intellij.idea.plugin.hybris.impex;

import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

import static com.intellij.idea.plugin.hybris.impex.ImpexHighlighterColors.*;

public class ImpexColorSettingsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Comment marker (#)", COMMENT_MARKER),
            new AttributesDescriptor("Comment body", COMMENT_BODY),
            new AttributesDescriptor("Macro declaration", MACRO_DECLARATION),
            new AttributesDescriptor("Macro value", MACRO_VALUE),
            new AttributesDescriptor("Macro usage", MACRO_USAGE),
            new AttributesDescriptor("Assign value", ASSIGN_VALUE),
            new AttributesDescriptor("Insert", HEADER_MODE_INSERT),
            new AttributesDescriptor("Update", HEADER_MODE_UPDATE),
            new AttributesDescriptor("Insert or update", HEADER_MODE_INSERT_UPDATE),
            new AttributesDescriptor("Remove", HEADER_MODE_REMOVE),
            new AttributesDescriptor("Header type", HEADER_TYPE),
            new AttributesDescriptor("Value subtype", VALUE_SUBTYPE),
            new AttributesDescriptor("Field value separator", FIELD_VALUE_SEPARATOR),
            new AttributesDescriptor("List item separator", FIELD_LIST_ITEM_SEPARATOR),
            new AttributesDescriptor("Field value", FIELD_VALUE),
            new AttributesDescriptor("Single string", SINGLE_STRING),
            new AttributesDescriptor("Double string", DOUBLE_STRING),
            new AttributesDescriptor("Ignore value", FIELD_VALUE_IGNORE),
            new AttributesDescriptor("Bean Shell marker", BEAN_SHELL_MARKER),
            new AttributesDescriptor("Bean Shell body", BEAN_SHELL_BODY),
            new AttributesDescriptor("Square brackets", SQUARE_BRACKETS),
            new AttributesDescriptor("Round brackets", ROUND_BRACKETS),
            new AttributesDescriptor("Attribute name", ATTRIBUTE_NAME),
            new AttributesDescriptor("Attribute value", ATTRIBUTE_VALUE),
            new AttributesDescriptor("Attribute separator", ATTRIBUTE_SEPARATOR),
            new AttributesDescriptor("Boolean", BOOLEAN),
            new AttributesDescriptor("Digit", DIGIT),
            new AttributesDescriptor("Class with package", CLASS_WITH_PACKAGE),
            new AttributesDescriptor("Alternative map delimiter", ALTERNATIVE_MAP_DELIMITER),
            new AttributesDescriptor("Default key-value delimiter", DEFAULT_KEY_VALUE_DELIMITER),
            new AttributesDescriptor("Default path delimiter", DEFAULT_PATH_DELIMITER),
            new AttributesDescriptor("Parameter name", HEADER_PARAMETER_NAME),
            new AttributesDescriptor("Special parameter name", HEADER_SPECIAL_PARAMETER_NAME),
            new AttributesDescriptor("Parameters separator", PARAMETERS_SEPARATOR),
            new AttributesDescriptor("Comma", COMMA),
            new AttributesDescriptor("Semicolon", SEMICOLON),
            new AttributesDescriptor("Alternative pattern", ALTERNATIVE_PATTERN),
            new AttributesDescriptor("Document id", DOCUMENT_ID),
            new AttributesDescriptor("Bac character", HighlighterColors.BAD_CHARACTER)
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return ImpexIcons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new ImpexSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return "#This is a comment\n" +
                "INSERT Product;code[unique=true];name[lang=en]; name[lang=de];someList(code)\n" +
                ";myProduct1;myProduct1's localized name;lokalisierter Name von myProduct1;value1,value2,value3\n" +
                ";myProduct2;myProduct2's localized name;lokalisierter Name von myProduct2\n" +
                "\n" +
                "INSERT_UPDATE Address;firstname;owner( Principal.uid | AbstractOrder.code )\n" +
                ";Hans;\"Double quoted string\"\n" +
                ";Klaus;'Single quoted string'\n";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Impex";
    }
}
