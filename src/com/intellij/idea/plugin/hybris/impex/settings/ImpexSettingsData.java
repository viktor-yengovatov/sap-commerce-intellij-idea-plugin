package com.intellij.idea.plugin.hybris.impex.settings;

import com.intellij.ide.util.PropertyName;

public class ImpexSettingsData {

    @PropertyName("foldingEnabled")
    private boolean foldingEnabled = true;

    @PropertyName("useSmartFolding")
    private boolean useSmartFolding = true;

    public ImpexSettingsData() {
    }

    public boolean isFoldingEnabled() {
        return foldingEnabled;
    }

    public void setFoldingEnabled(final boolean foldingEnabled) {
        this.foldingEnabled = foldingEnabled;
    }

    public boolean isUseSmartFolding() {
        return useSmartFolding;
    }

    public void setUseSmartFolding(final boolean foldingEnabled) {
        this.useSmartFolding = foldingEnabled;
    }
}