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

import com.intellij.icons.AllIcons;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.idea.plugin.hybris.utils.HybrisI18NBundleUtils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * Created 2:12 PM 28 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class JunkProjectViewNode extends ProjectViewNode<List<AbstractTreeNode>> {

    public JunkProjectViewNode(final Project project,
                               final List<AbstractTreeNode> children,
                               final ViewSettings settings) {
        super(project, children, settings);
    }

    @Override
    @NotNull
    public Collection<AbstractTreeNode> getChildren() {
        return this.getValue();
    }

    @Override
    public boolean contains(@NotNull final VirtualFile file) {
        for (AbstractTreeNode abstractTreeNode : this.getValue()) {
            if (abstractTreeNode instanceof ProjectViewNode) {
                final ProjectViewNode projectViewNode = (ProjectViewNode) abstractTreeNode;

                return null != projectViewNode.getVirtualFile() && projectViewNode.getVirtualFile().equals(file);
            }
        }

        return false;
    }

    @Override
    public void update(final PresentationData presentation) {
        presentation.setIcon(AllIcons.Modules.ExcludedGeneratedRoot);
        presentation.addText(new ColoredFragment(
            HybrisI18NBundleUtils.message("hybris.project.view.junk.directory.name"),
            SimpleTextAttributes.EXCLUDED_ATTRIBUTES
        ));
    }
}
