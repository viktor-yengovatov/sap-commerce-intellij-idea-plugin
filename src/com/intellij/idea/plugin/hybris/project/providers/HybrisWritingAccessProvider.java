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

package com.intellij.idea.plugin.hybris.project.providers;

import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.WritingAccessProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by Martin Zdarsky-Jones on 30/09/2016.
 */
public class HybrisWritingAccessProvider extends WritingAccessProvider {

    private final Project myProject;

    public HybrisWritingAccessProvider(@NotNull final Project project) {
        myProject = project;
    }

    @NotNull
    @Override
    public Collection<VirtualFile> requestWriting(final VirtualFile... files) {
        final List<VirtualFile> writingDenied = new ArrayList<>();
        for (VirtualFile file : files) {
            if (isFileReadOnly(file)) {
                writingDenied.add(file);
            }
        }
        return writingDenied;
    }

    @Override
    public boolean isPotentiallyWritable(@NotNull final VirtualFile file) {
        return !isFileReadOnly(file);
    }

    protected boolean isFileReadOnly(@NotNull final VirtualFile file) {
        return Optional.ofNullable(ModuleUtilCore.findModuleForFile(file, myProject))
                       .map(module -> module.getOptionValue(HybrisConstants.READ_ONLY))
                       .map(Boolean::parseBoolean)
                       .orElse(false);
    }
}
