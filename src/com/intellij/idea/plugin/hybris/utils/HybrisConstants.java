/*
 * Copyright 2015 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    public static final String COMMON_LIBS_GROUP = "Common libs";

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

    private HybrisConstants() throws IllegalAccessException {
        throw new IllegalAccessException("Should never be accessed.");
    }
}
