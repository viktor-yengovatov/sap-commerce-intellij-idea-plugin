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

package com.intellij.idea.plugin.hybris.project.configurators.impl;

import com.intellij.idea.plugin.hybris.project.configurators.LibRootsConfigurator;
import com.intellij.idea.plugin.hybris.project.settings.CoreHybrisHybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.settings.JavaLibraryDescriptor;
import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import com.intellij.idea.plugin.hybris.project.settings.PlatformHybrisModuleDescriptor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.roots.impl.libraries.ProjectLibraryTable;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
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

        final VirtualFile sourceCodeRoot = this.getSourceCodeRoot(moduleDescriptor);

        for (JavaLibraryDescriptor javaLibraryDescriptor : moduleDescriptor.getLibraryDescriptors()) {
            if (javaLibraryDescriptor.isDirectoryWithClasses()) {
                this.addClassesToModuleLibs(modifiableRootModel, sourceCodeRoot, javaLibraryDescriptor);
            } else {
                this.addJarFolderToModuleLibs(modifiableRootModel, sourceCodeRoot, javaLibraryDescriptor);
            }
        }

        if (moduleDescriptor instanceof PlatformHybrisModuleDescriptor) {
            final PlatformHybrisModuleDescriptor hybrisModuleDescriptor = (PlatformHybrisModuleDescriptor)moduleDescriptor;
            hybrisModuleDescriptor.createBootstrapLib(sourceCodeRoot, modifiableModelsProvider);
        }

        if (moduleDescriptor instanceof CoreHybrisHybrisModuleDescriptor) {
            addLibsToModule(modifiableRootModel, HybrisConstants.PLATFORM_LIBRARY_GROUP);
        }
    }

    @Nullable
    private VirtualFile getSourceCodeRoot(final @NotNull HybrisModuleDescriptor moduleDescriptor) {
        final VirtualFile sourceCodeRoot;
        final File sourceCodeZip = moduleDescriptor.getRootProjectDescriptor().getSourceCodeZip();

        if (null != sourceCodeZip) {
            final VirtualFile sourceZip = VfsUtil.findFileByIoFile(sourceCodeZip, true);
            if (null == sourceZip) {
                sourceCodeRoot = null;
            } else {
                sourceCodeRoot = JarFileSystem.getInstance().getJarRootForLocalFile(sourceZip);
            }
        } else {
            sourceCodeRoot = null;
        }

        return sourceCodeRoot;
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

        if (null != javaLibraryDescriptor.getSourcesFile()) {
            final VirtualFile srcDirVF = VfsUtil.findFileByIoFile(javaLibraryDescriptor.getSourcesFile(), true);
            if (null != srcDirVF) {
                libraryModifiableModel.addRoot(srcDirVF, OrderRootType.SOURCES);
            }
        }

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

        if (null != javaLibraryDescriptor.getSourcesFile()) {
            final VirtualFile srcDirVF = VfsUtil.findFileByIoFile(javaLibraryDescriptor.getSourcesFile(), true);
            if (null != srcDirVF) {
                libraryModifiableModel.addRoot(srcDirVF, OrderRootType.SOURCES);
            }
        }

        if (null != sourceCodeRoot) {
            libraryModifiableModel.addRoot(sourceCodeRoot, OrderRootType.SOURCES);
        }

        if (javaLibraryDescriptor.isExported()) {
            this.setLibraryEntryExported(modifiableRootModel, library);
        }

        libraryModifiableModel.commit();
    }

    protected void addLibsToModule(@NotNull final ModifiableRootModel modifiableRootModel,
                                   @NotNull final String libraryName) {
        Validate.notNull(modifiableRootModel);

        final LibraryTable projectLibraryTable = ProjectLibraryTable.getInstance(modifiableRootModel.getProject());
        Library libsGroup = projectLibraryTable.getLibraryByName(libraryName);

        if (null == libsGroup) {
            libsGroup = projectLibraryTable.createLibrary(libraryName);
        }

        modifiableRootModel.addLibraryEntry(libsGroup);

        setLibraryEntryExported(modifiableRootModel, libsGroup);
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
