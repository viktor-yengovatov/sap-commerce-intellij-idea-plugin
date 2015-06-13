package com.intellij.idea.plugin.hybris.project;

import com.intellij.idea.plugin.hybris.project.utils.HybrisProjectFinderUtils;
import com.intellij.idea.plugin.hybris.project.wizard.Options;
import com.intellij.idea.plugin.hybris.utils.HybrisI18NBundleUtils;
import com.intellij.idea.plugin.hybris.utils.HybrisIconsUtils;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ex.ApplicationInfoEx;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.impl.ModifiableModelCommitter;
import com.intellij.openapi.roots.impl.storage.ClassPathStorageUtil;
import com.intellij.openapi.roots.impl.storage.ClasspathStorage;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.ThrowableComputable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.packaging.artifacts.ModifiableArtifactModel;
import com.intellij.projectImport.ProjectImportBuilder;
import com.intellij.util.Function;
import com.intellij.util.Processor;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created 8:58 PM 07 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisProjectImportBuilder extends ProjectImportBuilder<String> {

    private static final Logger LOG = Logger.getInstance("#" + HybrisProjectImportBuilder.class.getName());

    private Parameters parameters;

    @NotNull
    @Override
    public String getName() {
        return HybrisI18NBundleUtils.message("hybris.project.name");
    }

    @Override
    public Icon getIcon() {
        return HybrisIconsUtils.HYBRIS_ICON;
    }

    @Override
    public List<String> getList() {
        return this.getParameters().workspace;
    }

    @Override
    public void setList(final List<String> list) throws ConfigurationException {
        this.getParameters().projectsToConvert = list;
    }

    @Override
    public boolean isMarked(final String element) {
        if (this.getParameters().projectsToConvert != null) {
            return this.getParameters().projectsToConvert.contains(element);
        }

        return !this.getParameters().existingModuleNames.contains(HybrisProjectFinderUtils.findProjectName(element));
    }

    @Nullable
    @Override
    public List<Module> commit(final Project project,
                               final ModifiableModuleModel model,
                               final ModulesProvider modulesProvider,
                               final ModifiableArtifactModel artifactModel) {

        final List<Module> result = new ArrayList<Module>();

        try {
            final ModifiableModuleModel moduleModel = model != null ? model : ModuleManager.getInstance(project).getModifiableModel();
            final ModifiableRootModel[] rootModels = new ModifiableRootModel[this.getParameters().projectsToConvert.size()];
            final Collection<File> files = new HashSet<File>();

            for (String path : this.getParameters().projectsToConvert) {

                String modulesDirectory = this.getParameters().converterOptions.commonModulesDirectory;
                if (modulesDirectory == null) {
                    modulesDirectory = path;
                }

                final String moduleName = HybrisProjectFinderUtils.findProjectName(path);

                final File imlFile = new File(modulesDirectory + File.separator + moduleName + ".iml");
                if (imlFile.isFile()) {
                    files.add(imlFile);
                }

                final File emlFile = new File(modulesDirectory + File.separator + moduleName + ".eml");
                if (emlFile.isFile()) {
                    files.add(emlFile);
                }
            }

            if (!files.isEmpty()) {
                final int resultCode = Messages.showYesNoCancelDialog(
                    String.format(
                        "%s module files found:\n%s.\n Would you like to reuse them?",
                        ApplicationInfoEx.getInstanceEx().getFullApplicationName(),
                        StringUtil.join(files, new GetFileNameFunction(), "\n")
                    ),
                    "Module Files Found",
                    Messages.getQuestionIcon()
                );

                if (resultCode != Messages.YES) {
                    if (resultCode == Messages.NO) {
                        final LocalFileSystem localFileSystem = LocalFileSystem.getInstance();

                        for (File file : files) {

                            final VirtualFile virtualFile = localFileSystem.findFileByIoFile(file);

                            if (virtualFile != null) {
                                ApplicationManager.getApplication().runWriteAction(new ThrowableComputable<Void, IOException>() {

                                    @Override
                                    public Void compute() throws IOException {
                                        virtualFile.delete(this);
                                        return null;
                                    }
                                });

                            } else {
                                FileUtil.delete(file);
                            }
                        }

                    } else {
                        return result;
                    }
                }
            }

            int idx = 0;

            for (String path : getParameters().projectsToConvert) {

                String modulesDirectory = getParameters().converterOptions.commonModulesDirectory;
                if (modulesDirectory == null) {
                    modulesDirectory = path;
                }

                final Module module = moduleModel.newModule(
                    modulesDirectory + "/" + HybrisProjectFinderUtils.findProjectName(path) + ".iml",
                    StdModuleTypes.JAVA.getId()
                );

                result.add(module);

                final ModifiableRootModel rootModel = ModuleRootManager.getInstance(module).getModifiableModel();
                rootModels[idx++] = rootModel;

                rootModel.addContentEntry(VfsUtilCore.pathToUrl(path));

                ClasspathStorage.setStorageType(rootModel, ClassPathStorageUtil.DEFAULT_STORAGE);

                if (model != null) {
                    ApplicationManager.getApplication().runWriteAction(new Runnable() {
                        public void run() {
                            rootModel.commit();
                        }
                    });
                }
            }

            if (model == null) {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    public void run() {
                        ModifiableModelCommitter.multiCommit(rootModels, moduleModel);
                    }
                });
            }
        } catch (Exception e) {
            LOG.error(e);
        }

        return result;
    }

    public void cleanup() {
        super.cleanup();
        this.parameters = null;
    }

    public boolean isOpenProjectSettingsAfter() {
        return this.getParameters().openModuleSettings;
    }

    @Override
    public void setOpenProjectSettingsAfter(final boolean on) {
        this.getParameters().openModuleSettings = on;
    }

    @NotNull
    public Parameters getParameters() {
        if (this.parameters == null) {
            this.parameters = new Parameters();
            this.parameters.existingModuleNames = new THashSet<String>();

            if (this.isUpdate()) {
                final Project project = getCurrentProject();

                if (project != null) {
                    for (Module module : ModuleManager.getInstance(project).getModules()) {
                        this.parameters.existingModuleNames.add(module.getName());
                    }
                }
            }
        }

        return this.parameters;
    }

    public boolean setRootDirectory(final String path) {
        ProgressManager.getInstance().run(new Task.Modal(
            getCurrentProject(),
            HybrisI18NBundleUtils.message("hybris.project.import.scanning"), true) {

            public void run(@NotNull final ProgressIndicator indicator) {
                final List<String> roots = new ArrayList<String>();
                HybrisProjectFinderUtils.findModuleRoots(roots, path, new Processor<String>() {

                    @Override
                    public boolean process(final String path) {
                        final ProgressIndicator progressIndicator = ProgressManager.getInstance().getProgressIndicator();

                        if (null != progressIndicator) {
                            if (progressIndicator.isCanceled()) {
                                return false;
                            }

                            progressIndicator.setText2(path);
                        }

                        return true;
                    }
                });

                Collections.sort(roots, new Comparator<String>() {

                    @Override
                    public int compare(final String path1, final String path2) {
                        final String projectName1 = HybrisProjectFinderUtils.findProjectName(path1);
                        final String projectName2 = HybrisProjectFinderUtils.findProjectName(path2);

                        return ((null != projectName1) && (null != projectName2))
                            ? projectName1.compareToIgnoreCase(projectName2)
                            : 0;
                    }
                });

                getParameters().workspace = roots;
                getParameters().root = path;
            }

            public void onCancel() {
                getParameters().workspace = null;
                getParameters().root = null;
            }
        });

        this.setFileToImport(path);

        return this.getParameters().workspace != null;
    }

    public static class Parameters {

        public String root;
        public List<String> workspace;
        public List<String> projectsToConvert = new ArrayList<String>();
        public boolean openModuleSettings;
        public Options converterOptions = new Options();
        public Set<String> existingModuleNames;
    }

    private static class GetFileNameFunction implements Function<File, String> {

        public String fun(final File file) {
            return file.getPath();
        }
    }
}
