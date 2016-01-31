/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
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

package com.intellij.idea.plugin.hybris.business.process.diagram;

import com.intellij.diagram.DiagramVfsResolver;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import javax.annotation.Nullable;
import java.io.File;

/**
 * Created 11:12 PM 31 January 2016.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class BusinessProcessDiagramVfsResolver implements DiagramVfsResolver<VirtualFile> {

    @Override
    public String getQualifiedName(final VirtualFile t) {
        return t.getPath();
    }

    @Nullable
    @Override
    public VirtualFile resolveElementByFQN(final String s, final Project project) {
        return LocalFileSystem.getInstance().findFileByIoFile(new File(s));
    }
}
