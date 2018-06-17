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
import com.intellij.idea.plugin.hybris.impex.psi.ImpexMacroUsageDec
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil.findChildrenOfAnyType
import com.intellij.util.SmartList

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
class ImpexMacrosFoldingBuilder : FoldingBuilderEx() {

    override fun getPlaceholderText(node: ASTNode): String? {
        val macroName = node.text

        if ("\$config" == macroName) {
            return null
        }
        val file = node.psi.containingFile
        val foundDec = findMacrosDeclaration(file, macroName)

        return if (foundDec != null) {
            "{${printText(foundDec)}}"
        } else {
            null
        }
    }

    private fun printText(declaration: ImpexMacroDeclaration): String {
        val sb = StringBuilder()
        declaration.node.getChildren(null).forEach {
            when {
                it.elementType == ImpexTypes.MACRO_VALUE_DEC -> sb.append(it.text)
                it.elementType == ImpexTypes.MACRO_USAGE_DEC -> {
                    if (it.text == "\$config-") {
                        sb.append(it.text)
                    } else {
                        val dec = findMacrosDeclaration(declaration.containingFile, it.text)
                        if (dec != null) {
                            sb.append(printText(dec))
                        }
                    }
                }
                else -> sb.append(it.text)
            }
        }
        return sb.toString().substringAfter('=').trim()
    }

    private fun findMacrosDeclaration(file: PsiFile, macroName: String): ImpexMacroDeclaration? {
        val declarations = findChildrenOfAnyType(file, ImpexMacroDeclaration::class.java)
        return declarations.find { it.firstChild.text == macroName }
    }

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        if (root is ImpexFile) {
            val results = SmartList<FoldingDescriptor>()
            val macros = findChildrenOfAnyType(root, ImpexMacroUsageDec::class.java)
            macros.forEach { results.add(FoldingDescriptor(it.node, it.textRange, null)) }
            return results.toTypedArray()
        }
        return FoldingDescriptor.EMPTY
    }

    override fun isCollapsedByDefault(node: ASTNode) = false
}
