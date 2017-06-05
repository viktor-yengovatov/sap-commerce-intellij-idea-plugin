package com.intellij.idea.plugin.hybris.moduleDiagram;

import com.intellij.diagram.DiagramEdgeBase;
import com.intellij.diagram.DiagramRelationshipInfo;
import org.jetbrains.annotations.NotNull;

/**
 * @author Eugene.Kudelevsky
 */
public class ModuleDepDiagramEdge extends DiagramEdgeBase<ModuleDepDiagramItem> {

    public ModuleDepDiagramEdge(
        @NotNull final ModuleDepDiagramNode from,
        @NotNull final ModuleDepDiagramNode to,
        @NotNull final DiagramRelationshipInfo relationship
        ) {
        super(from, to, relationship);
    }
}
