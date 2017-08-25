package com.intellij.idea.plugin.hybris.impex.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.idea.plugin.hybris.impex.inspection.analyzer.*
import com.intellij.idea.plugin.hybris.impex.psi.ImpexHeaderLine
import com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil


class NoUniqueValueInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor = NoUniqueValueVisitor(holder)
}

class NoUniqueValueVisitor(private val problemsHolder: ProblemsHolder) : PsiElementVisitor() {

    override fun visitFile(file: PsiFile) {
        val headers = PsiTreeUtil.getChildrenOfType(file, ImpexHeaderLine::class.java) ?: return

        val groupHeaders = headers.groupBy { "${it.fullHeaderType!!.text}|${keyAttrs(it).joinToString { it }}" }

        groupHeaders.forEach { _, headerLines ->
            val fullParametersList = fullParametersList(headerLines)

            val notKeyAttrsList = notKeyAttributesList(fullParametersList)
            val distinctCommonAttrsNames = notKeyAttrsList.map { it.text }.distinct()

            val keyAttrsGroupedByName = fullParametersList.filter { keyAttrPredicate(it) }.groupBy { it.anyHeaderParameterName.text }

            val dataMap = mutableMapOf<String, List<PsiElement>>()
            keyAttrsGroupedByName.forEach { name, attrs -> dataMap.put(name, attrs.flatMap { ImpexPsiUtils.getColumnForHeader(it).map { it.lastChild } }) }

            createTable(dataMap, distinctCommonAttrsNames, notKeyAttrsList).analyze(problemsHolder)
        }
    }
}