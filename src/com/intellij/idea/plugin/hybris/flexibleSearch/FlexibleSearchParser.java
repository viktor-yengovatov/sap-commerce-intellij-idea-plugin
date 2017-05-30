// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;

import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.AGGREGATE_FUNCTION;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.ALL;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.AND;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.ANY;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.AS;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.ASC;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.ASTERISK;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.AVG;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.BETWEEN;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.BETWEEN_PREDICATE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.BOOLEAN_FACTOR;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.BOOLEAN_PREDICAND;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.BOOLEAN_PRIMARY;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.BOOLEAN_TERM;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.BOOLEAN_TEST;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.BOOLEAN_VALUE_EXPRESSION;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.BY;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.CHARACTER_LIKE_PREDICATE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.CHARACTER_PATTERN;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.CHARACTER_STRING_LITERAL;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.CHARACTER_SUBSTRING_FUNCTION;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.CHARACTER_VALUE_FUNCTION;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.COLON;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.COLUMN_REFERENCE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.COLUMN_REFERENCE_IDENTIFIER;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.COMMA;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.COMMON_VALUE_EXPRESSION;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.COMP_OP;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.CONCAT;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.CORRELATION_NAME;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.COUNT;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.DERIVED_COLUMN;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.DESC;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.DISTINCT;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.DOT;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.EQUALS_OPERATOR;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.EVERY;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.EXCLAMATION_MARK;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.EXISTS;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.EXISTS_PREDICATE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.FALSE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.FIRST;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.FROM;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.FROM_CLAUSE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.GENERAL_LITERAL;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.GENERAL_SET_FUNCTION;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.GREATER_THAN_OPERATOR;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.GREATER_THAN_OR_EQUALS_OPERATOR;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.GROUP;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.GROUPING_COLUMN_REFERENCE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.GROUPING_COLUMN_REFERENCE_LIST;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.GROUPING_ELEMENT;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.GROUPING_ELEMENT_LIST;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.GROUP_BY_CLAUSE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.IDENTIFIER;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.IS;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.JOIN;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.JOINED_TABLE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.JOIN_CONDITION;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.JOIN_SPECIFICATION;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.JOIN_TYPE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.JOIN_TYPE_1_0;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.LANG;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.LAST;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.LEFT;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.LEFT_BRACE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.LEFT_BRACKET;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.LEFT_DOUBLE_BRACE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.LEFT_PAREN;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.LESS_THAN_OPERATOR;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.LESS_THAN_OR_EQUALS_OPERATOR;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.LIKE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.LIKE_PREDICATE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.MAX;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.MIN;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.NOT;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.NOT_EQUALS_OPERATOR;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.NULL;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.NULLS;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.NULL_ORDERING;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.NULL_PREDICATE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.NUMBER;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.ON;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.OR;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.ORDER;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.ORDERING_SPECIFICATION;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.ORDER_BY_CLAUSE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.ORDINARY_GROUPING_SET;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.PREDICATE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.QUERY_SPECIFICATION;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.QUESTION_MARK;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.RIGHT_BRACE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.RIGHT_BRACKET;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.RIGHT_DOUBLE_BRACE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.RIGHT_PAREN;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.ROW_VALUE_PREDICAND;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.SEARCH_CONDITION;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.SELECT;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.SELECT_LIST;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.SELECT_SUBLIST;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.SEMICOLON;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.SET_FUNCTION_TYPE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.SET_QUANTIFIER;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.SOME;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.SORT_KEY;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.SORT_SPECIFICATION;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.SORT_SPECIFICATION_LIST;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.STRING;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.STRING_VALUE_EXPRESSION;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.STRING_VALUE_FUNCTION;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.SUBQUERY;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.SUM;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.TABLE_EXPRESSION;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.TABLE_NAME;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.TABLE_NAME_IDENTIFIER;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.TABLE_PRIMARY;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.TABLE_REFERENCE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.TABLE_REFERENCE_LIST;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.TABLE_SUBQUERY;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.TRUE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.TRUTH_VALUE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.UNION;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.VALUE_EXPRESSION;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.WHERE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.WHERE_CLAUSE;
import static com.intellij.idea.plugin.hybris.flexibleSearch.utils.FlexibleSearchParserUtils.Parser;
import static com.intellij.idea.plugin.hybris.flexibleSearch.utils.FlexibleSearchParserUtils.TRUE_CONDITION;
import static com.intellij.idea.plugin.hybris.flexibleSearch.utils.FlexibleSearchParserUtils._COLLAPSE_;
import static com.intellij.idea.plugin.hybris.flexibleSearch.utils.FlexibleSearchParserUtils._NONE_;
import static com.intellij.idea.plugin.hybris.flexibleSearch.utils.FlexibleSearchParserUtils._NOT_;
import static com.intellij.idea.plugin.hybris.flexibleSearch.utils.FlexibleSearchParserUtils.adapt_builder_;
import static com.intellij.idea.plugin.hybris.flexibleSearch.utils.FlexibleSearchParserUtils.consumeToken;
import static com.intellij.idea.plugin.hybris.flexibleSearch.utils.FlexibleSearchParserUtils.consumeTokens;
import static com.intellij.idea.plugin.hybris.flexibleSearch.utils.FlexibleSearchParserUtils.current_position_;
import static com.intellij.idea.plugin.hybris.flexibleSearch.utils.FlexibleSearchParserUtils.empty_element_parsed_guard_;
import static com.intellij.idea.plugin.hybris.flexibleSearch.utils.FlexibleSearchParserUtils.enter_section_;
import static com.intellij.idea.plugin.hybris.flexibleSearch.utils.FlexibleSearchParserUtils.eof;
import static com.intellij.idea.plugin.hybris.flexibleSearch.utils.FlexibleSearchParserUtils.exit_section_;
import static com.intellij.idea.plugin.hybris.flexibleSearch.utils.FlexibleSearchParserUtils.nextTokenIs;
import static com.intellij.idea.plugin.hybris.flexibleSearch.utils.FlexibleSearchParserUtils.parseTokens;
import static com.intellij.idea.plugin.hybris.flexibleSearch.utils.FlexibleSearchParserUtils.recursion_guard_;
import static com.intellij.idea.plugin.hybris.flexibleSearch.utils.FlexibleSearchParserUtils.report_error_;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class FlexibleSearchParser implements PsiParser, LightPsiParser {

    public ASTNode parse(IElementType t, PsiBuilder b) {
        parseLight(t, b);
        return b.getTreeBuilt();
    }

    public void parseLight(IElementType t, PsiBuilder b) {
        boolean r;
        b = adapt_builder_(t, b, this, null);
        Marker m = enter_section_(b, 0, _COLLAPSE_, null);
        if (t == AGGREGATE_FUNCTION) {
            r = aggregate_function(b, 0);
        } else if (t == BETWEEN_PREDICATE) {
            r = between_predicate(b, 0);
        } else if (t == BOOLEAN_FACTOR) {
            r = boolean_factor(b, 0);
        } else if (t == BOOLEAN_PREDICAND) {
            r = boolean_predicand(b, 0);
        } else if (t == BOOLEAN_PRIMARY) {
            r = boolean_primary(b, 0);
        } else if (t == BOOLEAN_TERM) {
            r = boolean_term(b, 0);
        } else if (t == BOOLEAN_TEST) {
            r = boolean_test(b, 0);
        } else if (t == BOOLEAN_VALUE_EXPRESSION) {
            r = boolean_value_expression(b, 0);
        } else if (t == CHARACTER_LIKE_PREDICATE) {
            r = character_like_predicate(b, 0);
        } else if (t == CHARACTER_PATTERN) {
            r = character_pattern(b, 0);
        } else if (t == CHARACTER_STRING_LITERAL) {
            r = character_string_literal(b, 0);
        } else if (t == CHARACTER_SUBSTRING_FUNCTION) {
            r = character_substring_function(b, 0);
        } else if (t == CHARACTER_VALUE_FUNCTION) {
            r = character_value_function(b, 0);
        } else if (t == COLUMN_REFERENCE) {
            r = column_reference(b, 0);
        } else if (t == COMMON_VALUE_EXPRESSION) {
            r = common_value_expression(b, 0);
        } else if (t == COMP_OP) {
            r = comp_op(b, 0);
        } else if (t == CORRELATION_NAME) {
            r = correlation_name(b, 0);
        } else if (t == DERIVED_COLUMN) {
            r = derived_column(b, 0);
        } else if (t == EXISTS_PREDICATE) {
            r = exists_predicate(b, 0);
        } else if (t == FROM_CLAUSE) {
            r = from_clause(b, 0);
        } else if (t == GENERAL_LITERAL) {
            r = general_literal(b, 0);
        } else if (t == GENERAL_SET_FUNCTION) {
            r = general_set_function(b, 0);
        } else if (t == GROUP_BY_CLAUSE) {
            r = group_by_clause(b, 0);
        } else if (t == GROUPING_COLUMN_REFERENCE) {
            r = grouping_column_reference(b, 0);
        } else if (t == GROUPING_COLUMN_REFERENCE_LIST) {
            r = grouping_column_reference_list(b, 0);
        } else if (t == GROUPING_ELEMENT) {
            r = grouping_element(b, 0);
        } else if (t == GROUPING_ELEMENT_LIST) {
            r = grouping_element_list(b, 0);
        } else if (t == JOIN_CONDITION) {
            r = join_condition(b, 0);
        } else if (t == JOIN_SPECIFICATION) {
            r = join_specification(b, 0);
        } else if (t == JOIN_TYPE) {
            r = join_type(b, 0);
        } else if (t == JOINED_TABLE) {
            r = joined_table(b, 0);
        } else if (t == LANG) {
            r = lang(b, 0);
        } else if (t == LIKE_PREDICATE) {
            r = like_predicate(b, 0);
        } else if (t == NULL_ORDERING) {
            r = null_ordering(b, 0);
        } else if (t == NULL_PREDICATE) {
            r = null_predicate(b, 0);
        } else if (t == ORDER_BY_CLAUSE) {
            r = order_by_clause(b, 0);
        } else if (t == ORDERING_SPECIFICATION) {
            r = ordering_specification(b, 0);
        } else if (t == ORDINARY_GROUPING_SET) {
            r = ordinary_grouping_set(b, 0);
        } else if (t == PREDICATE) {
            r = predicate(b, 0);
        } else if (t == QUERY_SPECIFICATION) {
            r = query_specification(b, 0);
        } else if (t == ROW_VALUE_PREDICAND) {
            r = row_value_predicand(b, 0);
        } else if (t == SEARCH_CONDITION) {
            r = search_condition(b, 0);
        } else if (t == SELECT_LIST) {
            r = select_list(b, 0);
        } else if (t == SELECT_SUBLIST) {
            r = select_sublist(b, 0);
        } else if (t == SET_FUNCTION_TYPE) {
            r = set_function_type(b, 0);
        } else if (t == SET_QUANTIFIER) {
            r = set_quantifier(b, 0);
        } else if (t == SORT_KEY) {
            r = sort_key(b, 0);
        } else if (t == SORT_SPECIFICATION) {
            r = sort_specification(b, 0);
        } else if (t == SORT_SPECIFICATION_LIST) {
            r = sort_specification_list(b, 0);
        } else if (t == STRING_VALUE_EXPRESSION) {
            r = string_value_expression(b, 0);
        } else if (t == STRING_VALUE_FUNCTION) {
            r = string_value_function(b, 0);
        } else if (t == SUBQUERY) {
            r = subquery(b, 0);
        } else if (t == TABLE_EXPRESSION) {
            r = table_expression(b, 0);
        } else if (t == TABLE_NAME) {
            r = table_name(b, 0);
        } else if (t == TABLE_PRIMARY) {
            r = table_primary(b, 0);
        } else if (t == TABLE_REFERENCE) {
            r = table_reference(b, 0);
        } else if (t == TABLE_REFERENCE_LIST) {
            r = table_reference_list(b, 0);
        } else if (t == TABLE_SUBQUERY) {
            r = table_subquery(b, 0);
        } else if (t == TRUTH_VALUE) {
            r = truth_value(b, 0);
        } else if (t == VALUE_EXPRESSION) {
            r = value_expression(b, 0);
        } else if (t == WHERE_CLAUSE) {
            r = where_clause(b, 0);
        } else {
            r = parse_root_(t, b, 0);
        }
        exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
    }

    protected boolean parse_root_(IElementType t, PsiBuilder b, int l) {
        return flexibleSearchFile(b, l + 1);
    }

    /* ********************************************************** */
    // COUNT LEFT_PAREN ASTERISK RIGHT_PAREN
    // 	|	general_set_function
    public static boolean aggregate_function(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "aggregate_function")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, AGGREGATE_FUNCTION, "<aggregate function>");
        r = parseTokens(b, 0, COUNT, LEFT_PAREN, ASTERISK, RIGHT_PAREN);
        if (!r) {
            r = general_set_function(b, l + 1);
        }
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // row_value_predicand [ NOT ] BETWEEN row_value_predicand AND row_value_predicand
    public static boolean between_predicate(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "between_predicate")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, BETWEEN_PREDICATE, "<between predicate>");
        r = row_value_predicand(b, l + 1);
        r = r && between_predicate_1(b, l + 1);
        r = r && consumeToken(b, BETWEEN);
        r = r && row_value_predicand(b, l + 1);
        r = r && consumeToken(b, AND);
        r = r && row_value_predicand(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // [ NOT ]
    private static boolean between_predicate_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "between_predicate_1")) {
            return false;
        }
        consumeToken(b, NOT);
        return true;
    }

    /* ********************************************************** */
    // [ NOT ] boolean_test
    public static boolean boolean_factor(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "boolean_factor")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, BOOLEAN_FACTOR, "<boolean factor>");
        r = boolean_factor_0(b, l + 1);
        r = r && boolean_test(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // [ NOT ]
    private static boolean boolean_factor_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "boolean_factor_0")) {
            return false;
        }
        consumeToken(b, NOT);
        return true;
    }

    /* ********************************************************** */
    // parenthesized_boolean_value_expression | nonparenthesized_value_expression_primary
    public static boolean boolean_predicand(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "boolean_predicand")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, BOOLEAN_PREDICAND, "<boolean predicand>");
        r = parenthesized_boolean_value_expression(b, l + 1);
        if (!r) {
            r = nonparenthesized_value_expression_primary(b, l + 1);
        }
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // boolean_predicand | predicate
    public static boolean boolean_primary(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "boolean_primary")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, BOOLEAN_PRIMARY, "<boolean primary>");
        r = boolean_predicand(b, l + 1);
        if (!r) {
            r = predicate(b, l + 1);
        }
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // boolean_factor (((AND|OR) boolean_term)*)?
    public static boolean boolean_term(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "boolean_term")) {
            return false;
        }
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, BOOLEAN_TERM, "<boolean term>");
        r = boolean_factor(b, l + 1);
        p = r; // pin = 1
        r = r && boolean_term_1(b, l + 1);
        exit_section_(b, l, m, r, p, null);
        return r || p;
    }

    // (((AND|OR) boolean_term)*)?
    private static boolean boolean_term_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "boolean_term_1")) {
            return false;
        }
        boolean_term_1_0(b, l + 1);
        return true;
    }

    // ((AND|OR) boolean_term)*
    private static boolean boolean_term_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "boolean_term_1_0")) {
            return false;
        }
        int c = current_position_(b);
        while (true) {
            if (!boolean_term_1_0_0(b, l + 1)) {
                break;
            }
            if (!empty_element_parsed_guard_(b, "boolean_term_1_0", c)) {
                break;
            }
            c = current_position_(b);
        }
        return true;
    }

    // (AND|OR) boolean_term
    private static boolean boolean_term_1_0_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "boolean_term_1_0_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = boolean_term_1_0_0_0(b, l + 1);
        r = r && boolean_term(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // AND|OR
    private static boolean boolean_term_1_0_0_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "boolean_term_1_0_0_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, AND);
        if (!r) {
            r = consumeToken(b, OR);
        }
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // boolean_primary  [ IS [ NOT ] truth_value ]
    public static boolean boolean_test(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "boolean_test")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, BOOLEAN_TEST, "<boolean test>");
        r = boolean_primary(b, l + 1);
        r = r && boolean_test_1(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // [ IS [ NOT ] truth_value ]
    private static boolean boolean_test_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "boolean_test_1")) {
            return false;
        }
        boolean_test_1_0(b, l + 1);
        return true;
    }

    // IS [ NOT ] truth_value
    private static boolean boolean_test_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "boolean_test_1_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, IS);
        r = r && boolean_test_1_0_1(b, l + 1);
        r = r && truth_value(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // [ NOT ]
    private static boolean boolean_test_1_0_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "boolean_test_1_0_1")) {
            return false;
        }
        consumeToken(b, NOT);
        return true;
    }

    /* ********************************************************** */
    // boolean_term
    public static boolean boolean_value_expression(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "boolean_value_expression")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, BOOLEAN_VALUE_EXPRESSION, "<boolean value expression>");
        r = boolean_term(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // row_value_predicand [ NOT ] LIKE (character_pattern | value_expression)
    public static boolean character_like_predicate(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "character_like_predicate")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, CHARACTER_LIKE_PREDICATE, "<character like predicate>");
        r = row_value_predicand(b, l + 1);
        r = r && character_like_predicate_1(b, l + 1);
        r = r && consumeToken(b, LIKE);
        r = r && character_like_predicate_3(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // [ NOT ]
    private static boolean character_like_predicate_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "character_like_predicate_1")) {
            return false;
        }
        consumeToken(b, NOT);
        return true;
    }

    // character_pattern | value_expression
    private static boolean character_like_predicate_3(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "character_like_predicate_3")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = character_pattern(b, l + 1);
        if (!r) {
            r = value_expression(b, l + 1);
        }
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // STRING | string_value_function
    public static boolean character_pattern(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "character_pattern")) {
            return false;
        }
        if (!nextTokenIs(b, "<character pattern>", CONCAT, STRING)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, CHARACTER_PATTERN, "<character pattern>");
        r = consumeToken(b, STRING);
        if (!r) {
            r = string_value_function(b, l + 1);
        }
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // STRING
    public static boolean character_string_literal(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "character_string_literal")) {
            return false;
        }
        if (!nextTokenIs(b, STRING)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, STRING);
        exit_section_(b, m, CHARACTER_STRING_LITERAL, r);
        return r;
    }

    /* ********************************************************** */
    // CONCAT LEFT_PAREN string_value_expression COMMA  (character_substring_function | string_value_expression) RIGHT_PAREN
    public static boolean character_substring_function(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "character_substring_function")) {
            return false;
        }
        if (!nextTokenIs(b, CONCAT)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, CONCAT, LEFT_PAREN);
        r = r && string_value_expression(b, l + 1);
        r = r && consumeToken(b, COMMA);
        r = r && character_substring_function_4(b, l + 1);
        r = r && consumeToken(b, RIGHT_PAREN);
        exit_section_(b, m, CHARACTER_SUBSTRING_FUNCTION, r);
        return r;
    }

    // character_substring_function | string_value_expression
    private static boolean character_substring_function_4(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "character_substring_function_4")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = character_substring_function(b, l + 1);
        if (!r) {
            r = string_value_expression(b, l + 1);
        }
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // character_substring_function
    public static boolean character_value_function(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "character_value_function")) {
            return false;
        }
        if (!nextTokenIs(b, CONCAT)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = character_substring_function(b, l + 1);
        exit_section_(b, m, CHARACTER_VALUE_FUNCTION, r);
        return r;
    }

    /* ********************************************************** */
    // identifier_chain
    public static boolean column_reference(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "column_reference")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, COLUMN_REFERENCE, "<column reference>");
        r = identifier_chain(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // string_value_expression
    public static boolean common_value_expression(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "common_value_expression")) {
            return false;
        }
        if (!nextTokenIs(b, STRING)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = string_value_expression(b, l + 1);
        exit_section_(b, m, COMMON_VALUE_EXPRESSION, r);
        return r;
    }

    /* ********************************************************** */
    // EQUALS_OPERATOR
    // 	|	GREATER_THAN_OPERATOR
    // 	|	NOT_EQUALS_OPERATOR
    // 	|	LESS_THAN_OPERATOR
    // 	|	LESS_THAN_OR_EQUALS_OPERATOR
    // 	|	GREATER_THAN_OR_EQUALS_OPERATOR
    public static boolean comp_op(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "comp_op")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, COMP_OP, "<comp op>");
        r = consumeToken(b, EQUALS_OPERATOR);
        if (!r) {
            r = consumeToken(b, GREATER_THAN_OPERATOR);
        }
        if (!r) {
            r = consumeToken(b, NOT_EQUALS_OPERATOR);
        }
        if (!r) {
            r = consumeToken(b, LESS_THAN_OPERATOR);
        }
        if (!r) {
            r = consumeToken(b, LESS_THAN_OR_EQUALS_OPERATOR);
        }
        if (!r) {
            r = consumeToken(b, GREATER_THAN_OR_EQUALS_OPERATOR);
        }
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // row_value_predicand comp_op row_value_predicand
    static boolean comparison_predicate(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "comparison_predicate")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = row_value_predicand(b, l + 1);
        r = r && comp_op(b, l + 1);
        r = r && row_value_predicand(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // AVG | MAX | MIN | SUM
    // 	|	EVERY | ANY | SOME
    // 	|	COUNT
    static boolean computational_operation(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "computational_operation")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, AVG);
        if (!r) {
            r = consumeToken(b, MAX);
        }
        if (!r) {
            r = consumeToken(b, MIN);
        }
        if (!r) {
            r = consumeToken(b, SUM);
        }
        if (!r) {
            r = consumeToken(b, EVERY);
        }
        if (!r) {
            r = consumeToken(b, ANY);
        }
        if (!r) {
            r = consumeToken(b, SOME);
        }
        if (!r) {
            r = consumeToken(b, COUNT);
        }
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // IDENTIFIER
    public static boolean correlation_name(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "correlation_name")) {
            return false;
        }
        if (!nextTokenIs(b, IDENTIFIER)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, IDENTIFIER);
        exit_section_(b, m, CORRELATION_NAME, r);
        return r;
    }

    /* ********************************************************** */
    // value_expression [ AS  correlation_name ]
    public static boolean derived_column(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "derived_column")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, DERIVED_COLUMN, "<derived column>");
        r = value_expression(b, l + 1);
        r = r && derived_column_1(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // [ AS  correlation_name ]
    private static boolean derived_column_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "derived_column_1")) {
            return false;
        }
        derived_column_1_0(b, l + 1);
        return true;
    }

    // AS  correlation_name
    private static boolean derived_column_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "derived_column_1_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, AS);
        r = r && correlation_name(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // SEMICOLON | <<eof>>
    static boolean empty_statement(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "empty_statement")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, SEMICOLON);
        if (!r) {
            r = eof(b, l + 1);
        }
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // EXISTS table_subquery
    public static boolean exists_predicate(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "exists_predicate")) {
            return false;
        }
        if (!nextTokenIs(b, EXISTS)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, EXISTS);
        r = r && table_subquery(b, l + 1);
        exit_section_(b, m, EXISTS_PREDICATE, r);
        return r;
    }

    /* ********************************************************** */
    // !(SEMICOLON | <<eof>> )
    static boolean expressionRecoverWhile(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "expressionRecoverWhile")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NOT_);
        r = !expressionRecoverWhile_0(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // SEMICOLON | <<eof>>
    private static boolean expressionRecoverWhile_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "expressionRecoverWhile_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, SEMICOLON);
        if (!r) {
            r = eof(b, l + 1);
        }
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // statement*
    static boolean flexibleSearchFile(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "flexibleSearchFile")) {
            return false;
        }
        int c = current_position_(b);
        while (true) {
            if (!statement(b, l + 1)) {
                break;
            }
            if (!empty_element_parsed_guard_(b, "flexibleSearchFile", c)) {
                break;
            }
            c = current_position_(b);
        }
        return true;
    }

    /* ********************************************************** */
    // FROM (LEFT_BRACE table_reference_list RIGHT_BRACE | subquery )
    public static boolean from_clause(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "from_clause")) {
            return false;
        }
        if (!nextTokenIs(b, FROM)) {
            return false;
        }
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, FROM_CLAUSE, null);
        r = consumeToken(b, FROM);
        p = r; // pin = 1
        r = r && from_clause_1(b, l + 1);
        exit_section_(b, l, m, r, p, null);
        return r || p;
    }

    // LEFT_BRACE table_reference_list RIGHT_BRACE | subquery
    private static boolean from_clause_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "from_clause_1")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = from_clause_1_0(b, l + 1);
        if (!r) {
            r = subquery(b, l + 1);
        }
        exit_section_(b, m, null, r);
        return r;
    }

    // LEFT_BRACE table_reference_list RIGHT_BRACE
    private static boolean from_clause_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "from_clause_1_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, LEFT_BRACE);
        r = r && table_reference_list(b, l + 1);
        r = r && consumeToken(b, RIGHT_BRACE);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // character_string_literal
    public static boolean general_literal(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "general_literal")) {
            return false;
        }
        if (!nextTokenIs(b, STRING)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = character_string_literal(b, l + 1);
        exit_section_(b, m, GENERAL_LITERAL, r);
        return r;
    }

    /* ********************************************************** */
    // set_function_type LEFT_PAREN [ set_quantifier ] value_expression RIGHT_PAREN [ [ AS ] correlation_name ]
    public static boolean general_set_function(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "general_set_function")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, GENERAL_SET_FUNCTION, "<general set function>");
        r = set_function_type(b, l + 1);
        r = r && consumeToken(b, LEFT_PAREN);
        r = r && general_set_function_2(b, l + 1);
        r = r && value_expression(b, l + 1);
        r = r && consumeToken(b, RIGHT_PAREN);
        r = r && general_set_function_5(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // [ set_quantifier ]
    private static boolean general_set_function_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "general_set_function_2")) {
            return false;
        }
        set_quantifier(b, l + 1);
        return true;
    }

    // [ [ AS ] correlation_name ]
    private static boolean general_set_function_5(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "general_set_function_5")) {
            return false;
        }
        general_set_function_5_0(b, l + 1);
        return true;
    }

    // [ AS ] correlation_name
    private static boolean general_set_function_5_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "general_set_function_5_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = general_set_function_5_0_0(b, l + 1);
        r = r && correlation_name(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // [ AS ]
    private static boolean general_set_function_5_0_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "general_set_function_5_0_0")) {
            return false;
        }
        consumeToken(b, AS);
        return true;
    }

    /* ********************************************************** */
    // GROUP BY [ set_quantifier ] grouping_element_list
    public static boolean group_by_clause(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "group_by_clause")) {
            return false;
        }
        if (!nextTokenIs(b, GROUP)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, GROUP, BY);
        r = r && group_by_clause_2(b, l + 1);
        r = r && grouping_element_list(b, l + 1);
        exit_section_(b, m, GROUP_BY_CLAUSE, r);
        return r;
    }

    // [ set_quantifier ]
    private static boolean group_by_clause_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "group_by_clause_2")) {
            return false;
        }
        set_quantifier(b, l + 1);
        return true;
    }

    /* ********************************************************** */
    // value_expression
    public static boolean grouping_column_reference(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "grouping_column_reference")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, GROUPING_COLUMN_REFERENCE, "<grouping column reference>");
        r = value_expression(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // grouping_column_reference [ { COMMA grouping_column_reference }* ]
    public static boolean grouping_column_reference_list(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "grouping_column_reference_list")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, GROUPING_COLUMN_REFERENCE_LIST, "<grouping column reference list>");
        r = grouping_column_reference(b, l + 1);
        r = r && grouping_column_reference_list_1(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // [ { COMMA grouping_column_reference }* ]
    private static boolean grouping_column_reference_list_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "grouping_column_reference_list_1")) {
            return false;
        }
        grouping_column_reference_list_1_0(b, l + 1);
        return true;
    }

    // { COMMA grouping_column_reference }*
    private static boolean grouping_column_reference_list_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "grouping_column_reference_list_1_0")) {
            return false;
        }
        int c = current_position_(b);
        while (true) {
            if (!grouping_column_reference_list_1_0_0(b, l + 1)) {
                break;
            }
            if (!empty_element_parsed_guard_(b, "grouping_column_reference_list_1_0", c)) {
                break;
            }
            c = current_position_(b);
        }
        return true;
    }

    // COMMA grouping_column_reference
    private static boolean grouping_column_reference_list_1_0_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "grouping_column_reference_list_1_0_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, COMMA);
        r = r && grouping_column_reference(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // ordinary_grouping_set
    public static boolean grouping_element(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "grouping_element")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, GROUPING_ELEMENT, "<grouping element>");
        r = ordinary_grouping_set(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // grouping_element [ { COMMA grouping_element }* ]
    public static boolean grouping_element_list(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "grouping_element_list")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, GROUPING_ELEMENT_LIST, "<grouping element list>");
        r = grouping_element(b, l + 1);
        r = r && grouping_element_list_1(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // [ { COMMA grouping_element }* ]
    private static boolean grouping_element_list_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "grouping_element_list_1")) {
            return false;
        }
        grouping_element_list_1_0(b, l + 1);
        return true;
    }

    // { COMMA grouping_element }*
    private static boolean grouping_element_list_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "grouping_element_list_1_0")) {
            return false;
        }
        int c = current_position_(b);
        while (true) {
            if (!grouping_element_list_1_0_0(b, l + 1)) {
                break;
            }
            if (!empty_element_parsed_guard_(b, "grouping_element_list_1_0", c)) {
                break;
            }
            c = current_position_(b);
        }
        return true;
    }

    // COMMA grouping_element
    private static boolean grouping_element_list_1_0_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "grouping_element_list_1_0_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, COMMA);
        r = r && grouping_element(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // (IDENTIFIER | TABLE_NAME_IDENTIFIER | COLUMN_REFERENCE_IDENTIFIER) [ { (DOT|COLON) (IDENTIFIER | COLUMN_REFERENCE_IDENTIFIER)  }* ]
    static boolean identifier_chain(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "identifier_chain")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = identifier_chain_0(b, l + 1);
        r = r && identifier_chain_1(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // IDENTIFIER | TABLE_NAME_IDENTIFIER | COLUMN_REFERENCE_IDENTIFIER
    private static boolean identifier_chain_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "identifier_chain_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, IDENTIFIER);
        if (!r) {
            r = consumeToken(b, TABLE_NAME_IDENTIFIER);
        }
        if (!r) {
            r = consumeToken(b, COLUMN_REFERENCE_IDENTIFIER);
        }
        exit_section_(b, m, null, r);
        return r;
    }

    // [ { (DOT|COLON) (IDENTIFIER | COLUMN_REFERENCE_IDENTIFIER)  }* ]
    private static boolean identifier_chain_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "identifier_chain_1")) {
            return false;
        }
        identifier_chain_1_0(b, l + 1);
        return true;
    }

    // { (DOT|COLON) (IDENTIFIER | COLUMN_REFERENCE_IDENTIFIER)  }*
    private static boolean identifier_chain_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "identifier_chain_1_0")) {
            return false;
        }
        int c = current_position_(b);
        while (true) {
            if (!identifier_chain_1_0_0(b, l + 1)) {
                break;
            }
            if (!empty_element_parsed_guard_(b, "identifier_chain_1_0", c)) {
                break;
            }
            c = current_position_(b);
        }
        return true;
    }

    // (DOT|COLON) (IDENTIFIER | COLUMN_REFERENCE_IDENTIFIER)
    private static boolean identifier_chain_1_0_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "identifier_chain_1_0_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = identifier_chain_1_0_0_0(b, l + 1);
        r = r && identifier_chain_1_0_0_1(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // DOT|COLON
    private static boolean identifier_chain_1_0_0_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "identifier_chain_1_0_0_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, DOT);
        if (!r) {
            r = consumeToken(b, COLON);
        }
        exit_section_(b, m, null, r);
        return r;
    }

    // IDENTIFIER | COLUMN_REFERENCE_IDENTIFIER
    private static boolean identifier_chain_1_0_0_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "identifier_chain_1_0_0_1")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, IDENTIFIER);
        if (!r) {
            r = consumeToken(b, COLUMN_REFERENCE_IDENTIFIER);
        }
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // ON search_condition
    public static boolean join_condition(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "join_condition")) {
            return false;
        }
        if (!nextTokenIs(b, ON)) {
            return false;
        }
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, JOIN_CONDITION, null);
        r = consumeToken(b, ON);
        p = r; // pin = 1
        r = r && search_condition(b, l + 1);
        exit_section_(b, l, m, r, p, null);
        return r || p;
    }

    /* ********************************************************** */
    // join_condition
    public static boolean join_specification(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "join_specification")) {
            return false;
        }
        if (!nextTokenIs(b, ON)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = join_condition(b, l + 1);
        exit_section_(b, m, JOIN_SPECIFICATION, r);
        return r;
    }

    /* ********************************************************** */
    // LEFT |
    public static boolean join_type(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "join_type")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, JOIN_TYPE, "<join type>");
        r = consumeToken(b, LEFT);
        if (!r) {
            r = consumeToken(b, JOIN_TYPE_1_0);
        }
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // [(table_primary [joined_table] | joined_table)] [ join_type ] JOIN table_reference join_specification
    public static boolean joined_table(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "joined_table")) {
            return false;
        }
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, JOINED_TABLE, "<joined table>");
        r = joined_table_0(b, l + 1);
        r = r && joined_table_1(b, l + 1);
        r = r && consumeToken(b, JOIN);
        p = r; // pin = 3
        r = r && report_error_(b, table_reference(b, l + 1));
        r = p && join_specification(b, l + 1) && r;
        exit_section_(b, l, m, r, p, null);
        return r || p;
    }

    // [(table_primary [joined_table] | joined_table)]
    private static boolean joined_table_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "joined_table_0")) {
            return false;
        }
        joined_table_0_0(b, l + 1);
        return true;
    }

    // table_primary [joined_table] | joined_table
    private static boolean joined_table_0_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "joined_table_0_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = joined_table_0_0_0(b, l + 1);
        if (!r) {
            r = joined_table(b, l + 1);
        }
        exit_section_(b, m, null, r);
        return r;
    }

    // table_primary [joined_table]
    private static boolean joined_table_0_0_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "joined_table_0_0_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = table_primary(b, l + 1);
        r = r && joined_table_0_0_0_1(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // [joined_table]
    private static boolean joined_table_0_0_0_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "joined_table_0_0_0_1")) {
            return false;
        }
        joined_table(b, l + 1);
        return true;
    }

    // [ join_type ]
    private static boolean joined_table_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "joined_table_1")) {
            return false;
        }
        join_type(b, l + 1);
        return true;
    }

    /* ********************************************************** */
    // IDENTIFIER
    public static boolean lang(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "lang")) {
            return false;
        }
        if (!nextTokenIs(b, IDENTIFIER)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, IDENTIFIER);
        exit_section_(b, m, LANG, r);
        return r;
    }

    /* ********************************************************** */
    // character_like_predicate
    public static boolean like_predicate(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "like_predicate")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, LIKE_PREDICATE, "<like predicate>");
        r = character_like_predicate(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // column_reference
    static boolean nonparenthesized_value_expression_primary(PsiBuilder b, int l) {
        return column_reference(b, l + 1);
    }

    /* ********************************************************** */
    // NULLS FIRST | NULLS LAST
    public static boolean null_ordering(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "null_ordering")) {
            return false;
        }
        if (!nextTokenIs(b, NULLS)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = parseTokens(b, 0, NULLS, FIRST);
        if (!r) {
            r = parseTokens(b, 0, NULLS, LAST);
        }
        exit_section_(b, m, NULL_ORDERING, r);
        return r;
    }

    /* ********************************************************** */
    // row_value_predicand IS [ NOT ] NULL
    public static boolean null_predicate(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "null_predicate")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, NULL_PREDICATE, "<null predicate>");
        r = row_value_predicand(b, l + 1);
        r = r && consumeToken(b, IS);
        r = r && null_predicate_2(b, l + 1);
        r = r && consumeToken(b, NULL);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // [ NOT ]
    private static boolean null_predicate_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "null_predicate_2")) {
            return false;
        }
        consumeToken(b, NOT);
        return true;
    }

    /* ********************************************************** */
    // !( SEMICOLON | query_specification | RIGHT_DOUBLE_BRACE)
    static boolean orderByClauseRecoverWhile(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "orderByClauseRecoverWhile")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NOT_);
        r = !orderByClauseRecoverWhile_0(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // SEMICOLON | query_specification | RIGHT_DOUBLE_BRACE
    private static boolean orderByClauseRecoverWhile_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "orderByClauseRecoverWhile_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, SEMICOLON);
        if (!r) {
            r = query_specification(b, l + 1);
        }
        if (!r) {
            r = consumeToken(b, RIGHT_DOUBLE_BRACE);
        }
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // ORDER BY sort_specification_list
    public static boolean order_by_clause(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "order_by_clause")) {
            return false;
        }
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, ORDER_BY_CLAUSE, "<order by clause>");
        r = consumeTokens(b, 1, ORDER, BY);
        p = r; // pin = 1
        r = r && sort_specification_list(b, l + 1);
        exit_section_(b, l, m, r, p, orderByClauseRecoverWhile_parser_);
        return r || p;
    }

    /* ********************************************************** */
    // ASC | DESC
    public static boolean ordering_specification(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ordering_specification")) {
            return false;
        }
        if (!nextTokenIs(b, "<ordering specification>", ASC, DESC)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, ORDERING_SPECIFICATION, "<ordering specification>");
        r = consumeToken(b, ASC);
        if (!r) {
            r = consumeToken(b, DESC);
        }
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // grouping_column_reference | LEFT_PAREN grouping_column_reference_list RIGHT_PAREN
    public static boolean ordinary_grouping_set(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ordinary_grouping_set")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, ORDINARY_GROUPING_SET, "<ordinary grouping set>");
        r = grouping_column_reference(b, l + 1);
        if (!r) {
            r = ordinary_grouping_set_1(b, l + 1);
        }
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // LEFT_PAREN grouping_column_reference_list RIGHT_PAREN
    private static boolean ordinary_grouping_set_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "ordinary_grouping_set_1")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, LEFT_PAREN);
        r = r && grouping_column_reference_list(b, l + 1);
        r = r && consumeToken(b, RIGHT_PAREN);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // LEFT_PAREN boolean_value_expression RIGHT_PAREN
    static boolean parenthesized_boolean_value_expression(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parenthesized_boolean_value_expression")) {
            return false;
        }
        if (!nextTokenIs(b, LEFT_PAREN)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, LEFT_PAREN);
        r = r && boolean_value_expression(b, l + 1);
        r = r && consumeToken(b, RIGHT_PAREN);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // comparison_predicate
    // 	|	between_predicate
    // 	|	like_predicate
    // 	|	null_predicate
    // 	|	exists_predicate
    public static boolean predicate(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "predicate")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, PREDICATE, "<predicate>");
        r = comparison_predicate(b, l + 1);
        if (!r) {
            r = between_predicate(b, l + 1);
        }
        if (!r) {
            r = like_predicate(b, l + 1);
        }
        if (!r) {
            r = null_predicate(b, l + 1);
        }
        if (!r) {
            r = exists_predicate(b, l + 1);
        }
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // SELECT [ set_quantifier ] select_list table_expression
    public static boolean query_specification(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "query_specification")) {
            return false;
        }
        if (!nextTokenIs(b, SELECT)) {
            return false;
        }
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, QUERY_SPECIFICATION, null);
        r = consumeToken(b, SELECT);
        p = r; // pin = 1
        r = r && report_error_(b, query_specification_1(b, l + 1));
        r = p && report_error_(b, select_list(b, l + 1)) && r;
        r = p && table_expression(b, l + 1) && r;
        exit_section_(b, l, m, r, p, null);
        return r || p;
    }

    // [ set_quantifier ]
    private static boolean query_specification_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "query_specification_1")) {
            return false;
        }
        set_quantifier(b, l + 1);
        return true;
    }

    /* ********************************************************** */
    // value_expression | common_value_expression
    public static boolean row_value_predicand(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "row_value_predicand")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, ROW_VALUE_PREDICAND, "<row value predicand>");
        r = value_expression(b, l + 1);
        if (!r) {
            r = common_value_expression(b, l + 1);
        }
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // boolean_value_expression
    public static boolean search_condition(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "search_condition")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, SEARCH_CONDITION, "<search condition>");
        r = boolean_value_expression(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // ASTERISK | select_sublist [ ( COMMA select_sublist )* ]
    public static boolean select_list(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "select_list")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, SELECT_LIST, "<select list>");
        r = consumeToken(b, ASTERISK);
        if (!r) {
            r = select_list_1(b, l + 1);
        }
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // select_sublist [ ( COMMA select_sublist )* ]
    private static boolean select_list_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "select_list_1")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = select_sublist(b, l + 1);
        r = r && select_list_1_1(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // [ ( COMMA select_sublist )* ]
    private static boolean select_list_1_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "select_list_1_1")) {
            return false;
        }
        select_list_1_1_0(b, l + 1);
        return true;
    }

    // ( COMMA select_sublist )*
    private static boolean select_list_1_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "select_list_1_1_0")) {
            return false;
        }
        int c = current_position_(b);
        while (true) {
            if (!select_list_1_1_0_0(b, l + 1)) {
                break;
            }
            if (!empty_element_parsed_guard_(b, "select_list_1_1_0", c)) {
                break;
            }
            c = current_position_(b);
        }
        return true;
    }

    // COMMA select_sublist
    private static boolean select_list_1_1_0_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "select_list_1_1_0_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, COMMA);
        r = r && select_sublist(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // derived_column | (aggregate_function [COMMA aggregate_function*])
    public static boolean select_sublist(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "select_sublist")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, SELECT_SUBLIST, "<select sublist>");
        r = derived_column(b, l + 1);
        if (!r) {
            r = select_sublist_1(b, l + 1);
        }
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // aggregate_function [COMMA aggregate_function*]
    private static boolean select_sublist_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "select_sublist_1")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = aggregate_function(b, l + 1);
        r = r && select_sublist_1_1(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // [COMMA aggregate_function*]
    private static boolean select_sublist_1_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "select_sublist_1_1")) {
            return false;
        }
        select_sublist_1_1_0(b, l + 1);
        return true;
    }

    // COMMA aggregate_function*
    private static boolean select_sublist_1_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "select_sublist_1_1_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, COMMA);
        r = r && select_sublist_1_1_0_1(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // aggregate_function*
    private static boolean select_sublist_1_1_0_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "select_sublist_1_1_0_1")) {
            return false;
        }
        int c = current_position_(b);
        while (true) {
            if (!aggregate_function(b, l + 1)) {
                break;
            }
            if (!empty_element_parsed_guard_(b, "select_sublist_1_1_0_1", c)) {
                break;
            }
            c = current_position_(b);
        }
        return true;
    }

    /* ********************************************************** */
    // computational_operation
    public static boolean set_function_type(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "set_function_type")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, SET_FUNCTION_TYPE, "<set function type>");
        r = computational_operation(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // DISTINCT | ALL
    public static boolean set_quantifier(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "set_quantifier")) {
            return false;
        }
        if (!nextTokenIs(b, "<set quantifier>", ALL, DISTINCT)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, SET_QUANTIFIER, "<set quantifier>");
        r = consumeToken(b, DISTINCT);
        if (!r) {
            r = consumeToken(b, ALL);
        }
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // value_expression
    public static boolean sort_key(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "sort_key")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, SORT_KEY, "<sort key>");
        r = value_expression(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // sort_key [ ordering_specification ] [ null_ordering ]
    public static boolean sort_specification(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "sort_specification")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, SORT_SPECIFICATION, "<sort specification>");
        r = sort_key(b, l + 1);
        r = r && sort_specification_1(b, l + 1);
        r = r && sort_specification_2(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // [ ordering_specification ]
    private static boolean sort_specification_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "sort_specification_1")) {
            return false;
        }
        ordering_specification(b, l + 1);
        return true;
    }

    // [ null_ordering ]
    private static boolean sort_specification_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "sort_specification_2")) {
            return false;
        }
        null_ordering(b, l + 1);
        return true;
    }

    /* ********************************************************** */
    // sort_specification [ { COMMA sort_specification }* ]
    public static boolean sort_specification_list(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "sort_specification_list")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, SORT_SPECIFICATION_LIST, "<sort specification list>");
        r = sort_specification(b, l + 1);
        r = r && sort_specification_list_1(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // [ { COMMA sort_specification }* ]
    private static boolean sort_specification_list_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "sort_specification_list_1")) {
            return false;
        }
        sort_specification_list_1_0(b, l + 1);
        return true;
    }

    // { COMMA sort_specification }*
    private static boolean sort_specification_list_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "sort_specification_list_1_0")) {
            return false;
        }
        int c = current_position_(b);
        while (true) {
            if (!sort_specification_list_1_0_0(b, l + 1)) {
                break;
            }
            if (!empty_element_parsed_guard_(b, "sort_specification_list_1_0", c)) {
                break;
            }
            c = current_position_(b);
        }
        return true;
    }

    // COMMA sort_specification
    private static boolean sort_specification_list_1_0_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "sort_specification_list_1_0_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, COMMA);
        r = r && sort_specification(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // !<<eof>> (query_specification empty_statement?)* empty_statement?
    static boolean statement(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "statement")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_);
        r = statement_0(b, l + 1);
        r = r && statement_1(b, l + 1);
        r = r && statement_2(b, l + 1);
        exit_section_(b, l, m, r, false, expressionRecoverWhile_parser_);
        return r;
    }

    // !<<eof>>
    private static boolean statement_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "statement_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NOT_);
        r = !eof(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // (query_specification empty_statement?)*
    private static boolean statement_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "statement_1")) {
            return false;
        }
        int c = current_position_(b);
        while (true) {
            if (!statement_1_0(b, l + 1)) {
                break;
            }
            if (!empty_element_parsed_guard_(b, "statement_1", c)) {
                break;
            }
            c = current_position_(b);
        }
        return true;
    }

    // query_specification empty_statement?
    private static boolean statement_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "statement_1_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = query_specification(b, l + 1);
        r = r && statement_1_0_1(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // empty_statement?
    private static boolean statement_1_0_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "statement_1_0_1")) {
            return false;
        }
        empty_statement(b, l + 1);
        return true;
    }

    // empty_statement?
    private static boolean statement_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "statement_2")) {
            return false;
        }
        empty_statement(b, l + 1);
        return true;
    }

    /* ********************************************************** */
    // general_literal
    public static boolean string_value_expression(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "string_value_expression")) {
            return false;
        }
        if (!nextTokenIs(b, STRING)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = general_literal(b, l + 1);
        exit_section_(b, m, STRING_VALUE_EXPRESSION, r);
        return r;
    }

    /* ********************************************************** */
    // character_value_function
    public static boolean string_value_function(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "string_value_function")) {
            return false;
        }
        if (!nextTokenIs(b, CONCAT)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = character_value_function(b, l + 1);
        exit_section_(b, m, STRING_VALUE_FUNCTION, r);
        return r;
    }

    /* ********************************************************** */
    // LEFT_PAREN? LEFT_DOUBLE_BRACE query_specification RIGHT_DOUBLE_BRACE [(UNION ALL subquery)*]  RIGHT_PAREN? [ [ AS ] correlation_name ]
    public static boolean subquery(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "subquery")) {
            return false;
        }
        if (!nextTokenIs(b, "<subquery>", LEFT_DOUBLE_BRACE, LEFT_PAREN)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, SUBQUERY, "<subquery>");
        r = subquery_0(b, l + 1);
        r = r && consumeToken(b, LEFT_DOUBLE_BRACE);
        r = r && query_specification(b, l + 1);
        r = r && consumeToken(b, RIGHT_DOUBLE_BRACE);
        r = r && subquery_4(b, l + 1);
        r = r && subquery_5(b, l + 1);
        r = r && subquery_6(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // LEFT_PAREN?
    private static boolean subquery_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "subquery_0")) {
            return false;
        }
        consumeToken(b, LEFT_PAREN);
        return true;
    }

    // [(UNION ALL subquery)*]
    private static boolean subquery_4(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "subquery_4")) {
            return false;
        }
        subquery_4_0(b, l + 1);
        return true;
    }

    // (UNION ALL subquery)*
    private static boolean subquery_4_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "subquery_4_0")) {
            return false;
        }
        int c = current_position_(b);
        while (true) {
            if (!subquery_4_0_0(b, l + 1)) {
                break;
            }
            if (!empty_element_parsed_guard_(b, "subquery_4_0", c)) {
                break;
            }
            c = current_position_(b);
        }
        return true;
    }

    // UNION ALL subquery
    private static boolean subquery_4_0_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "subquery_4_0_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeTokens(b, 0, UNION, ALL);
        r = r && subquery(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // RIGHT_PAREN?
    private static boolean subquery_5(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "subquery_5")) {
            return false;
        }
        consumeToken(b, RIGHT_PAREN);
        return true;
    }

    // [ [ AS ] correlation_name ]
    private static boolean subquery_6(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "subquery_6")) {
            return false;
        }
        subquery_6_0(b, l + 1);
        return true;
    }

    // [ AS ] correlation_name
    private static boolean subquery_6_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "subquery_6_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = subquery_6_0_0(b, l + 1);
        r = r && correlation_name(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // [ AS ]
    private static boolean subquery_6_0_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "subquery_6_0_0")) {
            return false;
        }
        consumeToken(b, AS);
        return true;
    }

    /* ********************************************************** */
    // from_clause
    // 		 where_clause?
    // 		 order_by_clause?
    // 		 group_by_clause?
    public static boolean table_expression(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "table_expression")) {
            return false;
        }
        if (!nextTokenIs(b, FROM)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = from_clause(b, l + 1);
        r = r && table_expression_1(b, l + 1);
        r = r && table_expression_2(b, l + 1);
        r = r && table_expression_3(b, l + 1);
        exit_section_(b, m, TABLE_EXPRESSION, r);
        return r;
    }

    // where_clause?
    private static boolean table_expression_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "table_expression_1")) {
            return false;
        }
        where_clause(b, l + 1);
        return true;
    }

    // order_by_clause?
    private static boolean table_expression_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "table_expression_2")) {
            return false;
        }
        order_by_clause(b, l + 1);
        return true;
    }

    // group_by_clause?
    private static boolean table_expression_3(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "table_expression_3")) {
            return false;
        }
        group_by_clause(b, l + 1);
        return true;
    }

    /* ********************************************************** */
    // TABLE_NAME_IDENTIFIER [ EXCLAMATION_MARK ]
    public static boolean table_name(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "table_name")) {
            return false;
        }
        if (!nextTokenIs(b, TABLE_NAME_IDENTIFIER)) {
            return false;
        }
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, TABLE_NAME, null);
        r = consumeToken(b, TABLE_NAME_IDENTIFIER);
        p = r; // pin = 1
        r = r && table_name_1(b, l + 1);
        exit_section_(b, l, m, r, p, null);
        return r || p;
    }

    // [ EXCLAMATION_MARK ]
    private static boolean table_name_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "table_name_1")) {
            return false;
        }
        consumeToken(b, EXCLAMATION_MARK);
        return true;
    }

    /* ********************************************************** */
    // table_name [ [ AS ] correlation_name ]
    public static boolean table_primary(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "table_primary")) {
            return false;
        }
        if (!nextTokenIs(b, TABLE_NAME_IDENTIFIER)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = table_name(b, l + 1);
        r = r && table_primary_1(b, l + 1);
        exit_section_(b, m, TABLE_PRIMARY, r);
        return r;
    }

    // [ [ AS ] correlation_name ]
    private static boolean table_primary_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "table_primary_1")) {
            return false;
        }
        table_primary_1_0(b, l + 1);
        return true;
    }

    // [ AS ] correlation_name
    private static boolean table_primary_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "table_primary_1_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = table_primary_1_0_0(b, l + 1);
        r = r && correlation_name(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // [ AS ]
    private static boolean table_primary_1_0_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "table_primary_1_0_0")) {
            return false;
        }
        consumeToken(b, AS);
        return true;
    }

    /* ********************************************************** */
    // table_primary [joined_table] | joined_table
    public static boolean table_reference(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "table_reference")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, TABLE_REFERENCE, "<table reference>");
        r = table_reference_0(b, l + 1);
        if (!r) {
            r = joined_table(b, l + 1);
        }
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // table_primary [joined_table]
    private static boolean table_reference_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "table_reference_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = table_primary(b, l + 1);
        r = r && table_reference_0_1(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // [joined_table]
    private static boolean table_reference_0_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "table_reference_0_1")) {
            return false;
        }
        joined_table(b, l + 1);
        return true;
    }

    /* ********************************************************** */
    // table_reference [ { COMMA? table_reference }* ]
    public static boolean table_reference_list(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "table_reference_list")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, TABLE_REFERENCE_LIST, "<table reference list>");
        r = table_reference(b, l + 1);
        r = r && table_reference_list_1(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // [ { COMMA? table_reference }* ]
    private static boolean table_reference_list_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "table_reference_list_1")) {
            return false;
        }
        table_reference_list_1_0(b, l + 1);
        return true;
    }

    // { COMMA? table_reference }*
    private static boolean table_reference_list_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "table_reference_list_1_0")) {
            return false;
        }
        int c = current_position_(b);
        while (true) {
            if (!table_reference_list_1_0_0(b, l + 1)) {
                break;
            }
            if (!empty_element_parsed_guard_(b, "table_reference_list_1_0", c)) {
                break;
            }
            c = current_position_(b);
        }
        return true;
    }

    // COMMA? table_reference
    private static boolean table_reference_list_1_0_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "table_reference_list_1_0_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = table_reference_list_1_0_0_0(b, l + 1);
        r = r && table_reference(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // COMMA?
    private static boolean table_reference_list_1_0_0_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "table_reference_list_1_0_0_0")) {
            return false;
        }
        consumeToken(b, COMMA);
        return true;
    }

    /* ********************************************************** */
    // subquery
    public static boolean table_subquery(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "table_subquery")) {
            return false;
        }
        if (!nextTokenIs(b, "<table subquery>", LEFT_DOUBLE_BRACE, LEFT_PAREN)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, TABLE_SUBQUERY, "<table subquery>");
        r = subquery(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // TRUE | FALSE
    public static boolean truth_value(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "truth_value")) {
            return false;
        }
        if (!nextTokenIs(b, "<truth value>", FALSE, TRUE)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, TRUTH_VALUE, "<truth value>");
        r = consumeToken(b, TRUE);
        if (!r) {
            r = consumeToken(b, FALSE);
        }
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // NUMBER
    //                     | QUESTION_MARK? column_reference
    //                     | LEFT_BRACE column_reference (LEFT_BRACKET lang RIGHT_BRACKET)? COLON? column_reference? RIGHT_BRACE
    public static boolean value_expression(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "value_expression")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, VALUE_EXPRESSION, "<value expression>");
        r = consumeToken(b, NUMBER);
        if (!r) {
            r = value_expression_1(b, l + 1);
        }
        if (!r) {
            r = value_expression_2(b, l + 1);
        }
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // QUESTION_MARK? column_reference
    private static boolean value_expression_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "value_expression_1")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = value_expression_1_0(b, l + 1);
        r = r && column_reference(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // QUESTION_MARK?
    private static boolean value_expression_1_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "value_expression_1_0")) {
            return false;
        }
        consumeToken(b, QUESTION_MARK);
        return true;
    }

    // LEFT_BRACE column_reference (LEFT_BRACKET lang RIGHT_BRACKET)? COLON? column_reference? RIGHT_BRACE
    private static boolean value_expression_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "value_expression_2")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, LEFT_BRACE);
        r = r && column_reference(b, l + 1);
        r = r && value_expression_2_2(b, l + 1);
        r = r && value_expression_2_3(b, l + 1);
        r = r && value_expression_2_4(b, l + 1);
        r = r && consumeToken(b, RIGHT_BRACE);
        exit_section_(b, m, null, r);
        return r;
    }

    // (LEFT_BRACKET lang RIGHT_BRACKET)?
    private static boolean value_expression_2_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "value_expression_2_2")) {
            return false;
        }
        value_expression_2_2_0(b, l + 1);
        return true;
    }

    // LEFT_BRACKET lang RIGHT_BRACKET
    private static boolean value_expression_2_2_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "value_expression_2_2_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, LEFT_BRACKET);
        r = r && lang(b, l + 1);
        r = r && consumeToken(b, RIGHT_BRACKET);
        exit_section_(b, m, null, r);
        return r;
    }

    // COLON?
    private static boolean value_expression_2_3(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "value_expression_2_3")) {
            return false;
        }
        consumeToken(b, COLON);
        return true;
    }

    // column_reference?
    private static boolean value_expression_2_4(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "value_expression_2_4")) {
            return false;
        }
        column_reference(b, l + 1);
        return true;
    }

    /* ********************************************************** */
    // WHERE search_condition
    public static boolean where_clause(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "where_clause")) {
            return false;
        }
        if (!nextTokenIs(b, WHERE)) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, WHERE);
        r = r && search_condition(b, l + 1);
        exit_section_(b, m, WHERE_CLAUSE, r);
        return r;
    }

    final static Parser expressionRecoverWhile_parser_ = new Parser() {

        public boolean parse(PsiBuilder b, int l) {
            return expressionRecoverWhile(b, l + 1);
        }
    };
    final static Parser orderByClauseRecoverWhile_parser_ = new Parser() {

        public boolean parse(PsiBuilder b, int l) {
            return orderByClauseRecoverWhile(b, l + 1);
        }
    };
}
