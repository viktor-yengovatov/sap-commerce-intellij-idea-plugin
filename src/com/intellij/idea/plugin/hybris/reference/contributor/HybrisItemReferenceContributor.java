package com.intellij.idea.plugin.hybris.reference.contributor;

import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import org.jetbrains.annotations.NotNull;

/**
 * @author Nosov Aleksandr
 */
public class HybrisItemReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull final PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(XmlPatterns.xmlAttributeValue("code"), new HybrisItemValueReferenceProvider());
    }
}
