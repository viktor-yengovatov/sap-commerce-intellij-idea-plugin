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

package com.intellij.idea.plugin.hybris;

import com.intellij.diagram.*;
import com.intellij.diagram.extras.DiagramExtras;
import com.intellij.diagram.presentation.DiagramLineType;
import com.intellij.diagram.presentation.DiagramState;
import com.intellij.idea.plugin.hybris.business.process.diagram.BpDiagramDataModel;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.ModificationTracker;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiNamedElement;
import com.intellij.ui.SimpleColoredText;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.PlatformIcons;
import com.intellij.util.containers.ContainerUtil;
import gnu.trove.THashMap;
import org.intellij.grammar.KnownAttribute;
import org.intellij.grammar.generator.RuleGraphHelper;
import org.intellij.grammar.psi.BnfFile;
import org.intellij.grammar.psi.BnfRule;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created 1:23 AM 02 February 2016.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class BnfDiagramProvider extends DiagramProvider<PsiNamedElement> {

    public static final String ID = "GRAMMAR";
    private final DiagramVisibilityManager myVisibilityManager = new EmptyDiagramVisibilityManager();

    private static final DiagramCategory[] CATEGORIES = new DiagramCategory[]{new DiagramCategory("Properties", PlatformIcons.PROPERTY_ICON, true)};
    private final DiagramNodeContentManager myNodeContentManager = new MyAbstractDiagramNodeContentManager();
    private final DiagramElementManager myElementManager = new PsiNamedElementAbstractDiagramElementManager();

    private final DiagramVfsResolver myVfsResolver = new PsiNamedElementDiagramVfsResolver();
    private final DiagramRelationshipManager myRelationshipManager = new PsiNamedElementDiagramRelationshipManager();
    private final DiagramExtras myExtras = new DiagramExtras();
    private RuleGraphHelper myGraphHelper;

    public BnfDiagramProvider() {
        myElementManager.setUmlProvider(this);
    }

    @Pattern("[a-zA-Z0-9_-]*")
    @Override
    public String getID() {
        return ID;
    }

    @Override
    public DiagramVisibilityManager createVisibilityManager() {
        return myVisibilityManager;
    }

    @Override
    public DiagramNodeContentManager getNodeContentManager() {
        return myNodeContentManager;
    }

    @Override
    public DiagramElementManager<PsiNamedElement> getElementManager() {
        return myElementManager;
    }

    @Override
    public DiagramVfsResolver<PsiNamedElement> getVfsResolver() {
        return myVfsResolver;
    }

    @Override
    public DiagramRelationshipManager<PsiNamedElement> getRelationshipManager() {
        return myRelationshipManager;
    }

    @Override
    public DiagramDataModel<PsiNamedElement> createDataModel(@NotNull final Project project,
                                                               @Nullable final PsiNamedElement element,
                                                               @Nullable final VirtualFile file,
                                                               DiagramPresentationModel presentationModel) {
        return new MyDataModel(project, (BnfFile) element, this);
    }

    @NotNull
    @Override
    public DiagramExtras getExtras() {
        return myExtras;
    }

    @Override
    public String getActionName(boolean isPopup) {
        return "Visualisation";
    }

    @Override
    public String getPresentableName() {
        return "Grammar Diagrams";
    }

    private static class MyDataModel extends BpDiagramDataModel<PsiNamedElement> implements ModificationTracker {

        private final BnfFile myFile;

        private final Collection<DiagramNode<PsiNamedElement>> myNodes = new HashSet<DiagramNode<PsiNamedElement>>();
        private final Collection<DiagramEdge<PsiNamedElement>> myEdges = new HashSet<DiagramEdge<PsiNamedElement>>();


        MyDataModel(Project project, BnfFile file, BnfDiagramProvider provider) {
            super(project, provider);
            myFile = file;
        }

        @NotNull
        @Override
        public Collection<DiagramNode<PsiNamedElement>> getNodes() {
            return myNodes;
        }

        @NotNull
        @Override
        public Collection<DiagramEdge<PsiNamedElement>> getEdges() {
            return myEdges;
        }

        @NotNull
        @Override
        public String getNodeName(DiagramNode<PsiNamedElement> node) {
            return StringUtil.notNullize(node.getTooltip());
        }

        @Override
        public DiagramNode<PsiNamedElement> addElement(PsiNamedElement psiElement) {
            return null;
        }

        @Override
        public void refreshDataModel() {
            myNodes.clear();
            myEdges.clear();

            RuleGraphHelper ruleGraphHelper = RuleGraphHelper.getCached(myFile);
            ((BnfDiagramProvider) getProvider()).myGraphHelper = ruleGraphHelper;

            Map<BnfRule, DiagramNode<PsiNamedElement>> nodeMap = new THashMap<BnfRule, DiagramNode<PsiNamedElement>>();
            List<BnfRule> rules = myFile.getRules();
            BnfRule root = ContainerUtil.getFirstItem(rules);
            for (BnfRule rule : rules) {
                if (rule != root && !RuleGraphHelper.shouldGeneratePsi(rule, true)) {
                    continue;
                }
                DiagramNode<PsiNamedElement> diagramNode = new PsiDiagramNode<PsiNamedElement>(rule, getProvider()) {
                    @Override
                    public String getTooltip() {
                        return getIdentifyingElement().getName();
                    }
                };
                nodeMap.put(rule, diagramNode);
                myNodes.add(diagramNode);
            }
            for (BnfRule rule : rules) {
                if (rule != root && !RuleGraphHelper.shouldGeneratePsi(rule, true)) {
                    continue;
                }
                Map<PsiElement, RuleGraphHelper.Cardinality> map = ruleGraphHelper.getFor(rule);

                BnfRule superRule = myFile.getRule(getAttribute(rule, KnownAttribute.EXTENDS));
                if (superRule != null) {
                    DiagramNode<PsiNamedElement> source = nodeMap.get(rule);
                    DiagramNode<PsiNamedElement> target = nodeMap.get(superRule);

                    myEdges.add(new DiagramEdgeBase<PsiNamedElement>(source, target, new DiagramRelationshipInfoAdapter("EXTENDS", DiagramLineType.DASHED, "extends") {

                        public Shape getStartArrow() {
                            return DELTA;
                        }
                    }) {

                        @Override
                        public String getName() {
                            return "";
                        }
                    });
                }
                for (final PsiElement element : map.keySet()) {
                    if (!(element instanceof BnfRule)) {
                        continue;
                    }
                    final RuleGraphHelper.Cardinality cardinality = map.get(element);
                    assert cardinality != RuleGraphHelper.Cardinality.NONE;

                    DiagramNode<PsiNamedElement> source = nodeMap.get(rule);
                    DiagramNode<PsiNamedElement> target = nodeMap.get(element);
                    myEdges.add(new DiagramEdgeBase<PsiNamedElement>(source, target, new DiagramRelationshipInfoAdapter("CONTAINS", DiagramLineType.SOLID, "") {
                        @Override
                        public String getLabel() {
                            return cardinality.name().toLowerCase();
                        }

                        public Shape getStartArrow() {
                            return DELTA;
                        }
                    }) {

                        @Override
                        public Object getSourceAnchor() {
                            return element;
                        }

                        @Override
                        public Object getTargetAnchor() {
                            return element;
                        }

                        @Override
                        public Color getAnchorColor() {
                            return Color.BLUE;
                        }

                        @Override
                        public String getName() {
                            return "";
                        }
                    });
                }
            }
        }

        @NotNull
        @Override
        public ModificationTracker getModificationTracker() {
            return this;
        }

        @Override
        public void dispose() {
        }

        @Override
        public long getModificationCount() {
            return myFile.getModificationStamp();
        }
    }

    private static class MyAbstractDiagramNodeContentManager extends AbstractDiagramNodeContentManager {

        @Override
        public boolean isInCategory(Object o, DiagramCategory diagramCategory, DiagramState diagramState) {
            return true;
        }

        @Override
        public DiagramCategory[] getContentCategories() {
            return CATEGORIES;
        }
    }

    private static class PsiNamedElementDiagramRelationshipManager implements DiagramRelationshipManager<PsiNamedElement> {

        @Override
        public DiagramRelationshipInfo getDependencyInfo(PsiNamedElement e1, PsiNamedElement e2, DiagramCategory diagramCategory) {
            return null;
        }

        @Override
        public DiagramCategory[] getContentCategories() {
            return CATEGORIES;
        }
    }

    private static class PsiNamedElementDiagramVfsResolver implements DiagramVfsResolver<PsiNamedElement> {

        @Override
        public String getQualifiedName(PsiNamedElement bnfFile) {
            VirtualFile virtualFile = bnfFile instanceof PsiFile ? ((PsiFile) bnfFile).getVirtualFile() : null;
            return virtualFile == null ? null : virtualFile.getUrl();
        }

        @Override
        public PsiNamedElement resolveElementByFQN(String s, Project project) {
            VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl(s);
            PsiFile psiFile = virtualFile == null ? null : PsiManager.getInstance(project).findFile(virtualFile);
            return psiFile instanceof BnfFile ? psiFile : null;
        }

    }

    private class PsiNamedElementAbstractDiagramElementManager extends AbstractDiagramElementManager<PsiNamedElement> {

        @Override
        public PsiNamedElement findInDataContext(DataContext context) {
            PsiFile file = LangDataKeys.PSI_FILE.getData(context);
            return file instanceof BnfFile ? file : null;
        }

        @Override
        public boolean isAcceptableAsNode(Object o) {
            return o instanceof PsiNamedElement;
        }

        @Override
        public String getElementTitle(PsiNamedElement bnfFile) {
            return bnfFile.getName();
        }

        @Override
        public SimpleColoredText getItemName(Object o, DiagramState diagramState) {
            if (o instanceof Map.Entry) {
                o = ((Map.Entry) o).getKey();
            }
            if (o instanceof PsiNamedElement) {
                return new SimpleColoredText(StringUtil.notNullize(((PsiNamedElement) o).getName()), DEFAULT_TITLE_ATTR);
            }
            return null;
        }

        @Override
        public Object[] getNodeItems(PsiNamedElement parent) {
            if (parent instanceof BnfRule) {
                Map<PsiElement, RuleGraphHelper.Cardinality> map = myGraphHelper.getFor((BnfRule) parent);
                Object[] objects = ContainerUtil.findAll(map.entrySet(), new Condition<Map.Entry<PsiElement, RuleGraphHelper.Cardinality>>() {
                    @Override
                    public boolean value(Map.Entry<PsiElement, RuleGraphHelper.Cardinality> p) {
                        return p.getKey() instanceof BnfRule;
                    }
                }).toArray();
                Arrays.sort(objects, new Comparator<Object>() {
                    @Override
                    public int compare(Object o, Object o1) {
                        return Comparing.compare(((Map.Entry<BnfRule, ?>) o).getKey().getName(), ((Map.Entry<BnfRule, ?>) o1).getKey().getName());
                    }
                });
                return objects;
            }
            return super.getNodeItems(parent);
        }

        @Override
        public SimpleColoredText getItemType(Object element) {
            if (element instanceof Map.Entry) {
                RuleGraphHelper.Cardinality cardinality = (RuleGraphHelper.Cardinality) ((Map.Entry) element).getValue();
                String text = RuleGraphHelper.getCardinalityText(cardinality);
                if (StringUtil.isNotEmpty(text)) {
                    return new SimpleColoredText(" " + text + " ", SimpleTextAttributes.GRAY_ITALIC_ATTRIBUTES);
                }
            }
            return super.getItemType(element);
        }

        @Override
        public Icon getItemIcon(Object element, DiagramState presentation) {
            if (element instanceof Map.Entry) {
                element = ((Map.Entry) element).getKey();
            }
            return super.getItemIcon(element, presentation);
        }

        @Override
        public String getNodeTooltip(PsiNamedElement bnfFile) {
            return null;
        }
    }
}