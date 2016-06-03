/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2016 Alexander Bartash <AlexanderBartash@gmail.com>
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

package com.intellij.idea.plugin.hybris.type.system.file;

import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons;
import com.intellij.idea.plugin.hybris.type.system.TypeSystemLanguage;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 3/06/2016.
 */
public class TypeSystemFileType extends LanguageFileType {

    public static final TypeSystemFileType INSTANCE = new TypeSystemFileType();

    private TypeSystemFileType() {
        super(TypeSystemLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "[y] Type System";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "A type is a template for objects. Define and persist product data that objects may carry and specify relations between objects. Every object stored in the platform is a type instance.";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "xml";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return HybrisIcons.TYPE_SYSTEM;
    }
}
