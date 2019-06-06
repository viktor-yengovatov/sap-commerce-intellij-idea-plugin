package com.intellij.idea.plugin.hybris.impex.folding;

public class ImpexMacroDescriptor {
    private String macroName;
    private String resolvedValue;

    public ImpexMacroDescriptor(final String macroName, final String resolvedValue) {
        this.macroName = macroName;
        this.resolvedValue = resolvedValue;
        replaceBlank();
    }

    private void replaceBlank() {
        if (resolvedValue == null || resolvedValue.isEmpty()) {
            resolvedValue = "<blank>";
        }
    }

    public String getMacroName() {
        return macroName;
    }

    public void setMacroName(final String macroName) {
        this.macroName = macroName;
    }

    public String getResolvedValue() {
        return resolvedValue;
    }

    public void setResolvedValue(final String resolvedValue) {
        this.resolvedValue = resolvedValue;
    }
}
