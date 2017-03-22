// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.*;
import static com.intellij.idea.plugin.hybris.flexibleSearch.utils.FlexibleSearchParserUtils.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

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
    }
    else if (t == BETWEEN_PREDICATE) {
      r = between_predicate(b, 0);
    }
    else if (t == BOOLEAN_FACTOR) {
      r = boolean_factor(b, 0);
    }
    else if (t == BOOLEAN_PREDICAND) {
      r = boolean_predicand(b, 0);
    }
    else if (t == BOOLEAN_PRIMARY) {
      r = boolean_primary(b, 0);
    }
    else if (t == BOOLEAN_TERM) {
      r = boolean_term(b, 0);
    }
    else if (t == BOOLEAN_TEST) {
      r = boolean_test(b, 0);
    }
    else if (t == BOOLEAN_VALUE_EXPRESSION) {
      r = boolean_value_expression(b, 0);
    }
    else if (t == CHARACTER_LIKE_PREDICATE) {
      r = character_like_predicate(b, 0);
    }
    else if (t == CHARACTER_PATTERN) {
      r = character_pattern(b, 0);
    }
    else if (t == CHARACTER_STRING_LITERAL) {
      r = character_string_literal(b, 0);
    }
    else if (t == CHARACTER_SUBSTRING_FUNCTION) {
      r = character_substring_function(b, 0);
    }
    else if (t == CHARACTER_VALUE_FUNCTION) {
      r = character_value_function(b, 0);
    }
    else if (t == COLUMN_REFERENCE) {
      r = column_reference(b, 0);
    }
    else if (t == COMMON_VALUE_EXPRESSION) {
      r = common_value_expression(b, 0);
    }
    else if (t == COMP_OP) {
      r = comp_op(b, 0);
    }
    else if (t == CORRELATION_NAME) {
      r = correlation_name(b, 0);
    }
    else if (t == DERIVED_COLUMN) {
      r = derived_column(b, 0);
    }
    else if (t == EXISTS_PREDICATE) {
      r = exists_predicate(b, 0);
    }
    else if (t == FROM_CLAUSE) {
      r = from_clause(b, 0);
    }
    else if (t == GENERAL_LITERAL) {
      r = general_literal(b, 0);
    }
    else if (t == GENERAL_SET_FUNCTION) {
      r = general_set_function(b, 0);
    }
    else if (t == GROUP_BY_CLAUSE) {
      r = group_by_clause(b, 0);
    }
    else if (t == GROUPING_COLUMN_REFERENCE) {
      r = grouping_column_reference(b, 0);
    }
    else if (t == GROUPING_COLUMN_REFERENCE_LIST) {
      r = grouping_column_reference_list(b, 0);
    }
    else if (t == GROUPING_ELEMENT) {
      r = grouping_element(b, 0);
    }
    else if (t == GROUPING_ELEMENT_LIST) {
      r = grouping_element_list(b, 0);
    }
    else if (t == JOIN_CONDITION) {
      r = join_condition(b, 0);
    }
    else if (t == JOIN_SPECIFICATION) {
      r = join_specification(b, 0);
    }
    else if (t == JOIN_TYPE) {
      r = join_type(b, 0);
    }
    else if (t == JOINED_TABLE) {
      r = joined_table(b, 0);
    }
    else if (t == LANG) {
      r = lang(b, 0);
    }
    else if (t == LIKE_PREDICATE) {
      r = like_predicate(b, 0);
    }
    else if (t == NULL_ORDERING) {
      r = null_ordering(b, 0);
    }
    else if (t == NULL_PREDICATE) {
      r = null_predicate(b, 0);
    }
    else if (t == ORDER_BY_CLAUSE) {
      r = order_by_clause(b, 0);
    }
    else if (t == ORDERING_SPECIFICATION) {
      r = ordering_specification(b, 0);
    }
    else if (t == ORDINARY_GROUPING_SET) {
      r = ordinary_grouping_set(b, 0);
    }
    else if (t == PREDICATE) {
      r = predicate(b, 0);
    }
    else if (t == QUERY_SPECIFICATION) {
      r = query_specification(b, 0);
    }
    else if (t == ROW_VALUE_PREDICAND) {
      r = row_value_predicand(b, 0);
    }
    else if (t == SEARCH_CONDITION) {
      r = search_condition(b, 0);
    }
    else if (t == SELECT_LIST) {
      r = select_list(b, 0);
    }
    else if (t == SELECT_SUBLIST) {
      r = select_sublist(b, 0);
    }
    else if (t == SET_FUNCTION_TYPE) {
      r = set_function_type(b, 0);
    }
    else if (t == SET_QUANTIFIER) {
      r = set_quantifier(b, 0);
    }
    else if (t == SORT_KEY) {
      r = sort_key(b, 0);
    }
    else if (t == SORT_SPECIFICATION) {
      r = sort_specification(b, 0);
    }
    else if (t == SORT_SPECIFICATION_LIST) {
      r = sort_specification_list(b, 0);
    }
    else if (t == STRING_VALUE_EXPRESSION) {
      r = string_value_expression(b, 0);
    }
    else if (t == STRING_VALUE_FUNCTION) {
      r = string_value_function(b, 0);
    }
    else if (t == SUBQUERY) {
      r = subquery(b, 0);
    }
    else if (t == TABLE_EXPRESSION) {
      r = table_expression(b, 0);
    }
    else if (t == TABLE_NAME) {
      r = table_name(b, 0);
    }
    else if (t == TABLE_PRIMARY) {
      r = table_primary(b, 0);
    }
    else if (t == TABLE_REFERENCE) {
      r = table_reference(b, 0);
    }
    else if (t == TABLE_REFERENCE_LIST) {
      r = table_reference_list(b, 0);
    }
    else if (t == TABLE_SUBQUERY) {
      r = table_subquery(b, 0);
    }
    else if (t == TRUTH_VALUE) {
      r = truth_value(b, 0);
    }
    else if (t == VALUE_EXPRESSION) {
      r = value_expression(b, 0);
    }
    else if (t == WHERE_CLAUSE) {
      r = where_clause(b, 0);
    }
    else {
      r = parse_root_(t, b, 0);
    }
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return flexibleSearchFile(b, l + 1);
  }

  /* ********************************************************** */
  // COUNT '(' asterisk ')' 
  // 	|	general_set_function
  public static boolean aggregate_function(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "aggregate_function")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, AGGREGATE_FUNCTION, "<aggregate function>");
    r = parseTokens(b, 0, COUNT, LEFT_PAREN, ASTERISK, RIGHT_PAREN);
    if (!r) r = general_set_function(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // row_value_predicand [ NOT ] BETWEEN row_value_predicand AND row_value_predicand
  public static boolean between_predicate(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "between_predicate")) return false;
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
    if (!recursion_guard_(b, l, "between_predicate_1")) return false;
    consumeToken(b, NOT);
    return true;
  }

  /* ********************************************************** */
  // [ NOT ] boolean_test
  public static boolean boolean_factor(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "boolean_factor")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BOOLEAN_FACTOR, "<boolean factor>");
    r = boolean_factor_0(b, l + 1);
    r = r && boolean_test(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ NOT ]
  private static boolean boolean_factor_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "boolean_factor_0")) return false;
    consumeToken(b, NOT);
    return true;
  }

  /* ********************************************************** */
  // parenthesized_boolean_value_expression
  // 	|	nonparenthesized_value_expression_primary
  public static boolean boolean_predicand(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "boolean_predicand")) return false;
    if (!nextTokenIs(b, "<boolean predicand>", LEFT_PAREN, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BOOLEAN_PREDICAND, "<boolean predicand>");
    r = parenthesized_boolean_value_expression(b, l + 1);
    if (!r) r = nonparenthesized_value_expression_primary(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // boolean_predicand | predicate
  public static boolean boolean_primary(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "boolean_primary")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BOOLEAN_PRIMARY, "<boolean primary>");
    r = boolean_predicand(b, l + 1);
    if (!r) r = predicate(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // boolean_factor (((AND|OR) boolean_term)*)?
  public static boolean boolean_term(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "boolean_term")) return false;
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
    if (!recursion_guard_(b, l, "boolean_term_1")) return false;
    boolean_term_1_0(b, l + 1);
    return true;
  }

  // ((AND|OR) boolean_term)*
  private static boolean boolean_term_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "boolean_term_1_0")) return false;
    int c = current_position_(b);
    while (true) {
      if (!boolean_term_1_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "boolean_term_1_0", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // (AND|OR) boolean_term
  private static boolean boolean_term_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "boolean_term_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = boolean_term_1_0_0_0(b, l + 1);
    r = r && boolean_term(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // AND|OR
  private static boolean boolean_term_1_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "boolean_term_1_0_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AND);
    if (!r) r = consumeToken(b, OR);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // boolean_primary  [ IS [ NOT ] truth_value ]
  public static boolean boolean_test(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "boolean_test")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BOOLEAN_TEST, "<boolean test>");
    r = boolean_primary(b, l + 1);
    r = r && boolean_test_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ IS [ NOT ] truth_value ]
  private static boolean boolean_test_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "boolean_test_1")) return false;
    boolean_test_1_0(b, l + 1);
    return true;
  }

  // IS [ NOT ] truth_value
  private static boolean boolean_test_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "boolean_test_1_0")) return false;
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
    if (!recursion_guard_(b, l, "boolean_test_1_0_1")) return false;
    consumeToken(b, NOT);
    return true;
  }

  /* ********************************************************** */
  // boolean_term
  public static boolean boolean_value_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "boolean_value_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BOOLEAN_VALUE_EXPRESSION, "<boolean value expression>");
    r = boolean_term(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // row_value_predicand [ NOT ] LIKE (character_pattern | value_expression)
  public static boolean character_like_predicate(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "character_like_predicate")) return false;
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
    if (!recursion_guard_(b, l, "character_like_predicate_1")) return false;
    consumeToken(b, NOT);
    return true;
  }

  // character_pattern | value_expression
  private static boolean character_like_predicate_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "character_like_predicate_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = character_pattern(b, l + 1);
    if (!r) r = value_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // string | string_value_function
  public static boolean character_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "character_pattern")) return false;
    if (!nextTokenIs(b, "<character pattern>", CONCAT, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CHARACTER_PATTERN, "<character pattern>");
    r = consumeToken(b, STRING);
    if (!r) r = string_value_function(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // string
  public static boolean character_string_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "character_string_literal")) return false;
    if (!nextTokenIs(b, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STRING);
    exit_section_(b, m, CHARACTER_STRING_LITERAL, r);
    return r;
  }

  /* ********************************************************** */
  // CONCAT '(' string_value_expression comma  (character_substring_function | string_value_expression) ')'
  public static boolean character_substring_function(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "character_substring_function")) return false;
    if (!nextTokenIs(b, CONCAT)) return false;
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
    if (!recursion_guard_(b, l, "character_substring_function_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = character_substring_function(b, l + 1);
    if (!r) r = string_value_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // character_substring_function
  public static boolean character_value_function(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "character_value_function")) return false;
    if (!nextTokenIs(b, CONCAT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = character_substring_function(b, l + 1);
    exit_section_(b, m, CHARACTER_VALUE_FUNCTION, r);
    return r;
  }

  /* ********************************************************** */
  // identifier_chain
  public static boolean column_reference(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_reference")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = identifier_chain(b, l + 1);
    exit_section_(b, m, COLUMN_REFERENCE, r);
    return r;
  }

  /* ********************************************************** */
  // string_value_expression
  public static boolean common_value_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "common_value_expression")) return false;
    if (!nextTokenIs(b, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = string_value_expression(b, l + 1);
    exit_section_(b, m, COMMON_VALUE_EXPRESSION, r);
    return r;
  }

  /* ********************************************************** */
  // equals_operator
  // 	|	greater_than_operator
  // 	|	not_equals_operator
  // 	|	less_than_operator
  // 	|	less_than_or_equals_operator
  // 	|	greater_than_or_equals_operator
  public static boolean comp_op(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comp_op")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COMP_OP, "<comp op>");
    r = consumeToken(b, EQUALS_OPERATOR);
    if (!r) r = consumeToken(b, GREATER_THAN_OPERATOR);
    if (!r) r = consumeToken(b, NOT_EQUALS_OPERATOR);
    if (!r) r = consumeToken(b, LESS_THAN_OPERATOR);
    if (!r) r = consumeToken(b, LESS_THAN_OR_EQUALS_OPERATOR);
    if (!r) r = consumeToken(b, GREATER_THAN_OR_EQUALS_OPERATOR);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // row_value_predicand comp_op row_value_predicand
  static boolean comparison_predicate(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comparison_predicate")) return false;
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
    if (!recursion_guard_(b, l, "computational_operation")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AVG);
    if (!r) r = consumeToken(b, MAX);
    if (!r) r = consumeToken(b, MIN);
    if (!r) r = consumeToken(b, SUM);
    if (!r) r = consumeToken(b, EVERY);
    if (!r) r = consumeToken(b, ANY);
    if (!r) r = consumeToken(b, SOME);
    if (!r) r = consumeToken(b, COUNT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // identifier
  public static boolean correlation_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "correlation_name")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, CORRELATION_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // value_expression [ AS  correlation_name ]
  public static boolean derived_column(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "derived_column")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DERIVED_COLUMN, "<derived column>");
    r = value_expression(b, l + 1);
    r = r && derived_column_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ AS  correlation_name ]
  private static boolean derived_column_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "derived_column_1")) return false;
    derived_column_1_0(b, l + 1);
    return true;
  }

  // AS  correlation_name
  private static boolean derived_column_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "derived_column_1_0")) return false;
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
    if (!recursion_guard_(b, l, "empty_statement")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SEMICOLON);
    if (!r) r = eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // EXISTS table_subquery
  public static boolean exists_predicate(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "exists_predicate")) return false;
    if (!nextTokenIs(b, EXISTS)) return false;
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
    if (!recursion_guard_(b, l, "expressionRecoverWhile")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !expressionRecoverWhile_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SEMICOLON | <<eof>>
  private static boolean expressionRecoverWhile_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expressionRecoverWhile_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SEMICOLON);
    if (!r) r = eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // statement *
  static boolean flexibleSearchFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "flexibleSearchFile")) return false;
    int c = current_position_(b);
    while (true) {
      if (!statement(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "flexibleSearchFile", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // FROM (left_brace table_reference_list right_brace | subquery )
  public static boolean from_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "from_clause")) return false;
    if (!nextTokenIs(b, FROM)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FROM);
    r = r && from_clause_1(b, l + 1);
    exit_section_(b, m, FROM_CLAUSE, r);
    return r;
  }

  // left_brace table_reference_list right_brace | subquery
  private static boolean from_clause_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "from_clause_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = from_clause_1_0(b, l + 1);
    if (!r) r = subquery(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // left_brace table_reference_list right_brace
  private static boolean from_clause_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "from_clause_1_0")) return false;
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
    if (!recursion_guard_(b, l, "general_literal")) return false;
    if (!nextTokenIs(b, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = character_string_literal(b, l + 1);
    exit_section_(b, m, GENERAL_LITERAL, r);
    return r;
  }

  /* ********************************************************** */
  // set_function_type left_paren [ set_quantifier ] value_expression right_paren [ [ AS ] correlation_name ]
  public static boolean general_set_function(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "general_set_function")) return false;
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
    if (!recursion_guard_(b, l, "general_set_function_2")) return false;
    set_quantifier(b, l + 1);
    return true;
  }

  // [ [ AS ] correlation_name ]
  private static boolean general_set_function_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "general_set_function_5")) return false;
    general_set_function_5_0(b, l + 1);
    return true;
  }

  // [ AS ] correlation_name
  private static boolean general_set_function_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "general_set_function_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = general_set_function_5_0_0(b, l + 1);
    r = r && correlation_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ AS ]
  private static boolean general_set_function_5_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "general_set_function_5_0_0")) return false;
    consumeToken(b, AS);
    return true;
  }

  /* ********************************************************** */
  // GROUP BY [ set_quantifier ] grouping_element_list
  public static boolean group_by_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "group_by_clause")) return false;
    if (!nextTokenIs(b, GROUP)) return false;
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
    if (!recursion_guard_(b, l, "group_by_clause_2")) return false;
    set_quantifier(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // value_expression
  public static boolean grouping_column_reference(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grouping_column_reference")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, GROUPING_COLUMN_REFERENCE, "<grouping column reference>");
    r = value_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // grouping_column_reference [ { comma grouping_column_reference }* ]
  public static boolean grouping_column_reference_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grouping_column_reference_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, GROUPING_COLUMN_REFERENCE_LIST, "<grouping column reference list>");
    r = grouping_column_reference(b, l + 1);
    r = r && grouping_column_reference_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ { comma grouping_column_reference }* ]
  private static boolean grouping_column_reference_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grouping_column_reference_list_1")) return false;
    grouping_column_reference_list_1_0(b, l + 1);
    return true;
  }

  // { comma grouping_column_reference }*
  private static boolean grouping_column_reference_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grouping_column_reference_list_1_0")) return false;
    int c = current_position_(b);
    while (true) {
      if (!grouping_column_reference_list_1_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "grouping_column_reference_list_1_0", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // comma grouping_column_reference
  private static boolean grouping_column_reference_list_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grouping_column_reference_list_1_0_0")) return false;
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
    if (!recursion_guard_(b, l, "grouping_element")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, GROUPING_ELEMENT, "<grouping element>");
    r = ordinary_grouping_set(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // grouping_element [ { comma grouping_element }* ]
  public static boolean grouping_element_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grouping_element_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, GROUPING_ELEMENT_LIST, "<grouping element list>");
    r = grouping_element(b, l + 1);
    r = r && grouping_element_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ { comma grouping_element }* ]
  private static boolean grouping_element_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grouping_element_list_1")) return false;
    grouping_element_list_1_0(b, l + 1);
    return true;
  }

  // { comma grouping_element }*
  private static boolean grouping_element_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grouping_element_list_1_0")) return false;
    int c = current_position_(b);
    while (true) {
      if (!grouping_element_list_1_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "grouping_element_list_1_0", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // comma grouping_element
  private static boolean grouping_element_list_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grouping_element_list_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && grouping_element(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // identifier [ { (dot|colon) identifier }* ]
  static boolean identifier_chain(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "identifier_chain")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    r = r && identifier_chain_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ { (dot|colon) identifier }* ]
  private static boolean identifier_chain_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "identifier_chain_1")) return false;
    identifier_chain_1_0(b, l + 1);
    return true;
  }

  // { (dot|colon) identifier }*
  private static boolean identifier_chain_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "identifier_chain_1_0")) return false;
    int c = current_position_(b);
    while (true) {
      if (!identifier_chain_1_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "identifier_chain_1_0", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // (dot|colon) identifier
  private static boolean identifier_chain_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "identifier_chain_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = identifier_chain_1_0_0_0(b, l + 1);
    r = r && consumeToken(b, IDENTIFIER);
    exit_section_(b, m, null, r);
    return r;
  }

  // dot|colon
  private static boolean identifier_chain_1_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "identifier_chain_1_0_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT);
    if (!r) r = consumeToken(b, COLON);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ON search_condition
  public static boolean join_condition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "join_condition")) return false;
    if (!nextTokenIs(b, ON)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ON);
    r = r && search_condition(b, l + 1);
    exit_section_(b, m, JOIN_CONDITION, r);
    return r;
  }

  /* ********************************************************** */
  // join_condition
  public static boolean join_specification(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "join_specification")) return false;
    if (!nextTokenIs(b, ON)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = join_condition(b, l + 1);
    exit_section_(b, m, JOIN_SPECIFICATION, r);
    return r;
  }

  /* ********************************************************** */
  public static boolean join_type(PsiBuilder b, int l) {
    Marker m = enter_section_(b);
    exit_section_(b, m, JOIN_TYPE, true);
    return true;
  }

  /* ********************************************************** */
  // [(table_primary [joined_table] | joined_table)] [ join_type ] JOIN table_reference join_specification
  public static boolean joined_table(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "joined_table")) return false;
    if (!nextTokenIs(b, "<joined table>", JOIN, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, JOINED_TABLE, "<joined table>");
    r = joined_table_0(b, l + 1);
    r = r && joined_table_1(b, l + 1);
    r = r && consumeToken(b, JOIN);
    r = r && table_reference(b, l + 1);
    r = r && join_specification(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [(table_primary [joined_table] | joined_table)]
  private static boolean joined_table_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "joined_table_0")) return false;
    joined_table_0_0(b, l + 1);
    return true;
  }

  // table_primary [joined_table] | joined_table
  private static boolean joined_table_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "joined_table_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = joined_table_0_0_0(b, l + 1);
    if (!r) r = joined_table(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // table_primary [joined_table]
  private static boolean joined_table_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "joined_table_0_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = table_primary(b, l + 1);
    r = r && joined_table_0_0_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [joined_table]
  private static boolean joined_table_0_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "joined_table_0_0_0_1")) return false;
    joined_table(b, l + 1);
    return true;
  }

  // [ join_type ]
  private static boolean joined_table_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "joined_table_1")) return false;
    join_type(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // identifier
  public static boolean lang(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "lang")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, LANG, r);
    return r;
  }

  /* ********************************************************** */
  // character_like_predicate
  public static boolean like_predicate(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "like_predicate")) return false;
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
    if (!recursion_guard_(b, l, "null_ordering")) return false;
    if (!nextTokenIs(b, NULLS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, NULLS, FIRST);
    if (!r) r = parseTokens(b, 0, NULLS, LAST);
    exit_section_(b, m, NULL_ORDERING, r);
    return r;
  }

  /* ********************************************************** */
  // row_value_predicand IS [ NOT ] NULL
  public static boolean null_predicate(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "null_predicate")) return false;
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
    if (!recursion_guard_(b, l, "null_predicate_2")) return false;
    consumeToken(b, NOT);
    return true;
  }

  /* ********************************************************** */
  // ORDER BY sort_specification_list
  public static boolean order_by_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "order_by_clause")) return false;
    if (!nextTokenIs(b, ORDER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, ORDER, BY);
    r = r && sort_specification_list(b, l + 1);
    exit_section_(b, m, ORDER_BY_CLAUSE, r);
    return r;
  }

  /* ********************************************************** */
  // ASC | DESC
  public static boolean ordering_specification(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ordering_specification")) return false;
    if (!nextTokenIs(b, "<ordering specification>", ASC, DESC)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ORDERING_SPECIFICATION, "<ordering specification>");
    r = consumeToken(b, ASC);
    if (!r) r = consumeToken(b, DESC);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // grouping_column_reference
  // 	|	'(' grouping_column_reference_list ')'
  public static boolean ordinary_grouping_set(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ordinary_grouping_set")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ORDINARY_GROUPING_SET, "<ordinary grouping set>");
    r = grouping_column_reference(b, l + 1);
    if (!r) r = ordinary_grouping_set_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '(' grouping_column_reference_list ')'
  private static boolean ordinary_grouping_set_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ordinary_grouping_set_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LEFT_PAREN);
    r = r && grouping_column_reference_list(b, l + 1);
    r = r && consumeToken(b, RIGHT_PAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '(' boolean_value_expression ')'
  static boolean parenthesized_boolean_value_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parenthesized_boolean_value_expression")) return false;
    if (!nextTokenIs(b, LEFT_PAREN)) return false;
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
    if (!recursion_guard_(b, l, "predicate")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PREDICATE, "<predicate>");
    r = comparison_predicate(b, l + 1);
    if (!r) r = between_predicate(b, l + 1);
    if (!r) r = like_predicate(b, l + 1);
    if (!r) r = null_predicate(b, l + 1);
    if (!r) r = exists_predicate(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SELECT [ set_quantifier ] select_list table_expression
  public static boolean query_specification(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "query_specification")) return false;
    if (!nextTokenIs(b, SELECT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SELECT);
    r = r && query_specification_1(b, l + 1);
    r = r && select_list(b, l + 1);
    r = r && table_expression(b, l + 1);
    exit_section_(b, m, QUERY_SPECIFICATION, r);
    return r;
  }

  // [ set_quantifier ]
  private static boolean query_specification_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "query_specification_1")) return false;
    set_quantifier(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // value_expression | common_value_expression
  public static boolean row_value_predicand(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "row_value_predicand")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ROW_VALUE_PREDICAND, "<row value predicand>");
    r = value_expression(b, l + 1);
    if (!r) r = common_value_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // boolean_value_expression
  public static boolean search_condition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "search_condition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SEARCH_CONDITION, "<search condition>");
    r = boolean_value_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // asterisk | select_sublist [ ( comma select_sublist )* ]
  public static boolean select_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SELECT_LIST, "<select list>");
    r = consumeToken(b, ASTERISK);
    if (!r) r = select_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // select_sublist [ ( comma select_sublist )* ]
  private static boolean select_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_list_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = select_sublist(b, l + 1);
    r = r && select_list_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ ( comma select_sublist )* ]
  private static boolean select_list_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_list_1_1")) return false;
    select_list_1_1_0(b, l + 1);
    return true;
  }

  // ( comma select_sublist )*
  private static boolean select_list_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_list_1_1_0")) return false;
    int c = current_position_(b);
    while (true) {
      if (!select_list_1_1_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "select_list_1_1_0", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // comma select_sublist
  private static boolean select_list_1_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_list_1_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && select_sublist(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // derived_column | (aggregate_function [comma aggregate_function*])
  public static boolean select_sublist(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_sublist")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SELECT_SUBLIST, "<select sublist>");
    r = derived_column(b, l + 1);
    if (!r) r = select_sublist_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // aggregate_function [comma aggregate_function*]
  private static boolean select_sublist_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_sublist_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = aggregate_function(b, l + 1);
    r = r && select_sublist_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [comma aggregate_function*]
  private static boolean select_sublist_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_sublist_1_1")) return false;
    select_sublist_1_1_0(b, l + 1);
    return true;
  }

  // comma aggregate_function*
  private static boolean select_sublist_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_sublist_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && select_sublist_1_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // aggregate_function*
  private static boolean select_sublist_1_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_sublist_1_1_0_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!aggregate_function(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "select_sublist_1_1_0_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // computational_operation
  public static boolean set_function_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "set_function_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SET_FUNCTION_TYPE, "<set function type>");
    r = computational_operation(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // DISTINCT | ALL
  public static boolean set_quantifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "set_quantifier")) return false;
    if (!nextTokenIs(b, "<set quantifier>", ALL, DISTINCT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SET_QUANTIFIER, "<set quantifier>");
    r = consumeToken(b, DISTINCT);
    if (!r) r = consumeToken(b, ALL);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // value_expression
  public static boolean sort_key(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sort_key")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SORT_KEY, "<sort key>");
    r = value_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // sort_key [ ordering_specification ] [ null_ordering ]
  public static boolean sort_specification(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sort_specification")) return false;
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
    if (!recursion_guard_(b, l, "sort_specification_1")) return false;
    ordering_specification(b, l + 1);
    return true;
  }

  // [ null_ordering ]
  private static boolean sort_specification_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sort_specification_2")) return false;
    null_ordering(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // sort_specification [ { comma sort_specification }* ]
  public static boolean sort_specification_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sort_specification_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SORT_SPECIFICATION_LIST, "<sort specification list>");
    r = sort_specification(b, l + 1);
    r = r && sort_specification_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ { comma sort_specification }* ]
  private static boolean sort_specification_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sort_specification_list_1")) return false;
    sort_specification_list_1_0(b, l + 1);
    return true;
  }

  // { comma sort_specification }*
  private static boolean sort_specification_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sort_specification_list_1_0")) return false;
    int c = current_position_(b);
    while (true) {
      if (!sort_specification_list_1_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "sort_specification_list_1_0", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // comma sort_specification
  private static boolean sort_specification_list_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sort_specification_list_1_0_0")) return false;
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
    if (!recursion_guard_(b, l, "statement")) return false;
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
    if (!recursion_guard_(b, l, "statement_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !eof(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (query_specification empty_statement?)*
  private static boolean statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!statement_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "statement_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // query_specification empty_statement?
  private static boolean statement_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = query_specification(b, l + 1);
    r = r && statement_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // empty_statement?
  private static boolean statement_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_1_0_1")) return false;
    empty_statement(b, l + 1);
    return true;
  }

  // empty_statement?
  private static boolean statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_2")) return false;
    empty_statement(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // general_literal
  public static boolean string_value_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_value_expression")) return false;
    if (!nextTokenIs(b, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = general_literal(b, l + 1);
    exit_section_(b, m, STRING_VALUE_EXPRESSION, r);
    return r;
  }

  /* ********************************************************** */
  // character_value_function
  public static boolean string_value_function(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_value_function")) return false;
    if (!nextTokenIs(b, CONCAT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = character_value_function(b, l + 1);
    exit_section_(b, m, STRING_VALUE_FUNCTION, r);
    return r;
  }

  /* ********************************************************** */
  // '(' left_double_brace query_specification right_double_brace [(UNION ALL left_double_brace query_specification right_double_brace)*]  ')' [ [ AS ] correlation_name ]
  public static boolean subquery(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "subquery")) return false;
    if (!nextTokenIs(b, LEFT_PAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LEFT_PAREN, LEFT_DOUBLE_BRACE);
    r = r && query_specification(b, l + 1);
    r = r && consumeToken(b, RIGHT_DOUBLE_BRACE);
    r = r && subquery_4(b, l + 1);
    r = r && consumeToken(b, RIGHT_PAREN);
    r = r && subquery_6(b, l + 1);
    exit_section_(b, m, SUBQUERY, r);
    return r;
  }

  // [(UNION ALL left_double_brace query_specification right_double_brace)*]
  private static boolean subquery_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "subquery_4")) return false;
    subquery_4_0(b, l + 1);
    return true;
  }

  // (UNION ALL left_double_brace query_specification right_double_brace)*
  private static boolean subquery_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "subquery_4_0")) return false;
    int c = current_position_(b);
    while (true) {
      if (!subquery_4_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "subquery_4_0", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // UNION ALL left_double_brace query_specification right_double_brace
  private static boolean subquery_4_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "subquery_4_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, UNION, ALL, LEFT_DOUBLE_BRACE);
    r = r && query_specification(b, l + 1);
    r = r && consumeToken(b, RIGHT_DOUBLE_BRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ [ AS ] correlation_name ]
  private static boolean subquery_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "subquery_6")) return false;
    subquery_6_0(b, l + 1);
    return true;
  }

  // [ AS ] correlation_name
  private static boolean subquery_6_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "subquery_6_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = subquery_6_0_0(b, l + 1);
    r = r && correlation_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ AS ]
  private static boolean subquery_6_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "subquery_6_0_0")) return false;
    consumeToken(b, AS);
    return true;
  }

  /* ********************************************************** */
  // from_clause 
  // 		 where_clause? 
  // 		 order_by_clause? 
  // 		 group_by_clause?
  public static boolean table_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_expression")) return false;
    if (!nextTokenIs(b, FROM)) return false;
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
    if (!recursion_guard_(b, l, "table_expression_1")) return false;
    where_clause(b, l + 1);
    return true;
  }

  // order_by_clause?
  private static boolean table_expression_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_expression_2")) return false;
    order_by_clause(b, l + 1);
    return true;
  }

  // group_by_clause?
  private static boolean table_expression_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_expression_3")) return false;
    group_by_clause(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // identifier [ exclamation_mark ]
  public static boolean table_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_name")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, TABLE_NAME, null);
    r = consumeToken(b, IDENTIFIER);
    p = r; // pin = 1
    r = r && table_name_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ exclamation_mark ]
  private static boolean table_name_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_name_1")) return false;
    consumeToken(b, EXCLAMATION_MARK);
    return true;
  }

  /* ********************************************************** */
  // table_name [ [ AS ] correlation_name ]
  public static boolean table_primary(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_primary")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = table_name(b, l + 1);
    r = r && table_primary_1(b, l + 1);
    exit_section_(b, m, TABLE_PRIMARY, r);
    return r;
  }

  // [ [ AS ] correlation_name ]
  private static boolean table_primary_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_primary_1")) return false;
    table_primary_1_0(b, l + 1);
    return true;
  }

  // [ AS ] correlation_name
  private static boolean table_primary_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_primary_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = table_primary_1_0_0(b, l + 1);
    r = r && correlation_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ AS ]
  private static boolean table_primary_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_primary_1_0_0")) return false;
    consumeToken(b, AS);
    return true;
  }

  /* ********************************************************** */
  // table_primary [joined_table] | joined_table
  public static boolean table_reference(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_reference")) return false;
    if (!nextTokenIs(b, "<table reference>", JOIN, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TABLE_REFERENCE, "<table reference>");
    r = table_reference_0(b, l + 1);
    if (!r) r = joined_table(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // table_primary [joined_table]
  private static boolean table_reference_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_reference_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = table_primary(b, l + 1);
    r = r && table_reference_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [joined_table]
  private static boolean table_reference_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_reference_0_1")) return false;
    joined_table(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // table_reference [ { comma? table_reference }* ]
  public static boolean table_reference_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_reference_list")) return false;
    if (!nextTokenIs(b, "<table reference list>", JOIN, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TABLE_REFERENCE_LIST, "<table reference list>");
    r = table_reference(b, l + 1);
    r = r && table_reference_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ { comma? table_reference }* ]
  private static boolean table_reference_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_reference_list_1")) return false;
    table_reference_list_1_0(b, l + 1);
    return true;
  }

  // { comma? table_reference }*
  private static boolean table_reference_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_reference_list_1_0")) return false;
    int c = current_position_(b);
    while (true) {
      if (!table_reference_list_1_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "table_reference_list_1_0", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // comma? table_reference
  private static boolean table_reference_list_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_reference_list_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = table_reference_list_1_0_0_0(b, l + 1);
    r = r && table_reference(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // comma?
  private static boolean table_reference_list_1_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_reference_list_1_0_0_0")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // subquery
  public static boolean table_subquery(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_subquery")) return false;
    if (!nextTokenIs(b, LEFT_PAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = subquery(b, l + 1);
    exit_section_(b, m, TABLE_SUBQUERY, r);
    return r;
  }

  /* ********************************************************** */
  // TRUE | FALSE
  public static boolean truth_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "truth_value")) return false;
    if (!nextTokenIs(b, "<truth value>", FALSE, TRUE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TRUTH_VALUE, "<truth value>");
    r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // number 
  // | (question_mark? column_reference) 
  // | left_brace column_reference (left_bracket lang right_bracket)? colon? column_reference? right_brace
  public static boolean value_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VALUE_EXPRESSION, "<value expression>");
    r = consumeToken(b, NUMBER);
    if (!r) r = value_expression_1(b, l + 1);
    if (!r) r = value_expression_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // question_mark? column_reference
  private static boolean value_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_expression_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = value_expression_1_0(b, l + 1);
    r = r && column_reference(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // question_mark?
  private static boolean value_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_expression_1_0")) return false;
    consumeToken(b, QUESTION_MARK);
    return true;
  }

  // left_brace column_reference (left_bracket lang right_bracket)? colon? column_reference? right_brace
  private static boolean value_expression_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_expression_2")) return false;
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

  // (left_bracket lang right_bracket)?
  private static boolean value_expression_2_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_expression_2_2")) return false;
    value_expression_2_2_0(b, l + 1);
    return true;
  }

  // left_bracket lang right_bracket
  private static boolean value_expression_2_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_expression_2_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LEFT_BRACKET);
    r = r && lang(b, l + 1);
    r = r && consumeToken(b, RIGHT_BRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // colon?
  private static boolean value_expression_2_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_expression_2_3")) return false;
    consumeToken(b, COLON);
    return true;
  }

  // column_reference?
  private static boolean value_expression_2_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value_expression_2_4")) return false;
    column_reference(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // WHERE search_condition
  public static boolean where_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "where_clause")) return false;
    if (!nextTokenIs(b, WHERE)) return false;
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
}
