package com.intellij.idea.plugin.hybris.project;

import com.intellij.idea.plugin.hybris.project.settings.HybrisProjectImportParameters;
import com.intellij.idea.plugin.hybris.project.utils.HybrisProjectFinderUtils;
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

    private static final Logger LOG = Logger.getInstance(HybrisProjectImportBuilder.class.getName());

    private HybrisProjectImportParameters projectImportParameters;

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
        return this.getProjectImportParameters().getWorkspace();
    }

    @Override
    public void setList(final List<String> list) throws ConfigurationException {
        this.getProjectImportParameters().setProjectsToConvert(list);
    }

    @NotNull
    public HybrisProjectImportParameters getProjectImportParameters() {
        if (null == this.projectImportParameters) {
            this.projectImportParameters = new HybrisProjectImportParameters();
            this.projectImportParameters.setExistingModuleNames(new THashSet<String>());

            if (this.isUpdate()) {
                final Project project = getCurrentProject();

                if (null != project) {
                    for (Module module : ModuleManager.getInstance(project).getModules()) {
                        this.projectImportParameters.getExistingModuleNames().add(module.getName());
                    }
                }
            }
        }

        return this.projectImportParameters;
    }

    @Override
    public boolean isMarked(final String t) {
        if (null != this.getProjectImportParameters().getProjectsToConvert()) {
            return this.getProjectImportParameters().getProjectsToConvert().contains(t);
        }

        return !this.getProjectImportParameters().getExistingModuleNames().contains(HybrisProjectFinderUtils.findProjectName(t));
    }

    @Nullable
    @Override
    public List<Module> commit(final Project project,
                               final ModifiableModuleModel modifiableModuleModel,
                               final ModulesProvider modulesProvider,
                               final ModifiableArtifactModel modifiableArtifactModel) {

        final List<Module> result = new ArrayList<Module>();

        try {
            final ModifiableModuleModel moduleModel = (null != modifiableModuleModel)
                                                      ? modifiableModuleModel
                                                      : ModuleManager.getInstance(project).getModifiableModel();

            final ModifiableRootModel[] rootModels = new ModifiableRootModel[this.getProjectImportParameters().getProjectsToConvert().size()];
            final Collection<File> files = new HashSet<File>();

            for (String path : this.getProjectImportParameters().getProjectsToConvert()) {

                String modulesDirectory = this.getProjectImportParameters().getCommonModulesDirectory();
                if (null == modulesDirectory) {
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

                if (Messages.YES != resultCode) {
                    if (Messages.NO == resultCode) {
                        final LocalFileSystem localFileSystem = LocalFileSystem.getInstance();

                        for (File file : files) {

                            final VirtualFile virtualFile = localFileSystem.findFileByIoFile(file);

                            if (null != virtualFile) {
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

            for (String path : getProjectImportParameters().getProjectsToConvert()) {

                String modulesDirectory = getProjectImportParameters().getCommonModulesDirectory();
                if (null == modulesDirectory) {
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

                if (null != modifiableModuleModel) {
                    ApplicationManager.getApplication().runWriteAction(new Runnable() {
                        public void run() {
                            rootModel.commit();
                        }
                    });
                }
            }

            if (null == modifiableModuleModel) {
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
        this.projectImportParameters = null;
    }

    public boolean isOpenProjectSettingsAfter() {
        return this.getProjectImportParameters().isOpenModuleSettings();
    }

    @Override
    public void setOpenProjectSettingsAfter(final boolean b) {
        this.getProjectImportParameters().setOpenModuleSettings(b);
    }

    public boolean setRootDirectory(final String path) {
        ProgressManager.getInstance().run(new Task.Modal(
            getCurrentProject(),
            HybrisI18NBundleUtils.message("hybris.project.import.scanning"), true) {

            public void run(@NotNull final ProgressIndicator progressIndicator) {
                final List<String> roots = new ArrayList<String>();
                HybrisProjectFinderUtils.findModuleRoots(roots, path, new Processor<String>() {

                    @Override
                    public boolean process(final String t) {
                        final ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();

                        if (null != indicator) {
                            if (indicator.isCanceled()) {
                                return false;
                            }

                            indicator.setText2(t);
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

                getProjectImportParameters().setWorkspace(roots);
                getProjectImportParameters().setRoot(path);
            }

            public void onCancel() {
                getProjectImportParameters().setWorkspace(null);
                getProjectImportParameters().setRoot(null);
            }
        });

        this.setFileToImport(path);

        return null != this.getProjectImportParameters().getWorkspace();
    }

    private static class GetFileNameFunction implements Function<File, String> {

        public String fun(final File param) {
            return param.getPath();
        }
    }
}
