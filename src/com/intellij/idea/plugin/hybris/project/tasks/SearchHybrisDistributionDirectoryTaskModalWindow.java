/*
 * This file is part of "Hybris Integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2015 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.intellij.idea.plugin.hybris.project.tasks;

import com.intellij.idea.plugin.hybris.project.settings.HybrisProjectDescriptor;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import com.intellij.idea.plugin.hybris.utils.HybrisI18NBundleUtils;
import com.intellij.idea.plugin.hybris.utils.VirtualFileSystemUtils;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.projectImport.ProjectImportBuilder;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created 6:07 PM 13 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class SearchHybrisDistributionDirectoryTaskModalWindow extends Task.Modal {

    private static final Logger LOG = Logger.getInstance(SearchHybrisDistributionDirectoryTaskModalWindow.class);

    protected final File rootProjectDirectory;
    protected final HybrisProjectDescriptor projectImportParameters;

    public SearchHybrisDistributionDirectoryTaskModalWindow(
        @NotNull final File rootProjectDirectory,
        @NotNull final HybrisProjectDescriptor projectImportParameters
    ) {
        super(
            ProjectImportBuilder.getCurrentProject(),
            HybrisI18NBundleUtils.message("hybris.project.import.searching.hybris.distribution"),
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

        final File hybrisServerShellScriptFile;
        try {
            hybrisServerShellScriptFile = VirtualFileSystemUtils.findFileByNameInDirectory(
                rootProjectDirectory,
                HybrisConstants.HYBRIS_SERVER_SHELL_SCRIPT_NAME,
                new DirectoriesScannerProgressIndicatorUpdaterProcessor(indicator)
            );
        } catch (InterruptedException e) {
            LOG.warn(e);
            return;
        }

        if (null != hybrisServerShellScriptFile
            && null != hybrisServerShellScriptFile.getParentFile()
            && null != hybrisServerShellScriptFile.getParentFile().getParentFile()) {

            this.projectImportParameters.setHybrisDistributionDirectory(
                hybrisServerShellScriptFile.getParentFile().getParentFile().getParentFile()
            );
        }
    }

    @Override
    public void onCancel() {
        this.projectImportParameters.clear();
    }
}