package com.intellij.idea.plugin.hybris.utils;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.impl.libraries.ProjectLibraryTable;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author Vlad Bozhenok <vladbozhenok@gmail.com>
 */
public class LibUtils {

    public static void addLib(@NotNull final Project project, @NotNull final String libPath, @NotNull final String groupName){
        Validate.notNull(project);
        Validate.notNull(libPath);
        Validate.notNull(groupName);

        String jarPath= libPath + JarFileSystem.JAR_SEPARATOR;
        jarPath = VirtualFileManager.constructUrl(JarFileSystem.PROTOCOL, jarPath);
        final VirtualFile jarVirtualFile2 = VirtualFileManager.getInstance().findFileByUrl(jarPath);

        final LibraryTable projectLibraryTable = ProjectLibraryTable.getInstance(project);

        //TODO: check is already exists
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                Library libsGroup = projectLibraryTable.getLibraryByName(groupName);
                if(null == libsGroup){
                    libsGroup = projectLibraryTable.createLibrary(groupName);
                }
                final Library.ModifiableModel libModel = libsGroup.getModifiableModel();
                libModel.addRoot(jarVirtualFile2, OrderRootType.CLASSES);
                libModel.commit();
            }
        });
    }

    public static void addLib(@NotNull final ModifiableRootModel module, @NotNull final File jarFile){
        Validate.notNull(module);
        Validate.notNull(jarFile);

        String jarPath = jarFile.getAbsolutePath() + JarFileSystem.JAR_SEPARATOR;
        add(module, jarPath);

    }

    public void addLib(@NotNull final ModifiableRootModel module, @NotNull final String path){
        Validate.notNull(module);
        Validate.notNull(path);

        String jarPath = path + JarFileSystem.JAR_SEPARATOR;
        add(module, path);
    }

    public static void loadModuleLibFolder(@NotNull final ModifiableRootModel module, @NotNull final String folderPath){
        Validate.notNull(module);
        Validate.notNull(folderPath);

        File jarFilesFolder = new File(folderPath);
        if(jarFilesFolder.exists() && jarFilesFolder.isDirectory()){
            runByAllFilesInFolder(module, jarFilesFolder);
        }
    }


    private static void add(@NotNull final ModifiableRootModel module, @NotNull final String jarPath){
        String path = VirtualFileManager.constructUrl(JarFileSystem.PROTOCOL, jarPath);
        final VirtualFile jarVirtualFile = VirtualFileManager.getInstance().findFileByUrl(path);

        if(isAlreadyExist(module, jarVirtualFile.getName()))
            return;

        final Library jarToAdd = module.getModuleLibraryTable().createLibrary();
        final Library.ModifiableModel libraryModel = jarToAdd.getModifiableModel();
        libraryModel.addRoot(jarVirtualFile, OrderRootType.CLASSES);
        libraryModel.commit();
    }

    private static boolean isAlreadyExist(@NotNull final ModifiableRootModel module, @NotNull final String libName){
        final Library[] moduleLibs = module.getModuleLibraryTable().getLibraries();
        for(int i=0;i<moduleLibs.length;i++){
            if(null!=moduleLibs[i].getName() && moduleLibs[i].getName().equals(libName)){
                return true;
            }
        }
        return false;
    }

    private static void runByAllFilesInFolder(@NotNull final ModifiableRootModel module, @NotNull final File folder) {
        Validate.notNull(module);
        Validate.notNull(folder);

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                runByAllFilesInFolder(module, fileEntry);
            } else if(fileEntry.isFile() && fileEntry.getName().endsWith(".jar")){
                addLib(module, fileEntry);
            }
        }
    }

}
