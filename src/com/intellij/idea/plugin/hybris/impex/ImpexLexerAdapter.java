package com.intellij.idea.plugin.hybris.impex;

import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

public class ImpexLexerAdapter extends FlexAdapter {
    public ImpexLexerAdapter() {
        super(new ImpexLexer((Reader) null));
    }
}
