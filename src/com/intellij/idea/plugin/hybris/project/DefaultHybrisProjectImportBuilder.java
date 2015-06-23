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

package com.intellij.idea.plugin.hybris.project;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.intellij.idea.plugin.hybris.project.settings.DefaultHybrisProjectDescriptor;
import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.settings.HybrisProjectDescriptor;
import com.intellij.idea.plugin.hybris.project.tasks.SearchModulesRootsTaskModalWindow;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import com.intellij.idea.plugin.hybris.utils.HybrisI18NBundleUtils;
import com.intellij.idea.plugin.hybris.utils.HybrisIconsUtils;
import com.intellij.idea.plugin.hybris.utils.VirtualFileSystemUtils;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.roots.impl.ModifiableModelCommitter;
import com.intellij.openapi.roots.impl.storage.ClassPathStorageUtil;
import com.intellij.openapi.roots.impl.storage.ClasspathStorage;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.packaging.artifacts.ModifiableArtifactModel;
import com.intellij.util.Function;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.GuardedBy;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created 8:58 PM 07 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class DefaultHybrisProjectImportBuilder extends AbstractHybrisProjectImportBuilder {

    private static final Logger LOG = Logger.getInstance(DefaultHybrisProjectImportBuilder.class);
    protected final ContentRootConfigurator contentRootConfigurator = new HybrisModuleContentRootConfigurator();
    protected final Lock lock = new ReentrantLock();
    @Nullable
    @GuardedBy("lock")
    protected volatile HybrisProjectDescriptor hybrisProjectDescriptor;

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
        final boolean isProjectAlreadyOpen = null != model;
        final List<Module> result = new ArrayList<Module>();

        final List<HybrisModuleDescriptor> modulesChosenForImport = this.getHybrisProjectDescriptor()
                                                                        .getModulesChosenForImport();

        if (modulesChosenForImport.isEmpty()) {
            return Collections.emptyList();
        }

        this.performProjectsCleanup(modulesChosenForImport);

        final ModifiableModuleModel rootProjectModifiableModuleModel = (null != model)
            ? model
            : ModuleManager.getInstance(project).getModifiableModel();

        final Collection<ModifiableRootModel> modifiableRootModels = new ArrayList<ModifiableRootModel>();

        for (HybrisModuleDescriptor moduleDescriptor : modulesChosenForImport) {

            final Module javaModule = rootProjectModifiableModuleModel.newModule(
                moduleDescriptor.getIdeaModuleFile().getAbsolutePath(), StdModuleTypes.JAVA.getId()
            );

            final ModifiableRootModel modifiableRootModel = ModuleRootManager.getInstance(javaModule).getModifiableModel();

            ClasspathStorage.setStorageType(modifiableRootModel, ClassPathStorageUtil.DEFAULT_STORAGE);
            modifiableRootModel.inheritSdk();

            this.configureCompilerOutputPaths(moduleDescriptor, modifiableRootModel);

            moduleDescriptor.loadLibs(modifiableRootModel);
            this.contentRootConfigurator.configure(modifiableRootModel, moduleDescriptor);

            if (isProjectAlreadyOpen) {
                this.commitModule(modifiableRootModel);
            }

            result.add(javaModule);
            modifiableRootModels.add(modifiableRootModel);
        }

        if (!isProjectAlreadyOpen) {
            this.multiCommit(rootProjectModifiableModuleModel, modifiableRootModels);
        }

        this.configureModulesDependencies(modulesChosenForImport, rootProjectModifiableModuleModel);

        this.cleanup();

        return result;
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

    protected void configureCompilerOutputPaths(@NotNull final HybrisModuleDescriptor moduleDescriptor,
                                                @NotNull final ModifiableRootModel modifiableRootModel) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(modifiableRootModel);

        final CompilerModuleExtension compilerModuleExtension = modifiableRootModel.getModuleExtension(
            CompilerModuleExtension.class
        );

        final File outputDirectory = new File(moduleDescriptor.getModuleRootDirectory(), HybrisConstants.COMPILER_OUTPUT_PATH);

        compilerModuleExtension.setCompilerOutputPath(VfsUtilCore.pathToUrl(outputDirectory.getAbsolutePath()));
        compilerModuleExtension.setCompilerOutputPathForTests(VfsUtilCore.pathToUrl(outputDirectory.getAbsolutePath()));

        compilerModuleExtension.setExcludeOutput(true);
        compilerModuleExtension.inheritCompilerOutputPath(false);
    }

    protected void commitModule(@NotNull final ModifiableRootModel modifiableRootModel) {
        Validate.notNull(modifiableRootModel);

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                modifiableRootModel.commit();
            }
        });
    }

    protected void multiCommit(@NotNull final ModifiableModuleModel rootProjectModifiableModuleModel,
                               @NotNull final Collection<ModifiableRootModel> modifiableRootModels) {
        Validate.notNull(rootProjectModifiableModuleModel);
        Validate.notNull(modifiableRootModels);

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                ModifiableModelCommitter.multiCommit(modifiableRootModels, rootProjectModifiableModuleModel);
            }
        });
    }

    protected void configureModulesDependencies(@NotNull final Iterable<HybrisModuleDescriptor> modulesChosenForImport,
                                                @NotNull final ModifiableModuleModel rootProjectModifiableModuleModel) {
        Validate.notNull(modulesChosenForImport);
        Validate.notNull(rootProjectModifiableModuleModel);

        final List<Module> modules = Arrays.asList(rootProjectModifiableModuleModel.getModules());
        final Collection<ModifiableRootModel> modifiableRootModels = new ArrayList<ModifiableRootModel>();

        for (Module module : modules) {
            modifiableRootModels.add(ModuleRootManager.getInstance(module).getModifiableModel());
        }

        for (HybrisModuleDescriptor moduleDescriptor : modulesChosenForImport) {
            final ModifiableRootModel modifiableRootModel = Iterables.find(
                modifiableRootModels,
                new FindModifiableRootModelByName(moduleDescriptor.getModuleName())
            );

            for (String dependsOnModuleName : moduleDescriptor.getRequiredExtensionNames()) {
                final Optional<ModifiableRootModel> dependsOnModifiableRootModelOptional = Iterables.tryFind(
                    modifiableRootModels,
                    new FindModifiableRootModelByName(dependsOnModuleName)
                );

                final ModuleOrderEntry moduleOrderEntry = (dependsOnModifiableRootModelOptional.isPresent())
                    ? modifiableRootModel.addModuleOrderEntry(dependsOnModifiableRootModelOptional.get().getModule())
                    : modifiableRootModel.addInvalidModuleEntry(dependsOnModuleName);

                moduleOrderEntry.setExported(true);
                moduleOrderEntry.setScope(DependencyScope.COMPILE);
            }
        }

        this.multiCommit(rootProjectModifiableModuleModel, modifiableRootModels);
    }

    protected boolean shouldRemoveAlreadyExistingModuleFiles(@NotNull final Collection<File> files) {
        Validate.notNull(files);

        if (files.isEmpty()) {
            return false;
        }

        final int resultCode = Messages.showYesNoCancelDialog(
            HybrisI18NBundleUtils.message(
                "hybris.project.import.duplicate.modules.found",
                StringUtil.join(files, new GetFileNameFunction(), "\n")
            ),
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

    private static class FindModifiableRootModelByName implements Predicate<ModifiableRootModel> {

        private final String name;

        public FindModifiableRootModelByName(@NotNull final String name) {
            Validate.notEmpty(name);

            this.name = name;
        }

        @Override
        public boolean apply(@Nullable final ModifiableRootModel t) {
            return null != t && this.name.equalsIgnoreCase(t.getModule().getName());
        }
    }

    protected class GetFileNameFunction implements Function<File, String> {

        @Override
        public String fun(final File param) {
            if (null == getHybrisProjectDescriptor().getRootDirectory()) {
                return param.getPath();
            } else {
                return param.getPath().replaceFirst(getHybrisProjectDescriptor().getRootDirectory().getPath(), "");
            }
        }
    }
}
