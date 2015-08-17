/*
 * Copyright 2015 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.idea.plugin.hybris.impex.folding;

import com.intellij.idea.plugin.hybris.impex.settings.ImpexSettingsManager;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.PsiElementProcessor.CollectFilteredElements;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.isLineBreak;

/**
 * Created 14:28 01 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexFoldingBuilder extends FoldingBuilderEx {

    public static final String GROUP_NAME = "impex";

    private static final FoldingDescriptor[] EMPTY_ARRAY = new FoldingDescriptor[0];

    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull final PsiElement root,
                                                @NotNull final Document document,
                                                final boolean quick) {
        if (this.isFoldingDisabled()) {
            return EMPTY_ARRAY;
        }

        Validate.notNull(root);
        Validate.notNull(document);

        final Collection<PsiElement> psiElements = this.findFoldingBlocksAndLineBreaks(root);

        FoldingGroup currentLineGroup = FoldingGroup.newGroup(GROUP_NAME);

        /* Avoid spawning a lot of unnecessary objects for each line break. */
        boolean groupIsNotFresh = false;

        final List<FoldingDescriptor> descriptors = new ArrayList<FoldingDescriptor>();
        for (final PsiElement psiElement : psiElements) {

            if (isLineBreak(psiElement)) {
                if (groupIsNotFresh) {
                    currentLineGroup = FoldingGroup.newGroup(GROUP_NAME);
                    groupIsNotFresh = false;
                }
            } else {
                descriptors.add(new ImpexFoldingDescriptor(psiElement, currentLineGroup));
                groupIsNotFresh = true;
            }
        }

        return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
    }

    @Contract(pure = true)
    protected boolean isFoldingDisabled() {
        final ImpexSettingsManager settingsManager = ApplicationManager.getApplication().getComponent(
                ImpexSettingsManager.class
        );

        return !settingsManager.getImpexSettingsData().isFoldingEnabled();
    }

    @NotNull
    @Contract(pure = true)
    protected Collection<PsiElement> findFoldingBlocksAndLineBreaks(@Nullable final PsiElement root) {
        if (root == null) {
            return Collections.emptyList();
        }

        final List<PsiElement> foldingBlocks = new ArrayList<PsiElement>();
        PsiTreeUtil.processElements(root, new CollectFilteredElements<PsiElement>(
                PsiElementFilterFactory.getPsiElementFilter(), foldingBlocks
        ));

        return foldingBlocks;
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull final ASTNode node) {
        Validate.notNull(node);

        return ImpexFoldingPlaceholderBuilderFactory.getPlaceholderBuilder().getPlaceholder(node.getPsi());
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull final ASTNode node) {
        return true;
    }
}
