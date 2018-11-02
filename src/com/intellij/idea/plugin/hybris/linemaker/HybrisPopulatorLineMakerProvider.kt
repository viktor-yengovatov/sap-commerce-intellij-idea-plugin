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

package com.intellij.idea.plugin.hybris.linemaker

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils
import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons
import com.intellij.lang.jvm.JvmModifier
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import com.intellij.psi.SmartPointerManager
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.PsiShortNamesCache
import com.intellij.psi.search.searches.ClassInheritorsSearch

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
class HybrisPopulatorLineMakerProvider : RelatedItemLineMarkerProvider() {

    private var populatorClass: SmartPsiElementPointer<PsiClass>? = null

    override fun collectNavigationMarkers(psiClass: PsiElement,
                                          result: MutableCollection<in RelatedItemLineMarkerInfo<PsiElement>>) {
        if (psiClass is PsiClass) {
            val project = psiClass.project

            if (populatorClass == null) {
                populatorClass = findPopulatorClass(project)
            }

            val allPopulators = ClassInheritorsSearch.search(populatorClass!!.element!!)

            psiClass.fields
                    .filter { it.isNotStatic() }
                    .filter {
                        it.type is PsiClassReferenceType
                                && (it.type as PsiClassReferenceType).reference.qualifiedName == "de.hybris.platform.servicelayer.dto.converter.Converter"
                    }
                    .forEach { field ->

                        val fieldName = field.name.replace("Converter", "")
                        val candidates = allPopulators.filter { it.name!!.contains(fieldName, true) }

                        if (candidates.isNotEmpty()) {
                            createTargetsWithGutterIcon(result, field.nameIdentifier, candidates)
                        }
                    }
        }
    }

    private fun findPopulatorClass(project: Project): SmartPsiElementPointer<PsiClass> {
        val populators = PsiShortNamesCache.getInstance(project).getClassesByName("Populator", GlobalSearchScope.allScope(project))
        val populator = populators.first { it.qualifiedName == "de.hybris.platform.converters.Populator" }
        return SmartPointerManager.getInstance(project).createSmartPsiElementPointer<PsiClass>(populator)
    }

    private fun createTargetsWithGutterIcon(
            result: MutableCollection<in RelatedItemLineMarkerInfo<PsiElement>>,
            psiElement: PsiElement,
            list: Collection<PsiElement>
    ) {
        val builder = NavigationGutterIconBuilder.create(HybrisIcons.Gutter.POPULATOR).setTargets(list)

        builder.setEmptyPopupText(HybrisI18NBundleUtils.message(
                "hybris.gutter.navigate.no.matching.populators",
                *arrayOfNulls<Any>(0)
        ))

        builder.setPopupTitle(HybrisI18NBundleUtils.message(
                "hybris.gutter.populator.class.navigate.choose.class.title",
                *arrayOfNulls<Any>(0)
        ))
        builder.setTooltipText(HybrisI18NBundleUtils.message(
                "hybris.gutter.populator.class.tooltip.navigate.declaration", *arrayOfNulls<Any>(0)
        ))
        result.add(builder.createLineMarkerInfo(psiElement))
    }
}

private fun PsiField.isNotStatic() = !this.hasModifier(JvmModifier.STATIC)