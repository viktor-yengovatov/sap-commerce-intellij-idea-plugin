/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2015 Alexander Bartash <AlexanderBartash@gmail.com>
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

import com.google.common.collect.Iterables;
import com.intellij.idea.plugin.hybris.project.exceptions.HybrisConfigurationException;
import com.intellij.idea.plugin.hybris.project.settings.jaxb.localextensions.ExtensionType;
import com.intellij.idea.plugin.hybris.project.settings.jaxb.localextensions.Hybrisconfig;
import com.intellij.idea.plugin.hybris.project.utils.FindHybrisModuleDescriptorByName;
import com.intellij.idea.plugin.hybris.project.utils.HybrisProjectUtils;
import com.intellij.idea.plugin.hybris.project.utils.Processor;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import gnu.trove.THashSet;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.GuardedBy;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

/**
 * Created 3:55 PM 13 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class DefaultHybrisProjectDescriptor implements HybrisProjectDescriptor {

    private static final Logger LOG = Logger.getInstance(DefaultHybrisProjectDescriptor.class);

    @Nullable
    protected final Project project;
    @NotNull
    protected final List<HybrisModuleDescriptor> foundModules = new ArrayList<HybrisModuleDescriptor>();
    @NotNull
    protected final List<HybrisModuleDescriptor> modulesChosenForImport = new ArrayList<HybrisModuleDescriptor>();
    @NotNull
    @GuardedBy("lock")
    protected final Set<HybrisModuleDescriptor> alreadyOpenedModules = new HashSet<HybrisModuleDescriptor>();
    protected final Lock lock = new ReentrantLock();
    @Nullable
    protected File rootDirectory;
    @Nullable
    protected File modulesFilesDirectory;
    @Nullable
    protected File sourceCodeZip;
    protected boolean openProjectSettingsAfterImport;
    protected boolean importOotbModulesInReadOnlyMode = true;
    @NotNull
    protected final List<String> explicitlyDefinedModules = new ArrayList<String>();
    @Nullable
    protected File hybrisDistributionDirectory;
    @Nullable
    protected File customExtensionsDirectory;

    public DefaultHybrisProjectDescriptor(@Nullable final Project project) {
        this.project = project;
    }

    @Nullable
    @Override
    public Project getProject() {
        return this.project;
    }

    @NotNull
    @Override
    public List<HybrisModuleDescriptor> getFoundModules() {
        return Collections.unmodifiableList(this.foundModules);
    }

    @NotNull
    @Override
    public List<HybrisModuleDescriptor> getModulesChosenForImport() {
        return this.modulesChosenForImport;
    }

    @Override
    public void setModulesChosenForImport(@NotNull final List<HybrisModuleDescriptor> moduleDescriptors) {
        Validate.notNull(moduleDescriptors);

        this.modulesChosenForImport.clear();
        this.modulesChosenForImport.addAll(moduleDescriptors);
    }

    @NotNull
    @Override
    public Set<HybrisModuleDescriptor> getAlreadyOpenedModules() {
        if (null == this.project) {
            return Collections.emptySet();
        }

        this.lock.lock();

        try {
            if (this.alreadyOpenedModules.isEmpty()) {
                this.alreadyOpenedModules.addAll(this.getAlreadyOpenedModules(this.project));
            }
        } finally {
            this.lock.unlock();
        }

        return Collections.unmodifiableSet(this.alreadyOpenedModules);
    }

    @Nullable
    @Override
    public File getRootDirectory() {
        return this.rootDirectory;
    }

    @Override
    public void clear() {
        this.rootDirectory = null;
        this.hybrisDistributionDirectory = null;
        this.customExtensionsDirectory = null;
        this.foundModules.clear();
        this.modulesChosenForImport.clear();
        this.explicitlyDefinedModules.clear();
    }

    @Nullable
    @Override
    public File getModulesFilesDirectory() {
        return this.modulesFilesDirectory;
    }

    @Override
    public void setModulesFilesDirectory(@Nullable final File modulesFilesDirectory) {
        this.modulesFilesDirectory = modulesFilesDirectory;
    }

    @Nullable
    @Override
    public File getSourceCodeZip() {
        return sourceCodeZip;
    }

    @Override
    public void setSourceCodeZip(@Nullable final File sourceCodeZip) {
        this.sourceCodeZip = sourceCodeZip;
    }

    @Override
    public void setRootDirectoryAndScanForModules(@NotNull final File rootDirectory,
                                                  @Nullable final Processor<File> progressListenerProcessor,
                                                  @Nullable final Processor<List<File>> errorsProcessor) {
        Validate.notNull(rootDirectory);

        this.rootDirectory = rootDirectory;

        try {
            this.scanDirectoryForHybrisModules(rootDirectory, progressListenerProcessor, errorsProcessor);
            this.processLocalExtensions();
        } catch (InterruptedException e) {
            LOG.warn(e);

            this.rootDirectory = null;
            this.foundModules.clear();
            this.explicitlyDefinedModules.clear();
        }
    }

    private void processLocalExtensions() {
        final ConfigHybrisModuleDescriptor configHybrisModuleDescriptor = findConfigDir();
        if (configHybrisModuleDescriptor == null) {
            return;
        }
        Hybrisconfig hybrisconfig = unmarshalLocalExtensions(configHybrisModuleDescriptor);
        if (hybrisconfig == null) {
            return;
        }
        processHybrisConfig(hybrisconfig);
        preselectModules(configHybrisModuleDescriptor);
    }

    private void preselectModules(@NotNull final ConfigHybrisModuleDescriptor configHybrisModuleDescriptor) {
        Validate.notNull(configHybrisModuleDescriptor);
        for (HybrisModuleDescriptor hybrisModuleDescriptor: foundModules) {
            if (explicitlyDefinedModules.contains(hybrisModuleDescriptor.getName())) {
                hybrisModuleDescriptor.setInLocalExtensions(true);
            }
        }
        configHybrisModuleDescriptor.setPreselected(true);
    }

    @Nullable
    private ConfigHybrisModuleDescriptor findConfigDir() {
        List<ConfigHybrisModuleDescriptor> foundConfigModules = new ArrayList<ConfigHybrisModuleDescriptor>();
        PlatformHybrisModuleDescriptor platformHybrisModuleDescriptor = null;
        for (HybrisModuleDescriptor moduleDescriptor: foundModules) {
            if (moduleDescriptor instanceof ConfigHybrisModuleDescriptor) {
                foundConfigModules.add((ConfigHybrisModuleDescriptor) moduleDescriptor);
            }
            if (moduleDescriptor instanceof PlatformHybrisModuleDescriptor) {
                platformHybrisModuleDescriptor = (PlatformHybrisModuleDescriptor) moduleDescriptor;
            }
        }
        if (foundConfigModules.isEmpty()) {
            return null;
        }
        final File platformDir = platformHybrisModuleDescriptor.getRootDirectory();
        final File expectedConfigDir = new File(platformDir+ HybrisConstants.CONFIG_RELATIVE_PATH);
        if (!expectedConfigDir.isDirectory()) {
            if (foundConfigModules.size() == 1) {
                return foundConfigModules.get(0);
            }
            return null;
        }
        for (ConfigHybrisModuleDescriptor configHybrisModuleDescriptor: foundConfigModules) {
            if (FileUtil.filesEqual(configHybrisModuleDescriptor.getRootDirectory(), expectedConfigDir)) {
                return configHybrisModuleDescriptor;
            }
        }
        return null;
    }

    private void processHybrisConfig(@NotNull final Hybrisconfig hybrisconfig) {
        final List<ExtensionType> extensionTypeList = hybrisconfig.getExtensions().getExtension();
        for (ExtensionType extensionType: extensionTypeList) {
            final String name = extensionType.getName();
            if (name != null) {
                explicitlyDefinedModules.add(name);
                continue;
            }
            final String dir = extensionType.getDir();
            int indexSlash = dir.lastIndexOf("/");
            int indexBack = dir.lastIndexOf("\\");
            int index = Math.max(indexSlash, indexBack);
            if (index == -1) {
                explicitlyDefinedModules.add(dir);
            } else {
                explicitlyDefinedModules.add(dir.substring(index+1));
            }
        }
    }

    @Nullable
    private Hybrisconfig unmarshalLocalExtensions(@NotNull final ConfigHybrisModuleDescriptor configHybrisModuleDescriptor) {
        Validate.notNull(configHybrisModuleDescriptor);

        File localextensions = new File (configHybrisModuleDescriptor.getRootDirectory(), HybrisConstants.LOCAL_EXTENSIONS_XML);
        if (!localextensions.exists()) {
            return null;
        }
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Hybrisconfig.class);
            if (null == jaxbContext) {
                LOG.error(String.format(
                    "Can not unmarshal '%s' because JAXBContext has not been created.", localextensions.getAbsolutePath()
                ));

                return null;
            }

            final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            return (Hybrisconfig) jaxbUnmarshaller.unmarshal(localextensions);
        } catch (JAXBException e) {
            LOG.error("Can not unmarshal " + localextensions.getAbsolutePath(), e);
        }

        return null;
    }

    @Override
    public boolean isOpenProjectSettingsAfterImport() {
        return this.openProjectSettingsAfterImport;
    }

    @Override
    public void setOpenProjectSettingsAfterImport(final boolean openProjectSettingsAfterImport) {
        this.openProjectSettingsAfterImport = openProjectSettingsAfterImport;
    }

    @Override
    public boolean isImportOotbModulesInReadOnlyMode() {
        return importOotbModulesInReadOnlyMode;
    }

    @Override
    public void setImportOotbModulesInReadOnlyMode(final boolean importOotbModulesInReadOnlyMode) {
        this.importOotbModulesInReadOnlyMode = importOotbModulesInReadOnlyMode;
    }

    @Override
    @Nullable
    public File getHybrisDistributionDirectory() {
        return hybrisDistributionDirectory;
    }

    @Override
    public void setHybrisDistributionDirectory(@Nullable final File hybrisDistributionDirectory) {
        this.hybrisDistributionDirectory = hybrisDistributionDirectory;
    }

    @Override
    @Nullable
    public File getCustomExtensionsDirectory() {
        return customExtensionsDirectory;
    }

    @Override
    public void setCustomExtensionsDirectory(@Nullable final File customExtensionsDirectory) {
        this.customExtensionsDirectory = customExtensionsDirectory;
    }

    @NotNull
    protected Set<HybrisModuleDescriptor> getAlreadyOpenedModules(@NotNull final Project project) {
        Validate.notNull(project);

        final Set<HybrisModuleDescriptor> existingModules = new THashSet<HybrisModuleDescriptor>();

        for (Module module : ModuleManager.getInstance(project).getModules()) {
            try {
                final VirtualFile[] contentRoots = ModuleRootManager.getInstance(module).getContentRoots();
                if (ArrayUtils.isEmpty(contentRoots)) {
                    LOG.error("Can not find module root directory for module: " + module.getName());
                    continue;
                }

                final HybrisModuleDescriptor moduleDescriptor = HybrisProjectUtils.MODULE_DESCRIPTOR_FACTORY.createDescriptor(
                    VfsUtil.virtualToIoFile(contentRoots[0]), this
                );

                existingModules.add(moduleDescriptor);
            } catch (HybrisConfigurationException e) {
                LOG.error(e);
            }
        }

        return existingModules;
    }

    protected void scanDirectoryForHybrisModules(@NotNull final File rootDirectory,
                                                 @Nullable final Processor<File> progressListenerProcessor,
                                                 @Nullable final Processor<List<File>> errorsProcessor
    ) throws InterruptedException {
        Validate.notNull(rootDirectory);

        this.foundModules.clear();

        final List<File> moduleRootDirectories = this.findModuleRoots(
            rootDirectory, progressListenerProcessor
        );

        final List<HybrisModuleDescriptor> moduleDescriptors = new ArrayList<HybrisModuleDescriptor>();
        final List<File> pathsFailedToImport = new ArrayList<File>();

        for (File moduleRootDirectory : moduleRootDirectories) {
            try {
                moduleDescriptors.add(HybrisProjectUtils.MODULE_DESCRIPTOR_FACTORY.createDescriptor(
                    moduleRootDirectory, this
                ));
            } catch (HybrisConfigurationException e) {
                LOG.error("Can not import a module using path: " + pathsFailedToImport, e);

                pathsFailedToImport.add(moduleRootDirectory);
            }
        }

        if (null != errorsProcessor) {
            if (errorsProcessor.shouldContinue(pathsFailedToImport)) {
                throw new InterruptedException("Modules scanning has been interrupted.");
            }
        }

        Collections.sort(moduleDescriptors);

        this.buildDependencies(moduleDescriptors);

        this.foundModules.addAll(moduleDescriptors);
    }

    @NotNull
    protected List<File> findModuleRoots(@NotNull final File rootProjectDirectory,
                                         @Nullable final Processor<File> progressListenerProcessor
    ) throws InterruptedException {
        Validate.notNull(rootProjectDirectory);

        final List<File> paths = new ArrayList<File>(1);

        if (null != progressListenerProcessor) {
            if (!progressListenerProcessor.shouldContinue(rootProjectDirectory)) {
                throw new InterruptedException("Modules scanning has been interrupted.");
            }
        }

        if (HybrisProjectUtils.isRegularModule(rootProjectDirectory)
            || HybrisProjectUtils.isConfigModule(rootProjectDirectory)) {

            paths.add(rootProjectDirectory);

        } else {
            if (HybrisProjectUtils.isPlatformModule(rootProjectDirectory)) {
                paths.add(rootProjectDirectory);
            }

            if (rootProjectDirectory.isDirectory()) {
                final File[] files = rootProjectDirectory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);

                if (null != files) {
                    for (File file : files) {
                        paths.addAll(this.findModuleRoots(file, progressListenerProcessor));
                    }
                }
            }
        }

        return paths;
    }

    protected void buildDependencies(@NotNull final Iterable<HybrisModuleDescriptor> moduleDescriptors) {
        Validate.notNull(moduleDescriptors);

        for (HybrisModuleDescriptor moduleDescriptor : moduleDescriptors) {

            final Set<String> requiredExtensionNames = moduleDescriptor.getRequiredExtensionNames();

            if (isEmpty(requiredExtensionNames)) {
                continue;
            }

            final Set<HybrisModuleDescriptor> dependencies = new HashSet<HybrisModuleDescriptor>(
                requiredExtensionNames.size()
            );

            for (String requiresExtensionName : requiredExtensionNames) {

                final Iterable<HybrisModuleDescriptor> dependsOn = Iterables.filter(
                    moduleDescriptors, new FindHybrisModuleDescriptorByName(requiresExtensionName)
                );

                if (Iterables.isEmpty(dependsOn)) {
                    LOG.warn(String.format(
                        "Module '%s' contains unsatisfied dependency '%s'.",
                        moduleDescriptor.getName(), requiresExtensionName
                    ));
                } else {
                    for (HybrisModuleDescriptor hybrisModuleDescriptor : dependsOn) {
                        dependencies.add(hybrisModuleDescriptor);
                    }
                }
            }

            moduleDescriptor.setDependenciesTree(dependencies);
        }
    }
}
