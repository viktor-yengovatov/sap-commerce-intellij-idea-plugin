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

package com.intellij.idea.plugin.hybris.tools.remote.http;

import com.intellij.idea.plugin.hybris.settings.HybrisApplicationSettingsComponent;
import com.intellij.openapi.util.text.StringUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;

import static com.intellij.util.containers.ContainerUtil.newArrayList;
import static java.util.Arrays.asList;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.conn.ssl.SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;


public enum HybrisHttpClient {
    INSTANCE;

    private static final String USER_AGENT = "Mozilla/5.0";
    
    private HybrisApplicationSettingsComponent settingsComponent = HybrisApplicationSettingsComponent.getInstance();
    private String sessionId;

    public String getHostUrl() {
        return HybrisApplicationSettingsComponent.getInstance().getState().getHybrisHostUrl();
    }

//    public JsonObject getTypeandAttribute(final String type) throws Exception {
//        final List<BasicNameValuePair> params =
//            Collections.singletonList(new BasicNameValuePair("type", type));
//
//        final HttpResponse response = post(
//            getHostUrl() + "/console/impex/typeAndAttributes",
//            getSessionId(),
//            params
//        );
//        final BufferedReader rd = new BufferedReader(new InputStreamReader(response
//                                                                               .getEntity().getContent()));
//        return JsonObject.readFrom(rd);
//    }
//
//    public JsonArray getAllTypes() throws Exception {
//
//        final HttpResponse response = post(getHostUrl() + "/console/impex/allTypes", getSessionId(),
//                                           Collections.emptyList()
//        );
//        final BufferedReader rd = new BufferedReader(new InputStreamReader(response
//                                                                               .getEntity().getContent()));
//        final JsonObject impexJsonType = JsonObject.readFrom(rd);
//        final boolean isExist = impexJsonType.get("exists").asBoolean();
//        JsonArray types = null;
//        if (isExist) {
//            types = impexJsonType.get("types").asArray();
//        }
//        return types;
//    }


    private String login() {
        if (sessionId == null) {
            sessionId = getValidSessionId();
        }
        final String csrfToken;
        try {
            csrfToken = getCSRFToken(sessionId);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        final List<BasicNameValuePair> params =
            asList(
                new BasicNameValuePair(
                    "j_username",
                    settingsComponent.getState().getHybrisInstanceUsername()
                ),
                new BasicNameValuePair(
                    "j_password",
                    settingsComponent.getState().getHybrisInstancePassword()
                ),
                new BasicNameValuePair("_csrf", csrfToken)
            );
        
        final HttpResponse response;
        try {
            response = post(getHostUrl() + "/j_spring_security_check", sessionId, params);
            sessionId = CookieParser.getInstance().getSpecialCookie(response.getAllHeaders());

            return sessionId;
        } catch (final IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSessionId() {
        if (StringUtil.isEmpty(sessionId)) {
            sessionId = login();
            return sessionId;
        } else {
            return sessionId;
        }
    }

    public HttpResponse post(
        final String actionUrl, final String sessionId, final List<BasicNameValuePair> params
    ) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        final String csrfToken = getCSRFToken(sessionId);
        final HttpClient client = createAllowAllClient(6000l);

        final HttpPost post = new HttpPost(actionUrl);

        // add header
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("X-CSRF-TOKEN", csrfToken);

        final List<NameValuePair> urlParameters = newArrayList();
        for (final BasicNameValuePair nameValuePair : params) {
            urlParameters.add(nameValuePair);
        }
        if (sessionId != null) {
            post.setHeader("Cookie", "JSESSIONID=" + sessionId);
        }
        final HttpEntity entity;
        final HttpResponse response;
        try {
            entity = new UrlEncodedFormEntity(urlParameters, "utf-8");
            post.setEntity(entity);

            response = client.execute(post);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        if (response.getStatusLine().getStatusCode() == 302) {
            this.sessionId = null;
        }
        if (response.getStatusLine().getStatusCode() == SC_FORBIDDEN) {
            this.sessionId = null;
            throw new HttpStatusException("", response.getStatusLine().getStatusCode(), actionUrl);
        }
        return response;

    }


    /**
     * @param timeout Timeout in seconds
     */
    private HttpClient createAllowAllClient(final long timeout) throws
                                                                KeyStoreException,
                                                                NoSuchAlgorithmException,
                                                                KeyManagementException {
        final SSLContext sslcontext = org.apache.http.ssl.SSLContexts.custom()
                                                                     .loadTrustMaterial(null, (chain, authType) -> true)
                                                                     .build();

        final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
            sslcontext, ALLOW_ALL_HOSTNAME_VERIFIER
        );

        final RequestConfig config = RequestConfig.custom()
                                                  .setSocketTimeout((int) timeout)
                                                  .setConnectTimeout((int) timeout)
                                                  .build();

        return HttpClients.custom()
                          .setSslcontext(sslcontext)
                          .setSSLSocketFactory(sslsf)
                          .setDefaultRequestConfig(config)
                          .build();
    }


    private String getCSRFToken(final String sessionId) throws IOException {
        //<meta name="_csrf" content="c1dee1f7-8c79-43b1-8f3f-767662abc87a" />
        final Document doc = Jsoup.connect(getHostUrl()).cookie("JSESSIONID", sessionId).get();
        final Elements csrfMetaElt = doc.select("meta[name=_csrf]");
        return csrfMetaElt.attr("content");

    }

    private String getValidSessionId() {
        final Connection.Response res;
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            // Install the all-trusting trust manager
            final SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            final HostnameVerifier allHostsValid = (hostname, session) -> true;

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            res = Jsoup.connect(getHostUrl()).method(Method.GET).execute();
        } catch (final IOException | NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
        if (res == null) {
            return null;
        }
        return res.cookie("JSESSIONID");
    }

}