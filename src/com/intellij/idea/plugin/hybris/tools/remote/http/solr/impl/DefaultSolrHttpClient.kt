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
import com.intellij.idea.plugin.hybris.tools.remote.http.solr.SolrCoreData
import com.intellij.idea.plugin.hybris.tools.remote.http.solr.SolrHttpClient
import com.intellij.idea.plugin.hybris.tools.remote.http.solr.SolrQueryObject
import com.intellij.openapi.project.Project
import com.intellij.util.castSafelyTo
import com.intellij.util.containers.mapSmartNotNull
import org.apache.http.HttpStatus
import org.apache.solr.client.solrj.SolrServerException
import org.apache.solr.client.solrj.impl.HttpSolrClient
import org.apache.solr.client.solrj.request.CoreAdminRequest
import org.apache.solr.client.solrj.response.CoreAdminResponse
import org.apache.solr.common.params.CoreAdminParams
import org.apache.solr.common.util.NamedList

class DefaultSolrHttpClient : SolrHttpClient {

    override fun coresData(project: Project): Array<SolrCoreData> {
        return coresData(project, solrConnectionSettings(project))
    }

    override fun coresData(project: Project, connectionSettings: HybrisRemoteConnectionSettings): Array<SolrCoreData> {
        return CoreAdminRequest()
                .apply {
                    setAction(CoreAdminParams.CoreAdminAction.STATUS)
                    setBasicAuthCredentials(connectionSettings.adminLogin, connectionSettings.adminPassword)
                }
                .runCatching { process(HttpSolrClient.Builder(connectionSettings.generatedURL).build()) }
                .map { parseCoreResponse(it) }
                .getOrElse {
                    when (it) {
                        is SolrServerException -> emptyArray()
                        else -> throw it
                    }
                }
    }

    private fun parseCoreResponse(response: CoreAdminResponse) =
            response.coreStatus.asShallowMap().values.castSafelyTo<Collection<Map<Any, Any>>>()!!.mapSmartNotNull { buildSolrCoreData(it) }.toTypedArray()

    private fun buildSolrCoreData(it: Map<Any, Any>) =
            SolrCoreData(it["name"] as String, (it["index"] as NamedList<*>)["numDocs"] as Int)

    override fun listOfCores(project: Project): Array<String> {
        return listOfCores(project, solrConnectionSettings(project))
    }

    override fun listOfCores(project: Project, connectionSettings: HybrisRemoteConnectionSettings): Array<String> {
        return coresData(project, connectionSettings).map { data -> data.core }.toTypedArray()
    }

    private fun getHttpSolrClient(url: String): HttpSolrClient {
        return HttpSolrClient.Builder(url).build()
    }

    override fun executeSolrQuery(project: Project,
                                  queryObject: SolrQueryObject): HybrisHttpResult {
        HybrisHttpResult.HybrisHttpResultBuilder.createResult().errorMessage("it.message").httpCode(HttpStatus.SC_BAD_GATEWAY)
        return HybrisHttpResult.HybrisHttpResultBuilder.createResult().output("Dummy result").build()
    }

    override fun executeSolrQuery(project: Project,
                                  solrConnectionSettings: HybrisRemoteConnectionSettings,
                                  queryObject: SolrQueryObject): HybrisHttpResult {
        return HybrisHttpResult.HybrisHttpResultBuilder.createResult().output("Dummy result").build()
    }

    // active or default
    private fun solrConnectionSettings(project: Project): HybrisRemoteConnectionSettings {
        return HybrisDeveloperSpecificProjectSettingsComponent.getInstance(project).getDefaultSolrRemoteConnectionSettings(project)
    }
}