/*
 * Copyright 2015 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.idea.plugin.hybris.impex.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.idea.plugin.hybris.impex.ImpexLanguage;
import com.intellij.idea.plugin.hybris.impex.file.ImpexFileType;
import com.intellij.idea.plugin.hybris.impex.formatting.AlignmentStrategy;
import com.intellij.idea.plugin.hybris.impex.formatting.ColumnsAlignmentStrategy;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ImpexFile extends PsiFileBase {

    private final AlignmentStrategy alignmentStrategy = new ColumnsAlignmentStrategy();

    public ImpexFile(@NotNull final FileViewProvider viewProvider) {
        super(viewProvider, ImpexLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return ImpexFileType.INSTANCE;
    }

    @NotNull
    @Override
    public String toString() {
        return "Impex File";
    }

    @Override
    public Icon getIcon(final int flags) {
        return super.getIcon(flags);
    }

    @NotNull
    public AlignmentStrategy getAlignmentStrategy() {
        return alignmentStrategy;
    }
}