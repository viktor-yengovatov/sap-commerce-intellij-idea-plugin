/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
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

package com.intellij.idea.plugin.hybris.business.process.diagram;

import com.intellij.diagram.DiagramDataModel;
import com.intellij.diagram.DiagramNode;
import com.intellij.diagram.DiagramRelationshipInfo;
import com.intellij.diagram.presentation.DiagramLineType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.ModificationTracker;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created 11:11 PM 31 January 2016.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public final class BusinessProcessDiagramDataModel extends DiagramDataModel<VirtualFile> {

    private Collection<BusinessProcessDiagramFileNode> nodes = new ArrayList<BusinessProcessDiagramFileNode>();
    private Collection<BusinessProcessDiagramFileEdge> edges = new ArrayList<BusinessProcessDiagramFileEdge>();
    private Map<String, BusinessProcessDiagramFileNode> path2Node = new HashMap<String, BusinessProcessDiagramFileNode>(nodes.size());

    public BusinessProcessDiagramDataModel(final Project project, final VirtualFile file) {
        super(project, BusinessProcessDiagramProvider.getInstance());

        VirtualFile currentFile = file;

        while (currentFile != null) {
            final BusinessProcessDiagramFileNode fileNode = new BusinessProcessDiagramFileNode(currentFile);

            this.nodes.add(fileNode);
            this.path2Node.put(currentFile.getPath(), fileNode);

            currentFile = currentFile.getParent();
        }

        this.refreshDataModel();
    }

    @NotNull
    @Override
    public Collection<BusinessProcessDiagramFileNode> getNodes() {
        return nodes;
    }

    @NotNull
    @Override
    public Collection<BusinessProcessDiagramFileEdge> getEdges() {
        return edges;
    }

    @NotNull
    @Override
    public String getNodeName(final DiagramNode<VirtualFile> diagramNode) {
        return diagramNode.getIdentifyingElement().getName();
    }

    @Nullable
    @Override
    public BusinessProcessDiagramFileNode addElement(final VirtualFile t) {
        BusinessProcessDiagramFileNode node = this.path2Node.get(t.getPath());

        if (node == null) {
            node = new BusinessProcessDiagramFileNode(t);
            this.path2Node.put(t.getPath(), node);
            this.nodes.add(node);
        }

        return node;
    }

    @Override
    public void refreshDataModel() {
        edges.clear();

        for (BusinessProcessDiagramFileNode node : this.nodes) {
            VirtualFile virtualFile = node.getIdentifyingElement().getParent();
            int level = 1;

            while (virtualFile != null) {
                final BusinessProcessDiagramFileNode diagramFileNode = path2Node.get(virtualFile.getPath());

                if (diagramFileNode != null) {

                    final DiagramRelationshipInfo relationshipInfo;
                    if (level == 1) {
                        relationshipInfo = BusinessProcessDiagramRelationships.STRONG;
                    } else {
                        relationshipInfo = new DiagramRelationshipInfoAdapter(level);
                    }

                    this.edges.add(new BusinessProcessDiagramFileEdge(node, diagramFileNode, relationshipInfo));
                    virtualFile = null;

                } else {
                    virtualFile = virtualFile.getParent();
                    level++;
                }
            }
        }
    }

    @Override
    public void removeNode(final DiagramNode<VirtualFile> node) {
        this.nodes.remove(node);
        this.path2Node.remove(node.getIdentifyingElement().getPath());

        this.refreshDataModel();
    }

    @NotNull
    @Override
    public ModificationTracker getModificationTracker() {
        return VirtualFileManager.getInstance();
    }

    @Override
    public void dispose() {

    }

    protected static class DiagramRelationshipInfoAdapter extends com.intellij.diagram.DiagramRelationshipInfoAdapter {

        private final int level;

        public DiagramRelationshipInfoAdapter(final int level) {
            super("SOFT", DiagramLineType.DASHED);
            this.level = level;
        }

        @Override
        public Shape getStartArrow() {
            return STANDARD;
        }

        @Override
        public String getLabel() {
            return "   " + String.valueOf(level);
        }
    }
}
