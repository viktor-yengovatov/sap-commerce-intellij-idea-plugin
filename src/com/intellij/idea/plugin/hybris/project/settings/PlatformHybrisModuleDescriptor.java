package com.intellij.idea.plugin.hybris.project.settings;

import com.google.common.collect.Sets;
import com.intellij.idea.plugin.hybris.project.exceptions.HybrisConfigurationException;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import com.intellij.idea.plugin.hybris.utils.LibUtils;
import com.intellij.openapi.roots.ModifiableRootModel;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.Set;

/**
 * Created 3:55 PM 13 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class PlatformHybrisModuleDescriptor extends AbstractHybrisModuleDescriptor {

    public PlatformHybrisModuleDescriptor(@NotNull final File moduleRootDirectory,
                                          @NotNull final HybrisProjectDescriptor rootProjectDescriptor
    ) throws HybrisConfigurationException {
        super(moduleRootDirectory, rootProjectDescriptor);
    }

    @NotNull
    @Override
    public String getModuleName() {
        return HybrisConstants.PLATFORM_EXTENSION_NAME;
    }

    @NotNull
    @Override
    public Set<String> getRequiredExtensionNames() {
        final File extDirectory = new File(this.getModuleRootDirectory(), HybrisConstants.PLATFORM_EXTENSIONS_DIRECTORY_NAME);

        final Set<String> platformDependencies = Sets.newHashSet(
            HybrisConstants.CONFIG_EXTENSION_NAME
        );

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

    @Override
    public void loadLibs(@NotNull final ModifiableRootModel modifiableRootModel) {
        final File libFolder = new File(
            getModuleRootDirectory(), HybrisConstants.LIB_DIRECTORY
        );
        LibUtils.addJarFolderToProjectLibs(modifiableRootModel.getProject(), libFolder);
        LibUtils.addProjectLibsToModule(modifiableRootModel.getProject(), modifiableRootModel);

        final File binBootstrap = new File(
            getModuleRootDirectory(), HybrisConstants.PL_BOOTSTRAP_LIB_DIRECTORY
        );
        LibUtils.addJarFolderToModuleLibs(modifiableRootModel, binBootstrap, true);
        final File tomcatBin = new File(
            getModuleRootDirectory(), HybrisConstants.PL_TOMCAT_BIN_DIRECTORY
        );
        LibUtils.addJarFolderToModuleLibs(modifiableRootModel, tomcatBin, false);
        final File tomcatLib = new File(
            getModuleRootDirectory(), HybrisConstants.PL_TOMCAT_LIB_DIRECTORY
        );
        LibUtils.addJarFolderToModuleLibs(modifiableRootModel, tomcatLib, true);
        final File resFolder = new File(getModuleRootDirectory(), HybrisConstants.RESOURCES_DIRECTORY);
        File filderToLoadRes;

        final File[] files = resFolder.listFiles();

        if (null == files) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                filderToLoadRes = new File(file.getAbsolutePath(), HybrisConstants.LIB_DIRECTORY);
                LibUtils.addJarFolderToModuleLibs(modifiableRootModel, filderToLoadRes, false);
                filderToLoadRes = new File(file.getAbsolutePath(), HybrisConstants.BIN_DIRECTORY);
                LibUtils.addJarFolderToModuleLibs(modifiableRootModel, filderToLoadRes, true);
            }
        }
    }
}
