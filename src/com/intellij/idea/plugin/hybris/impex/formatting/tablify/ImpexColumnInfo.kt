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

import java.util.ArrayList
import java.util.Collections

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
class ImpexColumnInfo<T>(val columnIndex: Int, var maxLength: Int, var offset: Int) {

    private val elements: MutableList<T>

    init {
        this.elements = ArrayList()
    }

    fun getElements(): List<T> {
        return Collections.unmodifiableList(elements)
    }

    fun addElement(element: T): Boolean {
        return elements.add(element)
    }

    fun addElements(els: List<T>): Boolean {
        return elements.addAll(els)
    }

    fun addElement(element: T, row: Int) {
        if (row == elements.size) {
            addElement(element)
        } else if (row < elements.size) {
            elements[row] = element
        } else {
            elements.addAll(Collections.nCopies<T>(row - elements.size, null))
            addElement(element)
        }
    }
}
