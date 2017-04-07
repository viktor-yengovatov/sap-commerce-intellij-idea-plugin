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

import org.apache.commons.lang3.CharEncoding;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;
import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.intellij.idea.plugin.hybris.tools.remote.http.ImpexHttpResult.ImpexHttpResultBuilder.createResult;
import static java.util.Arrays.asList;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.jsoup.Jsoup.parse;

/**
 * @author Aleksandr Nosov
 */
public class ValidateImpexHttpClient {

    private HybrisHttpClient hybrisHttpClient = HybrisHttpClient.INSTANCE;

    public @NotNull ImpexHttpResult validateImpex(final String content) {
        final List<BasicNameValuePair> params = asList(
            new BasicNameValuePair("scriptContent", content),
            new BasicNameValuePair("validationEnum", "IMPORT_STRICT"),
            new BasicNameValuePair("encoding", "UTF-8"),
            new BasicNameValuePair("maxThreads", "4")
        );
        final String actionUrl = hybrisHttpClient.getHostUrl() + "/console/impex/import/validate";
        final String sessionId = hybrisHttpClient.getSessionId();
        ImpexHttpResult.ImpexHttpResultBuilder resultBuilder = createResult();
        try {
            final HttpResponse response = hybrisHttpClient.post(actionUrl, sessionId, params);
            resultBuilder = resultBuilder.httpCode(response.getStatusLine().getStatusCode());
            final Document document = parse(response.getEntity().getContent(), CharEncoding.UTF_8, "");
            final Element impexResultStatus = document.getElementById("validationResultMsg");
            
            final boolean hasDataLevelAttr = impexResultStatus.hasAttr("data-level");
            final boolean hasDataResultAttr = impexResultStatus.hasAttr("data-result");
            if (hasDataLevelAttr && hasDataResultAttr) {
                if ("error".equals(impexResultStatus.attr("data-level"))) {
                    final String dataResult = impexResultStatus.attr("data-result");
                    return resultBuilder.errorMessage(dataResult).build();
                } else {
                    final String dataResult = impexResultStatus.attr("data-result");
                    return resultBuilder.output(dataResult).build();
                }
            }
            return resultBuilder.errorMessage("No data in response").build();
        } catch (HttpStatusException e) {
            return resultBuilder.errorMessage(e.getMessage() + ' ' + actionUrl).httpCode(e.getStatusCode()).build();
        } catch (final IOException | NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            return resultBuilder.errorMessage(e.getMessage() + ' ' + actionUrl).httpCode(SC_BAD_REQUEST).build();
        }
    }

}
