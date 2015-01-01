package com.intellij.idea.plugin.hybris.impex.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.PsiElementProcessor.CollectFilteredElements;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.intellij.idea.plugin.hybris.impex.util.ImpexPsiUtil.isLineBreak;

/**
 * Created 14:28 01 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexFoldingBuilder extends FoldingBuilderEx {

    public static final String GROUP_NAME = "impex";
    private static final ImpexFoldingPlaceholderBuilder placeholderBuilder = new DefaultImpexFoldingPlaceholderBuilder();

    private final PsiElementFilter foldingBlocksFilter = new FoldingBlocksFilter();

    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull final PsiElement root,
                                                @NotNull final Document document,
                                                final boolean quick) {

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

    @NotNull
    private Collection<PsiElement> findFoldingBlocksAndLineBreaks(@Nullable final PsiElement root) {
        if (root == null) {
            return ContainerUtil.emptyList();
        }

        final List<PsiElement> foldingBlocks = new ArrayList<PsiElement>();
        PsiTreeUtil.processElements(root, new CollectFilteredElements<PsiElement>(this.foldingBlocksFilter, foldingBlocks));

        return foldingBlocks;
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull final ASTNode node) {
        return placeholderBuilder.getPlaceholder(node.getPsi());
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull final ASTNode node) {
        return true;
    }
}
