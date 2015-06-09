package com.intellij.idea.plugin.hybris.util;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * Created 1:54 AM 08 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisIcons {

    public static final Icon IMPEX_FILE = IconLoader.getIcon("/icons/jar-gray.png");
    public static final Icon HYBRIS_ICON = IconLoader.getIcon("/icons/hybris_icon.png");

    private HybrisIcons() throws IllegalAccessException {
        throw new IllegalAccessException();
    }
}
