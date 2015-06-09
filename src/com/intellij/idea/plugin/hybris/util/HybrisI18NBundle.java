package com.intellij.idea.plugin.hybris.util;

import com.intellij.AbstractBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

/**
 * Created 1:43 AM 08 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisI18NBundle extends AbstractBundle {

    private static final HybrisI18NBundle BUNDLE = new HybrisI18NBundle();
    public static final String PATH_TO_BUNDLE = "i18n.HybrisBundle";

    private HybrisI18NBundle() {
        super(PATH_TO_BUNDLE);
    }

    public static String message(@NotNull @PropertyKey(resourceBundle = PATH_TO_BUNDLE) final String key,
                                 @NotNull final Object... params) {
        return BUNDLE.getMessage(key, params);
    }
}
