package com.intellij.idea.plugin.hybris.flexibleSearch.highlighting;

import com.intellij.idea.plugin.hybris.flexibleSearch.FlexibleSearchLexerAdapter;
import com.intellij.idea.plugin.hybris.flexibleSearch.FlexibleSearchParserDefinition;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import static com.intellij.idea.plugin.hybris.flexibleSearch.highlighting.FlexibleSearchHighlighterColors.FS_BRACES;
import static com.intellij.idea.plugin.hybris.flexibleSearch.highlighting.FlexibleSearchHighlighterColors.FS_BRACKETS;
import static com.intellij.idea.plugin.hybris.flexibleSearch.highlighting.FlexibleSearchHighlighterColors.FS_COMMENT;
import static com.intellij.idea.plugin.hybris.flexibleSearch.highlighting.FlexibleSearchHighlighterColors.FS_KEYWORD;
import static com.intellij.idea.plugin.hybris.flexibleSearch.highlighting.FlexibleSearchHighlighterColors.FS_NUMBER;
import static com.intellij.idea.plugin.hybris.flexibleSearch.highlighting.FlexibleSearchHighlighterColors.FS_PARENTHESES;
import static com.intellij.idea.plugin.hybris.flexibleSearch.highlighting.FlexibleSearchHighlighterColors.FS_STRING;
import static com.intellij.idea.plugin.hybris.flexibleSearch.highlighting.FlexibleSearchHighlighterColors.FS_SYMBOL;

public class FlexibleSearchSyntaxHighlighter extends SyntaxHighlighterBase {

    private static final TokenSet BRACES_TOKEN_SET = TokenSet.create(
        FlexibleSearchTypes.LEFT_BRACE,
        FlexibleSearchTypes.LEFT_DOUBLE_BRACE,
        FlexibleSearchTypes.RIGHT_BRACE,
        FlexibleSearchTypes.RIGHT_DOUBLE_BRACE
    );
    private static final TokenSet BRACKETS_TOKEN_SET = TokenSet.create(
        FlexibleSearchTypes.LEFT_BRACKET,
        FlexibleSearchTypes.RIGHT_BRACKET
    );
    private static final TokenSet PARENTHESES_TOKEN_SET = TokenSet.create(
        FlexibleSearchTypes.LEFT_PAREN,
        FlexibleSearchTypes.RIGHT_PAREN
    );

    private static final TokenSet SYMBOL_TOKEN_SET = TokenSet.create(
        FlexibleSearchTypes.DOT,
        FlexibleSearchTypes.COLON,
        FlexibleSearchTypes.COMMA
    );

    private static final TokenSet KEYWORD_TOKEN_SET = TokenSet.create(
        FlexibleSearchTypes.SELECT,
        FlexibleSearchTypes.FROM,
        FlexibleSearchTypes.WHERE,
        FlexibleSearchTypes.GROUP,
        FlexibleSearchTypes.AS,
        FlexibleSearchTypes.IS,
        FlexibleSearchTypes.NULL,
        FlexibleSearchTypes.LIKE,
        FlexibleSearchTypes.AND,
        FlexibleSearchTypes.OR,
        FlexibleSearchTypes.ORDER,
        FlexibleSearchTypes.BY,
        FlexibleSearchTypes.DESC,
        FlexibleSearchTypes.ASC,
        FlexibleSearchTypes.UNION,
        FlexibleSearchTypes.NOT,
        FlexibleSearchTypes.EXISTS,
        FlexibleSearchTypes.COMPUTATIONAL_OPERATION
    );

    public static final TextAttributesKey[] KEYWORD_KEYS = pack(FS_KEYWORD);
    public static final TextAttributesKey[] STRING_KEYS = pack(FS_STRING);
    public static final TextAttributesKey[] SYMBOL_KEYS = pack(FS_SYMBOL);

    public static final TextAttributesKey[] BRACES_KEYS = pack(FS_BRACES);
    public static final TextAttributesKey[] BRACKETS_KEYS = pack(FS_BRACKETS);
    public static final TextAttributesKey[] PARENTHESES_KEYS = pack(FS_PARENTHESES);

    public static final TextAttributesKey[] NUMBER_KEYS = pack(FS_NUMBER);

    public static final TextAttributesKey[] COMMENT_KEYS = pack(FS_COMMENT);

    public static final TextAttributesKey[] BAD_CHARACTER_KEYS = pack(HighlighterColors.BAD_CHARACTER);

    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new FlexibleSearchLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (KEYWORD_TOKEN_SET.contains(tokenType)) {
            return KEYWORD_KEYS;
        } else if (tokenType.equals(FlexibleSearchTypes.STRING)) {
            return STRING_KEYS;
        } else if (SYMBOL_TOKEN_SET.contains(tokenType)) {
            return SYMBOL_KEYS;
        } else if (BRACES_TOKEN_SET.contains(tokenType)) {
            return BRACES_KEYS;
        } else if (BRACKETS_TOKEN_SET.contains(tokenType)) {
            return BRACKETS_KEYS;
        } else if (PARENTHESES_TOKEN_SET.contains(tokenType)) {
            return PARENTHESES_KEYS;
        } else if (tokenType.equals(FlexibleSearchTypes.NUMBER)) {
            return NUMBER_KEYS;
        } else if (tokenType.equals(FlexibleSearchParserDefinition.COMMENT)) {
            return COMMENT_KEYS;
        } else if (tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHARACTER_KEYS;
        } else {
            return EMPTY_KEYS;
        }
    }
}