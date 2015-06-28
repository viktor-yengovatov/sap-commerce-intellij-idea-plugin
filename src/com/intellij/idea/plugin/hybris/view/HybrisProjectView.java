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

package com.intellij.idea.plugin.hybris.view;

import com.google.common.collect.Sets;
import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.BasePsiNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
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
import java.util.Set;

/**
 * Created 10:14 PM 27 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisProjectView implements TreeStructureProvider, DumbAware {

    protected final Project project;
    protected final HybrisProjectSettings hybrisProjectSettings;
    protected final Set<String> junkFileNames = Sets.newHashSet(
        "bin",
        ".classpath",
        ".externalToolBuilders",
        ".settings",
        "eclipsebin",
        "testclasses",
        "addonsrc",
        "commonwebsrc",
        ".project",
        ".ruleset",
        ".springBeans",
        ".pmd",
        ".directory",
        "classes",
        "extensioninfo.xsd",
        "ruleset.xml",
        "beans.xsd",
        "items.xsd"
    );

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

    @Override
    public Object getData(final Collection<AbstractTreeNode> selected, final String dataName) {
        return null;
    }
}