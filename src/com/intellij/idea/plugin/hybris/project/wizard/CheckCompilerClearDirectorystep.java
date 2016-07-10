/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2016 Alexander Bartash <AlexanderBartash@gmail.com>
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

package com.intellij.idea.plugin.hybris.project.wizard;

import com.intellij.compiler.CompilerWorkspaceConfiguration;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.projectImport.ProjectImportWizardStep;

import javax.swing.*;

/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 23/06/2016.
 */
public class CheckCompilerClearDirectoryStep extends ProjectImportWizardStep {

    private JPanel rootPanel;
    private JLabel warningLabel;
    private JTextPane descriptionTextPane;
    private JLabel actionLabel;
    private JLabel screenShotLabel;

    public CheckCompilerClearDirectoryStep(final WizardContext context) {
        super(context);
    }

    @Override
    public JComponent getComponent() {
        return this.rootPanel;
    }

    @Override
    public void updateDataModel() {
        final Project project = ProjectManager.getInstance().getDefaultProject();
        CompilerWorkspaceConfiguration.getInstance(project).CLEAR_OUTPUT_DIRECTORY = false;
    }

    @Override
    public boolean isStepVisible() {
        final Project project = ProjectManager.getInstance().getDefaultProject();
        return CompilerWorkspaceConfiguration.getInstance(project).CLEAR_OUTPUT_DIRECTORY;
    }
}
