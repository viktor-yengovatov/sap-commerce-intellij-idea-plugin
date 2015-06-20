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
