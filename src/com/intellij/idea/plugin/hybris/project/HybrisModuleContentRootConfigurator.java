package com.intellij.idea.plugin.hybris.project;

import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.vfs.VfsUtil;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.java.JavaResourceRootType;
import org.jetbrains.jps.model.java.JavaSourceRootType;
import org.jetbrains.jps.model.java.JpsJavaExtensionService;

import java.io.File;

/**
 * Created 2:07 AM 15 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisModuleContentRootConfigurator implements ContentRootConfigurator {

    public static final String SRC_DIRECTORY = "src";
    public static final String GEN_SRC_DIRECTORY = "gensrc";
    public static final String TEST_SRC_DIRECTORY = "testsrc";
    public static final String RESOURCES_DIRECTORY = "resources";
    public static final String HMC_MODULE_DIRECTORY = "hmc";
    public static final String WEB_MODULE_DIRECTORY = "web";
    public static final String BACK_OFFICE_MODULE_DIRECTORY = "backoffice";
    public static final String ADDON_SRC_DIRECTORY = "addonsrc";
    public static final String TEST_CLASSES_DIRECTORY = "testclasses";
    public static final String CLASSES_DIRECTORY = "classes";
    public static final String SETTINGS_DIRECTORY = ".settings";
    public static final String EXTERNAL_TOOL_BUILDERS_DIRECTORY = ".externalToolBuilders";
    public static final String WEB_ROOT_DIRECTORY = "webroot";
    public static final String WEB_INF_DIRECTORY = "WEB-INF";
    public static final String ECLIPSE_BIN_DIRECTORY = "eclipsebin";
    public static final String COMMON_WEB_MODULE_DIRECTORY = "commonweb";
    public static final String ACCELERATOR_ADDON_DIRECTORY = "acceleratoraddon";

    @Override
    public void configure(@NotNull final ModifiableRootModel modifiableRootModel,
                          @NotNull final HybrisModuleDescriptor moduleDescriptor) {
        Validate.notNull(modifiableRootModel);
        Validate.notNull(moduleDescriptor);

        final ContentEntry contentEntry = modifiableRootModel.addContentEntry(VfsUtil.pathToUrl(
            moduleDescriptor.getRootDirectory().getAbsolutePath()
        ));

        this.configureCommonRoots(moduleDescriptor, contentEntry);
        this.configureHmcRoots(moduleDescriptor, contentEntry, moduleDescriptor.getRootDirectory());
        this.configureWebRoots(moduleDescriptor, contentEntry, moduleDescriptor.getRootDirectory());
        this.configureCommonWebRoots(moduleDescriptor, contentEntry);
        this.configureAcceleratorAddonRoots(moduleDescriptor, contentEntry);
        this.configureBackOfficeRoots(moduleDescriptor, contentEntry);
    }

    protected void configureCommonRoots(@NotNull final HybrisModuleDescriptor moduleDescriptor,
                                        @NotNull final ContentEntry contentEntry) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(contentEntry);

        final File srcDirectory = new File(moduleDescriptor.getRootDirectory(), SRC_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(srcDirectory.getAbsolutePath()),
            JavaSourceRootType.SOURCE
        );

        final File genSrcDirectory = new File(moduleDescriptor.getRootDirectory(), GEN_SRC_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(genSrcDirectory.getAbsolutePath()),
            JavaSourceRootType.SOURCE,
            JpsJavaExtensionService.getInstance().createSourceRootProperties("", true)
        );

        final File resourcesDirectory = new File(moduleDescriptor.getRootDirectory(), RESOURCES_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(resourcesDirectory.getAbsolutePath()),
            JavaResourceRootType.RESOURCE
        );

        final File testSrcDirectory = new File(moduleDescriptor.getRootDirectory(), TEST_SRC_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(testSrcDirectory.getAbsolutePath()),
            JavaSourceRootType.TEST_SOURCE
        );

        final File externalToolBuildersDirectory = new File(
            moduleDescriptor.getRootDirectory(), EXTERNAL_TOOL_BUILDERS_DIRECTORY
        );

        contentEntry.addExcludeFolder(
            VfsUtil.pathToUrl(externalToolBuildersDirectory.getAbsolutePath())
        );

        final File settingsDirectory = new File(moduleDescriptor.getRootDirectory(), SETTINGS_DIRECTORY);
        contentEntry.addExcludeFolder(
            VfsUtil.pathToUrl(settingsDirectory.getAbsolutePath())
        );

        final File classesDirectory = new File(moduleDescriptor.getRootDirectory(), CLASSES_DIRECTORY);
        contentEntry.addExcludeFolder(
            VfsUtil.pathToUrl(classesDirectory.getAbsolutePath())
        );

        final File eclipseBinDirectory = new File(moduleDescriptor.getRootDirectory(), ECLIPSE_BIN_DIRECTORY);
        contentEntry.addExcludeFolder(
            VfsUtil.pathToUrl(eclipseBinDirectory.getAbsolutePath())
        );
    }

    protected void configureHmcRoots(@NotNull final HybrisModuleDescriptor moduleDescriptor,
                                     @NotNull final ContentEntry contentEntry,
                                     @NotNull final File parentDirectory) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(contentEntry);
        Validate.notNull(parentDirectory);

        final File hmcModuleDirectory = new File(parentDirectory, HMC_MODULE_DIRECTORY);
        if (hmcModuleDirectory.isDirectory()) {
            final File hmcSrcDirectory = new File(hmcModuleDirectory, SRC_DIRECTORY);
            contentEntry.addSourceFolder(
                VfsUtil.pathToUrl(hmcSrcDirectory.getAbsolutePath()),
                JavaSourceRootType.SOURCE
            );

            final File hmcResourcesDirectory = new File(hmcModuleDirectory, RESOURCES_DIRECTORY);
            contentEntry.addSourceFolder(
                VfsUtil.pathToUrl(hmcResourcesDirectory.getAbsolutePath()),
                JavaResourceRootType.RESOURCE
            );

            final File hmcClassesDirectory = new File(hmcModuleDirectory, CLASSES_DIRECTORY);
            contentEntry.addExcludeFolder(
                VfsUtil.pathToUrl(hmcClassesDirectory.getAbsolutePath())
            );
        }
    }

    protected void configureWebRoots(@NotNull final HybrisModuleDescriptor moduleDescriptor,
                                     @NotNull final ContentEntry contentEntry,
                                     @NotNull final File parentDirectory) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(contentEntry);
        Validate.notNull(parentDirectory);

        final File webModuleDirectory = new File(parentDirectory, WEB_MODULE_DIRECTORY);
        if (webModuleDirectory.isDirectory()) {
            this.configureWebModuleRoots(contentEntry, webModuleDirectory);
        }
    }

    protected void configureCommonWebRoots(@NotNull final HybrisModuleDescriptor moduleDescriptor,
                                           @NotNull final ContentEntry contentEntry) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(contentEntry);

        final File commonWebModuleDirectory = new File(moduleDescriptor.getRootDirectory(), COMMON_WEB_MODULE_DIRECTORY);
        if (commonWebModuleDirectory.isDirectory()) {
            this.configureWebModuleRoots(contentEntry, commonWebModuleDirectory);
        }
    }

    protected void configureAcceleratorAddonRoots(@NotNull final HybrisModuleDescriptor moduleDescriptor,
                                                  @NotNull final ContentEntry contentEntry) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(contentEntry);

        final File commonWebModuleDirectory = new File(moduleDescriptor.getRootDirectory(), ACCELERATOR_ADDON_DIRECTORY);
        if (commonWebModuleDirectory.isDirectory()) {
            this.configureWebRoots(moduleDescriptor, contentEntry, commonWebModuleDirectory);
            this.configureHmcRoots(moduleDescriptor, contentEntry, commonWebModuleDirectory);
        }
    }

    protected void configureBackOfficeRoots(@NotNull final HybrisModuleDescriptor moduleDescriptor,
                                            @NotNull final ContentEntry contentEntry) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(contentEntry);

        final File hmcModuleDirectory = new File(moduleDescriptor.getRootDirectory(), BACK_OFFICE_MODULE_DIRECTORY);
        if (hmcModuleDirectory.isDirectory()) {
            final File hmcSrcDirectory = new File(hmcModuleDirectory, SRC_DIRECTORY);
            contentEntry.addSourceFolder(
                VfsUtil.pathToUrl(hmcSrcDirectory.getAbsolutePath()),
                JavaSourceRootType.SOURCE
            );

            final File hmcResourcesDirectory = new File(hmcModuleDirectory, RESOURCES_DIRECTORY);
            contentEntry.addSourceFolder(
                VfsUtil.pathToUrl(hmcResourcesDirectory.getAbsolutePath()),
                JavaResourceRootType.RESOURCE
            );

            final File hmcClassesDirectory = new File(hmcModuleDirectory, CLASSES_DIRECTORY);
            contentEntry.addExcludeFolder(
                VfsUtil.pathToUrl(hmcClassesDirectory.getAbsolutePath())
            );
        }
    }

    private void configureWebModuleRoots(final @NotNull ContentEntry contentEntry, final File webModuleDirectory) {
        final File webSrcDirectory = new File(webModuleDirectory, SRC_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(webSrcDirectory.getAbsolutePath()),
            JavaSourceRootType.SOURCE
        );

        final File webGenSrcDirectory = new File(webModuleDirectory, GEN_SRC_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(webGenSrcDirectory.getAbsolutePath()),
            JavaSourceRootType.SOURCE,
            JpsJavaExtensionService.getInstance().createSourceRootProperties("", true)
        );

        final File webTestSrcDirectory = new File(webModuleDirectory, TEST_SRC_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(webTestSrcDirectory.getAbsolutePath()),
            JavaSourceRootType.TEST_SOURCE
        );

        final File webAddonsrcDirectory = new File(webModuleDirectory, ADDON_SRC_DIRECTORY);
        contentEntry.addExcludeFolder(
            VfsUtil.pathToUrl(webAddonsrcDirectory.getAbsolutePath())
        );

        final File webTestClassesDirectory = new File(webModuleDirectory, TEST_CLASSES_DIRECTORY);
        contentEntry.addExcludeFolder(
            VfsUtil.pathToUrl(webTestClassesDirectory.getAbsolutePath())
        );

        final File webWebRootDirectory = new File(webModuleDirectory, WEB_ROOT_DIRECTORY);
        final File webInfDirectory = new File(webWebRootDirectory, WEB_INF_DIRECTORY);
        final File webInfClassesDirectory = new File(webInfDirectory, CLASSES_DIRECTORY);
        contentEntry.addExcludeFolder(
            VfsUtil.pathToUrl(webInfClassesDirectory.getAbsolutePath())
        );
    }
}
