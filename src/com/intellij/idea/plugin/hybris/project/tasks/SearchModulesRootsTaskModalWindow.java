package com.intellij.idea.plugin.hybris.project.tasks;

import com.intellij.idea.plugin.hybris.project.settings.HybrisProjectImportParameters;
import com.intellij.idea.plugin.hybris.project.utils.HybrisProjectUtils;
import com.intellij.idea.plugin.hybris.project.utils.ProjectRootsComparator;
import com.intellij.idea.plugin.hybris.utils.HybrisI18NBundleUtils;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.projectImport.ProjectImportBuilder;
import com.intellij.util.Processor;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Created 6:07 PM 13 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class SearchModulesRootsTaskModalWindow extends Task.Modal {

    protected final String rootProjectAbsolutePath;
    protected final HybrisProjectImportParameters projectImportParameters;

    public SearchModulesRootsTaskModalWindow(
        @NotNull final String rootProjectAbsolutePath,
        @NotNull final HybrisProjectImportParameters projectImportParameters
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

        final List<String> roots = HybrisProjectUtils.findModuleRoots(
            this.rootProjectAbsolutePath, new ProgressIndicatorUpdaterProcessor(indicator)
        );

        Collections.sort(roots, new ProjectRootsComparator());

        this.projectImportParameters.setFoundModulesRootsAbsolutePaths(roots);
        this.projectImportParameters.setRootProjectAbsolutePath(this.rootProjectAbsolutePath);
    }

    @Override
    public void onCancel() {
        this.projectImportParameters.setFoundModulesRootsAbsolutePaths(null);
        this.projectImportParameters.setRootProjectAbsolutePath(null);
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