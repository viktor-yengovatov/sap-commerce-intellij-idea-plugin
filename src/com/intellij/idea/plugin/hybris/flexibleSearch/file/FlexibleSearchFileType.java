package com.intellij.idea.plugin.hybris.flexibleSearch.file;

import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons;
import com.intellij.idea.plugin.hybris.flexibleSearch.FlexibleSearchLanguage;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class FlexibleSearchFileType extends LanguageFileType {

    private static final FlexibleSearchFileType INSTANCE = new FlexibleSearchFileType();

    public static FlexibleSearchFileType getInstance() {
        return INSTANCE;
    }

    protected FlexibleSearchFileType() {
        super(FlexibleSearchLanguage.getInstance());
    }

    @NotNull
    @Override
    public String getName() {
        return "FlexibleSearch file";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "FlexibleSearch language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "fxs";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return HybrisIcons.FS_FILE;
    }
}
