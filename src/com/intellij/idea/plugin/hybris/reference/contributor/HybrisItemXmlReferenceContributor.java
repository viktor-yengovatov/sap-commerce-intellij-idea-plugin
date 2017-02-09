package com.intellij.idea.plugin.hybris.reference.contributor;

import com.intellij.idea.plugin.hybris.reference.provider.HybrisEnumItemReferenceProvider;
import com.intellij.idea.plugin.hybris.reference.provider.HybrisEnumLiteralItemReferenceProvider;
import com.intellij.idea.plugin.hybris.reference.provider.HybrisModelItemReferenceProvider;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import org.jetbrains.annotations.NotNull;

import static com.intellij.idea.plugin.hybris.common.utils.PsiXmlUtils.insideTagPattern;
import static com.intellij.idea.plugin.hybris.common.utils.PsiXmlUtils.tagAttributeValuePattern;

/**
 * @author Nosov Aleksandr
 */
public class HybrisItemXmlReferenceContributor extends PsiReferenceContributor {

    public static final String ITEMS_TYPE_FILE_NAME = "-items";

    @Override
    public void registerReferenceProviders(@NotNull final PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
            tagAttributeValuePattern("enumtype", "code", ITEMS_TYPE_FILE_NAME),
            new HybrisEnumItemReferenceProvider()
        );
        registrar.registerReferenceProvider(
            tagAttributeValuePattern("value", "code", ITEMS_TYPE_FILE_NAME).inside(insideTagPattern("enumtype")),
            new HybrisEnumLiteralItemReferenceProvider()
        );
        registrar.registerReferenceProvider(
            tagAttributeValuePattern("itemtype", "code", ITEMS_TYPE_FILE_NAME),
            new HybrisModelItemReferenceProvider()
        );
    }


}
