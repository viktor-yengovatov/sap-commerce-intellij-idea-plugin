/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2016 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.intellij.idea.plugin.hybris.flexibleSearch;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import com.intellij.psi.TokenType;

import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.*;
import static com.intellij.idea.plugin.hybris.flexibleSearch.FlexibleSearchParserDefinition.COMMENT;
%%

%public
%class FlexibleSearchLexer
%implements FlexLexer
/* We're scanning text. */
%unicode


%function advance
%type IElementType
%eof{
    return;
%eof}


/* Ignore case when matching keywords */
%ignorecase


IDENTIFIER                      = [:jletter:] [:jletterdigit:]*
LINE_TERMINATOR                 = \r|\n|\r\n

/* comments */
COMMENT                         = "-""-"[^\r\n]*

// Strings
STRING_LITERAL                  = \'([^\\\'\r\n]|{ESCAPE_SEQUENCE})*(\'|\\)?
ESCAPE_SEQUENCE                 = \\[^\r\n]

/* main character classes */
DIGIT                           = [:digit:]
INTEGER                         = {DIGIT}+
WHITE_SPACE                     = \s+

PERCENT                         = [%]
QUOTE                           = [']
COMMA                           = [,]
DOT                             = [.]
COLON                           = [:]
SEMICOLON                       = [;]
LEFT_PAREN                      = [(]
RIGHT_PAREN                     = [)]
ASTERISK                        = [*]
PLUS_SIGN                       = [+]
MINUS_SIGN                      = [-]
LESS_THAN_OPERATOR              = [<]
EQUALS_OPERATOR                 = [=]
GREATER_THAN_OPERATOR           = [>]
LESS_THAN_OR_EQUALS_OPERATOR    = <=
GREATER_THAN_OR_EQUALS_OPERATOR = >=
NON_EQUAL_OPERATOR              = <>
QUESTION_MARK                   = [?]
EXCLAMATION_MARK                = [!]
LEFT_BRACKET                    = [\[]
RIGHT_BRACKET                   = [\]]
UNDERSCORE                      = [_]
LEFT_BRACE                      = "{"
LEFT_DOUBLE_BRACE               = "{{"
RIGHT_BRACE                     = "}"
RIGHT_DOUBLE_BRACE              = "}}"
EOL                             = \n|\r\n


%%

<YYINITIAL> {
    {INTEGER}                              { return NUMBER; }
    {COMMENT}                              { return COMMENT; }
    {WHITE_SPACE}                          { return WHITE_SPACE; }

    /* keywords */
    "SELECT"                               { return SELECT; }
    "AS"                                   { return AS; }
    "DISTINCT"                             { return DISTINCT; }
    "ALL"                                  { return ALL; }
    "COUNT"                                { return COUNT; }
    "CONCAT"                               { return CONCAT; }
    "AVG"                                  { return AVG; }
    "MAX"                                  { return MAX; }
    "MIN"                                  { return MIN; }
    "SUM"                                  { return SUM; }
    "EVERY"                                { return EVERY; }
    "ANY"                                  { return ANY; }
    "SOME"                                 { return SOME; }
    "ORDER"                                { return ORDER; }
    "BY"                                   { return BY; }
    "ASC"                                  { return ASC; }
    "DESC"                                 { return DESC; }
    "NULLS"                                { return NULLS; }
    "FIRST"                                { return FIRST; }
    "LAST"                                 { return LAST; }
    "FROM"                                 { return FROM; }
    "JOIN"                                 { return JOIN; }
    "ON"                                   { return ON; }
    "UNION"                                { return UNION; }
    "WHERE"                                { return WHERE; }
    "AND"                                  { return AND; }
    "OR"                                   { return OR; }
    "NOT"                                  { return NOT; }
    "IS"                                   { return IS; }
    "TRUE"                                 { return TRUE; }
    "FALSE"                                { return FALSE; }
    "NULL"                                 { return NULL; }
    "EXISTS"                               { return EXISTS; }
    "BETWEEN"                              { return BETWEEN; }
    "LIKE"                                 { return LIKE; }
    "CONCAT"                               { return CONCAT; }
    "GROUP"                                { return GROUP; }  

    
    {LEFT_BRACE}                           { return LEFT_BRACE; }
    {RIGHT_BRACE}                          { return RIGHT_BRACE; }
    {LEFT_DOUBLE_BRACE}                    { return LEFT_DOUBLE_BRACE; }
    {RIGHT_DOUBLE_BRACE}                   { return RIGHT_DOUBLE_BRACE; }
    {LEFT_PAREN}                           { return LEFT_PAREN; }
    {RIGHT_PAREN}                          { return RIGHT_PAREN; }
    {DOT}                                  { return DOT; }
    {COMMA}                                { return COMMA; }
    {ASTERISK}                             { return ASTERISK; }
    {EXCLAMATION_MARK}                     { return EXCLAMATION_MARK; }
    {QUESTION_MARK}                        { return QUESTION_MARK; }
    {COLON}                                { return COLON; }
    {SEMICOLON}                            { return SEMICOLON; }
     
    /* operators */
    {GREATER_THAN_OR_EQUALS_OPERATOR}      { return GREATER_THAN_OR_EQUALS_OPERATOR; }
    {LESS_THAN_OR_EQUALS_OPERATOR}         { return LESS_THAN_OR_EQUALS_OPERATOR; }
    {EQUALS_OPERATOR}                      { return EQUALS_OPERATOR; }
    {LESS_THAN_OPERATOR}                   { return LESS_THAN_OPERATOR; }
    {GREATER_THAN_OPERATOR}                { return GREATER_THAN_OPERATOR; }
    {NON_EQUAL_OPERATOR}                   { return NOT_EQUALS_OPERATOR; }
    
    
    {STRING_LITERAL}                       { return STRING; }
    {IDENTIFIER}                           { return IDENTIFIER; }
}

[^] { return TokenType.BAD_CHARACTER; }
