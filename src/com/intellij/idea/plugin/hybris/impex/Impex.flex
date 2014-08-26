package com.intellij.idea.plugin.hybris.impex;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.psi.TokenType;
import com.intellij.psi.CustomHighlighterTokenType;

%%

%class ImpexLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{
    return;
%eof}

crlf                = \n|\r|\r\n
white_space         = [ \t\f]

end_of_line_comment = ("#")[^\r\n]*

assign_value = [=]

attribute = [:jletterdigit:]+

macro_name = [$][:jletterdigit:]+

header_mode = ("INSERT" | "UPDATE" | "INSERT_UPDATE" | "REMOVE")
header_type = [:jletterdigit:]+

%state COMMENT
%state WAITING_MACRO_VALUE
%state MACRO_DECLARATION
%state HEADER_MODE
%state HEADER_TYPE

%%

// $catalogVersion = catalogversion(catalog(id[default=$productCatalog]),version[default='Staged'])[unique=true,default=$productCatalog:Staged]

{crlf}                                                      { yybegin(YYINITIAL); return ImpexTypes.CRLF; }

{white_space}+                                              { return TokenType.WHITE_SPACE; }

<YYINITIAL> {
    {end_of_line_comment}                                   { yybegin(COMMENT); return ImpexTypes.COMMENT; }

    {macro_name}                                            { yybegin(MACRO_DECLARATION); return ImpexTypes.MACRO_DECLARATION; }

    {header_mode}                                           { yybegin(HEADER_MODE); return ImpexTypes.HEADER_MODE; }
}

<MACRO_DECLARATION> {assign_value}                          { yybegin(WAITING_MACRO_VALUE); return ImpexTypes.ASSIGN_VALUE; }

<WAITING_MACRO_VALUE> {
    {attribute}                                             { yybegin(WAITING_MACRO_VALUE); return ImpexTypes.ASSIGN_VALUE; }
}

<HEADER_MODE> {header_type}                                 { yybegin(HEADER_TYPE); return ImpexTypes.HEADER_TYPE; }

.                                                           { return TokenType.BAD_CHARACTER; }
