package com.intellij.idea.plugin.hybris.project;

import com.intellij.idea.plugin.hybris.project.settings.HybrisProjectImportParameters;
import com.intellij.idea.plugin.hybris.project.tasks.SearchProjectRootsTaskModalWindow;
import com.intellij.idea.plugin.hybris.project.utils.HybrisProjectUtils;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import com.intellij.idea.plugin.hybris.utils.HybrisI18NBundleUtils;
import com.intellij.idea.plugin.hybris.utils.HybrisIconsUtils;
import com.intellij.idea.plugin.hybris.utils.VirtualFileSystemUtils;
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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.packaging.artifacts.ModifiableArtifactModel;
import com.intellij.util.Function;
import gnu.trove.THashSet;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
        this.getProjectImportParameters().setProjectsPathsToConvert(list);
    }

    @Override
    public boolean isMarked(final String element) {
        final Set<String> existingModuleNames = this.getProjectImportParameters().getExistingModuleNames();

        return null != existingModuleNames && !existingModuleNames.contains(
            HybrisProjectUtils.findProjectName(element)
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

        final List<String> projectsPathsToConvert = this.getProjectImportParameters().getProjectsPathsToConvert();

        if (null == projectsPathsToConvert || projectsPathsToConvert.isEmpty()) {
            return null;
        }

        try {
            final Collection<File> alreadyExistingModuleFiles = HybrisProjectUtils.findAlreadyExistingModuleFiles(
                projectsPathsToConvert
            );

            if (this.shouldRemoveAlreadyExistingModuleFiles(alreadyExistingModuleFiles)) {
                VirtualFileSystemUtils.removeAllFiles(alreadyExistingModuleFiles);
            }

            final ModifiableModuleModel modifiableModuleModel = (null != model)
                ? model
                : ModuleManager.getInstance(project).getModifiableModel();

            final Collection<ModifiableRootModel> modifiableRootModels = new ArrayList<ModifiableRootModel>();

            for (String projectPath : projectsPathsToConvert) {

                String projectRootDirectory = this.getProjectImportParameters().getCommonModulesDirectory();
                if (null == projectRootDirectory) {
                    projectRootDirectory = projectPath;
                }

                final Module javaModule = modifiableModuleModel.newModule(
                    projectRootDirectory + '/' + HybrisProjectUtils.findProjectName(projectPath) + HybrisConstants.NEW_MODULE_FILE_EXTENSION,
                    StdModuleTypes.JAVA.getId()
                );

                result.add(javaModule);

                final ModifiableRootModel modifiableRootModel = ModuleRootManager.getInstance(javaModule).getModifiableModel();
                modifiableRootModels.add(modifiableRootModel);

                // TODO: Add here logic for configuring project structure and dependencies
                modifiableRootModel.addContentEntry(VfsUtilCore.pathToUrl(projectPath));

                ClasspathStorage.setStorageType(modifiableRootModel, ClassPathStorageUtil.DEFAULT_STORAGE);

                if (null != model) {
                    ApplicationManager.getApplication().runWriteAction(new Runnable() {
                        @Override
                        public void run() {
                            modifiableRootModel.commit();
                        }
                    });
                }
            }

            if (null == model) {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    @Override
                    public void run() {
                        ModifiableModelCommitter.multiCommit(modifiableRootModels, modifiableModuleModel);
                    }
                });
            }
        } catch (Exception e) {
            LOG.error("Can not import Hybris project(s).", e);
        }

        return result;
    }

    protected boolean shouldRemoveAlreadyExistingModuleFiles(@NotNull final Collection<File> files) {
        Validate.notNull(files);

        if (files.isEmpty()) {
            return false;
        }

        final int resultCode = Messages.showYesNoCancelDialog(
            String.format(
                "%s module files found:\n%s.\n Would you like to reuse them?",
                ApplicationInfoEx.getInstanceEx().getFullApplicationName(),
                StringUtil.join(files, new GetFileNameFunction(), "\n")
            ),
            "Module Files Found",
            Messages.getQuestionIcon()
        );

        return (Messages.YES != resultCode) && (Messages.NO == resultCode);
    }

    protected static class GetFileNameFunction implements Function<File, String> {

        @Override
        public String fun(final File param) {
            return param.getPath();
        }
    }
}
