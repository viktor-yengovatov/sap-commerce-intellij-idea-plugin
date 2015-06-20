package com.intellij.idea.plugin.hybris.utils;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.LibraryOrderEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.impl.libraries.ProjectLibraryTable;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author Vlad Bozhenok <vladbozhenok@gmail.com>
 */
public class LibUtils {

    private static final String COMMON_LIBS_GROUP = "Common libs";

    public static void addProjectLibsToModule(@NotNull final Project project, @NotNull final ModifiableRootModel module) {
        Validate.notNull(module);
        Validate.notNull(project);

        final LibraryTable projectLibraryTable = ProjectLibraryTable.getInstance(project);
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                Library libsGroup = projectLibraryTable.getLibraryByName(COMMON_LIBS_GROUP);
                if (null == libsGroup) {
                    libsGroup = projectLibraryTable.createLibrary(COMMON_LIBS_GROUP);
                }
                module.addLibraryEntry(libsGroup);
            }
        });
    }

    public static void addLib(@NotNull final Project project, @NotNull final String libPath, @NotNull final String groupName) {
        Validate.notNull(project);
        Validate.notNull(libPath);
        Validate.notNull(groupName);

        String jarPath = libPath + JarFileSystem.JAR_SEPARATOR;
        jarPath = VirtualFileManager.constructUrl(JarFileSystem.PROTOCOL, jarPath);
        final VirtualFile jarVirtualFile = VirtualFileManager.getInstance().findFileByUrl(jarPath);

        final LibraryTable projectLibraryTable = ProjectLibraryTable.getInstance(project);
        projectLibraryTable.getLibraries();

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                Library libsGroup = projectLibraryTable.getLibraryByName(groupName);
                if (null == libsGroup) {
                    libsGroup = projectLibraryTable.createLibrary(groupName);
                }
                if (!isAlreadyPresent(libsGroup, jarVirtualFile)) {
                    final Library.ModifiableModel libModel = libsGroup.getModifiableModel();
                    libModel.addRoot(jarVirtualFile, OrderRootType.CLASSES);
                    libModel.commit();
                }
            }
        });
    }

    private static boolean isAlreadyPresent(@NotNull final Library destinationGroup, @NotNull final VirtualFile jarToAdd) {
        VirtualFile[] jars = destinationGroup.getRootProvider().getFiles(OrderRootType.CLASSES);
        for (int i = 0; i < jars.length; i++) {
            if (jars[i].getName().equals(jarToAdd.getName())) {
                return true;
            }
        }
        return false;
    }

    public static void addLib(@NotNull final ModifiableRootModel module, @NotNull final String path) {
        Validate.notNull(module);
        Validate.notNull(path);

        String jarPath = path + JarFileSystem.JAR_SEPARATOR;
        add(module, path);
    }


    public static void loadLibFolder(@NotNull final Project project, @NotNull final String folderPath) {
        Validate.notNull(project);
        Validate.notNull(folderPath);

        File jarFilesFolder = new File(folderPath);
        if (jarFilesFolder.exists() && jarFilesFolder.isDirectory()) {
            runByAllFilesInFolder(project, jarFilesFolder);
        }
    }


    private static void add(@NotNull final ModifiableRootModel module, @NotNull final String jarPath) {
        String path = VirtualFileManager.constructUrl(JarFileSystem.PROTOCOL, jarPath);
        final VirtualFile jarVirtualFile = VirtualFileManager.getInstance().findFileByUrl(path);

        if (isAlreadyExist(module, jarVirtualFile.getName())) {
            return;
        }

        final Library jarToAdd = module.getModuleLibraryTable().createLibrary();
        final Library.ModifiableModel libraryModel = jarToAdd.getModifiableModel();
        libraryModel.addRoot(jarVirtualFile, OrderRootType.CLASSES);
        libraryModel.commit();
    }

    private static boolean isAlreadyExist(@NotNull final ModifiableRootModel module, @NotNull final String libName) {
        final Library[] moduleLibs = module.getModuleLibraryTable().getLibraries();
        if (moduleLibs.length > 0) {
            VirtualFile[] moduleVirtFileLibs = moduleLibs[0].getRootProvider().getFiles(OrderRootType.CLASSES);
            for (int i = 0; i < moduleVirtFileLibs.length; i++) {
                if (moduleVirtFileLibs[i].getName().equals(libName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void runByAllFilesInFolder(@NotNull final Project project, @NotNull final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                runByAllFilesInFolder(project, fileEntry);
            } else if (fileEntry.isFile() && fileEntry.getName().endsWith(".jar")) {
                addLib(project, fileEntry.getAbsolutePath(), COMMON_LIBS_GROUP);
            }
        }
    }

    public static void addJarFolderToProjectLibs(@NotNull final Project project, @NotNull final File libFolder) {
        Validate.notNull(libFolder);
        Validate.notNull(project);
        if (!libFolder.exists()) {
            return;
        }

        final String jarUrl = VirtualFileManager.constructUrl(LocalFileSystem.PROTOCOL, libFolder.getAbsolutePath());
        final VirtualFile jarVirtualFile = VirtualFileSystemUtils.getByUrl(jarUrl);
        final LibraryTable projectLibraryTable = ProjectLibraryTable.getInstance(project);
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                Library libsGroup = projectLibraryTable.getLibraryByName(COMMON_LIBS_GROUP);
                if (null == libsGroup) {
                    libsGroup = projectLibraryTable.createLibrary(COMMON_LIBS_GROUP);
                }
                final Library.ModifiableModel libModel = libsGroup.getModifiableModel();
                libModel.addJarDirectory(jarVirtualFile, true);
                libModel.commit();
            }
        });
    }

    public static void addClassesToModuleLibs(@NotNull final ModifiableRootModel module, @NotNull final File classesFile, boolean exported){
        Validate.notNull(module);
        Validate.notNull(classesFile);
        if(!classesFile.exists())
            return;

        final String path = VirtualFileManager.constructUrl(LocalFileSystem.PROTOCOL, classesFile.getAbsolutePath());
        final VirtualFile jarVirtualFile = VirtualFileManager.getInstance().findFileByUrl(path);
        if(null == jarVirtualFile)
            return;
        final Library jarToAdd = module.getModuleLibraryTable().createLibrary();
        final Library.ModifiableModel libraryModel = jarToAdd.getModifiableModel();
        libraryModel.addRoot(jarVirtualFile, OrderRootType.CLASSES);
        if(exported){
            setLibraryEntryExported(module, true, jarToAdd);
        }
        libraryModel.commit();
    }

    public static void addJarFolderToModuleLibs(@NotNull final ModifiableRootModel module, @NotNull final File libFolder, final boolean exported) {
        Validate.notNull(libFolder);
        Validate.notNull(module);
        if (!libFolder.exists()) {
            return;
        }

        final String jarUrl = VirtualFileManager.constructUrl(LocalFileSystem.PROTOCOL, libFolder.getAbsolutePath());
        final VirtualFile jarVirtualFile = VirtualFileSystemUtils.getByUrl(jarUrl);
        final LibraryTable projectLibraryTable = module.getModuleLibraryTable();
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                Library libsGroup = projectLibraryTable.createLibrary();
                final Library.ModifiableModel libModel = libsGroup.getModifiableModel();
                libModel.addJarDirectory(jarVirtualFile, true);
                if(exported){
                    setLibraryEntryExported(module, true, libsGroup);
                }
                libModel.commit();
            }
        });
    }

    private static void setLibraryEntryExported(ModifiableRootModel rootModel, boolean exported, Library library) {
        for (OrderEntry orderEntry : rootModel.getOrderEntries()) {
            if (orderEntry instanceof LibraryOrderEntry &&
                ((LibraryOrderEntry)orderEntry).isModuleLevel() &&
                Comparing.equal(((LibraryOrderEntry) orderEntry).getLibrary(), library)) {
                ((LibraryOrderEntry)orderEntry).setExported(exported);
                break;
            }
        }
    }

}
