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

package com.intellij.idea.plugin.hybris.common.services.impl;

import com.intellij.idea.plugin.hybris.common.services.VersionSpecificService;
import com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils;
import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons;
import com.intellij.idea.plugin.hybris.project.actions.ProjectRefreshAction;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by Martin Zdarsky-Jones on 15/10/2016.
 */
public class LatestVersionService implements VersionSpecificService {

    @Override
    public Notification createNotification(
        @NotNull final String displayId,
        @NotNull final String title,
        @NotNull final String content,
        @NotNull final NotificationType notificationType,
        @NotNull final Icon icon
    ) {
        final NotificationGroup notificationGroup = new NotificationGroup(
            displayId,
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

        return notification;
    }
}
