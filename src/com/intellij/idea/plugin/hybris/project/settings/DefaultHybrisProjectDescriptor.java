/*
 * Copyright 2015 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.idea.plugin.hybris.project.settings;

import com.google.common.collect.Iterables;
import com.intellij.idea.plugin.hybris.project.exceptions.HybrisConfigurationException;
import com.intellij.idea.plugin.hybris.project.utils.FindHybrisModuleDescriptorByName;
import com.intellij.idea.plugin.hybris.project.utils.HybrisProjectUtils;
import com.intellij.idea.plugin.hybris.project.utils.Processor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import gnu.trove.THashSet;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.GuardedBy;
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
    protected boolean openProjectSettingsAfterImport;

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
    public void setRootDirectoryAndScanForModules(@Nullable final File rootDirectory,
                                                  @Nullable final Processor<File> progressListenerProcessor,
                                                  @Nullable final Processor<List<File>> errorsProcessor) {
        this.rootDirectory = rootDirectory;

        if (null == rootDirectory) {
            this.foundModules.clear();
        } else {
            try {
                this.scanDirectoryForHybrisModules(rootDirectory, progressListenerProcessor, errorsProcessor);
            } catch (InterruptedException e) {
                LOG.warn(e);

                this.rootDirectory = null;
                this.foundModules.clear();
            }
        }
    }

    @Override
    public boolean isOpenProjectSettingsAfterImport() {
        return this.openProjectSettingsAfterImport;
    }

    @Override
    public void setOpenProjectSettingsAfterImport(final boolean openProjectSettingsAfterImport) {
        this.openProjectSettingsAfterImport = openProjectSettingsAfterImport;
    }

    @NotNull
    protected Set<HybrisModuleDescriptor> getAlreadyOpenedModules(@NotNull final Project project) {
        Validate.notNull(project);

        final Set<HybrisModuleDescriptor> existingModules = new THashSet<HybrisModuleDescriptor>();

        for (Module module : ModuleManager.getInstance(project).getModules()) {
            try {
                final VirtualFile moduleFile = module.getModuleFile();
                if (null == moduleFile) {
                    LOG.error("Can not find module file for module: " + module.getName());
                    continue;
                }

                final HybrisModuleDescriptor moduleDescriptor = HybrisProjectUtils.MODULE_DESCRIPTOR_FACTORY.createDescriptor(
                    VfsUtil.virtualToIoFile(moduleFile.getParent()), this
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
