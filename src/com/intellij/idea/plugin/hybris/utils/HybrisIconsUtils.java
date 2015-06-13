package com.intellij.idea.plugin.hybris.utils;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * Created 1:54 AM 08 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public final class HybrisIconsUtils {

    public static final Icon IMPEX_FILE = IconLoader.getIcon("/icons/jar-gray.png");
    public static final Icon HYBRIS_ICON = IconLoader.getIcon("/icons/hybris_icon.png");

    private HybrisIconsUtils() throws IllegalAccessException {
        throw new IllegalAccessException("Should never be accessed.");
    }
}
