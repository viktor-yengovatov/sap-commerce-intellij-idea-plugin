package com.intellij.idea.plugin.hybris.project.utils;

import com.intellij.idea.plugin.hybris.project.exceptions.ConfigurationException;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.Processor;
import org.apache.commons.lang.Validate;
import org.jdom.JDOMException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created 10:39 PM 11 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public final class HybrisProjectUtils {

    private static final Pattern LINE_BREAK_PATTERN = Pattern.compile("\n");

    private HybrisProjectUtils() throws IllegalAccessException {
        throw new IllegalAccessException("Should never be accessed.");
    }

    @Nullable
    public static String buildPathForIdeaModuleFile(final String moduleRootAbsolutePath,
                                                    final String rootProjectAbsolutePath
    ) throws ConfigurationException {
        final String moduleName = HybrisProjectUtils.getModuleName(moduleRootAbsolutePath);

        if (null == moduleName) {
            return null;
        }

        return rootProjectAbsolutePath
               + File.separatorChar
               + moduleName
               + HybrisConstants.NEW_MODULE_FILE_EXTENSION;
    }

    @Nullable
    public static String getModuleName(@NotNull final String moduleRootAbsolutePath) {
        Validate.notEmpty(moduleRootAbsolutePath);

        String name = null;
        final File file = new File(moduleRootAbsolutePath, HybrisConstants.EXTENSION_INFO_XML);

        if (file.isFile()) {
            try {
                name = JDOMUtil.loadDocument(file).getRootElement().getChild("extension").getAttribute("name").getValue();

                if (StringUtil.isEmptyOrSpaces(name)) {
                    return null;
                }

                name = LINE_BREAK_PATTERN.matcher(name).replaceAll(" ").trim();
            } catch (JDOMException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
        }

        return name;
    }

    @NotNull
    public static Collection<File> findAlreadyExistingModuleFiles(
        @NotNull final Iterable<String> modulesRootAbsolutePaths
    ) {
        Validate.notNull(modulesRootAbsolutePaths);

        final Collection<File> files = new HashSet<File>();

        for (String path : modulesRootAbsolutePaths) {
            final File moduleFile = findAlreadyExistingModuleFile(path);

            if (null != moduleFile) {
                files.add(moduleFile);
            }
        }

        return files;
    }

    @Nullable
    public static File findAlreadyExistingModuleFile(@NotNull final String moduleRootAbsolutePath) {
        Validate.notEmpty(moduleRootAbsolutePath);

        final String moduleName = HybrisProjectUtils.getModuleName(moduleRootAbsolutePath);

        final File imlFile = new File(
            moduleRootAbsolutePath + File.separator + moduleName + HybrisConstants.NEW_MODULE_FILE_EXTENSION
        );

        if (imlFile.isFile()) {
            return imlFile;
        }

        final File emlFile = new File(
            moduleRootAbsolutePath + File.separator + moduleName + HybrisConstants.OLD_MODULE_FILE_EXTENSION
        );

        if (emlFile.isFile()) {
            return emlFile;
        }

        return null;
    }

    @NotNull
    public static List<String> findModuleRoots(@NotNull final String rootProjectAbsolutePath,
                                               @Nullable final Processor<String> stepProcessor
    ) {
        return new ArrayList<String>(findModuleRootsSet(rootProjectAbsolutePath, stepProcessor));
    }

    @NotNull
    private static Set<String> findModuleRootsSet(@NotNull final String rootProjectAbsolutePath,
                                                  @Nullable final Processor<String> stepProcessor
    ) {
        Validate.notEmpty(rootProjectAbsolutePath);

        final Set<String> paths = new HashSet<String>(1);

        if (null != stepProcessor) {
            stepProcessor.process(rootProjectAbsolutePath);
        }

        if (null != getModuleName(rootProjectAbsolutePath)) {
            paths.add(rootProjectAbsolutePath);
        }

        final File root = new File(rootProjectAbsolutePath);
        if (root.isDirectory()) {
            final File[] files = root.listFiles();

            if (null != files) {
                for (File file : files) {
                    paths.addAll(findModuleRootsSet(file.getPath(), stepProcessor));
                }
            }
        }

        return paths;
    }
}
