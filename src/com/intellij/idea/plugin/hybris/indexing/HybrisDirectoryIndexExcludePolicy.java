/*
 * This file is part of "SAP Commerce Developers Toolset" plugin for Intellij IDEA.
 * Copyright (C) 2019 EPAM Systems <hybrisideaplugin@epam.com>
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

package com.intellij.idea.plugin.hybris.indexing;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootModel;
import com.intellij.openapi.roots.impl.DirectoryIndexExcludePolicy;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileVisitor;
import com.intellij.openapi.vfs.pointers.VirtualFilePointer;
import com.intellij.openapi.vfs.pointers.VirtualFilePointerManager;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HybrisDirectoryIndexExcludePolicy implements DirectoryIndexExcludePolicy {

    private static final Set<String> EXCLUDED_FOLDER_PATHS =
        Set.of("smartedit-custom-build", "smartedit-build");
    private static final VirtualFilePointerManager VIRTUAL_FILE_POINTER_MANAGER =
        VirtualFilePointerManager.getInstance();

    private final Project project;

    public HybrisDirectoryIndexExcludePolicy(final Project project) {
        this.project = project;
    }

    @Override
    public VirtualFilePointer @NotNull [] getExcludeRootsForModule(@NotNull final ModuleRootModel rootModel) {
        final VirtualFile[] contentRoots = rootModel.getContentRoots();
        final List<VirtualFilePointer> excludedFoldersFromIndex = new ArrayList<>();
        for (VirtualFile contentRoot : contentRoots) {
            final List<VirtualFilePointer> excludedFolders = getExcludedFoldersFromIndex(contentRoot);
            excludedFoldersFromIndex.addAll(excludedFolders);
        }
        return excludedFoldersFromIndex.toArray(VirtualFilePointer[]::new);
    }

    private List<VirtualFilePointer> getExcludedFoldersFromIndex(final VirtualFile contentRoot) {
        final List<VirtualFilePointer> excludedFoldersFromIndex = new ArrayList<>();
        for (String excludedFolderPath : EXCLUDED_FOLDER_PATHS) {
            final String[] pathFragments = excludedFolderPath.split("/");

            VfsUtilCore.visitChildrenRecursively(contentRoot,
                new VirtualFileVisitor<VirtualFile>(VirtualFileVisitor.SKIP_ROOT) {

                private int currentDepth;

                @Override
                public boolean visitFile(@NotNull final VirtualFile file) {
                    return file.isDirectory() && pathFragments.length > currentDepth &&
                       StringUtils.equals(file.getName(), pathFragments[currentDepth]);
                }

                @Override
                public @NotNull Result visitFileEx(@NotNull final VirtualFile file) {
                    if (visitFile(file)) {
                        currentDepth++;
                        return CONTINUE;
                    }
                    return SKIP_CHILDREN;
                }

                @Override
                public void afterChildrenVisited(@NotNull final VirtualFile file) {
                    if (StringUtils.equals(file.getName(), pathFragments[pathFragments.length - 1])) {
                        final var excludedFolder = createVirtualFilePointer(file);
                        excludedFoldersFromIndex.add(excludedFolder);
                    }
                }
            });
        }
        return excludedFoldersFromIndex;
    }

    private VirtualFilePointer createVirtualFilePointer(final VirtualFile folder) {
        return VIRTUAL_FILE_POINTER_MANAGER.create(folder.getUrl(), project, null);
    }
}
