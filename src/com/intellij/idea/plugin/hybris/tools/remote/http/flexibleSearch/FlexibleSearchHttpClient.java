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

package com.intellij.idea.plugin.hybris.tools.remote.http.flexibleSearch;

import com.google.gson.Gson;
import com.intellij.idea.plugin.hybris.tools.remote.http.HybrisHttpClient;
import com.intellij.idea.plugin.hybris.tools.remote.http.impex.HybrisHttpResult;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.jsoup.Jsoup.parse;

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
public class FlexibleSearchHttpClient {

    private static final Gson gsonParser = new Gson();

    private HybrisHttpClient hybrisHttpClient = HybrisHttpClient.INSTANCE;

    public @NotNull
    HybrisHttpResult execute(final String content) {
//        HTTPRequestManager httpRequestManager = HTTPRequestManager.getInstance();
//        List<NameValuePair> scriptParameter = new ArrayList<NameValuePair>();
//        if(isHybrisVersion5OrAbove()) {
//            scriptParameter.add(new BasicNameValuePair("scriptType", "flexibleSearch"));
//            scriptParameter.add(new BasicNameValuePair("commit", "false"));
//        }
//        scriptParameter.add(new BasicNameValuePair("flexibleSearchQuery", script));
//        scriptParameter.add(new BasicNameValuePair("user", username));
//        scriptParameter.add(new BasicNameValuePair("locale", localeISOCode));
//        scriptParameter.add(new BasicNameValuePair("maxCount", maxCount));
//        scriptParameter.add(new BasicNameValuePair("sqlQuery", ""));
//        return httpRequestManager.doPostRequestWithCookie(serverURL + (isHybrisVersion5OrAbove() ? HYBRIS_5_CONSOLE_EXECUTE_URL : FLEXSEARCH_CONSOLE_EXECUTE_URL), jSessionID, scriptParameter);

        final List<BasicNameValuePair> params = asList(
            new BasicNameValuePair("scriptType", "flexibleSearch"),
            new BasicNameValuePair("commit", "false"),
            new BasicNameValuePair("flexibleSearchQuery", content),
            new BasicNameValuePair("sqlQuery", ""),
            new BasicNameValuePair("maxCount", "100")
//            new BasicNameValuePair("locale", localeISOCode)
        );
        HybrisHttpResult.HybrisHttpResultBuilder resultBuilder = HybrisHttpResult.HybrisHttpResultBuilder.createResult();
        final String actionUrl = hybrisHttpClient.getHostUrl() + "/console/flexsearch/execute";
        final String sessionId = hybrisHttpClient.getSessionId();
        try {
            final HttpResponse response = hybrisHttpClient.post(actionUrl, sessionId, params);
            resultBuilder = resultBuilder.httpCode(response.getStatusLine().getStatusCode());
            final Document document = parse(response.getEntity().getContent(), CharEncoding.UTF_8, "");

            final HashMap json = gsonParser.fromJson(document.getElementsByTag("body").text(), HashMap.class);
            if (json.get("exception") != null) {
                return HybrisHttpResult.HybrisHttpResultBuilder.createResult()
                                                               .errorMessage(json.get("exception").toString())
                                                               .detailMessage(json.get("exception").toString()).build();
            } else {
                TableBuilder tableBuilder = new TableBuilder();
                
                final List<String> headers = (List<String>) json.get("headers");
                final List<List<String>> resultList = (List<List<String>>) json.get("resultList");
                
                tableBuilder.addRow(headers.toArray(new String[]{}));
                resultList.forEach(row -> tableBuilder.addRow(row.toArray(new String[]{})));
                
                return resultBuilder.output(tableBuilder.toString()).build();
            }
        } catch (final IOException | NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            return resultBuilder.errorMessage(e.getMessage() + ' ' + actionUrl).httpCode(SC_BAD_REQUEST).build();
        }
    }

}
