package com.intellij.idea.plugin.hybris.utils;

import com.intellij.AbstractBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

/**
 * Created 1:43 AM 08 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public final class HybrisI18NBundleUtils extends AbstractBundle {

    public static final String PATH_TO_BUNDLE = "i18n.HybrisBundle";

    private static final HybrisI18NBundleUtils BUNDLE = new HybrisI18NBundleUtils();

    private HybrisI18NBundleUtils() {
        super(PATH_TO_BUNDLE);
    }

    @NotNull
    public static String message(@NotNull @PropertyKey(resourceBundle = PATH_TO_BUNDLE) final String key,
                                 @NotNull final Object... params) {
        final String message = BUNDLE.getMessage(key, params);

        return (null == message) ? "" : message;
    }
}
