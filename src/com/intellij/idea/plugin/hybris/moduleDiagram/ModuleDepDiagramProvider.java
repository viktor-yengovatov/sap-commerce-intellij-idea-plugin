package com.intellij.idea.plugin.hybris.moduleDiagram;

import com.intellij.diagram.DiagramColorManager;
import com.intellij.diagram.DiagramDataModel;
import com.intellij.diagram.DiagramElementManager;
import com.intellij.diagram.DiagramNodeContentManager;
import com.intellij.diagram.DiagramPresentationModel;
import com.intellij.diagram.DiagramProvider;
import com.intellij.diagram.DiagramRelationshipManager;
import com.intellij.diagram.DiagramVfsResolver;
import com.intellij.diagram.DiagramVisibilityManager;
import com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils;
import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Eugene.Kudelevsky
 */
@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
public class ModuleDepDiagramProvider extends DiagramProvider<ModuleDepDiagramItem> {

    private final ModuleDepDiagramElementManager myElementManager = new ModuleDepDiagramElementManager();
    private final ModuleDepDiagramNodeContentManager myNodeContentManager = new ModuleDepDiagramNodeContentManager();
    private final ModuleDepDiagramVfsResolver myVfsResolver = new ModuleDepDiagramVfsResolver();
    private final ModuleDepDiagramColorManager myColorManager = new ModuleDepDiagramColorManager();

    @Pattern("[a-zA-Z0-9_-]*")
    @Override
    public String getID() {
        return "HybrisModuleDependencies";
    }

    @Override
    public DiagramVisibilityManager createVisibilityManager() {
        return new ModuleDepDiagramVisibilityManager();
    }

    @Override
    public DiagramNodeContentManager getNodeContentManager() {
        return myNodeContentManager;
    }

    @Override
    public DiagramElementManager<ModuleDepDiagramItem> getElementManager() {
        return myElementManager;
    }

    @Override
    public DiagramVfsResolver<ModuleDepDiagramItem> getVfsResolver() {
        return myVfsResolver;
    }

    @Override
    public DiagramColorManager getColorManager() {
        return myColorManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public DiagramRelationshipManager<ModuleDepDiagramItem> getRelationshipManager() {
        return (DiagramRelationshipManager<ModuleDepDiagramItem>) DiagramRelationshipManager.NO_RELATIONSHIP_MANAGER;
    }

    @Override
    public String getPresentableName() {
        return HybrisI18NBundleUtils.message("hybris.module.dependencies.diagram.provider.name");
    }

    @Override
    public Icon getActionIcon(final boolean isPopup) {
        return HybrisIcons.HYBRIS_ICON;
    }

    @Override
    public DiagramDataModel<ModuleDepDiagramItem> createDataModel(
        @NotNull final Project project,
        @Nullable final ModuleDepDiagramItem item,
        @Nullable final VirtualFile file,
        final DiagramPresentationModel model
    ) {
        return new ModuleDepDiagramDataModel(project, this);
    }

}
