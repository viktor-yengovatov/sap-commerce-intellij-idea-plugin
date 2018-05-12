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

package com.intellij.idea.plugin.hybris.impex.formatting

import com.intellij.formatting.Alignment
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.idea.plugin.hybris.impex.formatting.simple.ImpexBlock
import com.intellij.idea.plugin.hybris.impex.formatting.simple.createSimpleSpaceBuilder
import com.intellij.idea.plugin.hybris.impex.formatting.tablify.ImpexFormattingInfo
import com.intellij.idea.plugin.hybris.impex.formatting.tablify.ImpexTableBlock
import com.intellij.idea.plugin.hybris.impex.formatting.tablify.createCurrentColumnInfoMap
import com.intellij.idea.plugin.hybris.impex.formatting.tablify.createTableSpaceBuilder
import com.intellij.idea.plugin.hybris.impex.formatting.tablify.findRoot
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
class ImpexFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(element: PsiElement, settings: CodeStyleSettings): FormattingModel {
        val impexCodeStyleSettings = settings.getCustomSettings(ImpexCodeStyleSettings::class.java)

        if (impexCodeStyleSettings.TABLIFY) {
            val root = findRoot(element.node)
            val formattingInfo = ImpexFormattingInfo(
                    settings,
                    createTableSpaceBuilder(settings),
                    createCurrentColumnInfoMap(root)
            )

            return FormattingModelProvider.createFormattingModelForPsiFile(
                    element.containingFile,
                    ImpexTableBlock(root, formattingInfo),
                    settings
            )
        } else {
            val impexBlock = ImpexBlock(
                    element.node,
                    null,
                    Alignment.createAlignment(),
                    createSimpleSpaceBuilder(settings)
            )

            return FormattingModelProvider.createFormattingModelForPsiFile(
                    element.containingFile,
                    impexBlock,
                    settings
            )

        }
    }


    override fun getRangeAffectingIndent(file: PsiFile, offset: Int, elementAtOffset: ASTNode): TextRange? = null
}
