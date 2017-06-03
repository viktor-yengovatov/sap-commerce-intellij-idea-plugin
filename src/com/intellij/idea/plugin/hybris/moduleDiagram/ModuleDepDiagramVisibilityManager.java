package com.intellij.idea.plugin.hybris.moduleDiagram;

import com.intellij.diagram.AbstractUmlVisibilityManager;
import com.intellij.diagram.VisibilityLevel;
import com.intellij.openapi.util.Comparing;
import com.intellij.util.ArrayUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

/**
 * @author Eugene.Kudelevsky
 */
public class ModuleDepDiagramVisibilityManager extends AbstractUmlVisibilityManager {

    public static final VisibilityLevel SMALL = new VisibilityLevel("Small");
    public static final VisibilityLevel MEDIUM = new VisibilityLevel("Medium");
    public static final VisibilityLevel LARGE = new VisibilityLevel("Large");

    private static final VisibilityLevel[] LEVELS = {SMALL, MEDIUM, LARGE};

    private final Comparator<VisibilityLevel> COMPARATOR = (level1, level2) ->
        Comparing.compare(ArrayUtil.indexOf(LEVELS, level1), ArrayUtil.indexOf(LEVELS, level2));

    public ModuleDepDiagramVisibilityManager() {
        setCurrentVisibilityLevel(SMALL);
    }

    @Override
    public VisibilityLevel[] getVisibilityLevels() {
        return LEVELS;
    }

    @Nullable
    @Override
    public VisibilityLevel getVisibilityLevel(final Object o) {
        return null;
    }

    @Override
    public Comparator<VisibilityLevel> getComparator() {
        return COMPARATOR;
    }

    @Override
    public boolean isRelayoutNeeded() {
        return true;
    }
}
