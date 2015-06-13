package com.intellij.idea.plugin.hybris.project.utils;

import com.intellij.idea.plugin.hybris.utils.HybrisConstantsUtils;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.Processor;
import org.apache.commons.lang.Validate;
import org.jdom.JDOMException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created 10:39 PM 11 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public final class HybrisProjectFinderUtils {

    private static final Pattern LINE_BREAK_PATTERN = Pattern.compile("\n");

    private HybrisProjectFinderUtils() throws IllegalAccessException {
        throw new IllegalAccessException("Should never be accessed.");
    }

    @NotNull
    public static List<String> findModuleRoots(@NotNull final String rootPath,
                                               @Nullable final Processor<String> stepProcessor
    ) {
        return new ArrayList<String>(findModuleRootsSet(rootPath, stepProcessor));
    }

    @NotNull
    private static Set<String> findModuleRootsSet(@NotNull final String rootPath,
                                                  @Nullable final Processor<String> stepProcessor
    ) {
        Validate.notEmpty(rootPath);

        final Set<String> paths = new HashSet<String>(1);

        if (null != stepProcessor) {
            stepProcessor.process(rootPath);
        }

        if (null != findProjectName(rootPath)) {
            paths.add(rootPath);
        }

        final File root = new File(rootPath);
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

    @Nullable
    public static String findProjectName(@NotNull final String rootPath) {
        Validate.notEmpty(rootPath);

        String name = null;
        final File file = new File(rootPath, HybrisConstantsUtils.EXTENSION_INFO_XML);

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
}
