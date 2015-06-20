package com.intellij.idea.plugin.hybris.project.settings;

import com.intellij.idea.plugin.hybris.project.exceptions.HybrisConfigurationException;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
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
    public HybrisModuleDescriptor createDescriptor(@NotNull final File file) throws HybrisConfigurationException {
        Validate.notNull(file);

        if (this.isConfigModule(file)) {
            return new ConfigHybrisModuleDescriptor(file);
        }

        if (this.isPlatformModule(file)) {
            return new PlatformHybrisModuleDescriptor(file);
        }

        return new DefaultHybrisModuleDescriptor(file);
    }

    protected boolean isConfigModule(@NotNull final File file) {
        Validate.notNull(file);

        return new File(file, HybrisConstants.LOCAL_EXTENSIONS_XML).isFile();
    }

    protected boolean isPlatformModule(@NotNull final File file) {
        Validate.notNull(file);

        return new File(file, HybrisConstants.EXTENSIONS_XML).isFile();
    }

}
