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

import com.intellij.idea.plugin.hybris.business.process.diagram.BpDiagramExtras;

/**
 * Created 11:12 PM 31 January 2016.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class BpDiagramExtrasIml extends BpDiagramExtras {

//    private DiagramDnDProvider<VirtualFile> dndProvider = new VirtualFileDiagramDnDProvider();

//    @Nonnull
//    @Override
//    public JComponent createNodeComponent(final DiagramNode<GenericAction> node,
//                                          final DiagramBuilder builder,
//                                          final Point basePoint,
//                                          final JPanel wrapper) {

//        if (node.getIdentifyingElement().getParent() == null) {
//            return new JLabel(AllIcons.Icon_128);
//        }

//        return super.createNodeComponent(node, builder, basePoint, wrapper);
//    }

//    @Nullable
//    @Override
//    public DiagramDnDProvider<GenericAction> getDnDProvider() {
//        return this.dndProvider;
//    }

//    @Nullable
//    @Override
//    public Object getData(final String dataId,
//                          final List<DiagramNode<GenericAction>> nodes,
//                          final DiagramBuilder builder) {
//        if (nodes.size() == 1) {
//
//            final GenericAction file = nodes.get(0).getIdentifyingElement();
//
//            if (CommonDataKeys.VIRTUAL_FILE.is(dataId)) {
//                return file;
//            }
//
//            if (CommonDataKeys.PSI_FILE.is(dataId) || CommonDataKeys.PSI_ELEMENT.is(dataId)) {
//
//                if (file.isDirectory() && CommonDataKeys.PSI_ELEMENT.is(dataId)) {
//
//                    return PsiManager.getInstance(builder.getProject()).findDirectory(file);
//                } else {
//                    return PsiManager.getInstance(builder.getProject()).findFile(file);
//                }
//            }
//        }
//
//        return super.getData(dataId, nodes, builder);
//    }

//    protected static class VirtualFileDiagramDnDProvider implements DiagramDnDProvider<VirtualFile> {
//
//        @Override
//        public boolean isAcceptedForDnD(final Object o, final Project project) {
//            return o instanceof VirtualFile || o instanceof PsiElement;
//        }
//
//        @Nullable
//        @Override
//        public VirtualFile[] wrapToModelObject(final Object o, final Project project) {
//
//            if (o instanceof PsiElement) {
//
//                final PsiFile file = ((PsiElement) o).getContainingFile();
//
//                if (file != null) {
//                    return new VirtualFile[]{file.getVirtualFile()};
//
//                } else if (o instanceof PsiDirectory) {
//                    return new VirtualFile[]{((PsiDirectory) o).getVirtualFile()};
//                }
//
//            } else if (o instanceof VirtualFile) {
//                return new VirtualFile[]{(VirtualFile) o};
//            }
//
//            return VirtualFile.EMPTY_ARRAY;
//        }
//    }
}
