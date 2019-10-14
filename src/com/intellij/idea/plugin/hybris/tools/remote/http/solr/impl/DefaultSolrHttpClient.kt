/*
 * This file is part of "SAP Commerce Developers Toolset" plugin for Intellij IDEA.
 * Copyright (C) 2019 EPAM Systems <hybrisideaplugin@epam.com>
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

package com.intellij.idea.plugin.hybris.tools.remote.http.solr.impl

import com.intellij.idea.plugin.hybris.settings.HybrisDeveloperSpecificProjectSettingsComponent
import com.intellij.idea.plugin.hybris.settings.HybrisRemoteConnectionSettings
import com.intellij.idea.plugin.hybris.tools.remote.http.impex.HybrisHttpResult
import com.intellij.idea.plugin.hybris.tools.remote.http.solr.SolrHttpClient
import com.intellij.idea.plugin.hybris.tools.remote.http.solr.SolrQueryObject
import com.intellij.openapi.project.Project

class DefaultSolrHttpClient : SolrHttpClient {

    val cores_path = "/admin/cores"

    override fun listOfCores(project: Project): Array<String> {
        return arrayOf("ONE", "TWO")
    }

    override fun executeSolrQuery(project: Project,
                                  queryObject: SolrQueryObject): HybrisHttpResult {
        return HybrisHttpResult.HybrisHttpResultBuilder.createResult().output("Dummy result").build()
    }

    override fun executeSolrQuery(project: Project,
                                  solrConnectionSettings: HybrisRemoteConnectionSettings,
                                  queryObject: SolrQueryObject): HybrisHttpResult {
        return HybrisHttpResult.HybrisHttpResultBuilder.createResult().output("Dummy result").build()
    }

    override fun listOfCores(project: Project, solrConnectionSettings: HybrisRemoteConnectionSettings): Array<String> {
        return arrayOf("ONE", "TWO")
    }

    // active or default
    private fun solrConnectionSettings(project: Project): HybrisRemoteConnectionSettings {
        return HybrisDeveloperSpecificProjectSettingsComponent.getInstance(project).getActiveHybrisRemoteConnectionSettings(project)
    }
}