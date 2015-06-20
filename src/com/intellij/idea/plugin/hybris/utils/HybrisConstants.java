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

    public static final String WEB_INF_LIB_DIRECTORY = "web/webroot/WEB-INF/lib";
    public static final String WEB_INF_CLASSES_DIRECTORY = "web/webroot/WEB-INF/classes";
    public static final String HMC_LIB_DIRECTORY = "hmc/bin";
    public static final String BACKOFFICE_LIB_DIRECTORY = "backoffice/bin";

    public static final String PL_BOOTSTRAP_LIB_DIRECTORY = "bootstrap/bin";
    public static final String PL_TOMCAT_LIB_DIRECTORY = "tomcat/lib";
    public static final String PL_TOMCAT_BIN_DIRECTORY = "tomcat/bin";

    private HybrisConstants() throws IllegalAccessException {
        throw new IllegalAccessException("Should never be accessed.");
    }
}
