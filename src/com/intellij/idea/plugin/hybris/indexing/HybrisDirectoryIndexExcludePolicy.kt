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

package com.intellij.idea.plugin.hybris.indexing

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootModel
import com.intellij.openapi.roots.impl.DirectoryIndexExcludePolicy
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileVisitor
import com.intellij.openapi.vfs.pointers.VirtualFilePointer
import com.intellij.openapi.vfs.pointers.VirtualFilePointerManager

class HybrisDirectoryIndexExcludePolicy(val project : Project) : DirectoryIndexExcludePolicy {

    companion object {
        private val EXCLUDED_FOLDER_PATHS = setOf("smartedit-custom-build", "smartedit-build", "node_modules")
        private val EXCLUDED_FOLDER_PATHS_BY_KEY = mapOf(
                "smartedit" to setOf("common/temp"))
    }

    override fun getExcludeRootsForModule(rootModel: ModuleRootModel): Array<VirtualFilePointer> {
        val contentRoots = rootModel.contentRoots
        val excludedFoldersFromIndex = mutableListOf<VirtualFilePointer>()
        contentRoots.forEach { contentRoot ->
            excludedFoldersFromIndex.addAll(getExcludedFoldersFromIndex(contentRoot))
        }
        return excludedFoldersFromIndex.toTypedArray()
    }

    private fun getExcludedFoldersFromIndex(contentRoot : VirtualFile) : List<VirtualFilePointer>  {
        val excludedFoldersFromIndex = mutableListOf<VirtualFilePointer>()
        EXCLUDED_FOLDER_PATHS_BY_KEY.forEach { excludedFolderPathMap ->
            if (contentRoot.parent.name == excludedFolderPathMap.key) {
                EXCLUDED_FOLDER_PATHS_BY_KEY[excludedFolderPathMap.key]?.forEach { excludedFolderPath ->
                    VfsUtilCore.visitChildrenRecursively(contentRoot, HybrisExcludeFromIndexVirtualFileVisitor(project,
                            excludedFolderPath, excludedFoldersFromIndex, VirtualFileVisitor.SKIP_ROOT))
                }
            }
        }
        EXCLUDED_FOLDER_PATHS.forEach { excludedFolderPath ->
            VfsUtilCore.visitChildrenRecursively(contentRoot, HybrisExcludeFromIndexVirtualFileVisitor(project,
                excludedFolderPath, excludedFoldersFromIndex, VirtualFileVisitor.SKIP_ROOT))
        }
        return excludedFoldersFromIndex
    }

    class HybrisExcludeFromIndexVirtualFileVisitor(
        private val project: Project,
        excludedFolderPath: String,
        private val excludedFoldersFromIndex: MutableList<VirtualFilePointer>,
        vararg options: Option?
    ) : VirtualFileVisitor<VirtualFile>(*options) {

        companion object {
            private val VIRTUAL_FILE_POINTER_MANAGER = VirtualFilePointerManager.getInstance()
        }

        private val pathFragments: List<String> = excludedFolderPath.split("/")

        private var currentDepth = 0

        override fun visitFile(file: VirtualFile): Boolean {
            return file.isDirectory && pathFragments.size > currentDepth &&
                isFolderNameEqualToPathFragment(file, currentDepth)
        }

        override fun visitFileEx(file: VirtualFile): Result {
            if (visitFile(file)) {
                currentDepth++
                return CONTINUE
            }
            return SKIP_CHILDREN
        }

        override fun afterChildrenVisited(file: VirtualFile) {
            if (isFolderNameEqualToPathFragment(file, pathFragments.size - 1)) {
                excludedFoldersFromIndex.add(createVirtualFilePointer(file))
            }
        }

        private fun isFolderNameEqualToPathFragment(file: VirtualFile, fragmentIndex: Int): Boolean {
            return file.name == pathFragments[fragmentIndex]
        }

        private fun createVirtualFilePointer(folder: VirtualFile): VirtualFilePointer {
            return VIRTUAL_FILE_POINTER_MANAGER.create(folder.url, project, null)
        }
    }
}
