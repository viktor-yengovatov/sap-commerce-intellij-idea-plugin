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

package com.intellij.idea.plugin.hybris.impex.file;

import com.intellij.idea.plugin.hybris.impex.ImpexLanguage;
import com.intellij.idea.plugin.hybris.utils.HybrisIconsUtils;
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
        return HybrisIconsUtils.IMPEX_FILE;
    }
}
