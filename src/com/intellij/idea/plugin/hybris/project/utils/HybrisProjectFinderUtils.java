package com.intellij.idea.plugin.hybris.project.utils;

import com.intellij.idea.plugin.hybris.utils.HybrisConstantsUtils;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.Processor;
import org.jdom.JDOMException;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;
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

    public static void findModuleRoots(final List<String> paths,
                                       final String rootPath,
                                       @Nullable final Processor<String> progressUpdater
    ) {
        if (null != progressUpdater) {
            progressUpdater.process(rootPath);
        }

        final boolean projectFound = null != findProjectName(rootPath);
        if (projectFound) {
            paths.add(rootPath);
        }

        final File root = new File(rootPath);
        if (root.isDirectory()) {
            final File[] files = root.listFiles();

            if (null != files) {
                for (File file : files) {
                    findModuleRoots(paths, file.getPath(), progressUpdater);
                }
            }
        }
    }

    @Nullable
    public static String findProjectName(final String rootPath) {
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
