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

package com.intellij.idea.plugin.hybris.project.components;

import com.intellij.idea.plugin.hybris.ant.HybrisAntBuildListener;
import com.intellij.idea.plugin.hybris.common.services.CommonIdeaService;
import com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils;
import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons;
import com.intellij.idea.plugin.hybris.project.actions.ProjectRefreshAction;
import com.intellij.idea.plugin.hybris.project.wizard.PermissionToSendStatisticsDialog;
import com.intellij.idea.plugin.hybris.settings.HybrisApplicationSettingsComponent;
import com.intellij.idea.plugin.hybris.statistics.StatsCollector;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.spring.settings.SpringGeneralSettings;

import java.awt.*;

import static com.intellij.idea.plugin.hybris.project.utils.PluginCommon.ANT_SUPPORT_PLUGIN_ID;
import static com.intellij.idea.plugin.hybris.project.utils.PluginCommon.SPRING_PLUGIN_ID;
import static com.intellij.idea.plugin.hybris.project.utils.PluginCommon.isPluginActive;

/**
 * Created by Martin Zdarsky-Jones on 29/09/2016.
 */
public class HybrisProjectManagerListener implements ProjectManagerListener {

    @Override
    public void projectOpened(final Project project) {
        if (isOldHybrisProject(project)) {
            showNotification(project);
        }
        registerAntListener(project);
        resetSpringGeneralSettings(project);
        popupPermissionToSendStatistics(project);
    }

    private void popupPermissionToSendStatistics(final Project project) {
        final CommonIdeaService commonIdeaService = ServiceManager.getService(CommonIdeaService.class);
        if (commonIdeaService.isHybrisProject(project)) {
            if (!HybrisApplicationSettingsComponent.getInstance().getState().isAllowedSendingPlainStatistics()) {
                EventQueue.invokeLater(() -> new PermissionToSendStatisticsDialog(project).show());
            }
        }
    }

    private void resetSpringGeneralSettings(final Project project) {
        final CommonIdeaService commonIdeaService = ServiceManager.getService(CommonIdeaService.class);
        if (commonIdeaService.isHybrisProject(project)) {
            if (isPluginActive(SPRING_PLUGIN_ID)) {
                SpringGeneralSettings springGeneralSettings = SpringGeneralSettings.getInstance(project);
                springGeneralSettings.setShowMultipleContextsPanel(false);
                springGeneralSettings.setShowProfilesPanel(false);
            }
        }
    }

    private void registerAntListener(final Project project) {
        final CommonIdeaService commonIdeaService = ServiceManager.getService(CommonIdeaService.class);
        if (commonIdeaService.isHybrisProject(project)) {
            if (isPluginActive(ANT_SUPPORT_PLUGIN_ID)) {
                HybrisAntBuildListener.registerAntListener();
            }
        }
    }

    private boolean isOldHybrisProject(final Project project) {
        final CommonIdeaService commonIdeaService = ServiceManager.getService(CommonIdeaService.class);
        final StatsCollector statsCollector = ServiceManager.getService(StatsCollector.class);
        if (commonIdeaService.isHybrisProject(project)) {
            statsCollector.collectStat(StatsCollector.ACTIONS.OPEN_PROJECT);
            return commonIdeaService.isOutDatedHybrisProject(project);
        } else {
            final boolean potential = commonIdeaService.isPotentiallyHybrisProject(project);
            if (potential) {
                statsCollector.collectStat(StatsCollector.ACTIONS.OPEN_POTENTIAL_PROJECT);
            }
            return potential;
        }
    }

    private void showNotification(final Project project) {
        final NotificationGroup notificationGroup = new NotificationGroup(
            "[y] project",
            NotificationDisplayType.BALLOON,
            true,
            null,
            HybrisIcons.HYBRIS_ICON
        );

        final Notification notification = notificationGroup.createNotification(
            HybrisI18NBundleUtils.message("hybris.project.open.outdated.title"),
            HybrisI18NBundleUtils.message("hybris.project.open.outdated.text"),
            NotificationType.INFORMATION,
            (myNotification, myHyperlinkEvent) -> ProjectRefreshAction.triggerAction()
        );
        notification.setImportant(true);
        Notifications.Bus.notify(notification, project);
    }
}
