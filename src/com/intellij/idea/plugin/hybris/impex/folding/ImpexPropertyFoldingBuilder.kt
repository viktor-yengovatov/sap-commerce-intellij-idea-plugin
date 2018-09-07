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

package com.intellij.idea.plugin.hybris.impex.folding

import com.intellij.idea.plugin.hybris.impex.psi.ImpexFile
import com.intellij.idea.plugin.hybris.impex.psi.ImpexMacroDeclaration
import com.intellij.idea.plugin.hybris.impex.psi.ImpexVisitor
import com.intellij.idea.plugin.hybris.settings.HybrisApplicationSettingsComponent
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.lang.properties.IProperty
import com.intellij.lang.properties.PropertiesImplUtil
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.util.SmartList

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
class ImpexPropertyFoldingBuilder : FoldingBuilderEx() {

    private val varPlaceholderRegexp = "\\$\\{(.+?)}".toRegex()
    private fun isUseSmartFolding() = HybrisApplicationSettingsComponent.getInstance().state.isUseSmartFolding

    override fun getPlaceholderText(node: ASTNode): String? {
        val key = node.text
        val properties = PropertiesImplUtil.findPropertiesByKey(node.psi.project, key)
        return computePlaceholderValue(node, properties)
    }

    private fun computePlaceholderValue(node: ASTNode, properties: List<IProperty>): String? {
        if (properties.isEmpty()) {
            return null
        }

        val value = properties.first().value
        if (value != null) {
            if (isUseSmartFolding() && value.contains(varPlaceholderRegexp)) {
                return computeSmartPlaceholderText(value, node.psi.project)
            }
            return if (value.isEmpty()) node.text else value
        }
        return null
    }

    private fun computeSmartPlaceholderText(input: String, project: Project): String? {
        var value = input
        val matches = varPlaceholderRegexp.findAll(input).toList()
        if (matches.isNotEmpty()) {
            matches.filter { match -> match.groups.isNotEmpty() }
                    .forEach { match -> value = replaceVarPlaceholder(match, project, value) }

            return value
        }
        return input
    }

    private fun replaceVarPlaceholder(match: MatchResult, project: Project, text: String): String {
        val matchGroups = match.groups
        if (matchGroups.size >= 2) {
            val matchPlaceholder = matchGroups.first()!!.value // ${var.var}
            val matchValue = matchGroups.last()!!.value // var.var
            val foundProperty = PropertiesImplUtil.findPropertiesByKey(project, matchValue).first()
            if (foundProperty != null) {
                return text.replace(matchPlaceholder, foundProperty.value!!)
            }
        }
        return text
    }

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        if (root is ImpexFile) {
            val results = SmartList<FoldingDescriptor>()
            root.acceptChildren(object : ImpexVisitor() {
                override fun visitMacroDeclaration(declaration: ImpexMacroDeclaration) {
                    val children = declaration.children
                    if (children.any { it.text.contains("\$config") }) {
                        val configTextElement = children.find { it.text.contains("\$config") }
                        if (configTextElement != null) {
                            val value = configTextElement.nextSibling
                            if (value != null) {
                                results.add(FoldingDescriptor(value.node, value.textRange, null))
                            }
                        }
                    }
                }
            })
            return results.toTypedArray()
        }
        return FoldingDescriptor.EMPTY
    }

    override fun isCollapsedByDefault(node: ASTNode) = true
}
