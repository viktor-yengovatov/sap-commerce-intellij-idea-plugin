package com.intellij.idea.plugin.hybris.flexibleSearch.psi;

import com.intellij.idea.plugin.hybris.flexibleSearch.FlexibleSearchLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class FlexibleSearchElementType extends IElementType {

    public FlexibleSearchElementType(@NotNull @NonNls final String debugName) {
        super(debugName, FlexibleSearchLanguage.getInstance());
    }
}
