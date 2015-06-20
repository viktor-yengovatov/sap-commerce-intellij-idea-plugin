package com.intellij.idea.plugin.hybris.project.settings;

import com.google.common.collect.Sets;
import com.intellij.idea.plugin.hybris.project.exceptions.HybrisConfigurationException;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

/**
 * Created 3:55 PM 13 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class PlatformHybrisModuleDescriptor extends AbstractHybrisModuleDescriptor {

    public PlatformHybrisModuleDescriptor(@NotNull final File moduleRootDirectory) throws HybrisConfigurationException {
        super(moduleRootDirectory);
    }

    @NotNull
    @Override
    public String getModuleName() {
        return HybrisConstants.PLATFORM_EXTENSION_NAME;
    }

    @NotNull
    @Override
    public Set<String> getRequiredExtensionNames() {
        final File extDirectory = new File(this.getRootDirectory(), HybrisConstants.PLATFORM_EXTENSIONS_DIRECTORY_NAME);

        final Set<String> platformDependencies = Sets.newHashSet(HybrisConstants.CONFIG_EXTENSION_NAME);

        if (extDirectory.isDirectory()) {
            final File[] files = extDirectory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);

            if (null != files) {
                for (File file : files) {
                    platformDependencies.add(file.getName());
                }
            }
        }

        return Collections.unmodifiableSet(platformDependencies);
    }
}
