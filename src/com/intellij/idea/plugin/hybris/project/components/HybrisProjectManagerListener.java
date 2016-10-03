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

import com.intellij.idea.plugin.hybris.common.services.CommonIdeaService;
import com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils;
import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerAdapter;
import com.intellij.openapi.project.ProjectManagerListener;

/**
 * Created by Martin Zdarsky-Jones on 29/09/2016.
 */
public class HybrisProjectManagerListener extends ProjectManagerAdapter implements ProjectManagerListener {

    public static final NotificationGroup GROUP_DISPLAY_ID_INFO =
        new NotificationGroup(
            "[y] project",
            NotificationDisplayType.BALLOON,
            true,
            null,
            HybrisIcons.HYBRIS_ICON
        );

    @Override
    public void projectOpened(final Project project) {
        super.projectOpened(project);
        if (isOldHybrisProject(project)) {
            showNotification(project);
        }
    }

    private boolean isOldHybrisProject(final Project project) {
        final CommonIdeaService commonIdeaService = ServiceManager.getService(CommonIdeaService.class);
        if (commonIdeaService.isHybrisProject(project)) {
            return commonIdeaService.isOutDatedHybrisProject(project);
        } else {
            return commonIdeaService.isPotentiallyHybrisProject(project);
        }
    }

    private void showNotification(final Project project) {
        final Notification notification = GROUP_DISPLAY_ID_INFO.createNotification(
            HybrisI18NBundleUtils.message("hybris.project.open.outdated.title"),
            null,
            HybrisI18NBundleUtils.message("hybris.project.open.outdated.text"),
            NotificationType.INFORMATION
        );
        notification.setImportant(true);
        Notifications.Bus.notify(notification, project);
    }
}
