package com.intellij.idea.plugin.hybris.moduleDiagram;

import com.intellij.diagram.AbstractDiagramNodeContentManager;
import com.intellij.diagram.DiagramCategory;
import com.intellij.diagram.presentation.DiagramState;

/**
 * @author Eugene.Kudelevsky
 */
public class ModuleDepDiagramNodeContentManager extends AbstractDiagramNodeContentManager {

    @Override
    public boolean isInCategory(
        final Object o,
        final DiagramCategory diagramCategory,
        final DiagramState diagramState
    ) {
        return false;
    }

    @Override
    public DiagramCategory[] getContentCategories() {
        return DiagramCategory.EMPTY_ARRAY;
    }
}
