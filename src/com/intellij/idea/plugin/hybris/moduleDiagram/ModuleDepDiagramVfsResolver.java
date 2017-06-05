package com.intellij.idea.plugin.hybris.moduleDiagram;

import com.intellij.diagram.DiagramVfsResolver;
import com.intellij.openapi.project.Project;

/**
 * @author Eugene.Kudelevsky
 */
@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
public class ModuleDepDiagramVfsResolver implements DiagramVfsResolver<ModuleDepDiagramItem> {

    @Override
    public String getQualifiedName(final ModuleDepDiagramItem element) {
        return element.getQualifiedName();
    }

    @Override
    public ModuleDepDiagramItem resolveElementByFQN(final String fqn, final Project project) {
        return ModuleDepDiagramItem.fromFQN(fqn, project);
    }
}
