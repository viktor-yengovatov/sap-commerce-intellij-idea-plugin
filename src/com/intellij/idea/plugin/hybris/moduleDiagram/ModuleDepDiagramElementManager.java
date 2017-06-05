package com.intellij.idea.plugin.hybris.moduleDiagram;

import com.intellij.diagram.AbstractDiagramElementManager;
import com.intellij.diagram.presentation.DiagramState;
import com.intellij.idea.plugin.hybris.actions.ActionUtils;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.ui.SimpleColoredText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Eugene.Kudelevsky
 */
@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
public class ModuleDepDiagramElementManager extends AbstractDiagramElementManager<ModuleDepDiagramItem> {

    @Nullable
    @Override
    public ModuleDepDiagramItem findInDataContext(final DataContext dataContext) {
        return null;
    }

    @NotNull
    @Override
    public Collection<ModuleDepDiagramItem> findElementsInDataContext(final DataContext context) {
        return new ArrayList<>(  // need to return mutable collection to avoid UOE from the platform
            ActionUtils.isHybrisContext(context)
                ? Collections.singletonList(new ModuleDepDiagramItem(null, false))
                : Collections.emptyList());
    }

    @Override
    public boolean isAcceptableAsNode(final Object element) {
        return element instanceof ModuleDepDiagramItem;
    }

    @Nullable
    @Override
    public String getElementTitle(final ModuleDepDiagramItem element) {
        return element.toString();
    }

    @Nullable
    @Override
    public SimpleColoredText getItemName(final Object element, final DiagramState diagramState) {
        return element instanceof ModuleDepDiagramItem
            ? new SimpleColoredText(((ModuleDepDiagramItem) element).getName(), DEFAULT_TITLE_ATTR)
            : null;
    }

    @Override
    public String getNodeTooltip(final ModuleDepDiagramItem element) {
        return element.getName();
    }
}
