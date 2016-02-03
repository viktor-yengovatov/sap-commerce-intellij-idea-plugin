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

package com.intellij.idea.plugin.hybris.business.process.diagram.impl;

import com.intellij.diagram.DiagramDataModel;
import com.intellij.diagram.DiagramNode;
import com.intellij.diagram.DiagramRelationshipInfoAdapter;
import com.intellij.diagram.presentation.DiagramLineType;
import com.intellij.idea.plugin.hybris.business.process.common.BpGraphNode;
import com.intellij.idea.plugin.hybris.business.process.diagram.BpDiagramProvider;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.ModificationTracker;
import org.jetbrains.annotations.Contract;
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
public final class BpDiagramDataModel extends DiagramDataModel<BpGraphNode> {

    private final Collection<BpDiagramFileEdge> edges = new ArrayList<BpDiagramFileEdge>();
    private final Map<String, BpDiagramFileNode> ids2Nodes = new HashMap<String, BpDiagramFileNode>();

    public BpDiagramDataModel(final Project project, final BpGraphNode bpGraphNode) {
        super(project, ServiceManager.getService(BpDiagramProvider.class));

        for (BpGraphNode graphNode : bpGraphNode.getNodesMap().values()) {
            final BpDiagramFileNode fileNode = new BpDiagramFileNode(graphNode);

            this.ids2Nodes.put(graphNode.getGenericAction().getId(), fileNode);
        }
    }

    @NotNull
    @Override
    public Collection<BpDiagramFileNode> getNodes() {
        return this.ids2Nodes.values();
    }

    @NotNull
    @Override
    public Collection<BpDiagramFileEdge> getEdges() {
        return this.edges;
    }

    @NotNull
    @Override
    public String getNodeName(final DiagramNode<BpGraphNode> diagramNode) {
        return diagramNode.getIdentifyingElement().getGenericAction().getId();
    }

    @Contract(value = "_ -> null", pure = true)
    @Nullable
    @Override
    public BpDiagramFileNode addElement(final BpGraphNode t) {
        return null;
    }

    @Override
    public void refreshDataModel() {
        this.edges.clear();

        for (BpDiagramFileNode node : this.ids2Nodes.values()) {
            final BpGraphNode bpGraphNode = node.getIdentifyingElement();

            for (Map.Entry<String, BpGraphNode> transition : bpGraphNode.getTransitions().entrySet()) {
                final String name = transition.getKey();
                final BpGraphNode graphNode = transition.getValue();

                final BpDiagramFileEdge edge = new BpDiagramFileEdge(
                    node, this.ids2Nodes.get(graphNode.getGenericAction().getId()), new BpDiagramRelationship(name)
                );

                this.edges.add(edge);
            }
        }
    }

    @Contract(pure = true)
    @NotNull
    @Override
    public ModificationTracker getModificationTracker() {
        return this;
    }

    @Override
    public void dispose() {

    }

    protected static class BpDiagramRelationship extends DiagramRelationshipInfoAdapter {

        private final String label;

        public BpDiagramRelationship(final String label) {
            super("SOFT", DiagramLineType.SOLID);
            this.label = label;
        }

        @Override
        public Shape getStartArrow() {
            return STANDARD;
        }

        @Override
        public String getLabel() {
            return label;
        }
    }
}
