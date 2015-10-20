/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
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

package com.intellij.idea.plugin.hybris.project.settings;

import com.intellij.idea.plugin.hybris.project.exceptions.HybrisConfigurationException;
import com.intellij.idea.plugin.hybris.project.utils.HybrisProjectUtils;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created 1:58 PM 20 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisModuleDescriptorFactoryImpl implements HybrisModuleDescriptorFactory {

    public static final HybrisModuleDescriptorFactory INSTANCE = new HybrisModuleDescriptorFactoryImpl();

    protected HybrisModuleDescriptorFactoryImpl() {
    }

    @NotNull
    @Override
    public HybrisModuleDescriptor createDescriptor(@NotNull final File file,
                                                   @NotNull final HybrisProjectDescriptor rootProjectDescriptor
    ) throws HybrisConfigurationException {
        Validate.notNull(file);
        Validate.notNull(rootProjectDescriptor);

        if (HybrisProjectUtils.isConfigModule(file)) {
            return new ConfigHybrisModuleDescriptor(file, rootProjectDescriptor);
        }

        if (HybrisProjectUtils.isPlatformModule(file)) {
            return new PlatformHybrisModuleDescriptor(file, rootProjectDescriptor);
        }

        return new DefaultHybrisModuleDescriptor(file, rootProjectDescriptor);
    }

}
