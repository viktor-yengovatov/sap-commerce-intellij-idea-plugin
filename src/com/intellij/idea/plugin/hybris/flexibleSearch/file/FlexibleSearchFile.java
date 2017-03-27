package com.intellij.idea.plugin.hybris.flexibleSearch.file;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.idea.plugin.hybris.flexibleSearch.FlexibleSearchLanguage;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class FlexibleSearchFile extends PsiFileBase {

    public FlexibleSearchFile(@NotNull final FileViewProvider viewProvider) {
        super(viewProvider, FlexibleSearchLanguage.getInstance());
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return FlexibleSearchFileType.getInstance();
    }

    @NotNull
    @Override
    public String toString() {
        return "FlexibleSearch File";
    }

    @Override
    public Icon getIcon(final int flags) {
        return super.getIcon(flags);
    }

}