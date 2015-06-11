package com.intellij.idea.plugin.hybris.project;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.util.io.FileUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created 1:42 AM 12 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisClasspathReader {

    protected final String myRootPath;

    @Nullable
    protected final List<String> myCurrentRoots;

    @Nullable
    protected final Set<String> myModuleNames;

    private final Project myProject;
    private ContentEntry myContentEntry;

    public HybrisClasspathReader(final @NotNull String rootPath,
                                 final @NotNull Project project,
                                 final @Nullable List<String> currentRoots) {
        this(rootPath, project, currentRoots, null);
    }

    public HybrisClasspathReader(final @NotNull String rootPath,
                                 final @NotNull Project project,
                                 final @Nullable List<String> currentRoots,
                                 final @Nullable Set<String> moduleNames) {
        myRootPath = FileUtil.toSystemIndependentName(rootPath);
        myCurrentRoots = currentRoots;
        myModuleNames = moduleNames;
        myProject = project;
    }

    public static void setOutputUrl(final @NotNull ModifiableRootModel rootModel, final @NotNull String path) {
        final CompilerModuleExtension compilerModuleExtension = rootModel.getModuleExtension(CompilerModuleExtension.class);
        compilerModuleExtension.setCompilerOutputPath(pathToUrl(path));
        compilerModuleExtension.inheritCompilerOutputPath(false);
    }

    @NotNull
    protected static String pathToUrl(final @NotNull String path) {
        return "file://" + path;
    }

    public void readClasspath(final @NotNull ModifiableRootModel model,
                              final @NotNull Collection<String> unknownLibraries,
                              final @NotNull Collection<String> unknownJdks,
                              final Set<String> refsToModules,
                              final String testPattern,
                              final Element classpathElement) throws IOException {
    }

    public void init(final @NotNull ModifiableRootModel model) {
        this.myContentEntry = model.addContentEntry(pathToUrl(this.myRootPath));
    }

}
