package com.intellij.idea.plugin.hybris.project.tasks;

import com.intellij.idea.plugin.hybris.project.exceptions.HybrisConfigurationException;
import com.intellij.idea.plugin.hybris.project.settings.DefaultHybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.settings.HybrisImportParameters;
import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.project.utils.HybrisProjectUtils;
import com.intellij.idea.plugin.hybris.utils.HybrisI18NBundleUtils;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.ui.Messages;
import com.intellij.projectImport.ProjectImportBuilder;
import com.intellij.util.Processor;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created 6:07 PM 13 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class SearchModulesRootsTaskModalWindow extends Task.Modal {

    private static final Logger LOG = Logger.getInstance(SearchModulesRootsTaskModalWindow.class.getName());

    protected final String rootProjectAbsolutePath;
    protected final HybrisImportParameters projectImportParameters;

    public SearchModulesRootsTaskModalWindow(
        @NotNull final String rootProjectAbsolutePath,
        @NotNull final HybrisImportParameters projectImportParameters
    ) {
        super(
            ProjectImportBuilder.getCurrentProject(),
            HybrisI18NBundleUtils.message("hybris.project.import.scanning"),
            true
        );

        Validate.notEmpty(rootProjectAbsolutePath);
        Validate.notNull(projectImportParameters);

        this.rootProjectAbsolutePath = rootProjectAbsolutePath;
        this.projectImportParameters = projectImportParameters;
    }

    @Override
    public void run(@NotNull final ProgressIndicator indicator) {
        Validate.notNull(indicator);

        this.projectImportParameters.getFoundModules().clear();
        this.projectImportParameters.setRootDirectory(null);

        final List<String> moduleRootAbsolutePaths = HybrisProjectUtils.findModuleRoots(
            this.rootProjectAbsolutePath, new ProgressIndicatorUpdaterProcessor(indicator)
        );

        final List<HybrisModuleDescriptor> moduleDescriptors = new ArrayList<HybrisModuleDescriptor>();
        final Collection<String> pathsFailedToImport = new ArrayList<String>();

        for (String moduleRootAbsolutePath : moduleRootAbsolutePaths) {
            try {
                moduleDescriptors.add(new DefaultHybrisModuleDescriptor(moduleRootAbsolutePath));
            } catch (HybrisConfigurationException e) {
                LOG.error("Can not import a module using path: " + pathsFailedToImport, e);

                pathsFailedToImport.add(moduleRootAbsolutePath);
            }
        }

        if (!pathsFailedToImport.isEmpty()) {
            this.showErrorMessage(pathsFailedToImport);
        }

        Collections.sort(moduleDescriptors);

        this.projectImportParameters.getFoundModules().addAll(moduleDescriptors);
        this.projectImportParameters.setRootDirectory(new File(this.rootProjectAbsolutePath));
    }

    protected void showErrorMessage(@NotNull final Collection<String> pathsFailedToImport) {
        Validate.notNull(pathsFailedToImport);

        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                Messages.showErrorDialog(
                    HybrisI18NBundleUtils.message("hybris.project.import.failed", pathsFailedToImport),
                    HybrisI18NBundleUtils.message("hybris.project.error")
                );
            }
        });
    }

    @Override
    public void onCancel() {
        this.projectImportParameters.getFoundModules().clear();
        this.projectImportParameters.setRootDirectory(null);
    }

    protected class ProgressIndicatorUpdaterProcessor implements Processor<String> {

        protected final ProgressIndicator progressIndicator;

        public ProgressIndicatorUpdaterProcessor(@NotNull final ProgressIndicator indicator) {
            Validate.notNull(indicator);

            this.progressIndicator = indicator;
        }

        @Override
        public boolean process(final String t) {
            if (this.progressIndicator.isCanceled()) {
                return false;
            }

            this.progressIndicator.setText2(t);

            return true;
        }
    }
}