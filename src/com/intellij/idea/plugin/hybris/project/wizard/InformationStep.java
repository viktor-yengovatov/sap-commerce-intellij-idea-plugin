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

import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.projectImport.ProjectImportWizardStep;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 6/5/17.
 */
public class InformationStep extends ProjectImportWizardStep {

    private JPanel rootPanel;
    private JTextPane informationTextPane;
    private JTextPane cvsTextPane;
    private JLabel importLabel;
    private JLabel cvsLabel;
    private JTextPane jrebelTextPane;
    private JLabel jrebelLabel;
    private JLabel jiraLabel;
    private JEditorPane jiraEditorPane;

    public InformationStep(final WizardContext wizardContext) {
        super(wizardContext);
    }

    @Override
    public JComponent getComponent() {
        return rootPanel;
    }

    @Override
    public void updateDataModel() {
    }

    @Override
    public boolean isStepVisible() {
        return true;
    }

    private void createUIComponents() {
        jiraEditorPane = new JEditorPane();
        jiraEditorPane.setEditorKit(new HTMLEditorKit());
        jiraEditorPane.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (IOException | URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
}
