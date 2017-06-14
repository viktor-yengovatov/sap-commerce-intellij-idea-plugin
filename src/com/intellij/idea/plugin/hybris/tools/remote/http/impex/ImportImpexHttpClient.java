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

package com.intellij.idea.plugin.hybris.tools.remote.http.impex;

import com.intellij.idea.plugin.hybris.tools.remote.http.HybrisHttpClient;
import org.apache.commons.lang3.CharEncoding;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.jsoup.Jsoup.parse;

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
public class ImportImpexHttpClient {

    private HybrisHttpClient hybrisHttpClient = HybrisHttpClient.INSTANCE;

    public @NotNull
    HybrisHttpResult importImpex(final String content) {
        final List<BasicNameValuePair> params = asList(
            new BasicNameValuePair("scriptContent", content),
            new BasicNameValuePair("validationEnum", "IMPORT_STRICT"),
            new BasicNameValuePair("encoding", "UTF-8"),
            new BasicNameValuePair("maxThreads", "4")
        );
        HybrisHttpResult.HybrisHttpResultBuilder resultBuilder = HybrisHttpResult.HybrisHttpResultBuilder.createResult();
        final String actionUrl = hybrisHttpClient.hostUrl() + "/console/impex/import";
        try {
            final String sessionId = hybrisHttpClient.getSessionId();
            final HttpResponse response = hybrisHttpClient.post(actionUrl, sessionId, params);
            resultBuilder = resultBuilder.httpCode(response.getStatusLine().getStatusCode());
            final Document document = parse(response.getEntity().getContent(), CharEncoding.UTF_8, "");

            final Element impexResultStatus = document.getElementById("impexResult");

            final boolean hasDataLevelAttr = impexResultStatus.hasAttr("data-level");
            final boolean hasDataResultAttr = impexResultStatus.hasAttr("data-result");
            if (hasDataLevelAttr && hasDataResultAttr) {
                if ("error".equals(impexResultStatus.attr("data-level"))) {
                    final String dataResult = impexResultStatus.attr("data-result");
                    final Element detailMessage = document.getElementsByClass("impexResult").first().children().first();
                    return HybrisHttpResult.HybrisHttpResultBuilder.createResult()
                                                                   .errorMessage(dataResult)
                                                                   .detailMessage(detailMessage.text())
                                                                   .build();
                } else {
                    final String dataResult = impexResultStatus.attr("data-result");
                    return HybrisHttpResult.HybrisHttpResultBuilder.createResult().output(dataResult).build();
                }
            }
            return resultBuilder.errorMessage("No data in response").build();
        } catch (final IOException | NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            return resultBuilder.errorMessage(e.getMessage() + ' ' + actionUrl).httpCode(SC_BAD_REQUEST).build();
        }
    }

}
