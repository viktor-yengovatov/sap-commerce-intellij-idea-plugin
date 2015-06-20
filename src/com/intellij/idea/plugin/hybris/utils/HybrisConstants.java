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
    public static final String CONFIG_EXTENSION_NAME = "config";

    private HybrisConstants() throws IllegalAccessException {
        throw new IllegalAccessException("Should never be accessed.");
    }
}
