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
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.lang.properties.PropertiesImplUtil
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiElement
import com.intellij.util.SmartList

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
class ImpexPropertyFoldingBuilder : FoldingBuilderEx() {

    override fun getPlaceholderText(node: ASTNode): String? {
        val key = node.text.replace("\$config", "").replace("-", "")
        val properties = PropertiesImplUtil.findPropertiesByKey(node.psi.project, key)
        return if (properties.isNotEmpty())
            "-" + properties.first().value
        else
            null
    }

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        if (root is ImpexFile) {
            val results = SmartList<FoldingDescriptor>()
            root.acceptChildren(object : ImpexVisitor() {
                override fun visitMacroDeclaration(declaration: ImpexMacroDeclaration) {
                    val children = declaration.children
                    if (children.any { it.text.contains("\$config") }) {
                        val configTextElement = children.find { it.text.contains("\$config") }
                        val value = configTextElement!!.nextSibling
                        results.add(FoldingDescriptor(value.node, value.textRange, null))
                    }
                }
            })
            return results.toTypedArray()
        }
        return FoldingDescriptor.EMPTY
    }

    override fun isCollapsedByDefault(node: ASTNode) = true
}
