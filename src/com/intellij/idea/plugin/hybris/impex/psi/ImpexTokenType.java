package com.intellij.idea.plugin.hybris.impex.psi;

import com.intellij.idea.plugin.hybris.impex.ImpexLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class ImpexTokenType extends IElementType {
    public ImpexTokenType(@NotNull @NonNls String debugName) {
        super(debugName, ImpexLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "ImpexTokenType." + super.toString();
    }
}