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
    return;
%eof}

crlf                = \n|\r|\r\n
white_space         = [ \t\f]

end_of_line_comment = \s*("#")[^\r\n]*

assign_value = [=]

macro_name = [$][:jletterdigit:]+

header_mode = ("INSERT" | "UPDATE" | "INSERT_UPDATE" | "REMOVE")
header_type = [:jletterdigit:]+

//semicolon = ;
//comma     = ,
//square_brackets = [\[\]]
//round_brackets  = [()]
//table_name      = [A-Za-z0-9_]+
//property_value  = [^;,\r\n\[\]()'\"\ ]
//single_quoted_string = \'[^\n\r\f\\']*\'
//string_double        = \"[^\n\r\f\\\"]*\"
//
//first_value_character = [^ \n\r\f\\]
//value_character = [^\n\r\f\\] | "\\"{crlf} | "\\".

%state COMMENT
%state WAITING_VALUE // Waiting for some value after '=' character, e.g. for macro body or modifier malue.
%state MACRO_DECLARATION // New macro name declararion
%state HEADER_MODE // Declaration of an insert or any other operation
%state HEADER_TYPE // Type declaration after insert or any other operation

%%

// $catalogVersion = catalogversion(catalog(id[default=$productCatalog]),version[default='Staged'])[unique=true,default=$productCatalog:Staged]

{crlf}                                                      { yybegin(YYINITIAL); return ImpexTypes.CRLF; }

{white_space}+                                              { return TokenType.WHITE_SPACE; }

<YYINITIAL> {
    {end_of_line_comment}                                   { yybegin(COMMENT); return ImpexTypes.COMMENT; }

    {macro_name}                                            { yybegin(MACRO_DECLARATION); return ImpexTypes.MACRO_DECLARATION; }

    {header_mode}                                           { yybegin(HEADER_MODE); return ImpexTypes.HEADER_MODE; }
}

<MACRO_DECLARATION> {assign_value}                          { yybegin(WAITING_VALUE); return ImpexTypes.ASSIGN_VALUE; }

<HEADER_MODE> {header_type}                                 { yybegin(HEADER_TYPE); return ImpexTypes.HEADER_TYPE; }

.                                                           { return TokenType.BAD_CHARACTER; }
