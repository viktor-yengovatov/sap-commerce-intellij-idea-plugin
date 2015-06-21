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
