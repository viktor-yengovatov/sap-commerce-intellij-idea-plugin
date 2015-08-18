/*
 * This file is part of "Hybris Integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2015 Alexander Bartash <AlexanderBartash@gmail.com>
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

package com.intellij.idea.plugin.hybris.utils;

/**
 * Created 10:30 PM 07 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public final class HybrisConstants {

    public static final String EXTENSION_INFO_XML = "extensioninfo.xml";
    public static final String LOCAL_EXTENSIONS_XML = "localextensions.xml";
    public static final String EXTENSIONS_XML = "extensions.xml";
    public static final String NEW_IDEA_MODULE_FILE_EXTENSION = ".iml";
    public static final String OLD_IDEA_MODULE_FILE_EXTENSION = ".eml";
    public static final String PLATFORM_EXTENSION_NAME = "platform";
    public static final String PLATFORM_EXTENSIONS_DIRECTORY_NAME = "ext";
    public static final String CONFIG_EXTENSION_NAME = "config";
    public static final String LIB_DIRECTORY = "lib";
    public static final String BIN_DIRECTORY = "bin";
    public static final String RESOURCES_DIRECTORY = "resources";
    public static final String PROJECT_PROPERTIES = "project.properties";
    public static final String APPLICATION_CONTEXT_SPRING_FILES = "application-context";
    public static final String ADDITIONAL_WEB_SPRING_CONFIG_FILES = "additionalWebSpringConfigs";
    public static final String GLOBAL_CONTEXT_SPRING_FILES = "global-context";

    public static final String COMMON_LIBS_GROUP = "Common libs";

    public static final String WEB_ROOT = "web/webroot";
    public static final String WEB_INF_LIB_DIRECTORY = "web/webroot/WEB-INF/lib";
    public static final String WEB_INF_CLASSES_DIRECTORY = "web/webroot/WEB-INF/classes";
    public static final String HMC_LIB_DIRECTORY = "hmc/bin";
    public static final String BACKOFFICE_LIB_DIRECTORY = "backoffice/bin";

    public static final String PL_BOOTSTRAP_LIB_DIRECTORY = "bootstrap/bin";
    public static final String PL_TOMCAT_LIB_DIRECTORY = "tomcat/lib";
    public static final String PL_TOMCAT_BIN_DIRECTORY = "tomcat/bin";

    public static final String CONFIG_LICENCE_DIRECTORY = "licence";

    public static final String COMPILER_OUTPUT_PATH = "/eclipsebin";

    public static final String HYBRIS_PROJECT_SETTINGS_FILE_NAME = "hybrisProjectSettings.xml";

    public static final String DEFAULT_DIRECTORY_NAME_FOR_IDEA_MODULE_FILES = "idea-module-files";

    private HybrisConstants() throws IllegalAccessException {
        throw new IllegalAccessException("Should never be accessed.");
    }
}
