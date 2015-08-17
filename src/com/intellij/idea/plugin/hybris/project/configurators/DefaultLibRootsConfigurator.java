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

package com.intellij.idea.plugin.hybris.project.configurators;

import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.settings.JavaLibraryDescriptor;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.roots.impl.libraries.ProjectLibraryTable;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.roots.ui.configuration.libraryEditor.ExistingLibraryEditor;
import com.intellij.openapi.roots.ui.configuration.projectRoot.LibrariesModifiableModel;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Created 11:45 PM 24 June 2015.
 *
 * @author Vlad Bozhenok <VladBozhenok@gmail.com>
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class DefaultLibRootsConfigurator implements LibRootsConfigurator {

    protected final ModifiableModelsProvider modifiableModelsProvider = new IdeaModifiableModelsProvider();

    @Override
    public void configure(@NotNull final ModifiableRootModel modifiableRootModel,
                          @NotNull final HybrisModuleDescriptor moduleDescriptor) {
        Validate.notNull(modifiableRootModel);
        Validate.notNull(modifiableRootModel);

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                configureInner(modifiableRootModel, moduleDescriptor);
            }
        });
    }

    protected void configureInner(@NotNull final ModifiableRootModel modifiableRootModel,
                                  @NotNull final HybrisModuleDescriptor moduleDescriptor) {
        Validate.notNull(modifiableRootModel);
        Validate.notNull(moduleDescriptor);

        final VirtualFile sourceCodeRoot;
        final File sourceCodeZip = moduleDescriptor.getRootProjectDescriptor().getSourceCodeZip();
        if (sourceCodeZip != null) {
            final VirtualFile sourceZip = VfsUtil.findFileByIoFile(sourceCodeZip, true);
            sourceCodeRoot = JarFileSystem.getInstance().getJarRootForLocalFile(sourceZip);
        } else {
            sourceCodeRoot = null;
        }

        this.addJarFolderToProjectLibs(
            modifiableRootModel.getProject(),
            sourceCodeRoot,
            new File(moduleDescriptor.getRootDirectory(), HybrisConstants.LIB_DIRECTORY)
        );

        this.addProjectLibsToModule(modifiableRootModel.getProject(), modifiableRootModel);

        for (JavaLibraryDescriptor javaLibraryDescriptor : moduleDescriptor.getLibraryDescriptors()) {
            if (javaLibraryDescriptor.isDirectoryWithClasses()) {

                this.addClassesToModuleLibs(modifiableRootModel, sourceCodeRoot, javaLibraryDescriptor);
            } else {
                this.addJarFolderToModuleLibs(modifiableRootModel, sourceCodeRoot, javaLibraryDescriptor);
            }
        }
    }

    protected void addJarFolderToProjectLibs(@NotNull final Project project,
                                             @Nullable final VirtualFile sourceCodeRoot,
                                             @NotNull final File libFolder) {
        Validate.notNull(libFolder);
        Validate.notNull(project);

        if (!libFolder.exists()) {
            return;
        }

        final LibraryTable.ModifiableModel libraryTableModifiableModel = this.modifiableModelsProvider
            .getLibraryTableModifiableModel(project);

        Library library = libraryTableModifiableModel.getLibraryByName(HybrisConstants.COMMON_LIBS_GROUP);
        if (null == library) {
            library = libraryTableModifiableModel.createLibrary(HybrisConstants.COMMON_LIBS_GROUP);
        }

        if (libraryTableModifiableModel instanceof LibrariesModifiableModel) {
            ExistingLibraryEditor existingLibraryEditor = ((LibrariesModifiableModel) libraryTableModifiableModel).getLibraryEditor(library);
            existingLibraryEditor.addJarDirectory(
                VfsUtil.getUrlForLibraryRoot(libFolder), true, OrderRootType.CLASSES
            );
            if (sourceCodeRoot != null) {
                existingLibraryEditor.addJarDirectory(sourceCodeRoot, true, OrderRootType.SOURCES );
            }
        } else {
            final Library.ModifiableModel libraryModifiableModel = library.getModifiableModel();
            libraryModifiableModel.addJarDirectory(VfsUtil.getUrlForLibraryRoot(libFolder), true);

            libraryModifiableModel.commit();
        }
    }

    protected void addProjectLibsToModule(@NotNull final Project project,
                                          @NotNull final ModifiableRootModel modifiableRootModel) {
        Validate.notNull(project);
        Validate.notNull(modifiableRootModel);

        final LibraryTable projectLibraryTable = ProjectLibraryTable.getInstance(project);
        Library libsGroup = projectLibraryTable.getLibraryByName(HybrisConstants.COMMON_LIBS_GROUP);

        if (null == libsGroup) {
            libsGroup = projectLibraryTable.createLibrary(HybrisConstants.COMMON_LIBS_GROUP);
        }

        modifiableRootModel.addLibraryEntry(libsGroup);
    }

    protected void addClassesToModuleLibs(@NotNull final ModifiableRootModel modifiableRootModel,
                                          @Nullable final VirtualFile sourceCodeRoot,
                                          @NotNull final JavaLibraryDescriptor javaLibraryDescriptor) {
        Validate.notNull(modifiableRootModel);
        Validate.notNull(javaLibraryDescriptor);

        if (!javaLibraryDescriptor.getLibraryFile().exists()) {
            return;
        }

        final Library library = modifiableRootModel.getModuleLibraryTable().createLibrary();
        final Library.ModifiableModel libraryModifiableModel = library.getModifiableModel();
        libraryModifiableModel.addRoot(
            VfsUtil.getUrlForLibraryRoot(javaLibraryDescriptor.getLibraryFile()), OrderRootType.CLASSES
        );

        if (sourceCodeRoot != null && javaLibraryDescriptor.getLibraryFile().getName().endsWith("server.jar")) {
            libraryModifiableModel.addRoot(sourceCodeRoot, OrderRootType.SOURCES);
        }

        if (javaLibraryDescriptor.isExported()) {
            this.setLibraryEntryExported(modifiableRootModel, library);
        }

        libraryModifiableModel.commit();
    }

    protected void addJarFolderToModuleLibs(@NotNull final ModifiableRootModel modifiableRootModel,
                                            @Nullable final VirtualFile sourceCodeRoot,
                                            @NotNull final JavaLibraryDescriptor javaLibraryDescriptor) {
        Validate.notNull(modifiableRootModel);
        Validate.notNull(javaLibraryDescriptor);

        if (!javaLibraryDescriptor.getLibraryFile().exists()) {
            return;
        }

        final LibraryTable projectLibraryTable = modifiableRootModel.getModuleLibraryTable();

        final Library library = projectLibraryTable.createLibrary();
        final Library.ModifiableModel libraryModifiableModel = library.getModifiableModel();

        libraryModifiableModel.addJarDirectory(
            VfsUtil.getUrlForLibraryRoot(javaLibraryDescriptor.getLibraryFile()), true
        );
        if (sourceCodeRoot != null) {
            libraryModifiableModel.addRoot(sourceCodeRoot, OrderRootType.SOURCES);
        }

        if (javaLibraryDescriptor.isExported()) {
            this.setLibraryEntryExported(modifiableRootModel, library);
        }

        libraryModifiableModel.commit();
    }

    protected void setLibraryEntryExported(@NotNull final ModifiableRootModel modifiableRootModel,
                                           @NotNull final Library library) {
        Validate.notNull(modifiableRootModel);
        Validate.notNull(library);

        final LibraryOrderEntry libraryOrderEntry = modifiableRootModel.findLibraryOrderEntry(library);
        if (null != libraryOrderEntry) {
            libraryOrderEntry.setExported(true);
        }
    }
}
