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

package com.intellij.idea.plugin.hybris.project;

import com.intellij.facet.ModifiableFacetModel;
import com.intellij.idea.plugin.hybris.project.configurators.CommunityEditionConfiguratorFactory;
import com.intellij.idea.plugin.hybris.project.configurators.CompilerOutputPathsConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.ConfiguratorFactory;
import com.intellij.idea.plugin.hybris.project.configurators.ContentRootConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.FacetConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.GroupModuleConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.LibRootsConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.ModulesDependenciesConfigurator;
import com.intellij.idea.plugin.hybris.project.configurators.SpringConfigurator;
import com.intellij.idea.plugin.hybris.project.settings.DefaultHybrisProjectDescriptor;
import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.settings.HybrisProjectDescriptor;
import com.intellij.idea.plugin.hybris.project.tasks.SearchModulesRootsTaskModalWindow;
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettings;
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettingsComponent;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import com.intellij.idea.plugin.hybris.utils.HybrisI18NBundleUtils;
import com.intellij.idea.plugin.hybris.utils.HybrisIconsUtils;
import com.intellij.idea.plugin.hybris.utils.VirtualFileSystemUtils;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.ExtensionPoint;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableModelsProvider;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.impl.storage.ClassPathStorageUtil;
import com.intellij.openapi.roots.impl.storage.ClasspathStorage;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.roots.ui.configuration.ProjectStructureConfigurable;
import com.intellij.openapi.roots.ui.configuration.projectRoot.StructureConfigurableContext;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.packaging.artifacts.ModifiableArtifactModel;
import com.intellij.util.Function;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.GuardedBy;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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


    public ConfiguratorFactory getConfiguratorFactory() {
        if (!Extensions.getRootArea().hasExtensionPoint(HybrisConstants.CONFIGURATOR_FACTORY_ID)) {
            return new CommunityEditionConfiguratorFactory();
        }
        ExtensionPoint ep = Extensions.getRootArea().getExtensionPoint(HybrisConstants.CONFIGURATOR_FACTORY_ID);
        ConfiguratorFactory ultimateConfiguratorFactory = (ConfiguratorFactory) ep.getExtension();
        if (ultimateConfiguratorFactory != null) {
            return ultimateConfiguratorFactory;
        }
        return new CommunityEditionConfiguratorFactory();
    }

    @Override
    public void setRootProjectDirectory(@NotNull final File directory) {
        Validate.notNull(directory);

        this.cleanup();

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
                this.hybrisProjectDescriptor = new DefaultHybrisProjectDescriptor(getCurrentProject());
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
    public List<Module> commit(final Project project,
                               final ModifiableModuleModel model,
                               final ModulesProvider modulesProvider,
                               final ModifiableArtifactModel artifactModel) {

        final ConfiguratorFactory configuratorFactory = getConfiguratorFactory();
        final ModifiableModelsProvider modifiableModelsProvider = configuratorFactory.getModifiableModelsProvider();
        final LibRootsConfigurator libRootsConfigurator = configuratorFactory.getLibRootsConfigurator();
        final FacetConfigurator facetConfigurator = configuratorFactory.getFacetConfigurator();
        final ContentRootConfigurator contentRootConfigurator = configuratorFactory.getContentRootConfigurator();
        final CompilerOutputPathsConfigurator compilerOutputPathsConfigurator = configuratorFactory.getCompilerOutputPathsConfigurator();
        final ModulesDependenciesConfigurator modulesDependenciesConfigurator = configuratorFactory.getModulesDependenciesConfigurator();
        final SpringConfigurator springConfigurator = configuratorFactory.getSpringConfigurator();
        final GroupModuleConfigurator groupModuleConfigurator = configuratorFactory.getGroupModuleConfigurator();

        final List<Module> result = new ArrayList<Module>();

        if (this.getHybrisProjectDescriptor().getModulesChosenForImport().isEmpty()) {
            return Collections.emptyList();
        }

        this.initializeHybrisProjectSettings(project);

        this.performProjectsCleanup(this.getHybrisProjectDescriptor().getModulesChosenForImport());

        final ModifiableModuleModel rootProjectModifiableModel = (null == model)
            ? ModuleManager.getInstance(project).getModifiableModel()
            : model;

        final ProjectStructureConfigurable projectStructureConfigurable = ProjectStructureConfigurable.getInstance(
            project
        );

        springConfigurator.findSpringConfiguration(this.getHybrisProjectDescriptor().getModulesChosenForImport());
        groupModuleConfigurator.findDependencyModules(this.getHybrisProjectDescriptor().getModulesChosenForImport());

        for (HybrisModuleDescriptor moduleDescriptor : this.getHybrisProjectDescriptor().getModulesChosenForImport()) {

            final Module javaModule = rootProjectModifiableModel.newModule(
                moduleDescriptor.getIdeaModuleFile().getAbsolutePath(), StdModuleTypes.JAVA.getId()
            );

            if (projectStructureConfigurable.isUiInitialized()) {
                final StructureConfigurableContext context = projectStructureConfigurable.getContext();
                if (null != context) {
                    context.getModulesConfigurator().getOrCreateModuleEditor(javaModule);
                }
            }

            final ModifiableRootModel modifiableRootModel = modifiableModelsProvider.getModuleModifiableModel(
                javaModule
            );
            final ModifiableFacetModel modifiableFacetModel = modifiableModelsProvider.getFacetModifiableModel(
                javaModule
            );

            ClasspathStorage.setStorageType(modifiableRootModel, ClassPathStorageUtil.DEFAULT_STORAGE);
            modifiableRootModel.inheritSdk();

            libRootsConfigurator.configure(modifiableRootModel, moduleDescriptor);
            contentRootConfigurator.configure(modifiableRootModel, moduleDescriptor);
            compilerOutputPathsConfigurator.configure(modifiableRootModel, moduleDescriptor);
            facetConfigurator.configure(modifiableFacetModel, moduleDescriptor, javaModule);
            groupModuleConfigurator.configure(rootProjectModifiableModel, javaModule, moduleDescriptor);

            ApplicationManager.getApplication().runWriteAction(new Runnable() {
                @Override
                public void run() {
                    modifiableModelsProvider.commitFacetModifiableModel(javaModule, modifiableFacetModel);
                    modifiableModelsProvider.commitModuleModifiableModel(modifiableRootModel);
                }
            });

            result.add(javaModule);
        }

        if (!this.isUpdate()) {
            this.commitRootModule(rootProjectModifiableModel);
        }

        modulesDependenciesConfigurator.configure(this.getHybrisProjectDescriptor(), rootProjectModifiableModel);
        springConfigurator.configureDependencies(this.getHybrisProjectDescriptor(), rootProjectModifiableModel);

        this.cleanup();

        return result;
    }

    protected void initializeHybrisProjectSettings(@NotNull final Project project) {
        Validate.notNull(project);

        final HybrisProjectSettings hybrisProjectSettings = HybrisProjectSettingsComponent.getInstance(project).getState();
        if (null != hybrisProjectSettings) {
            hybrisProjectSettings.setHybisProject(true);
        }
    }

    protected void performProjectsCleanup(@NotNull final Iterable<HybrisModuleDescriptor> modulesChosenForImport) {
        Validate.notNull(modulesChosenForImport);

        final List<File> alreadyExistingModuleFiles = new ArrayList<File>();
        for (HybrisModuleDescriptor moduleDescriptor : modulesChosenForImport) {
            if (moduleDescriptor.getIdeaModuleFile().exists()) {
                alreadyExistingModuleFiles.add(moduleDescriptor.getIdeaModuleFile());
            }
        }

        Collections.sort(alreadyExistingModuleFiles);
        if (this.shouldRemoveAlreadyExistingModuleFiles(alreadyExistingModuleFiles)) {
            try {
                VirtualFileSystemUtils.removeAllFiles(alreadyExistingModuleFiles);
            } catch (IOException e) {
                LOG.error("Can not remove old module files.", e);
            }
        }
    }

    protected void commitRootModule(@NotNull final ModifiableModuleModel rootProjectModifiableModuleModel) {
        Validate.notNull(rootProjectModifiableModuleModel);

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                rootProjectModifiableModuleModel.commit();
            }
        });
    }

    protected boolean shouldRemoveAlreadyExistingModuleFiles(@NotNull final List<File> files) {
        Validate.notNull(files);

        if (files.isEmpty()) {
            return false;
        }

        final String message;
        if (files.size() > HybrisConstants.MAX_EXISTING_MODULE_NAMES) {
            final String trimmedNames = StringUtil.join(files.subList(0, HybrisConstants.MAX_EXISTING_MODULE_NAMES),
                                                        new GetFileNameFunction(),
                                                        "\n");
            message = trimmedNames + "\n...\n...";
        } else {
            message = StringUtil.join(files, new GetFileNameFunction(), "\n");
        }

        final int resultCode = Messages.showYesNoDialog(
            HybrisI18NBundleUtils.message("hybris.project.import.duplicate.modules.found", message),
            HybrisI18NBundleUtils.message("hybris.project.import.found.idea.module.files"),
            Messages.getQuestionIcon()
        );
        return (Messages.YES != resultCode) && (Messages.NO == resultCode);
    }

    @NotNull
    @Override
    public String getName() {
        return HybrisI18NBundleUtils.message("hybris.project.name");
    }

    @Override
    public Icon getIcon() {
        return HybrisIconsUtils.HYBRIS_ICON;
    }

    @Override
    public List<HybrisModuleDescriptor> getList() {
        return this.getHybrisProjectDescriptor().getFoundModules();
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

    protected class GetFileNameFunction implements Function<File, String> {

        @Override
        public String fun(final File param) {
            final File projectRootDirectory = getHybrisProjectDescriptor().getRootDirectory();

            if (null != projectRootDirectory && param.getPath().startsWith(projectRootDirectory.getPath())) {
                return param.getPath().substring(projectRootDirectory.getPath().length());
            }

            return param.getPath();
        }
    }
}
