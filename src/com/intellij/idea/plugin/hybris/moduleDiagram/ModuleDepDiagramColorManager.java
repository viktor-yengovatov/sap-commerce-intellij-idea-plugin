package com.intellij.idea.plugin.hybris.moduleDiagram;

import com.intellij.diagram.DiagramBuilder;
import com.intellij.diagram.DiagramColorManagerBase;
import com.intellij.diagram.DiagramNode;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * @author Eugene.Kudelevsky
 */
public class ModuleDepDiagramColorManager extends DiagramColorManagerBase {

    private static final JBColor NON_CUSTOM_EXTENSIONS_COLOR = new JBColor(
        new Color(201, 228, 238),
        new Color(49, 66, 90)
    );

    @Override
    public Color getNodeHeaderColor(final DiagramBuilder builder, @Nullable final DiagramNode node) {
        if (node instanceof ModuleDepDiagramNode &&
            !((ModuleDepDiagramNode) node).getIdentifyingElement().isCustomExtension()) {
            return NON_CUSTOM_EXTENSIONS_COLOR;
        }
        return super.getNodeHeaderColor(builder, node);
    }
}
