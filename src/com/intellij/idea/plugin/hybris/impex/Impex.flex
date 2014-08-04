package com.intellij.idea.plugin.hybris.impex;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.psi.TokenType;
import com.intellij.psi.CustomHighlighterTokenType;

%%

//%class ImpexLexer
//%implements FlexLexer
//%unicode
//%function advance
//%type IElementType
//%eof{
//  return;
//%eof}
//
//CRLF= \n|\r|\r\n
//WHITE_SPACE=[\ \t\f]
//FIRST_VALUE_CHARACTER=[^ \n\r\f\\] | "\\"{CRLF} | "\\".
//VALUE_CHARACTER=[^\n\r\f\\] | "\\"{CRLF} | "\\".
//END_OF_LINE_COMMENT=("#"|"!")[^\r\n]*
//SEPARATOR=[:=]
//KEY_CHARACTER=[^:=\ \n\r\t\f\\] | "\\"{CRLF} | "\\".
//
//%state WAITING_VALUE
//
//%%
//
//<YYINITIAL> {END_OF_LINE_COMMENT}                           { yybegin(YYINITIAL); return ImpexTypes.COMMENT; }
//
//<YYINITIAL> {KEY_CHARACTER}+                                { yybegin(YYINITIAL); return ImpexTypes.KEY; }
//
//<YYINITIAL> {SEPARATOR}                                     { yybegin(WAITING_VALUE); return ImpexTypes.SEPARATOR; }
//
//<WAITING_VALUE> {CRLF}                                     { yybegin(YYINITIAL); return ImpexTypes.CRLF; }
//
//<WAITING_VALUE> {WHITE_SPACE}+                              { yybegin(WAITING_VALUE); return TokenType.WHITE_SPACE; }
//
//<WAITING_VALUE> {FIRST_VALUE_CHARACTER}{VALUE_CHARACTER}*   { yybegin(YYINITIAL); return ImpexTypes.VALUE; }
//
//{CRLF}                                                     { yybegin(YYINITIAL); return ImpexTypes.CRLF; }
//
//{WHITE_SPACE}+                                              { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }
//
//.                                                           { return TokenType.BAD_CHARACTER; }

%class ImpexLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{
%eof}

CRLF= \n|\r|\r\n
WHITE_SPACE=[\ \t\f]
END_OF_LINE_COMMENT=\s*("#")[^\r\n]*
SEMICOLON=[;]
COMMA=[,]
SQUARE_BRACKETS=[\[\]]
ROUND_BRACKETS=[()]
INSERT_UPDATE=("INSERT"|"UPDATE"|"INSERT_UPDATE")
TABLE_NAME=[A-Za-z0-9_]+
PROPERTY_VALUE=[^;,\r\n\[\]()\ ]
SINGLE_QUOTED_STRING=\'[^\n\r\f\\]*\'
STRING_DOUBLE=\"[^\n\r\f\\]*\"

FIRST_VALUE_CHARACTER=[^ \n\r\f\\]
VALUE_CHARACTER=[^\n\r\f\\] | "\\"{CRLF} | "\\".

%state INSERT_UPDATE, SEMICOLON, COMMA

%%

<YYINITIAL> {END_OF_LINE_COMMENT}                           { yybegin(YYINITIAL); return ImpexTypes.COMMENT; }

{CRLF}                                                      { yybegin(YYINITIAL); return ImpexTypes.CRLF; }

{SINGLE_QUOTED_STRING}                                      { return CustomHighlighterTokenType.SINGLE_QUOTED_STRING; }

{STRING_DOUBLE}                                             { return CustomHighlighterTokenType.STRING; }

{WHITE_SPACE}+                                              { return TokenType.WHITE_SPACE; }

{SEMICOLON}                                                 { yybegin(SEMICOLON); return ImpexTypes.SEMICOLON; }

{COMMA}                                                     { yybegin(COMMA); return ImpexTypes.COMMA; }

{SQUARE_BRACKETS}                                           { return ImpexTypes.SQUARE_BRACKETS; }

{ROUND_BRACKETS}                                            { return ImpexTypes.ROUND_BRACKETS; }

<SEMICOLON, COMMA> {PROPERTY_VALUE}+                        { return ImpexTypes.PROPERTY_VALUE; }

<YYINITIAL> {INSERT_UPDATE}                                 { yybegin(INSERT_UPDATE); return ImpexTypes.INSERT_UPDATE; }

<INSERT_UPDATE> {TABLE_NAME}                                { return ImpexTypes.TABLE_NAME; }

.                                                           { return ImpexTypes.UNMAPPED; }
//.                                                           { return TokenType.BAD_CHARACTER; }
