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

import com.intellij.diagram.BaseDiagramProvider;
import com.intellij.diagram.DiagramColorManager;
import com.intellij.diagram.DiagramDataModel;
import com.intellij.diagram.DiagramElementManager;
import com.intellij.diagram.DiagramPresentationModel;
import com.intellij.diagram.DiagramProvider;
import com.intellij.diagram.DiagramVfsResolver;
import com.intellij.diagram.extras.DiagramExtras;
import com.intellij.idea.plugin.hybris.utils.HybrisI18NBundleUtils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created 11:08 PM 31 January 2016.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class BusinessProcessDiagramProvider extends BaseDiagramProvider<VirtualFile> {

    public static final String ID = "HybrisBusinessProcessDiagramProvider";

    protected DiagramElementManager<VirtualFile> elementManager = new BusinessProcessDiagramElementManager();
    protected DiagramVfsResolver<VirtualFile> vfsResolver = new BusinessProcessDiagramVfsResolver();
    protected DiagramExtras<VirtualFile> extras = new BusinessProcessDiagramExtras();
    protected DiagramColorManager myColorManager = new BusinessProcessDiagramColorManager();

    @Pattern("[a-zA-Z0-9_-]*")
    @Override

    public String getID() {
        return ID;
    }

    @Override
    public DiagramElementManager<VirtualFile> getElementManager() {
        return this.elementManager;
    }

    @Override
    public DiagramVfsResolver<VirtualFile> getVfsResolver() {
        return this.vfsResolver;
    }

    @Override
    public String getPresentableName() {
        return HybrisI18NBundleUtils.message("hybris.business.process.provider.name");

    }

    @NotNull
    @Override
    public DiagramExtras<VirtualFile> getExtras() {
        return this.extras;
    }

    @Override
    public DiagramDataModel<VirtualFile> createDataModel(@NotNull final Project project,
                                                         @Nullable final VirtualFile t,
                                                         @Nullable final VirtualFile virtualFile,
                                                         final DiagramPresentationModel diagramPresentationModel) {
        return new BusinessProcessDiagramDataModel(project, t);
    }

    @Override
    public DiagramColorManager getColorManager() {
        return this.myColorManager;
    }

    public static DiagramProvider<VirtualFile> getInstance() {
        return DiagramProvider.findByID(ID);
    }
}
