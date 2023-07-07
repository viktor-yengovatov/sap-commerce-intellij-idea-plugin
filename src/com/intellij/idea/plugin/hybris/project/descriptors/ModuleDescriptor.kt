/*
 * This file is part of "SAP Commerce Developers Toolset" plugin for Intellij IDEA.
 * Copyright (C) 2023 EPAM Systems <hybrisideaplugin@epam.com>
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
package com.intellij.idea.plugin.hybris.project.descriptors

import com.intellij.idea.plugin.hybris.facet.ExtensionDescriptor
import java.io.File

interface ModuleDescriptor : Comparable<ModuleDescriptor> {
    val name: String
    var groupNames: Array<String>
    val moduleRootDirectory: File
    val rootProjectDescriptor: HybrisProjectDescriptor
    var importStatus: ModuleDescriptorImportStatus
    val descriptorType: ModuleDescriptorType
    var readonly: Boolean

    fun extensionDescriptor(): ExtensionDescriptor
    fun ideaModuleName(): String = (if (groupNames.size == 0) "" else groupNames.joinToString(separator = ".", postfix = ".")) + name
    fun isPreselected(): Boolean
    fun ideaModuleFile(): File
    fun getRelativePath(): String
    fun getRequiredExtensionNames(): Set<String>
    fun addRequiredExtensionNames(extensions: Set<YModuleDescriptor>): Boolean
    fun computeRequiredExtensionNames(moduleDescriptors: Map<String, ModuleDescriptor>)
    fun getSpringFiles(): Set<String>
    fun addSpringFile(file: String): Boolean
    fun getAllDependencies(): Set<ModuleDescriptor>
    fun getDirectDependencies(): Set<ModuleDescriptor>
    fun addDirectDependencies(dependencies: Set<ModuleDescriptor>): Boolean
}
