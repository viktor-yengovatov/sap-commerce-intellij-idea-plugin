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

package com.intellij.idea.plugin.hybris.project.settings;

import com.intellij.idea.plugin.hybris.project.exceptions.HybrisConfigurationException;
import com.intellij.idea.plugin.hybris.project.services.HybrisProjectService;
import com.intellij.openapi.components.ServiceManager;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created 1:58 PM 20 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class DefaultHybrisModuleDescriptorFactory implements HybrisModuleDescriptorFactory {

    @NotNull
    @Override
    public HybrisModuleDescriptor createDescriptor(@NotNull final File file,
                                                   @NotNull final HybrisProjectDescriptor rootProjectDescriptor
    ) throws HybrisConfigurationException {
        Validate.notNull(file);
        Validate.notNull(rootProjectDescriptor);

        final HybrisProjectService hybrisProjectService = ServiceManager.getService(HybrisProjectService.class);

        if (hybrisProjectService.isConfigModule(file)) {
            return new ConfigHybrisModuleDescriptor(file, rootProjectDescriptor);
        }

        if (hybrisProjectService.isPlatformModule(file)) {
            return new PlatformHybrisModuleDescriptor(file, rootProjectDescriptor);
        }

        if (hybrisProjectService.isCoreExtModule(file)) {
            return new CoreHybrisHybrisModuleDescriptor(file, rootProjectDescriptor);
        }

        if (hybrisProjectService.isPlatformExtModule(file)) {
            return new ExtHybrisModuleDescriptor(file, rootProjectDescriptor);
        }

        if (hybrisProjectService.isOutOfTheBoxModule(file)) {
            return new OotbHybrisModuleDescriptor(file, rootProjectDescriptor);
        }

        return new CustomHybrisModuleDescriptor(file, rootProjectDescriptor);
    }

}
