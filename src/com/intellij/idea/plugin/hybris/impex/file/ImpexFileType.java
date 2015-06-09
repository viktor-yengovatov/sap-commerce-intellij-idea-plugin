package com.intellij.idea.plugin.hybris.impex.file;

import com.intellij.idea.plugin.hybris.impex.ImpexLanguage;
import com.intellij.idea.plugin.hybris.util.HybrisIcons;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ImpexFileType extends LanguageFileType {

    public static final ImpexFileType INSTANCE = new ImpexFileType();

    private ImpexFileType() {
        super(ImpexLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Impex file";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Impex language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "impex";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return HybrisIcons.IMPEX_FILE;
    }
}
