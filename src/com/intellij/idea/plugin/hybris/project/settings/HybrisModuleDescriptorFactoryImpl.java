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
