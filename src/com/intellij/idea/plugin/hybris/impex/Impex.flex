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

single_string = ['](('')|([^'\r\n])*)[']
double_string = [\"](([\"][\"])|[^\"\r\n])*[\"]

assign_value = [=]

attribute = [:jletterdigit:]+

macro_declaration = [$][:jletterdigit:]+

header_mode_insert = ("INSERT")
header_mode_update = ("UPDATE")
header_mode_insert_update = ("INSERT_UPDATE")
header_mode_remove = ("REMOVE")
header_type = [:jletterdigit:]+

value_subtype = [:jletterdigit:]+
field_value_separator = [;]
field_list_item_separator = [,]
field_value = [^;, \r\n]+
field_value_ignore = "<ignore>"

%state COMMENT
%state WAITING_MACRO_VALUE
%state MACRO_DECLARATION
%state HEADER_MODE
%state HEADER_TYPE
%state WAITING_FOR_FIELD_VALUE
%state FIELD_VALUE

%%

// $catalogVersion = catalogversion(catalog(id[default=$productCatalog]),version[default='Staged'])[unique=true,default=$productCatalog:Staged]

{crlf}                                                      { yybegin(YYINITIAL); return ImpexTypes.CRLF; }

{white_space}+                                              { return TokenType.WHITE_SPACE; }

<YYINITIAL> {
    {end_of_line_comment}                                   { yybegin(COMMENT); return ImpexTypes.COMMENT; }

    {macro_declaration}                                     { yybegin(MACRO_DECLARATION); return ImpexTypes.MACRO_DECLARATION; }

    {header_mode_insert}                                    { yybegin(HEADER_MODE); return ImpexTypes.HEADER_MODE_INSERT; }
    {header_mode_update}                                    { yybegin(HEADER_MODE); return ImpexTypes.HEADER_MODE_UPDATE; }
    {header_mode_insert_update}                             { yybegin(HEADER_MODE); return ImpexTypes.HEADER_MODE_INSERT_UPDATE; }
    {header_mode_remove}                                    { yybegin(HEADER_MODE); return ImpexTypes.HEADER_MODE_REMOVE; }

    {value_subtype}                                         { yybegin(FIELD_VALUE); return ImpexTypes.VALUE_SUBTYPE; }
}

<YYINITIAL, FIELD_VALUE, WAITING_FOR_FIELD_VALUE> {
    {field_value_separator}                                 { yybegin(WAITING_FOR_FIELD_VALUE); return ImpexTypes.FIELD_VALUE_SEPARATOR; }
    {field_list_item_separator}                             { yybegin(WAITING_FOR_FIELD_VALUE); return ImpexTypes.FIELD_LIST_ITEM_SEPARATOR; }
}

<FIELD_VALUE, WAITING_FOR_FIELD_VALUE> {
    {double_string}                                         { yybegin(FIELD_VALUE); return ImpexTypes.DOUBLE_STRING; }
    {field_value_ignore}                                    { yybegin(FIELD_VALUE); return ImpexTypes.FIELD_VALUE_IGNORE; }
    {field_value}                                           { yybegin(FIELD_VALUE); return ImpexTypes.FIELD_VALUE; }
}

<MACRO_DECLARATION> {
    {assign_value}                                          { yybegin(WAITING_MACRO_VALUE); return ImpexTypes.ASSIGN_VALUE; }
}

<WAITING_MACRO_VALUE> {
    {attribute}                                             { yybegin(WAITING_MACRO_VALUE); return ImpexTypes.ASSIGN_VALUE; }
}

<HEADER_MODE> {
    {header_type}                                           { yybegin(HEADER_TYPE); return ImpexTypes.HEADER_TYPE; }
}

.                                                           { return TokenType.BAD_CHARACTER; }
