package com.intellij.idea.plugin.hybris.flexibleSearch;

import com.intellij.lang.Language;

public class FlexibleSearchLanguage extends Language {
    private static final FlexibleSearchLanguage INSTANCE = new FlexibleSearchLanguage();

    public static FlexibleSearchLanguage getInstance() {
        return INSTANCE;
    }

    protected FlexibleSearchLanguage() {
        super("FlexibleSearch");
    }
}