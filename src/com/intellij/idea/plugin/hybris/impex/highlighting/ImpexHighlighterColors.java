package com.intellij.idea.plugin.hybris.impex.highlighting;

import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.*;

public class ImpexHighlighterColors {

    private ImpexHighlighterColors() {
    }

    public static final TextAttributesKey COMMENT_MARKER = key("COMMENT_MARKER", LINE_COMMENT);
    public static final TextAttributesKey COMMENT_BODY = key("COMMENT_BODY", LINE_COMMENT);
    public static final TextAttributesKey MACRO_DECLARATION = key("MACRO_DECLARATION", INSTANCE_FIELD);
    public static final TextAttributesKey MACRO_VALUE = key("MACRO_VALUE", HighlighterColors.TEXT);
    public static final TextAttributesKey MACRO_USAGE = key("MACRO_USAGE", INSTANCE_FIELD);
    public static final TextAttributesKey ASSIGN_VALUE = key("ASSIGN_VALUE", HighlighterColors.TEXT);
    public static final TextAttributesKey HEADER_MODE_INSERT = key("HEADER_MODE_INSERT", KEYWORD);
    public static final TextAttributesKey HEADER_MODE_UPDATE = key("HEADER_MODE_UPDATE", KEYWORD);
    public static final TextAttributesKey HEADER_MODE_INSERT_UPDATE = key("HEADER_MODE_INSERT_UPDATE", KEYWORD);
    public static final TextAttributesKey HEADER_MODE_REMOVE = key("HEADER_MODE_REMOVE", KEYWORD);
    public static final TextAttributesKey HEADER_TYPE = key("HEADER_TYPE", FUNCTION_DECLARATION);
    public static final TextAttributesKey VALUE_SUBTYPE = key("VALUE_SUBTYPE", METADATA);
    public static final TextAttributesKey FIELD_VALUE_SEPARATOR = key("FIELD_VALUE_SEPARATOR", KEYWORD);
    public static final TextAttributesKey FIELD_LIST_ITEM_SEPARATOR = key("FIELD_LIST_ITEM_SEPARATOR", KEYWORD);
    public static final TextAttributesKey FIELD_VALUE = key("FIELD_VALUE", HighlighterColors.TEXT);
    public static final TextAttributesKey SINGLE_STRING = key("SINGLE_STRING", STRING);
    public static final TextAttributesKey DOUBLE_STRING = key("DOUBLE_STRING", STRING);
    public static final TextAttributesKey FIELD_VALUE_IGNORE = key("FIELD_VALUE_IGNORE", KEYWORD);
    public static final TextAttributesKey BEAN_SHELL_MARKER = key("BEAN_SHELL_MARKER", KEYWORD);
    public static final TextAttributesKey BEAN_SHELL_BODY = key("BEAN_SHELL_BODY", HighlighterColors.TEXT);
    public static final TextAttributesKey SQUARE_BRACKETS = key("SQUARE_BRACKETS", KEYWORD);
    public static final TextAttributesKey ROUND_BRACKETS = key("ROUND_BRACKETS", KEYWORD);
    public static final TextAttributesKey ATTRIBUTE_NAME = key("ATTRIBUTE_NAME", HighlighterColors.TEXT);
    public static final TextAttributesKey ATTRIBUTE_VALUE = key("ATTRIBUTE_VALUE", HighlighterColors.TEXT);
    public static final TextAttributesKey ATTRIBUTE_SEPARATOR = key("ATTRIBUTE_SEPARATOR", KEYWORD);
    public static final TextAttributesKey BOOLEAN = key("BOOLEAN", KEYWORD);
    public static final TextAttributesKey DIGIT = key("DIGIT", NUMBER);
    public static final TextAttributesKey CLASS_WITH_PACKAGE = key("CLASS_WITH_PACKAGE", HighlighterColors.TEXT);
    public static final TextAttributesKey ALTERNATIVE_MAP_DELIMITER = key("ALTERNATIVE_MAP_DELIMITER", KEYWORD);
    public static final TextAttributesKey DEFAULT_KEY_VALUE_DELIMITER = key("DEFAULT_KEY_VALUE_DELIMITER", KEYWORD);
    public static final TextAttributesKey DEFAULT_PATH_DELIMITER = key("DEFAULT_PATH_DELIMITER", KEYWORD);
    public static final TextAttributesKey HEADER_PARAMETER_NAME = key("HEADER_PARAMETER_NAME", HighlighterColors.TEXT);
    public static final TextAttributesKey HEADER_SPECIAL_PARAMETER_NAME = key("HEADER_SPECIAL_PARAMETER_NAME", INSTANCE_FIELD);
    public static final TextAttributesKey PARAMETERS_SEPARATOR = key("PARAMETERS_SEPARATOR", KEYWORD);
    public static final TextAttributesKey COMMA = key("COMMA", KEYWORD);
    public static final TextAttributesKey SEMICOLON = key("SEMICOLON", KEYWORD);
    public static final TextAttributesKey ALTERNATIVE_PATTERN = key("ALTERNATIVE_PATTERN", KEYWORD);
    public static final TextAttributesKey DOCUMENT_ID = key("DOCUMENT_ID", STATIC_FIELD);

    private static TextAttributesKey key(@NonNls @NotNull final String externalName, final TextAttributesKey fallbackAttributeKey) {
        return TextAttributesKey.createTextAttributesKey(externalName, fallbackAttributeKey);
    }
}
