/*
 * This file is part of "Hybris Integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2015 Alexander Bartash <AlexanderBartash@gmail.com>
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

package com.intellij.idea.plugin.hybris.project.utils;

import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptorFactory;
import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptorFactoryImpl;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import com.intellij.openapi.diagnostic.Logger;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created 10:39 PM 11 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public final class HybrisProjectUtils {

    public static final HybrisModuleDescriptorFactory MODULE_DESCRIPTOR_FACTORY = HybrisModuleDescriptorFactoryImpl.INSTANCE;

    private static final Logger LOG = Logger.getInstance(HybrisProjectUtils.class);

    private HybrisProjectUtils() throws IllegalAccessException {
        throw new IllegalAccessException("Should never be accessed.");
    }

    public static boolean isConfigModule(@NotNull final File file) {
        Validate.notNull(file);

        return file.getName().equals(HybrisConstants.CONFIG_EXTENSION_NAME)
               && new File(file, HybrisConstants.LOCAL_EXTENSIONS_XML).isFile();
    }

    public static boolean isPlatformModule(@NotNull final File file) {
        Validate.notNull(file);

        return file.getName().equals(HybrisConstants.PLATFORM_EXTENSION_NAME)
               && new File(file, HybrisConstants.EXTENSIONS_XML).isFile();
    }

    public static boolean isRegularModule(@NotNull final File file) {
        Validate.notNull(file);

        return new File(file, HybrisConstants.EXTENSION_INFO_XML).isFile();
    }
}
