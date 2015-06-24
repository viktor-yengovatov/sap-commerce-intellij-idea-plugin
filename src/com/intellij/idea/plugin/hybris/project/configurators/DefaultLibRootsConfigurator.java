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
import com.intellij.idea.plugin.hybris.utils.VirtualFileSystemUtils;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.roots.impl.libraries.ProjectLibraryTable;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created 11:45 PM 24 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class DefaultLibRootsConfigurator implements LibRootsConfigurator {

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
        Validate.notNull(modifiableRootModel);

        this.addJarFolderToProjectLibs(
            modifiableRootModel.getProject(),
            new File(moduleDescriptor.getRootDirectory(), HybrisConstants.LIB_DIRECTORY)
        );

        this.addProjectLibsToModule(modifiableRootModel.getProject(), modifiableRootModel);

        for (JavaLibraryDescriptor javaLibraryDescriptor : moduleDescriptor.getLibraryDescriptors()) {
            if (javaLibraryDescriptor.isDirectoryWithClasses()) {

                this.addClassesToModuleLibs(
                    modifiableRootModel,
                    javaLibraryDescriptor.getLibraryFile(),
                    javaLibraryDescriptor.isExported()
                );

            } else {

                this.addJarFolderToModuleLibs(
                    modifiableRootModel,
                    javaLibraryDescriptor.getLibraryFile(),
                    javaLibraryDescriptor.isExported()
                );
            }
        }
    }

    protected void addJarFolderToProjectLibs(@NotNull final Project project,
                                             @NotNull final File libFolder
    ) {
        Validate.notNull(libFolder);
        Validate.notNull(project);

        if (!libFolder.exists()) {
            return;
        }

        final String jarUrl = VirtualFileManager.constructUrl(LocalFileSystem.PROTOCOL, libFolder.getAbsolutePath());
        final VirtualFile jarVirtualFile = VirtualFileSystemUtils.getByUrl(jarUrl);
        final LibraryTable projectLibraryTable = ProjectLibraryTable.getInstance(project);

        Library libsGroup = projectLibraryTable.getLibraryByName(HybrisConstants.COMMON_LIBS_GROUP);
        if (null == libsGroup) {
            libsGroup = projectLibraryTable.createLibrary(HybrisConstants.COMMON_LIBS_GROUP);
        }

        final Library.ModifiableModel libModel = libsGroup.getModifiableModel();
        libModel.addJarDirectory(jarVirtualFile, true);

        libModel.commit();
    }

    protected void addProjectLibsToModule(@NotNull final Project project,
                                          @NotNull final ModifiableRootModel module
    ) {
        Validate.notNull(module);
        Validate.notNull(project);

        final LibraryTable projectLibraryTable = ProjectLibraryTable.getInstance(project);
        Library libsGroup = projectLibraryTable.getLibraryByName(HybrisConstants.COMMON_LIBS_GROUP);

        if (null == libsGroup) {
            libsGroup = projectLibraryTable.createLibrary(HybrisConstants.COMMON_LIBS_GROUP);
        }

        module.addLibraryEntry(libsGroup);
    }

    protected void addClassesToModuleLibs(@NotNull final ModifiableRootModel module,
                                          @NotNull final File classesFile,
                                          final boolean exported
    ) {
        Validate.notNull(module);
        Validate.notNull(classesFile);

        if (!classesFile.exists()) {
            return;
        }

        final String path = VirtualFileManager.constructUrl(LocalFileSystem.PROTOCOL, classesFile.getAbsolutePath());
        final VirtualFile jarVirtualFile = VirtualFileManager.getInstance().findFileByUrl(path);

        if (null == jarVirtualFile) {
            return;
        }

        final Library jarToAdd = module.getModuleLibraryTable().createLibrary();
        final Library.ModifiableModel libraryModel = jarToAdd.getModifiableModel();
        libraryModel.addRoot(jarVirtualFile, OrderRootType.CLASSES);

        if (exported) {
            setLibraryEntryExported(module, jarToAdd, true);
        }

        libraryModel.commit();
    }

    protected void addJarFolderToModuleLibs(@NotNull final ModifiableRootModel module,
                                            @NotNull final File libFolder,
                                            final boolean exported
    ) {
        Validate.notNull(libFolder);
        Validate.notNull(module);

        if (!libFolder.exists()) {
            return;
        }

        final String jarUrl = VirtualFileManager.constructUrl(LocalFileSystem.PROTOCOL, libFolder.getAbsolutePath());
        final VirtualFile jarVirtualFile = VirtualFileSystemUtils.getByUrl(jarUrl);
        final LibraryTable projectLibraryTable = module.getModuleLibraryTable();

        final Library libsGroup = projectLibraryTable.createLibrary();
        final Library.ModifiableModel libModel = libsGroup.getModifiableModel();

        libModel.addJarDirectory(jarVirtualFile, true);

        if (exported) {
            setLibraryEntryExported(module, libsGroup, true);
        }

        libModel.commit();
    }

    protected void setLibraryEntryExported(@NotNull final ModuleRootModel rootModel,
                                           @NotNull final Library library,
                                           final boolean exported
    ) {
        Validate.notNull(rootModel);
        Validate.notNull(library);

        for (OrderEntry orderEntry : rootModel.getOrderEntries()) {

            if (orderEntry instanceof LibraryOrderEntry) {
                final LibraryOrderEntry libraryOrderEntry = (LibraryOrderEntry) orderEntry;

                if (libraryOrderEntry.isModuleLevel()) {

                    if (Comparing.equal(libraryOrderEntry.getLibrary(), library)) {

                        libraryOrderEntry.setExported(exported);
                        break;
                    }
                }
            }
        }
    }
}
