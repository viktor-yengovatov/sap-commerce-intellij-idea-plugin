/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2016 Alexander Bartash <AlexanderBartash@gmail.com>
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

package com.intellij.idea.plugin.hybris.project.descriptors;

import com.google.common.collect.Iterables;
import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import com.intellij.idea.plugin.hybris.project.exceptions.HybrisConfigurationException;
import com.intellij.idea.plugin.hybris.project.services.HybrisProjectService;
import com.intellij.idea.plugin.hybris.project.settings.jaxb.localextensions.ExtensionType;
import com.intellij.idea.plugin.hybris.project.settings.jaxb.localextensions.Hybrisconfig;
import com.intellij.idea.plugin.hybris.project.tasks.TaskProgressProcessor;
import com.intellij.idea.plugin.hybris.project.utils.FindHybrisModuleDescriptorByName;
import com.intellij.idea.plugin.hybris.settings.HybrisApplicationSettings;
import com.intellij.idea.plugin.hybris.settings.HybrisApplicationSettingsComponent;
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettingsComponent;
import com.intellij.openapi.components.ServiceManager;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.GuardedBy;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.intellij.idea.plugin.hybris.common.utils.CollectionUtils.emptyIfNull;
import static org.apache.commons.collections.CollectionUtils.isEmpty;

/**
 * Created 3:55 PM 13 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class DefaultHybrisProjectDescriptor implements HybrisProjectDescriptor {

    private static final Logger LOG = Logger.getInstance(DefaultHybrisProjectDescriptor.class);

    @Nullable
    protected Project project;
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
    protected Boolean importOotbModulesInReadOnlyMode;
    @NotNull
    protected final List<String> explicitlyDefinedModules = new ArrayList<String>();
    @Nullable
    protected File hybrisDistributionDirectory;
    @Nullable
    protected File customExtensionsDirectory;
    protected boolean customExtensionsPresent;
    @Nullable
    protected String javadocUrl;

    @Override
    public void setProject(@Nullable final Project project) {
        if (project == null) {
            return;
        }
        // the project may not be hybris based project.
        final HybrisProjectSettingsComponent hybrisProjectSettings = HybrisProjectSettingsComponent.getInstance(project);
        if (hybrisProjectSettings == null) {
            return;
        }
        if (hybrisProjectSettings.getState() != null) {
            if (hybrisProjectSettings.getState().isHybisProject()) {
                setHybrisProject(project);
            }
        }
    }

    @Override
    public void setHybrisProject(@Nullable final Project project) {
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
        if (null == getProject()) {
            return Collections.emptySet();
        }

        this.lock.lock();

        try {
            if (this.alreadyOpenedModules.isEmpty()) {
                this.alreadyOpenedModules.addAll(this.getAlreadyOpenedModules(getProject()));
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
                                                  @Nullable final TaskProgressProcessor<File> progressListenerProcessor,
                                                  @Nullable final TaskProgressProcessor<List<File>> errorsProcessor) {
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
        final Hybrisconfig hybrisconfig = unmarshalLocalExtensions(configHybrisModuleDescriptor);
        if (hybrisconfig == null) {
            return;
        }
        processHybrisConfig(hybrisconfig);
        preselectModules(configHybrisModuleDescriptor);
    }

    private void preselectModules(@NotNull final ConfigHybrisModuleDescriptor configHybrisModuleDescriptor) {
        Validate.notNull(configHybrisModuleDescriptor);
        for (HybrisModuleDescriptor hybrisModuleDescriptor: foundModules) {
            if (explicitlyDefinedModules.contains(StringUtils.lowerCase(hybrisModuleDescriptor.getName()))) {
                hybrisModuleDescriptor.setInLocalExtensions(true);
            }
        }
        configHybrisModuleDescriptor.setPreselected(true);
    }

    @Nullable
    private ConfigHybrisModuleDescriptor findConfigDir() {
        final List<ConfigHybrisModuleDescriptor> foundConfigModules = new ArrayList<ConfigHybrisModuleDescriptor>();
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
            final String name = StringUtils.lowerCase(extensionType.getName());
            if (name != null) {
                explicitlyDefinedModules.add(name);
                continue;
            }
            final String dir = extensionType.getDir();
            final int indexSlash = dir.lastIndexOf('/');
            final int indexBack = dir.lastIndexOf('\\');
            final int index = Math.max(indexSlash, indexBack);
            if (index == -1) {
                explicitlyDefinedModules.add(StringUtils.lowerCase(dir));
            } else {
                explicitlyDefinedModules.add(StringUtils.lowerCase(dir.substring(index+1)));
            }
        }
    }

    @Nullable
    private Hybrisconfig unmarshalLocalExtensions(@NotNull final ConfigHybrisModuleDescriptor configHybrisModuleDescriptor) {
        Validate.notNull(configHybrisModuleDescriptor);

        final File localextensions = new File (configHybrisModuleDescriptor.getRootDirectory(), HybrisConstants.LOCAL_EXTENSIONS_XML);
        if (!localextensions.exists()) {
            return null;
        }
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(Hybrisconfig.class);
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
    public Boolean isImportOotbModulesInReadOnlyMode() {
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

    @Override
    public void setCustomExtensionsPresent(final boolean present) {
        this.customExtensionsPresent = present;
    }

    @Nullable
    @Override
    public String getJavadocUrl() {
        return javadocUrl;
    }

    @Override
    public void setJavadocUrl(@Nullable final String javadocUrl) {
        this.javadocUrl = javadocUrl;
    }

    public boolean isCustomExtensionsPresent() {
        return customExtensionsPresent;
    }

    @NotNull
    protected Set<HybrisModuleDescriptor> getAlreadyOpenedModules(@NotNull final Project project) {
        Validate.notNull(project);

        final HybrisModuleDescriptorFactory hybrisModuleDescriptorFactory = ServiceManager.getService(
            HybrisModuleDescriptorFactory.class
        );

        final Set<HybrisModuleDescriptor> existingModules = new THashSet<HybrisModuleDescriptor>();

        for (Module module : ModuleManager.getInstance(project).getModules()) {
            try {
                final VirtualFile[] contentRoots = ModuleRootManager.getInstance(module).getContentRoots();
                if (ArrayUtils.isEmpty(contentRoots)) {
                    LOG.error("Can not find module root directory for module: " + module.getName());
                    continue;
                }

                final HybrisModuleDescriptor moduleDescriptor = hybrisModuleDescriptorFactory.createDescriptor(
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
                                                 @Nullable final TaskProgressProcessor<File> progressListenerProcessor,
                                                 @Nullable final TaskProgressProcessor<List<File>> errorsProcessor
    ) throws InterruptedException {
        Validate.notNull(rootDirectory);

        this.foundModules.clear();

        final List<File> moduleRootDirectories = this.findModuleRoots(
            rootDirectory, progressListenerProcessor
        );

        final List<HybrisModuleDescriptor> moduleDescriptors = new ArrayList<HybrisModuleDescriptor>();
        final List<File> pathsFailedToImport = new ArrayList<File>();

        addRootModule(rootDirectory, moduleDescriptors, pathsFailedToImport);

        final HybrisModuleDescriptorFactory hybrisModuleDescriptorFactory = ServiceManager.getService(
            HybrisModuleDescriptorFactory.class
        );

        for (File moduleRootDirectory : moduleRootDirectories) {
            try {
                moduleDescriptors.add(hybrisModuleDescriptorFactory.createDescriptor(moduleRootDirectory, this));
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

    private void addRootModule(
        final File rootDirectory, final List<HybrisModuleDescriptor> moduleDescriptors,
        final List<File> pathsFailedToImport
    ) {
        final HybrisApplicationSettings hybrisApplicationSettings = HybrisApplicationSettingsComponent.getInstance().getState();
        final boolean groupModules = hybrisApplicationSettings.isGroupModules();
        if (groupModules) {
            return;
        }
        try {
            moduleDescriptors.add(new RootModuleDescriptor(rootDirectory, this));
        } catch (HybrisConfigurationException e) {
            LOG.error("Can not import a module using path: " + pathsFailedToImport, e);
            pathsFailedToImport.add(rootDirectory);
        }
    }

    @NotNull
    protected List<File> findModuleRoots(@NotNull final File rootProjectDirectory,
                                         @Nullable final TaskProgressProcessor<File> progressListenerProcessor
    ) throws InterruptedException {
        Validate.notNull(rootProjectDirectory);

        final List<File> paths = new ArrayList<File>(1);

        if (null != progressListenerProcessor) {
            if (!progressListenerProcessor.shouldContinue(rootProjectDirectory)) {
                throw new InterruptedException("Modules scanning has been interrupted.");
            }
        }

        final HybrisProjectService hybrisProjectService = ServiceManager.getService(HybrisProjectService.class);

        if (hybrisProjectService.isRegularModule(rootProjectDirectory)
            || hybrisProjectService.isPlatformExtModule(rootProjectDirectory)
            || hybrisProjectService.isOutOfTheBoxModule(rootProjectDirectory)
            || hybrisProjectService.isCoreExtModule(rootProjectDirectory)
            || hybrisProjectService.isConfigModule(rootProjectDirectory)) {

            paths.add(rootProjectDirectory);

        } else {
            if (hybrisProjectService.isPlatformModule(rootProjectDirectory)) {
                paths.add(rootProjectDirectory);
            }

            if (rootProjectDirectory.isDirectory()) {
                final File[] files = rootProjectDirectory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);

                if (null != files) {
                    for (File file : files) {
                        if (!file.getPath().endsWith(HybrisConstants.MEDIA_DIRECTORY)) {
                            paths.addAll(this.findModuleRoots(file, progressListenerProcessor));
                        }
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

                final Iterable<HybrisModuleDescriptor> dependsOn = this.findHybrisModuleDescriptorsByName(
                    moduleDescriptors, requiresExtensionName
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

            if (moduleDescriptor.isAddOn()) {
                this.processAddOnBackwardDependencies(moduleDescriptors, moduleDescriptor, dependencies);
            }

            moduleDescriptor.setDependenciesTree(dependencies);
        }
    }

    protected void processAddOnBackwardDependencies(
        @NotNull final Iterable<HybrisModuleDescriptor> moduleDescriptors,
        @NotNull final HybrisModuleDescriptor addOn,
        @NotNull final Set<HybrisModuleDescriptor> addOnDependencies
    ) {
        Validate.notNull(moduleDescriptors);
        Validate.notNull(addOn);
        Validate.notNull(addOnDependencies);

        if (HybrisApplicationSettingsComponent.getInstance().getState().isCreateBackwardCyclicDependenciesForAddOns()) {
            for (HybrisModuleDescriptor moduleDescriptor : moduleDescriptors) {
                if (moduleDescriptor.getRequiredExtensionNames().contains(addOn.getName())) {
                    addOnDependencies.add(moduleDescriptor);
                }
            }
        }
    }

    @NotNull
    protected Iterable<HybrisModuleDescriptor> findHybrisModuleDescriptorsByName(
        @NotNull final Iterable<HybrisModuleDescriptor> moduleDescriptors,
        @NotNull final String requiresExtensionName
    ) {
        Validate.notNull(moduleDescriptors);
        Validate.notEmpty(requiresExtensionName);

        return emptyIfNull(Iterables.filter(
            moduleDescriptors, new FindHybrisModuleDescriptorByName(requiresExtensionName)
        ));
    }
}
