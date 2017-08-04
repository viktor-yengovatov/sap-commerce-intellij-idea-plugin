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

package com.intellij.idea.plugin.hybris.project;

import com.intellij.ide.caches.CachesInvalidator;
import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import com.intellij.idea.plugin.hybris.common.services.VirtualFileSystemService;
import com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils;
import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons;
import com.intellij.idea.plugin.hybris.project.configurators.AntConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.ConfiguratorFactory;
import com.intellij.idea.plugin.hybris.project.configurators.EclipseConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.MavenConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.RunConfigurationConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.impl.DefaultConfiguratorFactory;
import com.intellij.idea.plugin.hybris.project.descriptors.DefaultHybrisProjectDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.EclipseModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.HybrisProjectDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.MavenModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.descriptors.RootModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.tasks.ImportProjectProgressModalWindow;
import com.intellij.idea.plugin.hybris.project.tasks.SearchModulesRootsTaskModalWindow;
import com.intellij.idea.plugin.hybris.statistics.StatsCollector;
import com.intellij.internal.statistic.UsageTrigger;
import com.intellij.openapi.application.ex.ApplicationManagerEx;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.ExtensionPoint;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.vfs.newvfs.persistent.FSRecords;
import com.intellij.packaging.artifacts.ModifiableArtifactModel;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.GuardedBy;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Created 8:58 PM 07 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class DefaultHybrisProjectImportBuilder extends AbstractHybrisProjectImportBuilder {

    private static final Logger LOG = Logger.getInstance(DefaultHybrisProjectImportBuilder.class);
    protected final Lock lock = new ReentrantLock();

    @Nullable
    @GuardedBy("lock")
    protected volatile HybrisProjectDescriptor hybrisProjectDescriptor;
    protected final VirtualFileSystemService virtualFileSystemService;
    private List<HybrisModuleDescriptor> moduleList;
    private List<HybrisModuleDescriptor> hybrisModulesToImport;
    private String name = null;

    public DefaultHybrisProjectImportBuilder(@NotNull final VirtualFileSystemService virtualFileSystemService) {
        Validate.notNull(virtualFileSystemService);

        this.virtualFileSystemService = virtualFileSystemService;
    }

    public ConfiguratorFactory getConfiguratorFactory() {
        if (!Extensions.getRootArea().hasExtensionPoint(HybrisConstants.CONFIGURATOR_FACTORY_ID)) {
            return ServiceManager.getService(DefaultConfiguratorFactory.class);
        }

        final ExtensionPoint ep = Extensions.getRootArea().getExtensionPoint(HybrisConstants.CONFIGURATOR_FACTORY_ID);
        final ConfiguratorFactory ultimateConfiguratorFactory = (ConfiguratorFactory) ep.getExtension();

        if (ultimateConfiguratorFactory != null) {
            return ultimateConfiguratorFactory;
        }

        return ServiceManager.getService(DefaultConfiguratorFactory.class);
    }

    @Nullable
    public Project createProject(String name, String path) {
        final Project project = super.createProject(name, path);
        getHybrisProjectDescriptor().setHybrisProject(project);
        return project;
    }

    @Override
    public void setRootProjectDirectory(@NotNull final File directory) {
        Validate.notNull(directory);

        ProgressManager.getInstance().run(new SearchModulesRootsTaskModalWindow(
            directory, this.getHybrisProjectDescriptor()
        ));

        this.setFileToImport(directory.getAbsolutePath());
    }

    @Override
    public void cleanup() {
        super.cleanup();

        this.lock.lock();

        try {
            this.hybrisProjectDescriptor = null;
        } finally {
            this.lock.unlock();
        }
    }

    @NotNull
    @Override
    public HybrisProjectDescriptor getHybrisProjectDescriptor() {
        this.lock.lock();

        try {
            if (null == this.hybrisProjectDescriptor) {
                this.hybrisProjectDescriptor = new DefaultHybrisProjectDescriptor();
                this.hybrisProjectDescriptor.setProject(getCurrentProject());
            }

            //noinspection ConstantConditions
            return this.hybrisProjectDescriptor;
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean isOpenProjectSettingsAfter() {
        return this.getHybrisProjectDescriptor().isOpenProjectSettingsAfterImport();
    }

    @Override
    public void setOpenProjectSettingsAfter(final boolean on) {
        this.getHybrisProjectDescriptor().setOpenProjectSettingsAfterImport(on);
    }

    @Nullable
    @Override
    public List<Module> commit(
        final Project project,
        final ModifiableModuleModel model,
        final ModulesProvider modulesProvider,
        final ModifiableArtifactModel artifactModel
    ) {
        final List<Module> result = new ArrayList<>();
        final HybrisProjectDescriptor hybrisProjectDescriptor = getHybrisProjectDescriptor();
        final List<HybrisModuleDescriptor> allModules = hybrisProjectDescriptor.getModulesChosenForImport();
        if (allModules.isEmpty()) {
            return Collections.emptyList();
        }
        final ConfiguratorFactory configuratorFactory = this.getConfiguratorFactory();

        this.performProjectsCleanup(allModules);
        this.collectStatistics(hybrisProjectDescriptor);

        final MavenConfigurator mavenConfigurator = configuratorFactory.getMavenConfigurator();
        final List<MavenModuleDescriptor> mavenModules = new ArrayList<>();

        try {
            mavenModules.addAll(
                hybrisProjectDescriptor.getModulesChosenForImport()
                                       .stream()
                                       .filter(e -> e instanceof MavenModuleDescriptor)
                                       .map(e -> (MavenModuleDescriptor) e)
                                       .collect(Collectors.toList())
            );

            new ImportProjectProgressModalWindow(
                project, model, configuratorFactory, hybrisProjectDescriptor, this.isUpdate(), result
            ).queue();

            if (mavenConfigurator != null && !mavenModules.isEmpty()) {
                mavenConfigurator.configure(hybrisProjectDescriptor, project, mavenModules, configuratorFactory);
            }
        } catch (Exception e) {
            LOG.error("Can not import Maven modules due to an error.", e);
        }

        try {
            final EclipseConfigurator eclipseConfigurator = configuratorFactory.getEclipseConfigurator();
            if (eclipseConfigurator != null) {
                final List<EclipseModuleDescriptor> eclipseModules = hybrisProjectDescriptor
                    .getModulesChosenForImport()
                    .stream()
                    .filter(e -> e instanceof EclipseModuleDescriptor)
                    .map(e -> (EclipseModuleDescriptor) e)
                    .collect(Collectors.toList());
                if (!eclipseModules.isEmpty()) {
                    final String[] eclipseRootGroup = configuratorFactory.getGroupModuleConfigurator().getGroupName(
                        eclipseModules.get(0));
                    eclipseConfigurator.configure(hybrisProjectDescriptor, project, eclipseModules, eclipseRootGroup);
                }
            }
        } catch (Exception e) {
            LOG.error("Can not import Eclipse modules due to an error.", e);
        }

        StartupManager.getInstance(project).runWhenProjectIsInitialized(() -> {
            try {
                final AntConfigurator antConfigurator = configuratorFactory.getAntConfigurator();
                if (null != antConfigurator) {
                    antConfigurator.configure(allModules, project);
                }
            } catch (Exception e) {
                LOG.error("Can not configure Ant due to an error.", e);
            }

            try {
                final RunConfigurationConfigurator runConfigurationConfigurator = configuratorFactory.getJUnitRunConfigurationConfigurator();
                if (null != runConfigurationConfigurator) {
                    runConfigurationConfigurator.configure(hybrisProjectDescriptor, project);
                }
            } catch (Exception e) {
                LOG.error("Can not configure JUnit due to an error.", e);
            }

            triggerCacheInvalidation();
        });

        return result;
    }

    private void collectStatistics(final HybrisProjectDescriptor hybrisProjectDescriptor) {
        try {
            final StringBuilder parameters = new StringBuilder();
            parameters.append("readOnly:");
            parameters.append(hybrisProjectDescriptor.isImportOotbModulesInReadOnlyMode());
            parameters.append(",customDirectoryOverride:");

            final boolean override = hybrisProjectDescriptor.getExternalExtensionsDirectory() != null;
            parameters.append(override);

            final boolean hasSourceZip = hybrisProjectDescriptor.getSourceCodeZip() != null;
            parameters.append(",hasSources:");
            parameters.append(hasSourceZip);

            final StatsCollector statsCollector = ServiceManager.getService(StatsCollector.class);
            statsCollector.collectStat(StatsCollector.ACTIONS.IMPORT_PROJECT, parameters.toString());
        } catch (Exception e) {
            // we do not care
        }
    }

    private void triggerCacheInvalidation() {
        try {
            UsageTrigger.trigger(ApplicationManagerEx.getApplicationEx().getName() + ".caches.invalidated");
            FSRecords.invalidateCaches();

            for (CachesInvalidator invalidater : CachesInvalidator.EP_NAME.getExtensions()) {
                invalidater.invalidateCaches();
            }
        } catch (Exception e) {
            LOG.error("Can not invalidate cache due to an error.", e);
        }
    }

    protected void performProjectsCleanup(@NotNull final Iterable<HybrisModuleDescriptor> modulesChosenForImport) {
        Validate.notNull(modulesChosenForImport);
        final List<File> alreadyExistingModuleFiles;
        final File dir = hybrisProjectDescriptor.getModulesFilesDirectory();
        if (dir != null && dir.isDirectory()) {
            alreadyExistingModuleFiles = getAllImlFiles(dir);
        } else {
            alreadyExistingModuleFiles = getModulesChosenForImportFiles(modulesChosenForImport);
        }
        Collections.sort(alreadyExistingModuleFiles);

        try {
            this.virtualFileSystemService.removeAllFiles(alreadyExistingModuleFiles);
        } catch (IOException e) {
            LOG.error("Can not remove old module files.", e);
        }
    }

    private List<File> getAllImlFiles(final File dir) {
        final List<File> imlFiles = Arrays.stream(dir.listFiles(
            e -> e.getName().endsWith(HybrisConstants.NEW_IDEA_MODULE_FILE_EXTENSION)
        )).collect(Collectors.toList());
        return imlFiles;
    }

    private List<File> getModulesChosenForImportFiles(final Iterable<HybrisModuleDescriptor> modulesChosenForImport) {
        List<File> alreadyExistingModuleFiles = new ArrayList<>();
        for (HybrisModuleDescriptor moduleDescriptor : modulesChosenForImport) {
            if (moduleDescriptor.getIdeaModuleFile().exists()) {
                alreadyExistingModuleFiles.add(moduleDescriptor.getIdeaModuleFile());
            }
        }
        return alreadyExistingModuleFiles;
    }

    @Override
    public void resetExternalStepName() {
        name = null;
    }

    @Override
    public void setExternalStepName() {
        name = HybrisI18NBundleUtils.message("non.hybris.project.name");
    }

    @Override
    public String getName() {
        if (name == null) {
            return HybrisI18NBundleUtils.message("hybris.project.name");
        }
        return name;
    }

    @Override
    public Icon getIcon() {
        return HybrisIcons.HYBRIS_ICON;
    }

    protected void setAllModuleList() {
        moduleList = this.getHybrisProjectDescriptor().getFoundModules();
    }

    @Override
    public void setCoreStepModuleList() {
        moduleList = this.getHybrisProjectDescriptor()
                         .getFoundModules()
                         .stream()
                         .filter(e -> !(e instanceof RootModuleDescriptor))
                         .collect(Collectors.toList());
    }

    @Override
    public void setExternalStepModuleList() {
        moduleList = this.getHybrisProjectDescriptor()
                         .getFoundModules()
                         .stream()
                         .filter(e -> e instanceof RootModuleDescriptor)
                         .collect(Collectors.toList());
    }

    @Override
    public void setHybrisModulesToImport(final List<HybrisModuleDescriptor> hybrisModules) {
        hybrisModulesToImport = hybrisModules;
        try {
            setList(hybrisModules);
        } catch (ConfigurationException e) {
            LOG.error(e);
            // no-op already validated
        }
    }

    @Override
    public List<HybrisModuleDescriptor> getHybrisModulesToImport() {
        return hybrisModulesToImport;
    }

    @Override
    public List<HybrisModuleDescriptor> getList() {
        if (moduleList == null) {
            setAllModuleList();
        }
        return moduleList;
    }

    @Override
    public void setList(final List<HybrisModuleDescriptor> list) throws ConfigurationException {

        final List<HybrisModuleDescriptor> chosenForImport = new ArrayList<HybrisModuleDescriptor>(list);

        chosenForImport.removeAll(this.getHybrisProjectDescriptor().getAlreadyOpenedModules());

        this.getHybrisProjectDescriptor().setModulesChosenForImport(chosenForImport);
    }

    @Override
    public boolean isMarked(final HybrisModuleDescriptor element) {
        return false;
    }

}
