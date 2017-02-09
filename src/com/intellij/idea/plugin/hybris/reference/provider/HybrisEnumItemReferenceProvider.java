package com.intellij.idea.plugin.hybris.reference.provider;

import com.intellij.idea.plugin.hybris.reference.HybrisEnumItemReference;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nosov Aleksandr
 */
public class HybrisEnumItemReferenceProvider extends PsiReferenceProvider {

    private final static Logger LOG = Logger.getInstance(
        "#com.intellij.idea.plugin.hybris.reference.contributor.HybrisItemValueReferenceProvider");

    @Override
    @NotNull
    public final PsiReference[] getReferencesByElement(
        @NotNull final PsiElement element,
        @NotNull final ProcessingContext context
    ) {

        final HybrisEnumItemReference reference
            = new HybrisEnumItemReference(element, true);
        final List<PsiReference> results = new ArrayList<>();
        results.add(reference);
        return results.toArray(new PsiReference[results.size()]);
    }
}