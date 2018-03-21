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

package com.intellij.idea.plugin.hybris.tools.remote.http

import com.intellij.idea.plugin.hybris.common.services.CommonIdeaService
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettingsComponent
import com.intellij.openapi.project.Project
import org.apache.commons.lang3.StringUtils
import org.apache.http.HttpResponse
import org.apache.http.HttpStatus.SC_FORBIDDEN
import org.apache.http.HttpStatus.SC_MOVED_TEMPORARILY
import org.apache.http.client.HttpClient
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.ssl.SSLContexts.custom
import org.jsoup.Connection
import org.jsoup.Connection.Method
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import java.io.IOException
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.X509Certificate
import java.util.Arrays.asList
import javax.net.ssl.*


object HybrisHttpClient {

    private val USER_AGENT = "Mozilla/5.0"
    private var currentSessionId: String = StringUtils.EMPTY

    fun hostHacUrl(project: Project): String = CommonIdeaService.getInstance().getHostHacUrl(project)


    private fun login(project: Project): String {
        val settingsComponent = HybrisProjectSettingsComponent.getInstance(project)
        val validSessionId = validSessionId(project)
        val csrfToken = csrfToken(project, validSessionId)

        val params = asList(
                BasicNameValuePair(
                        "j_username",
                        settingsComponent.state.hacLogin
                ),
                BasicNameValuePair(
                        "j_password",
                        settingsComponent.state.hacPassword
                ),
                BasicNameValuePair("_csrf", csrfToken)
        )

        val response = post(project, hostHacUrl(project) + "/j_spring_security_check", validSessionId, params)
        currentSessionId = CookieParser.getInstance().getSpecialCookie(response.allHeaders)
        return currentSessionId
    }

    fun sessionId(project: Project): String = if (currentSessionId.isNotBlank()) currentSessionId else login(project)


    @Throws(IOException::class, NoSuchAlgorithmException::class, KeyStoreException::class, KeyManagementException::class)
    fun post(project: Project, actionUrl: String, sessionId: String, params: List<BasicNameValuePair>): HttpResponse {

        val csrfToken = csrfToken(project, sessionId)
        val client = createAllowAllClient(6000L)

        val post = HttpPost(actionUrl)

        // add header
        post.setHeader("User-Agent", USER_AGENT)
        post.setHeader("X-CSRF-TOKEN", csrfToken)

        if (sessionId.isNotBlank()) {
            post.setHeader("Cookie", "JSESSIONID=" + sessionId)
        }

        val response: HttpResponse
        try {
            post.entity = UrlEncodedFormEntity(params, "utf-8")
            response = client.execute(post)
        } catch (e: IOException) {
            throw RuntimeException(actionUrl, e)
        }

        if (response.statusLine.statusCode == SC_MOVED_TEMPORARILY) {
            this.currentSessionId = StringUtils.EMPTY
        }
        if (response.statusLine.statusCode == SC_FORBIDDEN) {
            this.currentSessionId = StringUtils.EMPTY
            throw HttpStatusException("", response.statusLine.statusCode, actionUrl)
        }
        return response

    }


    /**
     * @param timeout Timeout in seconds
     */
    @Throws(KeyStoreException::class, NoSuchAlgorithmException::class, KeyManagementException::class)
    private fun createAllowAllClient(timeout: Long): HttpClient {
        val sslcontext = custom().loadTrustMaterial(null) { chain, authType -> true }.build()
        val sslsf = SSLConnectionSocketFactory(sslcontext, ALLOW_ALL_HOSTNAME_VERIFIER)

        val config = RequestConfig.custom()
                .setSocketTimeout(timeout.toInt())
                .setConnectTimeout(timeout.toInt())
                .build()

        return HttpClients.custom()
                .setSslcontext(sslcontext)
                .setSSLSocketFactory(sslsf)
                .setDefaultRequestConfig(config)
                .build()
    }


    @Throws(IOException::class)
    private fun csrfToken(project: Project, sessionId: String): String {
        //<meta name="_csrf" content="c1dee1f7-8c79-43b1-8f3f-767662abc87a" />
        if (sessionId.isNotBlank()) {
            val doc = Jsoup.connect(hostHacUrl(project)).cookie("JSESSIONID", sessionId).get()
            val csrfMetaElt = doc.select("meta[name=_csrf]")
            return csrfMetaElt.attr("content")
        }
        return StringUtils.EMPTY
    }

    private fun validSessionId(project: Project): String {
        val res: Connection.Response
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

                override fun getAcceptedIssuers(): Array<X509Certificate>? {
                    return null
                }

                override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}

                override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
            })

            // Install the all-trusting trust manager
            val sc = SSLContext.getInstance("SSL")
            sc.init(null, trustAllCerts, java.security.SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)

            // Create all-trusting host name verifier
            val allHostsValid = { hostname: String?, _: SSLSession -> true }

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid)
            res = Jsoup.connect(hostHacUrl(project)).method(Method.GET).execute()
        } catch (e: IOException) {
            return StringUtils.EMPTY
        }

        if (res == null) {
            return StringUtils.EMPTY
        }
        return res.cookie("JSESSIONID")
    }
}

