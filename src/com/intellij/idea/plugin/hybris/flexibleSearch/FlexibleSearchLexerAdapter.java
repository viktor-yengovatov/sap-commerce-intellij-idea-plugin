package com.intellij.idea.plugin.hybris.flexibleSearch;

import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

public class FlexibleSearchLexerAdapter extends FlexAdapter {
    public FlexibleSearchLexerAdapter() {
        super(new FlexibleSearchLexer((Reader) null));
    }
}
