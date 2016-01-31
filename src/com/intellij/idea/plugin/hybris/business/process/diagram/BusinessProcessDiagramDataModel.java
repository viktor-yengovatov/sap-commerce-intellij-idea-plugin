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
import com.intellij.diagram.DiagramRelationshipInfoAdapter;
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
import java.util.List;
import java.util.Map;

/**
 * Created 11:11 PM 31 January 2016.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class BusinessProcessDiagramDataModel extends DiagramDataModel<VirtualFile> {

    private List<BusinessProcessDiagramFileNode> myNodes = new ArrayList<BusinessProcessDiagramFileNode>();
    private List<BusinessProcessDiagramFileEdge> myEdges = new ArrayList<BusinessProcessDiagramFileEdge>();
    private Map<String, BusinessProcessDiagramFileNode> path2Node = new HashMap<String, BusinessProcessDiagramFileNode>(myNodes.size());

    public BusinessProcessDiagramDataModel(Project project, VirtualFile file) {
        super(project, BusinessProcessDiagramProvider.getInstance());
        VirtualFile f = file;
        while (f != null) {
            final BusinessProcessDiagramFileNode n = new BusinessProcessDiagramFileNode(f);
            myNodes.add(n);
            path2Node.put(f.getPath(), n);
            f = f.getParent();
        }
        refreshDataModel();
    }

    @NotNull
    @Override
    public Collection<BusinessProcessDiagramFileNode> getNodes() {
        return myNodes;
    }

    @NotNull
    @Override
    public Collection<BusinessProcessDiagramFileEdge> getEdges() {
        return myEdges;
    }

    @NotNull
    @Override
    public String getNodeName(DiagramNode<VirtualFile> node) {
        return node.getIdentifyingElement().getName();
    }

    @Nullable
    @Override
    public BusinessProcessDiagramFileNode addElement(VirtualFile file) {
        BusinessProcessDiagramFileNode node = path2Node.get(file.getPath());
        if (node == null) {
            node = new BusinessProcessDiagramFileNode(file);
            path2Node.put(file.getPath(), node);
            myNodes.add(node);
        }
        return node;
    }

    @Override
    public void refreshDataModel() {
        myEdges.clear();

        for (BusinessProcessDiagramFileNode node : myNodes) {
            VirtualFile f = node.getIdentifyingElement().getParent();
            int i = 1;
            while (f != null) {
                final BusinessProcessDiagramFileNode n = path2Node.get(f.getPath());
                if (n != null) {
                    final int level = i;
                    DiagramRelationshipInfo r = level == 1 ?
                        BusinessProcessDiagramRelationships.STRONG : new DiagramRelationshipInfoAdapter("SOFT", DiagramLineType.DASHED) {
                        @Override
                        public Shape getStartArrow() {
                            return STANDARD;
                        }

                        @Override
                        public String getLabel() {
                            return "   " + String.valueOf(level);
                        }
                    };
                    myEdges.add(new BusinessProcessDiagramFileEdge(node, n, r));
                    f = null;
                } else {
                    f = f.getParent();
                    i++;
                }
            }
        }
    }

    @Override
    public void removeNode(DiagramNode<VirtualFile> node) {
        myNodes.remove(node);
        path2Node.remove(node.getIdentifyingElement().getPath());
        refreshDataModel();
    }

    @NotNull
    @Override
    public ModificationTracker getModificationTracker() {
        return VirtualFileManager.getInstance();
    }

    @Override
    public void dispose() {

    }
}
