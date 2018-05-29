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

package com.intellij.idea.plugin.hybris.impex.completion.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.properties.IProperty
import com.intellij.lang.properties.PropertiesFileType
import com.intellij.lang.properties.psi.PropertiesFile
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.search.DelegatingGlobalSearchScope
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.GlobalSearchScope.everythingScope
import com.intellij.psi.search.GlobalSearchScope.getScopeRestrictedByFileTypes
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import java.util.ArrayList


/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
class ImpexMacrosConfigCompletionProvider : CompletionProvider<CompletionParameters>() {

    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val psiElementUnderCaret = parameters.position
        val prevLeaf = PsiTreeUtil.prevLeaf(psiElementUnderCaret)
        if (prevLeaf != null && prevLeaf.text.contains("\$config")) {
            val position = parameters.position
            val query = getQuery(position)
            val module = ModuleUtil.findModuleForPsiElement(position)
            PropertiesUtil.findProperties(module!!, query).forEach { 
                result.addElement(LookupElementBuilder.create("${it.key}\n").withIcon(AllIcons.Nodes.Property)) 
            }
        }
    }

    private fun getQuery(position: PsiElement) = position.text.replace("-", "")
            .replace("IntellijIdeaRulezzz", "")
}

fun GlobalSearchScope.filter(filter: (VirtualFile) -> Boolean) = object : DelegatingGlobalSearchScope(this) {
    override fun contains(file: VirtualFile): Boolean {
        return filter(file) && super.contains(file)
    }
}

private fun GlobalSearchScope.or(otherScope: GlobalSearchScope): GlobalSearchScope = union(otherScope)

private object PropertiesUtil {
    fun findProperties(module: Module, query: String): List<IProperty> {
        val result: MutableList<IProperty> = ArrayList()
        val configModule = obtainConfigModule(module)
        val scope = createSearchScope(module, configModule)
        val files = FileTypeIndex.getFiles(PropertiesFileType.INSTANCE, scope)
        for (virtualFile in files) {
            val file = PsiManager.getInstance(module.project).findFile(virtualFile) as PropertiesFile?
            if (file != null) {
                val properties = file.properties
                for (property in properties) {
                    if ((property.key != null && property.key!!.contains(query)) || query.isBlank()) {
                        result.add(property)
                    }
                }
            }
        }
        return result
    }

    private fun createSearchScope(module: Module, configModule: Module) =
            getScopeRestrictedByFileTypes(everythingScope(module.project), PropertiesFileType.INSTANCE)
                    .filter { it.name.contains("project") }.or(configModule.moduleContentScope)

    private fun obtainConfigModule(module: Module) =
            ModuleManager.getInstance(module.project).modules.first { it.name == "config" }
}