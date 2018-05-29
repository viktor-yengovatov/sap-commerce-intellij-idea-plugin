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

package com.intellij.idea.plugin.hybris.impex.constants.modifier

import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettingsComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.GlobalSearchScope.allScope
import com.intellij.psi.search.searches.ClassInheritorsSearch

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
object ImpexProcessorModifier {

    fun values(): Array<ImpexProcessorModifierValue> {
        val project = project()!!
        val clazz = JavaPsiFacade.getInstance(project)
                .findClass("de.hybris.platform.impex.jalo.imp.ImportProcessor", allScope(project))
        if (clazz != null) {
            val extends = ClassInheritorsSearch.search(clazz, GlobalSearchScope.allScope(project), true)
            return extends.map { ImpexProcessorModifierValue(it) }.toTypedArray()
        }
        return emptyArray()
    }
}

class ImpexProcessorModifierValue(val psiClass: PsiClass) : ImpexModifierValue {
    override fun getModifierValue(): String {
        return psiClass.qualifiedName!!
    }
}

private fun project(): Project? {
    for (project in ProjectManager.getInstance().openProjects) {
        val hybrisProjectSettings = HybrisProjectSettingsComponent.getInstance(project).state

        if (!hybrisProjectSettings.isHybrisProject) {
            continue
        }

        return project
    }
    return null
}

