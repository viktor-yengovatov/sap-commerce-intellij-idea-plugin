package com.intellij.idea.plugin.hybris.impex;

import com.intellij.lang.Language;

public class ImpexLanguage extends Language {
    public static final ImpexLanguage INSTANCE = new ImpexLanguage();

    private ImpexLanguage() {
        super("Impex");
    }
}
