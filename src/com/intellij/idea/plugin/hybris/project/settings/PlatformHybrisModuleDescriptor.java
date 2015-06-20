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

    @NotNull
    @Override
    public void loadLibs(@NotNull final ModifiableRootModel modifiableRootModel) {
        final File libFolder = new File(
            getRootDirectory(), HybrisConstants.LIB_DIRECTORY
        );
        LibUtils.addJarFolderToProjectLibs(modifiableRootModel.getProject(), libFolder);
        LibUtils.addProjectLibsToModule(modifiableRootModel.getProject(), modifiableRootModel);

        final File binBootstrap = new File(
            getRootDirectory(), HybrisConstants.PL_BOOTSTRAP_LIB_DIRECTORY
        );
        LibUtils.addJarFolderToModuleLibs(modifiableRootModel, binBootstrap, false);
        final File tomcatBin = new File(
            getRootDirectory(), HybrisConstants.PL_TOMCAT_BIN_DIRECTORY
        );
        LibUtils.addJarFolderToModuleLibs(modifiableRootModel, tomcatBin, false);
        final File tomcatLib = new File(
            getRootDirectory(), HybrisConstants.PL_TOMCAT_LIB_DIRECTORY
        );
        LibUtils.addJarFolderToModuleLibs(modifiableRootModel, tomcatLib, false);
        final File resFolder = new File(getRootDirectory(), HybrisConstants.RESOURCES_DIRECTORY);
        File filderToLoadRes;
        for(File file : resFolder.listFiles())
        {
            if(file.isDirectory()){
                filderToLoadRes = new File(file.getAbsolutePath(), HybrisConstants.LIB_DIRECTORY);
                LibUtils.addJarFolderToModuleLibs(modifiableRootModel, filderToLoadRes, false);
                filderToLoadRes = new File(file.getAbsolutePath(), HybrisConstants.BIN_DIRECTORY);
                LibUtils.addJarFolderToModuleLibs(modifiableRootModel, filderToLoadRes, true);
            }
        }
    }
}
