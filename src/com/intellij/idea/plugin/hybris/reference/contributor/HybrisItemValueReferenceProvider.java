package com.intellij.idea.plugin.hybris.reference.contributor;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
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
public class HybrisItemValueReferenceProvider extends PsiReferenceProvider {

    private final static Logger LOG = Logger.getInstance(
        "#com.intellij.idea.plugin.hybris.reference.contributor.HybrisItemValueReferenceProvider");

    @Override
    @NotNull
    public final PsiReference[] getReferencesByElement(
        @NotNull final PsiElement element,
        @NotNull final ProcessingContext context
    ) {

        final HybrisModelItemReference reference = new HybrisModelItemReference(
            element, new TextRange(0, element.getText().length()),
            true
        );
        final List<PsiReference> results = new ArrayList<>();
        results.add(reference);
        return results.toArray(new PsiReference[results.size()]);
    }
}