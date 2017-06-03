package com.intellij.idea.plugin.hybris.moduleDiagram;

import com.intellij.diagram.DiagramNodeBase;
import com.intellij.diagram.DiagramProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Eugene.Kudelevsky
 */
public class ModuleDepDiagramNode extends DiagramNodeBase<ModuleDepDiagramItem> {

    @NotNull private final ModuleDepDiagramItem myItem;

    public ModuleDepDiagramNode(
        @NotNull final ModuleDepDiagramItem item,
        @NotNull final DiagramProvider<ModuleDepDiagramItem> provider
    ) {
        super(provider);
        myItem = item;
    }

    @Override
    public String getTooltip() {
        return myItem.getName();
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    @NotNull
    public ModuleDepDiagramItem getIdentifyingElement() {
        return myItem;
    }
}
