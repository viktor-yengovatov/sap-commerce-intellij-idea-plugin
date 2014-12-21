package com.intellij.idea.plugin.hybris.impex.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.idea.plugin.hybris.impex.file.ImpexFileType;
import com.intellij.idea.plugin.hybris.impex.ImpexLanguage;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ImpexFile extends PsiFileBase {

    public ImpexFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, ImpexLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return ImpexFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Impex File";
    }

    @Override
    public Icon getIcon(int flags) {
        return super.getIcon(flags);
    }
}