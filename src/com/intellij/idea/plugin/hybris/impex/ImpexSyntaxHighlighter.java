package com.intellij.idea.plugin.hybris.impex;

import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class ImpexSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey COMMENT = createTextAttributesKey("IMPEX_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey SEMICOLON = createTextAttributesKey("IMPEX_SEMICOLON", new TextAttributes(JBColor.decode("#C96B0E"), null, null, null, Font.BOLD));
    public static final TextAttributesKey COMMA = createTextAttributesKey("IMPEX_COMMA", new TextAttributes(JBColor.decode("#1C9C0A"), null, null, null, Font.BOLD));
    public static final TextAttributesKey ROUND_BRACKETS = createTextAttributesKey("IMPEX_ROUND_BRACKETS", new TextAttributes(JBColor.decode("#376DCF"), null, null, null, Font.BOLD));
    public static final TextAttributesKey SQUARE_BRACKETS = createTextAttributesKey("IMPEX_SQUARE_BRACKETS", new TextAttributes(JBColor.decode("#783FCF"), null, null, null, Font.BOLD));
    public static final TextAttributesKey INSERT_UPDATE = createTextAttributesKey("IMPEX_INSERT_UPDATE", new TextAttributes(JBColor.decode("#783FCF"), null, null, null, Font.BOLD));
    public static final TextAttributesKey TABLE_NAME = createTextAttributesKey("IMPEX_TABLE_NAME", new TextAttributes(JBColor.decode("#775AD4"), null, null, null, Font.BOLD));

    //static final TextAttributesKey BAD_CHARACTER = createTextAttributesKey("IMPEX_BAD_CHARACTER", new TextAttributes(Color.RED, null, null, null, Font.BOLD));
    static final TextAttributesKey BAD_CHARACTER = createTextAttributesKey("IMPEX_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] SEMICOLON_KEYS = new TextAttributesKey[]{SEMICOLON};
    private static final TextAttributesKey[] COMMA_KEYS = new TextAttributesKey[]{COMMA};
    private static final TextAttributesKey[] ROUND_BRACKETS_KEYS = new TextAttributesKey[]{ROUND_BRACKETS};
    private static final TextAttributesKey[] SQUARE_BRACKETS_KEYS = new TextAttributesKey[]{SQUARE_BRACKETS};
    private static final TextAttributesKey[] INSERT_UPDATE_KEYS = new TextAttributesKey[]{INSERT_UPDATE};
    private static final TextAttributesKey[] TABLE_NAME_KEYS = new TextAttributesKey[]{TABLE_NAME};
    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new ImpexLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(ImpexTypes.COMMENT)) {
            return COMMENT_KEYS;
        } else if (tokenType.equals(ImpexTypes.SEMICOLON)) {
            return SEMICOLON_KEYS;
        } else if (tokenType.equals(ImpexTypes.COMMA)) {
            return COMMA_KEYS;
        } else if (tokenType.equals(ImpexTypes.ROUND_BRACKETS)) {
            return ROUND_BRACKETS_KEYS;
        } else if (tokenType.equals(ImpexTypes.SQUARE_BRACKETS)) {
            return SQUARE_BRACKETS_KEYS;
        } else if (tokenType.equals(ImpexTypes.INSERT_UPDATE)) {
            return INSERT_UPDATE_KEYS;
        } else if (tokenType.equals(ImpexTypes.TABLE_NAME)) {
            return TABLE_NAME_KEYS;
        } else if (tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHAR_KEYS;
        } else {
            return EMPTY_KEYS;
        }
    }
}
