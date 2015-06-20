package com.intellij.idea.plugin.hybris.project.settings;

import com.intellij.idea.plugin.hybris.project.exceptions.HybrisConfigurationException;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created 1:58 PM 20 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public interface HybrisModuleDescriptorFactory {

    @NotNull
    HybrisModuleDescriptor createDescriptor(@NotNull File file) throws HybrisConfigurationException;

}
