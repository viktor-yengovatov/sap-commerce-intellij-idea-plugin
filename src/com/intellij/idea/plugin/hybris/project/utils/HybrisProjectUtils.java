package com.intellij.idea.plugin.hybris.project.utils;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.intellij.idea.plugin.hybris.project.exceptions.HybrisConfigurationException;
import com.intellij.idea.plugin.hybris.project.settings.DefaultHybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Processor;
import gnu.trove.THashSet;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

/**
 * Created 10:39 PM 11 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public final class HybrisProjectUtils {

    private static final Logger LOG = Logger.getInstance(HybrisProjectUtils.class);

    private HybrisProjectUtils() throws IllegalAccessException {
        throw new IllegalAccessException("Should never be accessed.");
    }

    public static void buildDependencies(@NotNull final Iterable<HybrisModuleDescriptor> moduleDescriptors) {
        Validate.notNull(moduleDescriptors);

        for (HybrisModuleDescriptor moduleDescriptor : moduleDescriptors) {

            moduleDescriptor.getDependenciesTree().clear();

            final Set<String> requiredExtensionNames = moduleDescriptor.getRequiredExtensionNames();

            if (isEmpty(requiredExtensionNames)) {
                continue;
            }

            for (String requiresExtensionName : requiredExtensionNames) {

                final HybrisModuleDescriptor dependsOn = Iterables.find(
                    moduleDescriptors, new FindHybrisModuleDescriptorByName(requiresExtensionName)
                );

                if (null != dependsOn) {
                    moduleDescriptor.getDependenciesTree().add(dependsOn);
                }
            }
        }
    }

    @NotNull
    public static Set<HybrisModuleDescriptor> getAlreadyOpenedModules(@NotNull final Project project) {
        Validate.notNull(project);

        final Set<HybrisModuleDescriptor> existingModules = new THashSet<HybrisModuleDescriptor>();

        for (Module module : ModuleManager.getInstance(project).getModules()) {
            try {
                final VirtualFile moduleFile = module.getModuleFile();
                if (null == moduleFile) {
                    LOG.error("Can not find module file for module: " + module.getName());
                    continue;
                }

                final HybrisModuleDescriptor moduleDescriptor = new DefaultHybrisModuleDescriptor(
                    VfsUtil.virtualToIoFile(moduleFile.getParent())
                );

                existingModules.add(moduleDescriptor);
            } catch (HybrisConfigurationException e) {
                LOG.error(e);
            }
        }

        return existingModules;
    }

    @NotNull
    public static List<File> findModuleRoots(@NotNull final File rootProjectDirectory,
                                             @Nullable final Processor<File> stepProcessor
    ) {
        Validate.notNull(rootProjectDirectory);

        final List<File> paths = new ArrayList<File>(1);

        if (null != stepProcessor) {
            stepProcessor.process(rootProjectDirectory);
        }

        if (isDirectoryContainsHybrisModuleFile(rootProjectDirectory)) {

            paths.add(rootProjectDirectory);

        } else {

            if (rootProjectDirectory.isDirectory()) {
                final File[] files = rootProjectDirectory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);

                if (null != files) {
                    for (File file : files) {
                        paths.addAll(findModuleRoots(file, stepProcessor));
                    }
                }
            }
        }

        return paths;
    }

    public static boolean isDirectoryContainsHybrisModuleFile(final @NotNull File directory) {
        Validate.notNull(directory);

        for (String file : getSupportedModuleFileNames()) {
            if (new File(directory, file).isFile()) {
                return true;
            }
        }

        return false;
    }

    public static String[] getSupportedModuleFileNames() {
        return new String[]{
            HybrisConstants.EXTENSION_INFO_XML
//            HybrisConstants.LOCAL_EXTENSIONS_XML,
//            HybrisConstants.EXTENSIONS_XML
        };
    }

    public static class FindHybrisModuleDescriptorByName implements Predicate<HybrisModuleDescriptor> {

        private final String name;

        public FindHybrisModuleDescriptorByName(@NotNull final String name) {
            Validate.notEmpty(name);

            this.name = name;
        }

        @Override
        public boolean apply(@Nullable final HybrisModuleDescriptor t) {
            return null != t && name.equalsIgnoreCase(t.getModuleName());
        }
    }
}
