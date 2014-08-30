package com.intellij.idea.plugin.hybris.impex.psi;

import com.intellij.idea.plugin.hybris.impex.ImpexLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class ImpexElementType extends IElementType {
    public ImpexElementType(@NotNull @NonNls String debugName) {
        super(debugName, ImpexLanguage.INSTANCE);
    }
}
