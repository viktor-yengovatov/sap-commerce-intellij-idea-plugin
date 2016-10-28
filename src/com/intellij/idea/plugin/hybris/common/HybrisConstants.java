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

package com.intellij.idea.plugin.hybris.common;

import static org.apache.commons.io.FilenameUtils.separatorsToSystem;

/**
 * Created 10:30 PM 07 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public interface HybrisConstants {

    String EXTENSION_INFO_XML = "extensioninfo.xml";
    String LOCAL_EXTENSIONS_XML = "localextensions.xml";
    String HYBRIS_SERVER_SHELL_SCRIPT_NAME = separatorsToSystem("bin/platform/hybrisserver.sh");
    String PLATFORM_EXT_MODULE_PREFIX = separatorsToSystem("bin/platform/ext/");
    String PLATFORM_OOTB_MODULE_PREFIX = separatorsToSystem("bin/ext-");
    String HYBRIS_OOTB_MODULE_PREFIX = separatorsToSystem("hybris/")+PLATFORM_OOTB_MODULE_PREFIX;
    String MEDIA_DIRECTORY = separatorsToSystem("/data/media");
    String CUSTOM_MODULES_DIRECTORY_RELATIVE_PATH = separatorsToSystem("bin/custom");
    String EXTENSIONS_XML = "extensions.xml";
    String NEW_IDEA_MODULE_FILE_EXTENSION = ".iml";
    String PLATFORM_EXTENSION_NAME = "platform";
    String PLATFORM_DB_DRIVER = separatorsToSystem("lib/dbdriver");
    String CORE_EXTENSION_NAME = "core";
    String BACK_OFFICE_EXTENSION_NAME = "backoffice";
    String BACK_OFFICE_MODULE_META_KEY_NAME = "backoffice-module";
    String HMC_EXTENSION_NAME = "hmc";
    String PLATFORM_EXTENSIONS_DIRECTORY_NAME = "ext";
    String CONFIG_EXTENSION_NAME = "config";
    String CONFIG_RELATIVE_PATH = separatorsToSystem("/../../" + CONFIG_EXTENSION_NAME);
    String LIB_DIRECTORY = "lib";
    String BIN_DIRECTORY = "bin";
    String RESOURCES_DIRECTORY = "resources";
    String LOCAL_PROPERTIES = "local.properties";
    String PROJECT_PROPERTIES = "project.properties";
    String APPLICATION_CONTEXT_SPRING_FILES = "application-context";
    String ADDITIONAL_WEB_SPRING_CONFIG_FILES = "additionalWebSpringConfigs";
    String GLOBAL_CONTEXT_SPRING_FILES = "global-context";
    String BUILD_NUMBER_FILE_PATH = separatorsToSystem("/bin/platform/build.number");
    String HYBRIS_API_VERSION_KEY = "version.api";
    String DEFAULT_JAVADOC_ROOT_URL = "https://download.hybris.com/api/%s/commercesuite";
    String HYBRIS_6_0_PLUS_JAVADOC_ROOT_URL = "https://help.hybris.com/%s/api/commercesuite/index.html";

    String SRC_DIRECTORY = "src";
    String GEN_SRC_DIRECTORY = "gensrc";
    String TEST_SRC_DIRECTORY = "testsrc";
    String HMC_MODULE_DIRECTORY = "hmc";
    String HAC_MODULE_DIRECTORY = "hac";
    String WEB_MODULE_DIRECTORY = "web";
    String BACK_OFFICE_MODULE_DIRECTORY = "backoffice";
    String ADDON_SRC_DIRECTORY = "addonsrc";
    String TEST_CLASSES_DIRECTORY = "testclasses";
    String CLASSES_DIRECTORY = "classes";
    String SETTINGS_DIRECTORY = ".settings";
    String EXTERNAL_TOOL_BUILDERS_DIRECTORY = ".externalToolBuilders";
    String WEB_ROOT_DIRECTORY = "webroot";
    String WEB_ROOT_DIRECTORY_RELATIVE_PATH = separatorsToSystem(WEB_MODULE_DIRECTORY + '/' + WEB_ROOT_DIRECTORY);
    String WEB_INF_DIRECTORY = "WEB-INF";
    String WEB_INF_DIRECTORY_RELATIVE_PATH = separatorsToSystem(WEB_ROOT_DIRECTORY_RELATIVE_PATH + '/' + WEB_INF_DIRECTORY);
    String WEB_XML_FILE_NAME = "web.xml";
    String WEB_XML_DIRECTORY_RELATIVE_PATH = separatorsToSystem(WEB_INF_DIRECTORY_RELATIVE_PATH + '/' + WEB_XML_FILE_NAME);
    String COMMON_WEB_SRC_DIRECTORY = "commonwebsrc";
    String COMMON_WEB_MODULE_DIRECTORY = "commonweb";
    String ACCELERATOR_ADDON_DIRECTORY = "acceleratoraddon";
    String PLATFORM_BOOTSTRAP_DIRECTORY = "bootstrap";
    String PLATFORM_MODEL_CLASSES_DIRECTORY = "modelclasses";
    String PLATFORM_TOMCAT_DIRECTORY = "tomcat";
    String PLATFORM_TOMCAT_WORK_DIRECTORY = "work";

    String PLATFORM_LIBRARY_GROUP = "Platform Bootstrap";

    String WEBINF_LIB_DIRECTORY = separatorsToSystem("webroot/WEB-INF/lib");
    String WEB_WEBINF_LIB_DIRECTORY = separatorsToSystem(WEB_MODULE_DIRECTORY + '/' + WEBINF_LIB_DIRECTORY);
    String COMMONWEB_WEBINF_LIB_DIRECTORY = separatorsToSystem(COMMON_WEB_MODULE_DIRECTORY + '/' + WEBINF_LIB_DIRECTORY);
    String WEB_INF_CLASSES_DIRECTORY = separatorsToSystem("web/webroot/WEB-INF/classes");
    String WEB_SRC_DIRECTORY = separatorsToSystem("web/src");
    String HMC_LIB_DIRECTORY = separatorsToSystem("hmc/bin");
    String BACKOFFICE_LIB_DIRECTORY = separatorsToSystem("backoffice/bin");

    String PL_BOOTSTRAP_LIB_DIRECTORY = separatorsToSystem("bootstrap/bin");
    String PL_BOOTSTRAP_GEN_SRC_DIRECTORY = separatorsToSystem("bootstrap/gensrc");
    String PL_TOMCAT_LIB_DIRECTORY = separatorsToSystem("tomcat/lib");
    String PL_TOMCAT_BIN_DIRECTORY = separatorsToSystem("tomcat/bin");

    String CONFIG_LICENCE_DIRECTORY = "licence";

    String ECLIPSE_BIN_DIRECTORY = "eclipsebin";
    String WEB_COMPILER_OUTPUT_PATH = WEB_INF_CLASSES_DIRECTORY;
    String WEB_COMPILER_FAKE_OUTPUT_PATH = separatorsToSystem("web/webroot/WEB-INF/eclipsebin");
    String BACKOFFICE_COMPILER_OUTPUT_PATH = separatorsToSystem("/backoffice/classes");
    String BACKOFFICE_COMPILER_FAKE_OUTPUT_PATH = separatorsToSystem("/backoffice/eclipsebin");
    String JAVA_COMPILER_OUTPUT_PATH = separatorsToSystem("/classes");
    String JAVA_COMPILER_FAKE_OUTPUT_PATH = ECLIPSE_BIN_DIRECTORY;

    String HYBRIS_PROJECT_SETTINGS_FILE_NAME = "hybrisProjectSettings.xml";
    String HYBRIS_INTEGRATION_SETTINGS_FILE_NAME = "hybrisIntegrationSettings.xml";

    String DEFAULT_DIRECTORY_NAME_FOR_IDEA_MODULE_FILES = "idea-module-files";
    String PLUGIN_ID = "ccom.intellij.idea.plugin.hybris.impex";
    String CONFIGURATOR_FACTORY_ID = PLUGIN_ID + ".hybrisConfiguratorFactory";

    String HYBRIS_ITEMS_XML_FILE_ENDING = "-items.xml";
    String RULESET_XML = "ruleset.xml";

    String HMC_WEB_INF_CLASSES = separatorsToSystem("/bin/ext-platform-optional/hmc/web/webroot/WEB-INF/classes");
    String BACKOFFICE_WEB_INF_LIB = separatorsToSystem("/bin/ext-backoffice/backoffice/web/webroot/WEB-INF/lib");

    String DESCRIPTOR_TYPE = "descriptorType";
    String READ_ONLY = "importedAsReadOnly";

    int MAX_EXISTING_MODULE_NAMES = 20;
    String DEBUG_PORT = "8000";
    String TOMCAT_JAVA_DEBUG_OPTIONS = "tomcat.debugjavaoptions";
    String X_RUNJDWP_TRANSPORT = "-Xrunjdwp:transport=";
    String ADDRESS = "address=";
    String ANT_ENCODING = "-Dfile.encoding=UTF-8";
    String ANT_XMX = "-Xmx";
    String ANT_PLATFORM_HOME = "PLATFORM_HOME";
    String ANT_OPTS = "ANT_OPTS";
    String ANT_HOME = "ANT_HOME";
    String ANT_LIB_DIR = "resources/ant/lib";
    String ANT_BUILD_XML = "build.xml";
    int ANT_HEAP_SIZE_MB = 512;
    int ANT_STACK_SIZE_MB = 128;
    String ANT_TASK_NAME_ALL = "all";
    String ANT_TASK_NAME_CLEAN = "clean";
}
