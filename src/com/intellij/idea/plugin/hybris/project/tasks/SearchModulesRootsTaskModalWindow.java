/*
 * Copyright 2015 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.idea.plugin.hybris.project.tasks;

import com.intellij.idea.plugin.hybris.project.settings.HybrisProjectDescriptor;
import com.intellij.idea.plugin.hybris.project.utils.Processor;
import com.intellij.idea.plugin.hybris.utils.HybrisI18NBundleUtils;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.ui.Messages;
import com.intellij.projectImport.ProjectImportBuilder;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

/**
 * Created 6:07 PM 13 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class SearchModulesRootsTaskModalWindow extends Task.Modal {

    protected final File rootProjectDirectory;
    protected final HybrisProjectDescriptor projectImportParameters;

    public SearchModulesRootsTaskModalWindow(
        @NotNull final File rootProjectDirectory,
        @NotNull final HybrisProjectDescriptor projectImportParameters
    ) {
        super(
            ProjectImportBuilder.getCurrentProject(),
            HybrisI18NBundleUtils.message("hybris.project.import.scanning"),
            true
        );

        Validate.notNull(rootProjectDirectory);
        Validate.notNull(projectImportParameters);

        this.rootProjectDirectory = rootProjectDirectory;
        this.projectImportParameters = projectImportParameters;
    }

    @Override
    public void run(@NotNull final ProgressIndicator indicator) {
        Validate.notNull(indicator);

        this.projectImportParameters.setRootDirectoryAndScanForModules(
            this.rootProjectDirectory,
            new ProgressIndicatorUpdaterProcessor(indicator),
            new ModuleScanErrorsProcessor()
        );
    }

    @Override
    public void onCancel() {
        this.projectImportParameters.setRootDirectoryAndScanForModules(null, null, null);
    }

    protected class ProgressIndicatorUpdaterProcessor implements Processor<File> {

        protected final ProgressIndicator progressIndicator;

        public ProgressIndicatorUpdaterProcessor(@NotNull final ProgressIndicator indicator) {
            Validate.notNull(indicator);

            this.progressIndicator = indicator;
        }

        @Override
        public boolean shouldContinue(final File t) {
            if (this.progressIndicator.isCanceled()) {
                return false;
            }

            this.progressIndicator.setText2(t.getAbsolutePath());

            return true;
        }
    }

    protected class ModuleScanErrorsProcessor implements Processor<List<File>> {

        @Override
        public boolean shouldContinue(final List<File> t) {
            if (!t.isEmpty()) {
                ApplicationManager.getApplication().invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Messages.showErrorDialog(
                            HybrisI18NBundleUtils.message("hybris.project.import.failed", t),
                            HybrisI18NBundleUtils.message("hybris.project.error")
                        );
                    }
                });
            }

            return false;
        }
    }
}