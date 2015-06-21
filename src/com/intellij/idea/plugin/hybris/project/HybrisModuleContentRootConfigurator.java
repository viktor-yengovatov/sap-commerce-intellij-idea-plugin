package com.intellij.idea.plugin.hybris.project;

import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
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
    public static final String PLATFORM_BOOTSTRAP_DIRECTORY = "bootstrap";
    public static final String PLATFORM_MODEL_CLASSES_DIRECTORY = "modelclasses";
    public static final String PLATFORM_TOMCAT_DIRECTORY = "tomcat";
    public static final String PLATFORM_TOMCAT_WORK_DIRECTORY = "work";

    @Override
    public void configure(@NotNull final ModifiableRootModel modifiableRootModel,
                          @NotNull final HybrisModuleDescriptor moduleDescriptor) {
        Validate.notNull(modifiableRootModel);
        Validate.notNull(moduleDescriptor);

        final ContentEntry contentEntry = modifiableRootModel.addContentEntry(VfsUtil.pathToUrl(
            moduleDescriptor.getModuleRootDirectory().getAbsolutePath()
        ));

        this.configureCommonRoots(moduleDescriptor, contentEntry);
        this.configureHmcRoots(moduleDescriptor, contentEntry, moduleDescriptor.getModuleRootDirectory());
        this.configureWebRoots(moduleDescriptor, contentEntry, moduleDescriptor.getModuleRootDirectory());
        this.configureCommonWebRoots(moduleDescriptor, contentEntry);
        this.configureAcceleratorAddonRoots(moduleDescriptor, contentEntry);
        this.configureBackOfficeRoots(moduleDescriptor, contentEntry);
        this.configurePlatformRoots(moduleDescriptor, contentEntry);
    }

    protected void configureCommonRoots(@NotNull final HybrisModuleDescriptor moduleDescriptor,
                                        @NotNull final ContentEntry contentEntry) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(contentEntry);

        final File srcDirectory = new File(moduleDescriptor.getModuleRootDirectory(), SRC_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(srcDirectory.getAbsolutePath()),
            JavaSourceRootType.SOURCE
        );

        final File genSrcDirectory = new File(moduleDescriptor.getModuleRootDirectory(), GEN_SRC_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(genSrcDirectory.getAbsolutePath()),
            JavaSourceRootType.SOURCE,
            JpsJavaExtensionService.getInstance().createSourceRootProperties("", true)
        );

        final File resourcesDirectory = new File(moduleDescriptor.getModuleRootDirectory(), RESOURCES_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(resourcesDirectory.getAbsolutePath()),
            JavaResourceRootType.RESOURCE
        );

        final File testSrcDirectory = new File(moduleDescriptor.getModuleRootDirectory(), TEST_SRC_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(testSrcDirectory.getAbsolutePath()),
            JavaSourceRootType.TEST_SOURCE
        );

        final File externalToolBuildersDirectory = new File(
            moduleDescriptor.getModuleRootDirectory(), EXTERNAL_TOOL_BUILDERS_DIRECTORY
        );

        contentEntry.addExcludeFolder(
            VfsUtil.pathToUrl(externalToolBuildersDirectory.getAbsolutePath())
        );

        final File settingsDirectory = new File(moduleDescriptor.getModuleRootDirectory(), SETTINGS_DIRECTORY);
        contentEntry.addExcludeFolder(
            VfsUtil.pathToUrl(settingsDirectory.getAbsolutePath())
        );

        final File classesDirectory = new File(moduleDescriptor.getModuleRootDirectory(), CLASSES_DIRECTORY);
        contentEntry.addExcludeFolder(
            VfsUtil.pathToUrl(classesDirectory.getAbsolutePath())
        );

        final File eclipseBinDirectory = new File(moduleDescriptor.getModuleRootDirectory(), ECLIPSE_BIN_DIRECTORY);
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

    protected void configureWebRoots(@NotNull final HybrisModuleDescriptor moduleDescriptor,
                                     @NotNull final ContentEntry contentEntry,
                                     @NotNull final File parentDirectory) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(contentEntry);
        Validate.notNull(parentDirectory);

        final File webModuleDirectory = new File(parentDirectory, WEB_MODULE_DIRECTORY);
        this.configureWebModuleRoots(contentEntry, webModuleDirectory);
    }

    protected void configureCommonWebRoots(@NotNull final HybrisModuleDescriptor moduleDescriptor,
                                           @NotNull final ContentEntry contentEntry) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(contentEntry);

        final File commonWebModuleDirectory = new File(
            moduleDescriptor.getModuleRootDirectory(), COMMON_WEB_MODULE_DIRECTORY
        );

        this.configureWebModuleRoots(contentEntry, commonWebModuleDirectory);
    }

    protected void configureAcceleratorAddonRoots(@NotNull final HybrisModuleDescriptor moduleDescriptor,
                                                  @NotNull final ContentEntry contentEntry) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(contentEntry);

        final File commonWebModuleDirectory = new File(
            moduleDescriptor.getModuleRootDirectory(), ACCELERATOR_ADDON_DIRECTORY
        );
        this.configureWebRoots(moduleDescriptor, contentEntry, commonWebModuleDirectory);
        this.configureHmcRoots(moduleDescriptor, contentEntry, commonWebModuleDirectory);
    }

    protected void configureBackOfficeRoots(@NotNull final HybrisModuleDescriptor moduleDescriptor,
                                            @NotNull final ContentEntry contentEntry) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(contentEntry);

        final File backOfficeModuleDirectory = new File(
            moduleDescriptor.getModuleRootDirectory(), BACK_OFFICE_MODULE_DIRECTORY
        );
        final File backOfficeSrcDirectory = new File(backOfficeModuleDirectory, SRC_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(backOfficeSrcDirectory.getAbsolutePath()),
            JavaSourceRootType.SOURCE
        );

        final File hmcResourcesDirectory = new File(backOfficeModuleDirectory, RESOURCES_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(hmcResourcesDirectory.getAbsolutePath()),
            JavaResourceRootType.RESOURCE
        );

        final File hmcClassesDirectory = new File(backOfficeModuleDirectory, CLASSES_DIRECTORY);
        contentEntry.addExcludeFolder(
            VfsUtil.pathToUrl(hmcClassesDirectory.getAbsolutePath())
        );
    }

    protected void configurePlatformRoots(@NotNull final HybrisModuleDescriptor moduleDescriptor,
                                          @NotNull final ContentEntry contentEntry) {
        Validate.notNull(moduleDescriptor);
        Validate.notNull(contentEntry);

        if (!HybrisConstants.PLATFORM_EXTENSION_NAME.equalsIgnoreCase(moduleDescriptor.getModuleName())) {
            return;
        }

        final File platformBootstrapDirectory = new File(
            moduleDescriptor.getModuleRootDirectory(), PLATFORM_BOOTSTRAP_DIRECTORY
        );
        final File platformBootstrapGenSrcDirectory = new File(platformBootstrapDirectory, GEN_SRC_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(platformBootstrapGenSrcDirectory.getAbsolutePath()),
            JavaSourceRootType.SOURCE,
            JpsJavaExtensionService.getInstance().createSourceRootProperties("", true)
        );

        final File platformBootstrapResourcesDirectory = new File(platformBootstrapDirectory, RESOURCES_DIRECTORY);
        contentEntry.addSourceFolder(
            VfsUtil.pathToUrl(platformBootstrapResourcesDirectory.getAbsolutePath()),
            JavaResourceRootType.RESOURCE
        );

        final File platformBootstrapModelClassesDirectory = new File(
            platformBootstrapDirectory, PLATFORM_MODEL_CLASSES_DIRECTORY
        );

        contentEntry.addExcludeFolder(
            VfsUtil.pathToUrl(platformBootstrapModelClassesDirectory.getAbsolutePath())
        );

        final File platformTomcatDirectory = new File(
            moduleDescriptor.getModuleRootDirectory(), PLATFORM_TOMCAT_DIRECTORY
        );
        final File platformTomcatWorkDirectory = new File(platformTomcatDirectory, PLATFORM_TOMCAT_WORK_DIRECTORY);
        contentEntry.addExcludeFolder(
            VfsUtil.pathToUrl(platformTomcatWorkDirectory.getAbsolutePath())
        );
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

        final File webAddonSrcDirectory = new File(webModuleDirectory, ADDON_SRC_DIRECTORY);
        contentEntry.addExcludeFolder(
            VfsUtil.pathToUrl(webAddonSrcDirectory.getAbsolutePath())
        );

        final File webTestClassesDirectory = new File(webModuleDirectory, TEST_CLASSES_DIRECTORY);
        contentEntry.addExcludeFolder(
            VfsUtil.pathToUrl(webTestClassesDirectory.getAbsolutePath())
        );
    }
}
