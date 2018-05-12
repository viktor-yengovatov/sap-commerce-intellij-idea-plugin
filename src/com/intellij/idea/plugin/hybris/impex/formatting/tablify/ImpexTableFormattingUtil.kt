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

package com.intellij.idea.plugin.hybris.impex.formatting.tablify

import com.intellij.formatting.SpacingBuilder
import com.intellij.idea.plugin.hybris.impex.ImpexLanguage
import com.intellij.idea.plugin.hybris.impex.formatting.ImpexCodeStyleSettings
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.util.PsiTreeUtil
import java.util.HashMap

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
fun findRoot(node: ASTNode): ASTNode {
    var node = node
    while (node.treeParent != null) {
        node = node.treeParent
    }
    return node
}


fun createTableSpaceBuilder(settings: CodeStyleSettings): SpacingBuilder {
    val impexSettings = settings.getCustomSettings(ImpexCodeStyleSettings::class.java)

    return SpacingBuilder(settings, ImpexLanguage.getInstance())
            .before(ImpexTypes.ATTRIBUTE_SEPARATOR)
            .spaceIf(impexSettings.SPACE_BEFORE_ATTRIBUTE_SEPARATOR)

            .after(ImpexTypes.COMMA)
            .spaceIf(impexSettings.SPACE_AFTER_COMMA)

            .before(ImpexTypes.COMMA)
            .spaceIf(impexSettings.SPACE_BEFORE_COMMA)

            .after(ImpexTypes.ATTRIBUTE_SEPARATOR)
            .spaceIf(impexSettings.SPACE_AFTER_ATTRIBUTE_SEPARATOR)

            .before(ImpexTypes.FIELD_LIST_ITEM_SEPARATOR)
            .spaceIf(impexSettings.SPACE_BEFORE_FIELD_LIST_ITEM_SEPARATOR)

            .after(ImpexTypes.FIELD_LIST_ITEM_SEPARATOR)
            .spaceIf(impexSettings.SPACE_AFTER_FIELD_LIST_ITEM_SEPARATOR)

            .after(ImpexTypes.ASSIGN_VALUE)
            .spaceIf(impexSettings.SPACE_AFTER_ASSIGN_VALUE)

            .before(ImpexTypes.ASSIGN_VALUE)
            .spaceIf(impexSettings.SPACE_BEFORE_ASSIGN_VALUE)

            .after(ImpexTypes.LEFT_ROUND_BRACKET)
            .spaceIf(impexSettings.SPACE_AFTER_LEFT_ROUND_BRACKET)

            .before(ImpexTypes.RIGHT_ROUND_BRACKET)
            .spaceIf(impexSettings.SPACE_BEFORE_RIGHT_ROUND_BRACKET)

            .after(ImpexTypes.LEFT_SQUARE_BRACKET)
            .spaceIf(impexSettings.SPACE_AFTER_LEFT_SQUARE_BRACKET)

            .before(ImpexTypes.RIGHT_SQUARE_BRACKET)
            .spaceIf(impexSettings.SPACE_BEFORE_RIGHT_SQUARE_BRACKET)

            .after(ImpexTypes.ALTERNATIVE_PATTERN)
            .spaceIf(impexSettings.SPACE_AFTER_ALTERNATIVE_PATTERN)

            .before(ImpexTypes.ALTERNATIVE_PATTERN)
            .spaceIf(impexSettings.SPACE_BEFORE_ALTERNATIVE_PATTERN)
}

fun createCurrentColumnInfoMap(root: ASTNode): HashMap<ASTNode, Map<Int, ImpexColumnInfo<ASTNode>>> {
    val holder = HashMap<ASTNode, Map<Int, ImpexColumnInfo<ASTNode>>>()

    val WHITE_SPACE_SYMBOL_LENGTH = 1
    var withHeader = true
    var child: ASTNode? = root.firstChildNode
    var currentColumnInfoMap = HashMap<Int, ImpexColumnInfo<ASTNode>>()
    while (child != null) {
        var offset = 0
        if (child.elementType == ImpexTypes.HEADER_LINE || (child.elementType == ImpexTypes.ROOT_MACRO_USAGE && child.text.contains("\$START_USERRIGHTS"))) {
            currentColumnInfoMap = HashMap()
            holder[child] = currentColumnInfoMap

            withHeader = child.elementType != ImpexTypes.ROOT_MACRO_USAGE

            var column = 0
            val children = child.getChildren(null)

            for (subChild in children) {
                if (subChild.elementType == ImpexTypes.FULL_HEADER_TYPE) continue
                if (subChild.elementType === ImpexTypes.ANY_HEADER_MODE) {
                    val type = PsiTreeUtil.findSiblingForward(subChild.psi, ImpexTypes.FULL_HEADER_TYPE, null)

                    val length1 = getTextLength(subChild)
                    val length2 = getWithoutStartTrimTextLength(type!!.node)

                    offset = length1 + length2 + WHITE_SPACE_SYMBOL_LENGTH
                    if (!currentColumnInfoMap.containsKey(column)) {
                        currentColumnInfoMap[column] = ImpexColumnInfo(column, offset, offset)
                    } else if (currentColumnInfoMap[column]!!.maxLength < offset) {
                        currentColumnInfoMap[column]!!.maxLength = offset
                    }
                    currentColumnInfoMap[column]!!.addElement(subChild)
                    currentColumnInfoMap[column]!!.addElement(type.node)
                } else {
                    if (subChild.elementType == ImpexTypes.PARAMETERS_SEPARATOR || subChild.elementType == ImpexTypes.FIELD_VALUE_SEPARATOR) column += 1

                    val length = if (getTextLength(subChild) == 0) 1 else getTextLength(subChild)
                    if (!currentColumnInfoMap.containsKey(column)) {
                        currentColumnInfoMap[column] = ImpexColumnInfo(column, length, offset)
                    } else if (currentColumnInfoMap[column]!!.maxLength < length) {
                        currentColumnInfoMap[column]!!.maxLength = length
                    }
                    currentColumnInfoMap[column]!!.addElement(subChild)
                }
            }

        } else if (child.elementType == ImpexTypes.VALUE_LINE) {
            var column = 1
            val children = child.getChildren(null)
            val columnNumber = children.size

            if (children.first().elementType == ImpexTypes.VALUE_SUBTYPE) column -= 1

            for (subChild in children) {
                val length = getTextLength(subChild)
                if (!currentColumnInfoMap.containsKey(column)) {
                    currentColumnInfoMap[column] = ImpexColumnInfo(column, length, offset)
                } else if (currentColumnInfoMap[column]!!.maxLength < length) {
                    currentColumnInfoMap[column]!!.maxLength = length
                    if (!withHeader)
                        currentColumnInfoMap[column]!!.offset = length + WHITE_SPACE_SYMBOL_LENGTH
                }
                currentColumnInfoMap[column]!!.addElements(subChild.getChildren(null).asList())

                if (subChild.elementType == ImpexTypes.VALUE_SUBTYPE) {
                    currentColumnInfoMap[column]!!.addElement(subChild)
                }

                if (subChild.elementType == ImpexTypes.VALUE_GROUP || subChild.elementType == ImpexTypes.VALUE_SUBTYPE) column += 1

                if (column > columnNumber) {
                    column = 1
                }
            }


        }

        child = child.treeNext
    }
    return holder
}

fun getTextLength(node: ASTNode): Int {
    var text = node.text.replace(";", "")
    text = text.trim()
    return text.length
}

fun getWithoutStartTrimTextLength(node: ASTNode): Int {
    var text = node.text.replace(";", "")
    text = text.trimEnd()
    return text.length
}
