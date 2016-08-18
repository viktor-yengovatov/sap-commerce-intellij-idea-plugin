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

package com.intellij.idea.plugin.hybris.project.services.impl;

import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import com.intellij.idea.plugin.hybris.project.services.HybrisProjectService;
import com.intellij.openapi.diagnostic.Logger;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created 10:39 PM 11 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class DefaultHybrisProjectService implements HybrisProjectService {

    private static final Logger LOG = Logger.getInstance(DefaultHybrisProjectService.class);

    @Override
    public boolean isConfigModule(@NotNull final File file) {
        Validate.notNull(file);

        return file.getName().equals(HybrisConstants.CONFIG_EXTENSION_NAME)
               && new File(file, HybrisConstants.LOCAL_EXTENSIONS_XML).isFile();
    }

    @Override
    public boolean isPlatformModule(@NotNull final File file) {
        Validate.notNull(file);

        return file.getName().equals(HybrisConstants.PLATFORM_EXTENSION_NAME)
               && new File(file, HybrisConstants.EXTENSIONS_XML).isFile();
    }

    @Override
    public boolean isPlatformExtModule(@NotNull final File file) {
        Validate.notNull(file);

        return file.getAbsolutePath().contains(HybrisConstants.PLATFORM_EXT_MODULE_PREFIX)
               && new File(file, HybrisConstants.EXTENSION_INFO_XML).isFile()
               && !isCoreExtModule(file);
    }

    @Override
    public boolean isCoreExtModule(@NotNull final File file) {
        Validate.notNull(file);

        return file.getAbsolutePath().contains(HybrisConstants.PLATFORM_EXT_MODULE_PREFIX)
               && file.getName().equals(HybrisConstants.CORE_EXTENSION_NAME)
               && new File(file, HybrisConstants.EXTENSION_INFO_XML).isFile();
    }

    @Override
    public boolean isRegularModule(@NotNull final File file) {
        Validate.notNull(file);

        return new File(file, HybrisConstants.EXTENSION_INFO_XML).isFile()
               && !isPlatformExtModule(file);
    }

    @Override
    public boolean isOutOfTheBoxModule(@NotNull final File file) {
        Validate.notNull(file);

        return file.getAbsolutePath().contains(HybrisConstants.PLATFORM_OOTB_MODULE_PREFIX)
               && new File(file, HybrisConstants.EXTENSION_INFO_XML).isFile();
    }
}
