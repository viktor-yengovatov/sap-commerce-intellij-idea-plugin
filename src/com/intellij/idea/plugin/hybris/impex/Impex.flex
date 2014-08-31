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

end_of_line_comment_marker = [#]
end_of_line_comment_body = [^\r\n]*

bean_shell_marker = [#][%]

single_string = ['](('')|([^'\r\n])*)[']
// Double string can contain line break
double_string = [\"](([\"][\"])|[^\"])*[\"]

assign_value = [=]

attribute = [:jletterdigit:]+

macro_declaration = [$][:jletterdigit:]+

left_square_bracket = [\[]
right_square_bracket = [\]]

semicolon = [;]
comma = [,]

attribute_name = (([:jletterdigit:]+))
attribute_value = (([:jletterdigit:]+)|([:jletterdigit:]+[.][:jletterdigit:]+))+

header_mode_insert = ("INSERT")
header_mode_update = ("UPDATE")
header_mode_insert_update = ("INSERT_UPDATE")
header_mode_remove = ("REMOVE")
header_type = [:jletterdigit:]+

value_subtype = [:jletterdigit:]+
field_value = [^;, \r\n]+
field_value_ignore = "<ignore>"

%state COMMENT
%state WAITING_MACRO_VALUE
%state MACRO_DECLARATION
%state HEADER_MODE
%state HEADER_TYPE
%state WAITING_FOR_FIELD_VALUE
%state FIELD_VALUE
%state BEAN_SHELL
%state MODYFIERS_BLOCK
%state ATTRIBUTE_NAME
%state WAITING_ATTRIBUTE_VALUE
%state ATTRIBUTE_VALUE

%%

// $catalogVersion = catalogversion(catalog(id[default=$productCatalog]),version[default='Staged'])[unique=true,default=$productCatalog:Staged]

{crlf}                                                      { yybegin(YYINITIAL); return ImpexTypes.CRLF; }

{white_space}+                                              { return TokenType.WHITE_SPACE; }

<YYINITIAL> {
    {bean_shell_marker}                                     { yybegin(BEAN_SHELL); return ImpexTypes.BEAN_SHELL_MARKER; }

    {end_of_line_comment_marker}                            { yybegin(COMMENT); return ImpexTypes.COMMENT_MARKER; }

    {macro_declaration}                                     { yybegin(MACRO_DECLARATION); return ImpexTypes.MACRO_DECLARATION; }

    {header_mode_insert}                                    { yybegin(HEADER_MODE); return ImpexTypes.HEADER_MODE_INSERT; }
    {header_mode_update}                                    { yybegin(HEADER_MODE); return ImpexTypes.HEADER_MODE_UPDATE; }
    {header_mode_insert_update}                             { yybegin(HEADER_MODE); return ImpexTypes.HEADER_MODE_INSERT_UPDATE; }
    {header_mode_remove}                                    { yybegin(HEADER_MODE); return ImpexTypes.HEADER_MODE_REMOVE; }

    {value_subtype}                                         { yybegin(FIELD_VALUE); return ImpexTypes.VALUE_SUBTYPE; }
}

<COMMENT> {
    {end_of_line_comment_body}                              { return ImpexTypes.COMMENT_BODY; }
}

<BEAN_SHELL> {
    {double_string}                                         {return ImpexTypes.BEAN_SHELL_BODY; }
}

<WAITING_FOR_FIELD_VALUE> {
    {double_string}                                         { yybegin(FIELD_VALUE); return ImpexTypes.DOUBLE_STRING; }

    <FIELD_VALUE> {
        {field_value_ignore}                                { yybegin(FIELD_VALUE); return ImpexTypes.FIELD_VALUE_IGNORE; }
        {field_value}                                       { yybegin(FIELD_VALUE); return ImpexTypes.FIELD_VALUE; }

        {comma}                                             { yybegin(WAITING_FOR_FIELD_VALUE); return ImpexTypes.FIELD_LIST_ITEM_SEPARATOR; }

        <YYINITIAL> {
            {semicolon}                                     { yybegin(WAITING_FOR_FIELD_VALUE); return ImpexTypes.FIELD_VALUE_SEPARATOR; }
        }
    }
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

<HEADER_TYPE> {
    {left_square_bracket}                                   { yybegin(MODYFIERS_BLOCK); return ImpexTypes.SQUARE_BRACKETS; }

    <ATTRIBUTE_VALUE> {
        {right_square_bracket}                              { yybegin(MODYFIERS_BLOCK); return ImpexTypes.SQUARE_BRACKETS; }
    }
}

<MODYFIERS_BLOCK> {
    {attribute_name}                                        { yybegin(ATTRIBUTE_NAME); return ImpexTypes.ATTRIBUTE_NAME; }
    {single_string}                                         { yybegin(ATTRIBUTE_NAME); return ImpexTypes.SINGLE_STRING; }
    {double_string}                                         { yybegin(ATTRIBUTE_NAME); return ImpexTypes.DOUBLE_STRING; }

    {right_square_bracket}                                  { return ImpexTypes.SQUARE_BRACKETS; }
}

<ATTRIBUTE_NAME> {
    {assign_value}                                          { yybegin(WAITING_ATTRIBUTE_VALUE); return ImpexTypes.ASSIGN_VALUE; }
}

<WAITING_ATTRIBUTE_VALUE> {
    {attribute_value}                                       { yybegin(ATTRIBUTE_VALUE); return ImpexTypes.ATTRIBUTE_VALUE; }
    {single_string}                                         { yybegin(ATTRIBUTE_VALUE); return ImpexTypes.SINGLE_STRING; }
    {double_string}                                         { yybegin(ATTRIBUTE_VALUE); return ImpexTypes.DOUBLE_STRING; }
}

<ATTRIBUTE_VALUE> {
    {comma}                                                 { yybegin(MODYFIERS_BLOCK); return ImpexTypes.ATTRIBUTE_SEPARATOR; }
}

// Fallback
.                                                           { return TokenType.BAD_CHARACTER; }
