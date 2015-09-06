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

package com.intellij.idea.plugin.hybris.view;

import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.BasePsiNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.idea.plugin.hybris.settings.HybrisApplicationSettingsComponent;
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettings;
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettingsComponent;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created 10:14 PM 27 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisProjectView implements TreeStructureProvider, DumbAware {

    protected final Project project;
    protected final HybrisProjectSettings hybrisProjectSettings;
    private List<String> junkFileNames;

    public HybrisProjectView(final Project project) {
        this.project = project;
        this.hybrisProjectSettings = HybrisProjectSettingsComponent.getInstance(project).getState();
    }

    @Override
    @NotNull
    public Collection<AbstractTreeNode> modify(@NotNull final AbstractTreeNode parent,
                                               @NotNull final Collection<AbstractTreeNode> children,
                                               final ViewSettings settings) {
        if (this.isNotHybrisProject()) {
            return children;
        }

        if (parent instanceof JunkProjectViewNode) {
            return children;
        }

        junkFileNames = getJunkFileNames();

        if (junkFileNames == null || junkFileNames.isEmpty()) {
            return children;
        }

        final List<AbstractTreeNode> junkTreeNodes = new ArrayList<AbstractTreeNode>();
        final Collection<AbstractTreeNode> treeNodes = new ArrayList<AbstractTreeNode>();

        for (AbstractTreeNode child : children) {
            if (child instanceof BasePsiNode) {
                final VirtualFile virtualFile = ((BasePsiNode) child).getVirtualFile();

                if (null == virtualFile) {
                    continue;
                }

                if (this.isJunk(virtualFile)) {
                    junkTreeNodes.add(child);
                } else {
                    treeNodes.add(child);
                }

            } else {
                treeNodes.add(child);
            }
        }

        if (!junkTreeNodes.isEmpty()) {
            treeNodes.add(new JunkProjectViewNode(this.project, junkTreeNodes, settings));
        }

        return treeNodes;
    }

    protected boolean isNotHybrisProject() {
        return null != this.hybrisProjectSettings && !this.hybrisProjectSettings.isHybisProject();
    }

    protected boolean isJunk(@NotNull final VirtualFile virtualFile) {
        Validate.notNull(virtualFile);
        return this.junkFileNames.contains(virtualFile.getName()) || this.isIdeaModuleFile(virtualFile);
    }

    protected boolean isIdeaModuleFile(@NotNull final VirtualFile virtualFile) {
        Validate.notNull(virtualFile);

        return virtualFile.getName().endsWith(HybrisConstants.NEW_IDEA_MODULE_FILE_EXTENSION);
    }

    protected List<String> getJunkFileNames() {
        return HybrisApplicationSettingsComponent.getInstance().getState().getJunkDirectoryList();
    }

    @Override
    public Object getData(final Collection<AbstractTreeNode> selected, final String dataName) {
        return null;
    }
}