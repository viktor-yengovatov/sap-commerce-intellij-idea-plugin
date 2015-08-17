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
