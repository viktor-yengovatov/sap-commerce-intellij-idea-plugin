package com.intellij.idea.plugin.hybris.utils;

/**
 * Created 10:30 PM 07 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public final class HybrisConstants {

    public static final String EXTENSION_INFO_XML = "extensioninfo.xml";
    public static final String NEW_MODULE_FILE_EXTENSION = ".iml";
    public static final String OLD_MODULE_FILE_EXTENSION = ".iml";

    private HybrisConstants() throws IllegalAccessException {
        throw new IllegalAccessException("Should never be accessed.");
    }
}
