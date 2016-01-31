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

package com.intellij.idea.plugin.hybris.business.process.diagram;

import com.intellij.diagram.DiagramNodeBase;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;

import javax.annotation.Nonnull;

/**
 * Created 11:34 PM 31 January 2016.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class FileNode extends DiagramNodeBase<VirtualFile> {

    private final VirtualFile myFile;

    public FileNode(VirtualFile file) {
        super(BusinessProcessDiagramProvider.getInstance());
        myFile = file;
    }

    @Nonnull
    @Override
    public String getTooltip() {
        return getIdentifyingElement().getCanonicalPath();
    }

    @Override
    public Icon getIcon() {
        return myFile.isDirectory() ? AllIcons.Nodes.Folder : myFile.getFileType().getIcon();
    }

    @Nonnull
    @Override
    public VirtualFile getIdentifyingElement() {
        return myFile;
    }
}