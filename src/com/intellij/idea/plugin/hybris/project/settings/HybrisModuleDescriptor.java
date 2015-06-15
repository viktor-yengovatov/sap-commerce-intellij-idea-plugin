package com.intellij.idea.plugin.hybris.project.settings;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created 1:20 PM 14 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public interface HybrisModuleDescriptor extends Comparable<HybrisModuleDescriptor> {

    @NotNull
    String getModuleName();

    @NotNull
    File getRootDirectory();

    @NotNull
    File getModuleFile();

    @NotNull
    File getHybrisProjectFile();
}
