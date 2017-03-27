package com.intellij.idea.plugin.hybris.flexibleSearch;

import com.intellij.idea.plugin.hybris.flexibleSearch.file.FlexibleSearchFile;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTokenType;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public class FlexibleSearchParserDefinition implements ParserDefinition {

    public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
    public static final FlexibleSearchTokenType COMMENT = new FlexibleSearchTokenType("COMMENT");
    public static final TokenSet COMMENTS = TokenSet.create(COMMENT);

    public static final IFileElementType FILE = new IFileElementType(FlexibleSearchLanguage.getInstance());

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new FlexibleSearchLexerAdapter();
    }

    @NotNull
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    public PsiParser createParser(final Project project) {
        return new FlexibleSearchParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    public PsiFile createFile(FileViewProvider viewProvider) {
        return new FlexibleSearchFile(viewProvider);
    }

    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }

    @NotNull
    public PsiElement createElement(ASTNode node) {
        return FlexibleSearchTypes.Factory.createElement(node);
    }
}
