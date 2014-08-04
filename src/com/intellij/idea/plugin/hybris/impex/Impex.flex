package com.intellij.idea.plugin.hybris.impex;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.psi.TokenType;

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
INSERT_UPDATE=("INSERT_UPDATE"|"UPDATE")
TABLE_NAME=[A-Za-z0-9_]+

FIRST_VALUE_CHARACTER=[^ \n\r\f\\]
VALUE_CHARACTER=[^\n\r\f\\] | "\\"{CRLF} | "\\".

%state UNMAPPED, INSERT_UPDATE

%%

<YYINITIAL> {END_OF_LINE_COMMENT}                           { yybegin(YYINITIAL); return ImpexTypes.COMMENT; }

{CRLF}                                                      { yybegin(YYINITIAL); return ImpexTypes.CRLF; }

<INSERT_UPDATE> {WHITE_SPACE}                               {return TokenType.WHITE_SPACE;}

{WHITE_SPACE}+                                              { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

<YYINITIAL, UNMAPPED> {SEMICOLON}                           {return ImpexTypes.SEMICOLON;}

<YYINITIAL, UNMAPPED> {COMMA}                               {return ImpexTypes.COMMA;}

<YYINITIAL, UNMAPPED> {SQUARE_BRACKETS}                     {return ImpexTypes.SQUARE_BRACKETS;}

<YYINITIAL, UNMAPPED> {ROUND_BRACKETS}                      {return ImpexTypes.ROUND_BRACKETS;}

<YYINITIAL, UNMAPPED> {INSERT_UPDATE}                       {yybegin(INSERT_UPDATE); return ImpexTypes.INSERT_UPDATE;}
<INSERT_UPDATE> {TABLE_NAME}                                {return ImpexTypes.TABLE_NAME;}

.                                                           { yybegin(UNMAPPED); return ImpexTypes.UNMAPPED; }
//.                                                           { return TokenType.BAD_CHARACTER; }
