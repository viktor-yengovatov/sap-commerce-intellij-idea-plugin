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
import com.intellij.idea.plugin.hybris.settings.HybrisApplicationSettingsComponent;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 28/2/17.
 */
public class DefaultStatsCollector implements StatsCollector {

    private final ExecutorService executor;
    private final HttpClient client;
    private final Map<ACTIONS, Long> cache = new HashMap<>();

    public DefaultStatsCollector() {
        executor = Executors.newFixedThreadPool(1);
        client = HttpClientBuilder.create().build();
    }

    @Override
    public void collectStat(@NotNull final ACTIONS action) {
        collectStat(action, null);
    }

    @Override
    public void collectStat(@NotNull final ACTIONS action, @Nullable final String parameters) {
        HybrisApplicationSettingsComponent.getInstance().addUsedAction(action);

        if (shouldPostStat(action)) {
            cache.put(action, System.currentTimeMillis());
            try {
                final HttpPost post = buildRequest(action.name(), parameters);
                sendRequest(post);
            } catch (Throwable e) {
                // we don't care
            }
        }
    }

    private boolean shouldPostStat(final ACTIONS action) {
        Long sendTime = cache.get(action);
        if (sendTime == null) {
            return true;
        }
        long diff = System.currentTimeMillis() - sendTime;
        return diff > TimeUnit.HOURS.toMillis(12);
    }

    private HttpPost buildRequest(@NotNull final String action, @Nullable final String parameters) throws UnsupportedEncodingException {
        final HttpPost post = new HttpPost(HybrisConstants.STATS_COLLECTOR_URL);
        final List<NameValuePair> urlParameters = new ArrayList<>();

        urlParameters.add(new BasicNameValuePair("ide_version", getIdeVersion()));
        urlParameters.add(new BasicNameValuePair("ide_type", getIdeType()));

        final String registeredTo = getRegisteredTo();
        if (null != registeredTo) {
            urlParameters.add(new BasicNameValuePair("registered_to", DigestUtils.sha512Hex(registeredTo)));
            if (acceptedSendingStatistics()) {
                urlParameters.add(new BasicNameValuePair("registered_to_plain", registeredTo));
            }
        }

        final String computerName = getComputerName();
        if (null != computerName) {
            urlParameters.add(new BasicNameValuePair("computer_name", DigestUtils.sha512Hex(computerName)));
            if (acceptedSendingStatistics()) {
                urlParameters.add(new BasicNameValuePair("computer_name_plain", computerName));
            }
        }

        String loginName = getLoginName();
        if (null != loginName) {
            urlParameters.add(new BasicNameValuePair("login_name", DigestUtils.sha512Hex(loginName)));
            if (acceptedSendingStatistics()) {
                urlParameters.add(new BasicNameValuePair("login_name_plain", loginName));
            }
        }
        String osName = System.getProperty("os.name").toLowerCase();
        urlParameters.add(new BasicNameValuePair("os", osName));


        urlParameters.add(new BasicNameValuePair("plugin_version", getPluginVersion()));
        urlParameters.add(new BasicNameValuePair("request_date", getCurrentDateTimeWithTimeZone()));
        urlParameters.add(new BasicNameValuePair("action", action));
        if (parameters != null) {
            urlParameters.add(new BasicNameValuePair("parameters", parameters));
        }
        post.setEntity(new UrlEncodedFormEntity(urlParameters, UTF_8));
        return post;
    }

    private boolean acceptedSendingStatistics() {
        return HybrisApplicationSettingsComponent.getInstance().getState().isAllowedSendingPlainStatistics();
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
            if (StringUtils.isNotEmpty(result)) {
                return result.trim();
            }
        } catch (UnknownHostException e) {
            // failed;  try alternate means.
        }

        // try environment properties.
        final String envCompName = System.getenv("COMPUTERNAME");
        if (envCompName != null) {
            return envCompName;
        }
        final String host = System.getenv("HOSTNAME");
        if (host != null) {
            return host;
        }

        return null;
    }

    private String getLoginName() {
        String osName = System.getProperty("os.name").toLowerCase();
        String className = null;
        String methodName = "getUsername";

        if (osName.contains("windows")) {
            className = "com.sun.security.auth.module.NTSystem";
            methodName = "getName";
        } else if (osName.contains("linux")) {
            className = "com.sun.security.auth.module.UnixSystem";
        } else if (osName.contains("solaris") || osName.contains("sunos")) {
            className = "com.sun.security.auth.module.SolarisSystem";
        }

        if (className != null) {
            try {
                Class<?> c = Class.forName(className);
                Method method = c.getDeclaredMethod(methodName);
                Object o = c.newInstance();
                return (String) method.invoke(o);
            } catch (ClassNotFoundException | NoSuchMethodException |
                IllegalAccessException | InstantiationException |
                InvocationTargetException e) {
                // no-op
            }
        }

        return System.getProperty("user.name");
    }
}
