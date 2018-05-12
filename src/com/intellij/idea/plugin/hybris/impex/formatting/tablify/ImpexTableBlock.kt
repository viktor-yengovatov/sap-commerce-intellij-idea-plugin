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

import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.Spacing
import com.intellij.formatting.Spacing.createSpacing
import com.intellij.formatting.Wrap
import com.intellij.formatting.WrapType
import com.intellij.idea.plugin.hybris.impex.psi.ImpexElementType
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes
import com.intellij.lang.ASTFactory
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.tree.IElementType
import java.util.ArrayList

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
val DOCUMENT_START: IElementType = ImpexElementType("DOCUMENT_START")

open class ImpexTableBlock(node: ASTNode, protected var formattingInfo: ImpexFormattingInfo) : AbstractBlock(node, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment()) {

    val elementType: IElementType
        get() = node.elementType

    open val textLength: Int
        get() = textRange.length

    private fun validateBlocks(blocks: MutableList<Block>) {
        if (blocks.isEmpty()) {
            return
        }
        blocks.add(0, ImpexTableBlockElement(ASTFactory.leaf(DOCUMENT_START, ""), formattingInfo))
    }

    override fun buildChildren(): List<Block> {
        val todoNodes = ArrayList<Container>()
        val blocks = ArrayList<Block>()
        todoNodes.add(Container(node.firstChildNode))
        var currentField: ImpexTableBlockField? = null
        while (todoNodes.size > 0) {
            val container = todoNodes.removeAt(todoNodes.size - 1)

            if (container.node != null) {
                val elementType = container.node.elementType
                todoNodes.add(Container(container.node.treeNext))
                if (elementType == ImpexTypes.COMMENT) {
                    blocks.add(ImpexDummyTableBlock(container.node, formattingInfo))
                } else if (elementType === ImpexTypes.ROOT_MACRO_USAGE) {
                    todoNodes.add(Container(container.node.firstChildNode))
                } else if (elementType === ImpexTypes.HEADER_LINE) {
                    todoNodes.add(Container(container.node.firstChildNode))
                } else if (elementType === ImpexTypes.ANY_HEADER_MODE) {
                    blocks.add(ImpexDummyTableBlock(container.node, formattingInfo))
                } else if (elementType === ImpexTypes.FULL_HEADER_TYPE) {
                    blocks.add(ImpexHeaderTypeTableBlock(container.node, formattingInfo))
                } else if (elementType === ImpexTypes.FULL_HEADER_PARAMETER) {
                    blocks.add(ImpexTableBlockField(container.node, formattingInfo))
                } else if (elementType === ImpexTypes.VALUE_LINE) {
                    todoNodes.add(Container(container.node.firstChildNode, true))
                } else if (elementType === ImpexTypes.VALUE_GROUP) {
                    todoNodes.add(Container(container.node.firstChildNode, container.isSpecial))
                } else if (elementType === ImpexTypes.VALUE) {
                    currentField = if (container.isSpecial) ImpexTableSpecialField(container.node, formattingInfo) else ImpexTableBlockField(container.node, formattingInfo)
                    if (currentField.textLength > 0) {
                        blocks.add(currentField)
                    }
                } else if (elementType === ImpexTypes.VALUE_SUBTYPE) {
                    currentField = ImpexTableBlockField(container.node, formattingInfo)
                    if (currentField.textLength > 0) {
                        blocks.add(currentField)
                    }
                } else if (elementType === ImpexTypes.FIELD_VALUE_SEPARATOR || elementType === ImpexTypes.PARAMETERS_SEPARATOR) {
                    blocks.add(if (container.isSpecial) ImpexTableSpecialField(container.node, formattingInfo) else ImpexTableSeparatorField(container.node, formattingInfo))
                } else if (elementType === ImpexTypes.COMMA || elementType === ImpexTypes.CRLF) {
                    blocks.add(ImpexTableBlockElement(container.node, formattingInfo, currentField))
                } else if (elementType !== TokenType.WHITE_SPACE && container.node.textLength > 0) {
                    blocks.add(ImpexDummyTableBlock(container.node, formattingInfo))
                }
            }
        }
        validateBlocks(blocks)
        return blocks
    }

    override fun getIndent() = null

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        var spacing: Spacing? = null
        if (child1 != null && child1 !is ImpexDummyTableBlock && child2 !is ImpexDummyTableBlock) {
            val block1 = child1 as ImpexTableBlockElement?
            val block2 = child2 as ImpexTableBlockElement
            spacing = if (block2 is ImpexTableSeparatorField) {
                val spaces = if (block1 is ImpexTableSeparatorField) {
                    block1.columnInfo!!.maxLength + getAdditionalSpaces(block1, block2)
                } else {
                    if (block1 is ImpexHeaderTypeTableBlock) getAdditionalSpaces(block1, block2) else block1!!.columnInfo!!.maxLength - block1.field!!.textLength + getAdditionalSpaces(block1, block2)
                }
                createSpacing(spaces, spaces, 0, true, 0)
            } else {
                val spaces = getAdditionalSpaces(block1, block2)
                createSpacing(spaces, spaces, 0, true, 0)
            }
        }
        return spacing
    }

    private fun getAdditionalSpaces(block1: ImpexTableBlockElement?, block2: ImpexTableBlockElement): Int {
        var spaces = 0
        if (formattingInfo.impexCodeStyleSettings.SPACE_AFTER_FIELD_VALUE_SEPARATOR && block1!!.elementType === ImpexTypes.FIELD_VALUE_SEPARATOR) {
            ++spaces
        }
        if (formattingInfo.impexCodeStyleSettings.SPACE_AFTER_PARAMETERS_SEPARATOR && block1!!.elementType === ImpexTypes.PARAMETERS_SEPARATOR) {
            ++spaces
        }
        if (formattingInfo.impexCodeStyleSettings.SPACE_BEFORE_FIELD_VALUE_SEPARATOR && block2.elementType === ImpexTypes.FIELD_VALUE_SEPARATOR) {
            ++spaces
        }
        if (formattingInfo.impexCodeStyleSettings.SPACE_BEFORE_PARAMETERS_SEPARATOR && block2.elementType === ImpexTypes.PARAMETERS_SEPARATOR) {
            ++spaces
        }

        if (block2 is ImpexTableSpecialField) {
            spaces += block2.columnInfo!!.offset
        }

        return spaces
    }

    override fun isLeaf() = node.firstChildNode == null
}


open class ImpexTableBlockElement @JvmOverloads constructor(node: ASTNode, formattingInfo: ImpexFormattingInfo, open var field: ImpexTableBlockField? = null) : ImpexTableBlock(node, formattingInfo) {

    open var columnInfo: ImpexColumnInfo<ASTNode>? = null
        get() = if (this.field == null) null else this.field!!.columnInfo

    override fun buildChildren(): List<Block> = emptyList()
    override fun getSpacing(block1: Block?, block2: Block): Spacing? = null
}

open class ImpexTableBlockField(node: ASTNode, formattingInfo: ImpexFormattingInfo) : ImpexTableBlockElement(node, formattingInfo, null) {

    override var columnInfo: ImpexColumnInfo<ASTNode>? = formattingInfo.getColumnInfo(node)

    override var field: ImpexTableBlockField? = null
        get() = this

    override val textLength: Int
        get() = getTextLength(node)

    override fun buildChildren(): List<Block> {
        var node = this.node.firstChildNode
        val blocks = ArrayList<Block>()
        while (node != null) {
            if (node.elementType !== TokenType.WHITE_SPACE) {
                val block = ImpexTableBlockElement(node, formattingInfo, this)
                blocks.add(block)
            }
            node = node.treeNext
        }
        return blocks
    }

    override fun getSpacing(block1: Block?, block2: Block): Spacing? {
        if (block1 == null) {
            return null
        }
        return formattingInfo.spacingBuilder.getSpacing(this, block1, block2)
    }
}

class ImpexDummyTableBlock(node: ASTNode, formattingInfo: ImpexFormattingInfo) : ImpexTableBlock(node, formattingInfo) {
    override fun buildChildren(): List<Block> = emptyList()

    override fun getSpacing(block1: Block?, block2: Block): Spacing? = null
}

class ImpexTableSpecialField(node: ASTNode, formattingInfo: ImpexFormattingInfo) : ImpexTableBlockField(node, formattingInfo)
class ImpexHeaderTypeTableBlock(node: ASTNode, formattingInfo: ImpexFormattingInfo) : ImpexTableBlockField(node, formattingInfo)
class ImpexTableSeparatorField(node: ASTNode, formattingInfo: ImpexFormattingInfo) : ImpexTableBlockField(node, formattingInfo)

class Container(val node: ASTNode?, val isSpecial: Boolean = false)