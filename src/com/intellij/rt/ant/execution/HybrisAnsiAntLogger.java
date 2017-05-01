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

package com.intellij.rt.ant.execution;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.BuildEvent;

/**
 * Created by  Martin Zdarsky-Jones <martin.zdarsky@hybris.com> on 30/4/17.
 *
 * Strips ANSI encoding from hybris output and assigns correct message priority.
 */
public class HybrisAnsiAntLogger extends HybrisParsingAntLogger {

    // IDEA
    public static final int MSG_ERR = 0;
    public static final int MSG_WARN = 1;
    public static final int MSG_INFO = 2;
    public static final int MSG_VERBOSE = 3;
    public static final int MSG_DEBUG = 4;

    // ANSI
    private static final int FOREGROUND_RED = 31;
    private static final int FOREGROUND_GREEN = 32;
    private static final int FOREGROUND_YELLOW = 33;
    private static final int FOREGROUND_BLUE = 34;
    private static final int FOREGROUND_CYAN = 36;

    private static final String PREFIX = "\u001b[";
    private static final String SUFFIX = "m";
    private static final String SEPARATOR = ";";
    private static final String END_COLOR = PREFIX + SUFFIX;

    private static final int ERROR_COLOR = FOREGROUND_RED;
    private static final int WARN_COLOR = FOREGROUND_YELLOW;
    private static final int INFO_COLOR = FOREGROUND_GREEN;
    private static final int DEBUG_COLOR = FOREGROUND_CYAN;
    private static final int TRACE_COLOR = FOREGROUND_BLUE;


    @Override
    public synchronized void messageLogged(BuildEvent event) {
        processAnsiColor(event);
        super.messageLogged(event);
    }

    private void processAnsiColor(final BuildEvent event) {
        String message = event.getMessage();
        if (message == null) {
            return;
        }
        if (!message.contains(PREFIX)) {
            return;
        }
        message = message.replace(END_COLOR,"");
        message = StringUtils.strip(message, "\b ");
        if (!message.startsWith(PREFIX)) {
            event.setMessage(message, event.getPriority());
            return;
        }
        int mIndex = message.indexOf(SUFFIX);
        if (mIndex < 0) {
            event.setMessage(message, event.getPriority());
            return;
        }
        String attribute = message.substring(PREFIX.length(), mIndex);
        int priority = getPriority(attribute);
        message = message.substring(mIndex+1);
        event.setMessage(message, priority);
    }

    private int getPriority(final String attribute) {
        final String[] colors = attribute.split(SEPARATOR);
        if (colors.length == 0) {
            return MSG_INFO;
        }
        final String color = colors[colors.length - 1];
        try {
            final int ansi = Integer.parseInt(color);
            switch (ansi) {
                case ERROR_COLOR:
                    return MSG_ERR;
                case WARN_COLOR:
                    return MSG_WARN;
                case INFO_COLOR:
                    return MSG_INFO;
                case DEBUG_COLOR:
                    return MSG_DEBUG;
                case TRACE_COLOR:
                    return MSG_VERBOSE;
            }
        } catch (NumberFormatException nfe) {}
        return MSG_INFO;
    }

}
