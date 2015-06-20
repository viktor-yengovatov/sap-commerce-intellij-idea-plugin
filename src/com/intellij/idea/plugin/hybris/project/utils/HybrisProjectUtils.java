package com.intellij.idea.plugin.hybris.project.utils;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.intellij.idea.plugin.hybris.project.exceptions.HybrisConfigurationException;
import com.intellij.idea.plugin.hybris.project.settings.DefaultHybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.settings.jaxb.ExtensionInfo;
import com.intellij.idea.plugin.hybris.project.settings.jaxb.RequiresExtensionType;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Processor;
import gnu.trove.THashSet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileFilter;
import java.util.*;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

/**
 * Created 10:39 PM 11 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public final class HybrisProjectUtils {

    private static final Logger LOG = Logger.getInstance(HybrisProjectUtils.class.getName());
    @Nullable
    public static final JAXBContext EXTENSION_INFO_JAXB_CONTEXT = getExtensionInfoJaxbContext();

    private HybrisProjectUtils() throws IllegalAccessException {
        throw new IllegalAccessException("Should never be accessed.");
    }

    @Nullable
    public static ExtensionInfo unmarshalExtensionInfo(@NotNull final File file) {
        Validate.notNull(file);

        try {
            if (null == EXTENSION_INFO_JAXB_CONTEXT) {
                LOG.error(String.format(
                    "Can not unmarshal '%s' because JAXBContext has not been created.", file.getAbsolutePath()
                ));

                return null;
            }

            final Unmarshaller jaxbUnmarshaller = EXTENSION_INFO_JAXB_CONTEXT.createUnmarshaller();

            return (ExtensionInfo) jaxbUnmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            LOG.error("Can not unmarshal " + file.getAbsolutePath(), e);
        }

        return null;
    }

    @Nullable
    private static JAXBContext getExtensionInfoJaxbContext() {
        try {
            return JAXBContext.newInstance(ExtensionInfo.class);
        } catch (JAXBException e) {
            LOG.error("Can not create JAXBContext for ExtensionInfo.", e);
        }

        return null;
    }

    public static void buildDependencies(@NotNull final Iterable<HybrisModuleDescriptor> moduleDescriptors) {
        Validate.notNull(moduleDescriptors);

        for (HybrisModuleDescriptor moduleDescriptor : moduleDescriptors) {

            moduleDescriptor.getDependenciesTree().clear();

            if (null == moduleDescriptor.getExtensionInfo().getExtension()) {
                continue;
            }

            final List<RequiresExtensionType> requiresExtensions = moduleDescriptor.getExtensionInfo()
                                                                                   .getExtension()
                                                                                   .getRequiresExtension();

            if (isEmpty(requiresExtensions)) {
                continue;
            }

            for (RequiresExtensionType requiresExtension : requiresExtensions) {

                final HybrisModuleDescriptor dependsOn = Iterables.find(
                    moduleDescriptors, new FindHybrisModuleDescriptorByName(requiresExtension.getName())
                );

                if (null != dependsOn) {
                    moduleDescriptor.getDependenciesTree().add(dependsOn);
                }
            }
        }
    }

    @NotNull
    public static Set<HybrisModuleDescriptor> getDependenciesPlainSet(
        @NotNull final HybrisModuleDescriptor descriptor
    ) {
        Validate.notNull(descriptor);

        return recursivelyCollectDependenciesPlainSet(descriptor, new HashSet<HybrisModuleDescriptor>());
    }

    @NotNull
    private static Set<HybrisModuleDescriptor> recursivelyCollectDependenciesPlainSet(
        @NotNull final HybrisModuleDescriptor descriptor,
        @NotNull final Set<HybrisModuleDescriptor> dependenciesSet
    ) {
        Validate.notNull(descriptor);
        Validate.notNull(dependenciesSet);

        if (CollectionUtils.isEmpty(descriptor.getDependenciesTree())) {
            return Collections.emptySet();
        }

        for (HybrisModuleDescriptor moduleDescriptor : descriptor.getDependenciesTree()) {
            if (dependenciesSet.contains(moduleDescriptor)) {
                continue;
            }

            dependenciesSet.add(moduleDescriptor);
            dependenciesSet.addAll(recursivelyCollectDependenciesPlainSet(moduleDescriptor, dependenciesSet));
        }

        return dependenciesSet;
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
                final File[] files = rootProjectDirectory.listFiles(new DirectoriesFilter());

                if (null != files) {
                    for (File file : files) {
                        paths.addAll(findModuleRoots(file, stepProcessor));
                    }
                }
            }
        }

        return paths;
    }

    public static boolean isDirectoryContainsHybrisModuleFile(final @NotNull File rootProjectOrModuleDirectory) {
        Validate.notNull(rootProjectOrModuleDirectory);

        return new File(rootProjectOrModuleDirectory, HybrisConstants.EXTENSION_INFO_XML).isFile();
//               || new File(rootProjectOrModuleDirectory, HybrisConstants.LOCAL_EXTENSIONS_XML).isFile()
//               || new File(rootProjectOrModuleDirectory, HybrisConstants.EXTENSIONS_XML).isFile();
    }

    public static class DirectoriesFilter implements FileFilter {

        @Override
        public boolean accept(final File pathname) {
            return pathname.isDirectory();
        }
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
