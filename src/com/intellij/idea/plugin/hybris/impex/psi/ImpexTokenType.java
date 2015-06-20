package com.intellij.idea.plugin.hybris.impex.psi;

import com.intellij.idea.plugin.hybris.impex.ImpexLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.lowerCase;

public class ImpexTokenType extends IElementType {

    private static final Pattern PATTERN = Pattern.compile("[_]");

    public ImpexTokenType(@NotNull @NonNls final String debugName) {
        super(debugName, ImpexLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        final String name = super.toString();

        if (isBlank(name)) return name;

        final String fixedName = PATTERN.matcher(lowerCase(name)).replaceAll(" ");

        return new StringBuilder("<").append(fixedName).append(">").toString();
    }
}