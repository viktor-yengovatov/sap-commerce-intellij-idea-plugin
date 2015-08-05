/*
 * This file is part of "Hybris Integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2015 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
