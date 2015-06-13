package com.intellij.idea.plugin.hybris.project;

import com.intellij.idea.plugin.hybris.project.settings.HybrisProjectImportParameters;
import com.intellij.idea.plugin.hybris.project.tasks.SearchProjectRootsTaskModalWindow;
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
import com.intellij.openapi.progress.ProgressManager;
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
import com.intellij.util.Function;
import gnu.trove.THashSet;
import org.apache.commons.lang.Validate;
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
public class DefaultHybrisProjectImportBuilder extends AbstractHybrisProjectImportBuilder {

    private static final Logger LOG = Logger.getInstance(DefaultHybrisProjectImportBuilder.class.getName());

    @Nullable
    protected HybrisProjectImportParameters projectImportParameters;

    @Override
    public void setRootDirectory(@NotNull final String path) {
        Validate.notEmpty(path);

        ProgressManager.getInstance().run(new SearchProjectRootsTaskModalWindow(
            path, this.getProjectImportParameters()
        ));

        this.setFileToImport(path);
    }

    @Override
    public boolean isRootDirectorySet() {
        final List<String> workspace = this.getProjectImportParameters().getWorkspace();

        return null != workspace && !workspace.isEmpty();
    }

    @NotNull
    @Override
    public HybrisProjectImportParameters getProjectImportParameters() {
        if (null == this.projectImportParameters) {

            final Set<String> existingModuleNames = new THashSet<String>();

            if (this.isUpdate()) {
                final Project project = getCurrentProject();

                if (null != project) {
                    for (Module module : ModuleManager.getInstance(project).getModules()) {
                        existingModuleNames.add(module.getName());
                    }
                }
            }

            this.projectImportParameters = new HybrisProjectImportParameters();
            this.projectImportParameters.setExistingModuleNames(existingModuleNames);
        }

        return this.projectImportParameters;
    }

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

    @Override
    public boolean isMarked(final String element) {
        final Set<String> existingModuleNames = this.getProjectImportParameters().getExistingModuleNames();

        return null != existingModuleNames && !existingModuleNames.contains(
            HybrisProjectFinderUtils.findProjectName(element)
        );
    }

    @Override
    public void cleanup() {
        super.cleanup();
        this.projectImportParameters = null;
    }

    @Override
    public boolean isOpenProjectSettingsAfter() {
        return this.getProjectImportParameters().isOpenModuleSettings();
    }

    @Override
    public void setOpenProjectSettingsAfter(final boolean on) {
        this.getProjectImportParameters().setOpenModuleSettings(on);
    }

    @Nullable
    @Override
    public List<Module> commit(final Project project,
                               final ModifiableModuleModel model,
                               final ModulesProvider modulesProvider,
                               final ModifiableArtifactModel artifactModel) {

        final List<Module> result = new ArrayList<Module>();

        final List<String> projectsToConvert = this.getProjectImportParameters().getProjectsToConvert();

        if (null == projectsToConvert || projectsToConvert.isEmpty()) {
            return null;
        }

        try {
            final ModifiableModuleModel moduleModel = (null != model)
                                                      ? model
                                                      : ModuleManager.getInstance(project).getModifiableModel();

            final ModifiableRootModel[] rootModels = new ModifiableRootModel[projectsToConvert.size()];
            final Collection<File> files = new HashSet<File>();

            for (String path : projectsToConvert) {

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

            for (String path : projectsToConvert) {

                String modulesDirectory = this.getProjectImportParameters().getCommonModulesDirectory();
                if (null == modulesDirectory) {
                    modulesDirectory = path;
                }

                final Module module = moduleModel.newModule(
                    modulesDirectory + '/' + HybrisProjectFinderUtils.findProjectName(path) + ".iml",
                    StdModuleTypes.JAVA.getId()
                );

                result.add(module);

                final ModifiableRootModel rootModel = ModuleRootManager.getInstance(module).getModifiableModel();
                rootModels[idx++] = rootModel;

                rootModel.addContentEntry(VfsUtilCore.pathToUrl(path));

                ClasspathStorage.setStorageType(rootModel, ClassPathStorageUtil.DEFAULT_STORAGE);

                if (null != model) {
                    ApplicationManager.getApplication().runWriteAction(new Runnable() {
                        @Override
                        public void run() {
                            rootModel.commit();
                        }
                    });
                }
            }

            if (null == model) {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    @Override
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

    protected static class GetFileNameFunction implements Function<File, String> {

        @Override
        public String fun(final File param) {
            return param.getPath();
        }
    }
}
