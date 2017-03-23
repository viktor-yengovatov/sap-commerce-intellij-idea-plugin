package com.intellij.idea.plugin.hybris.flexibleSearch.highlighting;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.BRACES;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.BRACKETS;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.INSTANCE_FIELD;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.KEYWORD;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.LINE_COMMENT;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.NUMBER;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.PARENTHESES;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.STRING;

public class FlexibleSearchHighlighterColors {

    private FlexibleSearchHighlighterColors() {
    }

    public static final TextAttributesKey FS_KEYWORD = key("KEYWORD", KEYWORD);
    public static final TextAttributesKey FS_STRING = key("STRING", STRING);
    
    public static final TextAttributesKey FS_BRACES = key("BRACES", BRACES);
    public static final TextAttributesKey FS_PARENTHESES = key("PARENTHESES", PARENTHESES);
    public static final TextAttributesKey FS_BRACKETS = key("BRACKETS", BRACKETS);
    
    public static final TextAttributesKey FS_SYMBOL = key("COMMA", KEYWORD);
    public static final TextAttributesKey FS_NUMBER = key("NUMBER", NUMBER);
    
    public static final TextAttributesKey FS_COLUMN = key("COLUMN", INSTANCE_FIELD);
    
    public static final TextAttributesKey FS_COMMENT = key("COMMENT", LINE_COMMENT);
    
    private static TextAttributesKey key(
        @NonNls @NotNull final String externalName,
        final TextAttributesKey fallbackAttributeKey
    ) {
        return TextAttributesKey.createTextAttributesKey(externalName, fallbackAttributeKey);
    }
}