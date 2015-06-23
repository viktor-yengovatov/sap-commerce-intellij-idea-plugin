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

package com.intellij.idea.plugin.hybris.impex.highlighting;

import com.intellij.idea.plugin.hybris.impex.ImpexLexerAdapter;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.idea.plugin.hybris.impex.highlighting.ImpexHighlighterColors.*;

public class ImpexSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey[] COMMENT_MARKER_KEYS = pack(COMMENT_MARKER);
    public static final TextAttributesKey[] COMMENT_BODY_KEYS = pack(COMMENT_BODY);
    public static final TextAttributesKey[] MACRO_NAME_DECLARATION_KEYS = pack(MACRO_NAME_DECLARATION);
    public static final TextAttributesKey[] MACRO_VALUE_KEYS = pack(MACRO_VALUE);
    public static final TextAttributesKey[] MACRO_USAGE_KEYS = pack(MACRO_USAGE);
    public static final TextAttributesKey[] ASSIGN_VALUE_KEYS = pack(ASSIGN_VALUE);
    public static final TextAttributesKey[] HEADER_MODE_INSERT_KEYS = pack(HEADER_MODE_INSERT);
    public static final TextAttributesKey[] HEADER_MODE_UPDATE_KEYS = pack(HEADER_MODE_UPDATE);
    public static final TextAttributesKey[] HEADER_MODE_INSERT_UPDATE_KEYS = pack(HEADER_MODE_INSERT_UPDATE);
    public static final TextAttributesKey[] HEADER_MODE_REMOVE_KEYS = pack(HEADER_MODE_REMOVE);
    public static final TextAttributesKey[] HEADER_TYPE_KEYS = pack(HEADER_TYPE);
    public static final TextAttributesKey[] VALUE_SUBTYPE_KEYS = pack(VALUE_SUBTYPE);
    public static final TextAttributesKey[] FIELD_VALUE_SEPARATOR_KEYS = pack(FIELD_VALUE_SEPARATOR);
    public static final TextAttributesKey[] FIELD_LIST_ITEM_SEPARATOR_KEYS = pack(FIELD_LIST_ITEM_SEPARATOR);
    public static final TextAttributesKey[] FIELD_VALUE_KEYS = pack(FIELD_VALUE);
    public static final TextAttributesKey[] SINGLE_STRING_KEYS = pack(SINGLE_STRING);
    public static final TextAttributesKey[] DOUBLE_STRING_KEYS = pack(DOUBLE_STRING);
    public static final TextAttributesKey[] FIELD_VALUE_IGNORE_KEYS = pack(FIELD_VALUE_IGNORE);
    public static final TextAttributesKey[] BEAN_SHELL_MARKER_KEYS = pack(BEAN_SHELL_MARKER);
    public static final TextAttributesKey[] BEAN_SHELL_BODY_KEYS = pack(BEAN_SHELL_BODY);
    public static final TextAttributesKey[] SQUARE_BRACKETS_KEYS = pack(SQUARE_BRACKETS);
    public static final TextAttributesKey[] ROUND_BRACKETS_KEYS = pack(ROUND_BRACKETS);
    public static final TextAttributesKey[] ATTRIBUTE_NAME_KEYS = pack(ATTRIBUTE_NAME);
    public static final TextAttributesKey[] ATTRIBUTE_VALUE_KEYS = pack(ATTRIBUTE_VALUE);
    public static final TextAttributesKey[] ATTRIBUTE_SEPARATOR_KEYS = pack(ATTRIBUTE_SEPARATOR);
    public static final TextAttributesKey[] BOOLEAN_KEYS = pack(BOOLEAN);
    public static final TextAttributesKey[] DIGIT_KEYS = pack(DIGIT);
    public static final TextAttributesKey[] ALTERNATIVE_MAP_DELIMITER_KEYS = pack(ALTERNATIVE_MAP_DELIMITER);
    public static final TextAttributesKey[] DEFAULT_KEY_VALUE_DELIMITER_KEYS = pack(DEFAULT_KEY_VALUE_DELIMITER);
    public static final TextAttributesKey[] DEFAULT_PATH_DELIMITER_KEYS = pack(DEFAULT_PATH_DELIMITER);
    public static final TextAttributesKey[] HEADER_PARAMETER_NAME_KEYS = pack(HEADER_PARAMETER_NAME);
    public static final TextAttributesKey[] HEADER_SPECIAL_PARAMETER_NAME_KEYS = pack(HEADER_SPECIAL_PARAMETER_NAME);
    public static final TextAttributesKey[] PARAMETERS_SEPARATOR_KEYS = pack(PARAMETERS_SEPARATOR);
    public static final TextAttributesKey[] COMMA_KEYS = pack(COMMA);
    public static final TextAttributesKey[] BAD_CHARACTER_KEYS = pack(HighlighterColors.BAD_CHARACTER);
    public static final TextAttributesKey[] ALTERNATIVE_PATTERN_KEYS = pack(ALTERNATIVE_PATTERN);
    public static final TextAttributesKey[] DOCUMENT_ID_KEYS = pack(DOCUMENT_ID);

    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new ImpexLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(@NotNull final IElementType tokenType) {
        if (tokenType.equals(ImpexTypes.ALTERNATIVE_MAP_DELIMITER)) {
            return ALTERNATIVE_MAP_DELIMITER_KEYS;
        } else if (tokenType.equals(ImpexTypes.DEFAULT_KEY_VALUE_DELIMITER)) {
            return DEFAULT_KEY_VALUE_DELIMITER_KEYS;
        } else if (tokenType.equals(ImpexTypes.ASSIGN_VALUE)) {
            return ASSIGN_VALUE_KEYS;
        } else if (tokenType.equals(ImpexTypes.ATTRIBUTE_NAME)) {
            return ATTRIBUTE_NAME_KEYS;
        } else if (tokenType.equals(ImpexTypes.ATTRIBUTE_SEPARATOR)) {
            return ATTRIBUTE_SEPARATOR_KEYS;
        } else if (tokenType.equals(ImpexTypes.ATTRIBUTE_VALUE)) {
            return ATTRIBUTE_VALUE_KEYS;
        } else if (tokenType.equals(ImpexTypes.BEAN_SHELL_BODY)) {
            return BEAN_SHELL_BODY_KEYS;
        } else if (tokenType.equals(ImpexTypes.BEAN_SHELL_MARKER)) {
            return BEAN_SHELL_MARKER_KEYS;
        } else if (tokenType.equals(ImpexTypes.BOOLEAN)) {
            return BOOLEAN_KEYS;
        } else if (tokenType.equals(ImpexTypes.COMMA)) {
            return COMMA_KEYS;
        } else if (tokenType.equals(ImpexTypes.COMMENT_BODY)) {
            return COMMENT_BODY_KEYS;
        } else if (tokenType.equals(ImpexTypes.COMMENT_MARKER)) {
            return COMMENT_MARKER_KEYS;
        } else if (tokenType.equals(ImpexTypes.DEFAULT_PATH_DELIMITER)) {
            return DEFAULT_PATH_DELIMITER_KEYS;
        } else if (tokenType.equals(ImpexTypes.DIGIT)) {
            return DIGIT_KEYS;
        } else if (tokenType.equals(ImpexTypes.DOUBLE_STRING)) {
            return DOUBLE_STRING_KEYS;
        } else if (tokenType.equals(ImpexTypes.FIELD_LIST_ITEM_SEPARATOR)) {
            return FIELD_LIST_ITEM_SEPARATOR_KEYS;
        } else if (tokenType.equals(ImpexTypes.FIELD_VALUE)) {
            return FIELD_VALUE_KEYS;
        } else if (tokenType.equals(ImpexTypes.FIELD_VALUE_IGNORE)) {
            return FIELD_VALUE_IGNORE_KEYS;
        } else if (tokenType.equals(ImpexTypes.FIELD_VALUE_SEPARATOR)) {
            return FIELD_VALUE_SEPARATOR_KEYS;
        } else if (tokenType.equals(ImpexTypes.HEADER_MODE_INSERT)) {
            return HEADER_MODE_INSERT_KEYS;
        } else if (tokenType.equals(ImpexTypes.HEADER_MODE_INSERT_UPDATE)) {
            return HEADER_MODE_INSERT_UPDATE_KEYS;
        } else if (tokenType.equals(ImpexTypes.HEADER_MODE_REMOVE)) {
            return HEADER_MODE_REMOVE_KEYS;
        } else if (tokenType.equals(ImpexTypes.HEADER_MODE_UPDATE)) {
            return HEADER_MODE_UPDATE_KEYS;
        } else if (tokenType.equals(ImpexTypes.HEADER_PARAMETER_NAME)) {
            return HEADER_PARAMETER_NAME_KEYS;
        } else if (tokenType.equals(ImpexTypes.HEADER_SPECIAL_PARAMETER_NAME)) {
            return HEADER_SPECIAL_PARAMETER_NAME_KEYS;
        } else if (tokenType.equals(ImpexTypes.HEADER_TYPE)) {
            return HEADER_TYPE_KEYS;
        } else if (tokenType.equals(ImpexTypes.MACRO_NAME_DECLARATION)) {
            return MACRO_NAME_DECLARATION_KEYS;
        } else if (tokenType.equals(ImpexTypes.MACRO_USAGE)) {
            return MACRO_USAGE_KEYS;
        } else if (tokenType.equals(ImpexTypes.MACRO_VALUE)) {
            return MACRO_VALUE_KEYS;
        } else if (tokenType.equals(ImpexTypes.PARAMETERS_SEPARATOR)) {
            return PARAMETERS_SEPARATOR_KEYS;
        } else if (tokenType.equals(ImpexTypes.LEFT_ROUND_BRACKET) || tokenType.equals(ImpexTypes.RIGHT_ROUND_BRACKET)) {
            return ROUND_BRACKETS_KEYS;
        } else if (tokenType.equals(ImpexTypes.SINGLE_STRING)) {
            return SINGLE_STRING_KEYS;
        } else if (tokenType.equals(ImpexTypes.LEFT_SQUARE_BRACKET) || tokenType.equals(ImpexTypes.RIGHT_SQUARE_BRACKET)) {
            return SQUARE_BRACKETS_KEYS;
        } else if (tokenType.equals(ImpexTypes.VALUE_SUBTYPE)) {
            return VALUE_SUBTYPE_KEYS;
        } else if (tokenType.equals(ImpexTypes.ALTERNATIVE_PATTERN)) {
            return ALTERNATIVE_PATTERN_KEYS;
        } else if (tokenType.equals(ImpexTypes.DOCUMENT_ID)) {
            return DOCUMENT_ID_KEYS;
        } else if (tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHARACTER_KEYS;
        } else {
            return EMPTY_KEYS;
        }
    }
}
