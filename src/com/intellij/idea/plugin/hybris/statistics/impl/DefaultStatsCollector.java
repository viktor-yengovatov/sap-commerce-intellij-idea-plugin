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

package com.intellij.idea.plugin.hybris.statistics.impl;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import com.intellij.idea.plugin.hybris.statistics.StatsCollector;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.ui.LicensingFacade;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 28/2/17.
 */
public class DefaultStatsCollector implements StatsCollector {
    private final ExecutorService executor;
    private final HttpClient client;

    public DefaultStatsCollector() {
        executor = Executors.newFixedThreadPool(1);
        client = HttpClientBuilder.create().build();
    }

    @Override
    public void collectStat(final ACTIONS action) {
        collectStat(action, "");
    }

    @Override
    public void collectStat(final ACTIONS action, final String parameters) {
        try {
            final HttpPost post = buildRequest(action.name(), parameters);
            sendRequest(post);
        } catch(Throwable e) {
            // we don't care
        }
    }

    private HttpPost buildRequest(final String action, final String parameters) throws UnsupportedEncodingException {
        final HttpPost post = new HttpPost(HybrisConstants.STATS_COLLECTOR_URL);
        final List<NameValuePair> urlParameters = new ArrayList<>();

        urlParameters.add(new BasicNameValuePair("ide_version", getIdeVersion()));
        urlParameters.add(new BasicNameValuePair("ide_type", getIdeType()));

        final String registeredTo = getRegisteredTo();
        if (null != registeredTo) {
            urlParameters.add(new BasicNameValuePair("registered_to", DigestUtils.sha512Hex(registeredTo)));
        }

        final String computerName = getComputerName();
        if (null != computerName) {
            urlParameters.add(new BasicNameValuePair("computer_name", DigestUtils.sha512Hex(computerName)));
        }

        urlParameters.add(new BasicNameValuePair("plugin_version", getPluginVersion()));
        urlParameters.add(new BasicNameValuePair("request_date", getCurrentDateTimeWithTimeZone()));
        urlParameters.add(new BasicNameValuePair("action", action));
        urlParameters.add(new BasicNameValuePair("parameters", parameters));

        post.setEntity(new UrlEncodedFormEntity(urlParameters, UTF_8));
        return post;
    }

    private String getCurrentDateTimeWithTimeZone() {
        final ZonedDateTime localDateTime = ZonedDateTime.now();
        return localDateTime.toString();
    }

    private String getPluginVersion() {
        final IdeaPluginDescriptor plugin = PluginManager.getPlugin(PluginId.getId(HybrisConstants.PLUGIN_ID));
        return null == plugin ? null : plugin.getVersion();
    }

    private Future<StatsResponse> sendRequest(final HttpPost post) {
        Future<StatsResponse> response = executor.submit(new StatsRequest(client, post));
        return response;
    }

    public String getIdeVersion() {
        return ApplicationInfo.getInstance().getFullVersion();
    }

    public String getIdeType() {
        return ApplicationInfo.getInstance().getBuild().getProductCode();
    }

    @Nullable
    public String getRegisteredTo() {
        final LicensingFacade instance = LicensingFacade.getInstance();

        return null == instance ? null : instance.getLicensedToMessage();
    }

    //the same user can have multiple computers with identical license. This would affect the user counter.
    private String getComputerName() {
        try {
            final String result = InetAddress.getLocalHost().getHostName();
            if (StringUtils.isNotEmpty(result))
                return result.trim();
        } catch (UnknownHostException e) {
            // failed;  try alternate means.
        }

        // try environment properties.
        final String envCompName = System.getenv("COMPUTERNAME");
        if (envCompName != null)
            return envCompName;
        final String host = System.getenv("HOSTNAME");
        if (host != null)
            return host;

        // undetermined.
        return "UNDETERMINED";
    }
}
