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

package com.intellij.idea.plugin.hybris.settings

import org.apache.commons.lang3.StringUtils
import java.io.Serializable
import java.util.*

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
data class SolrConnectionSettings(
        var uuid: String = "",
        var displayName: String = "",
        var hostIP: String = "",
        var port: String = "",
        var solrWebroot: String = "",
        var adminLogin: String = "",
        var adminPassword: String = "",
        var generatedURL: String = ""
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as SolrConnectionSettings?
        return uuid == that!!.uuid
    }

    override fun hashCode(): Int {
        return Objects.hash(uuid)
    }

    override fun toString(): String {
        return if (!StringUtils.isBlank(displayName)) {
            displayName
        } else generatedURL
    }
}

