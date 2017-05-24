package com.intellij.idea.plugin.hybris.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.PsiShortNamesCache

/**
 * @author Nosov Aleksandr
 */
class HybrisEnumItemReference(element: PsiElement, soft: Boolean) : PsiReferenceBase<PsiElement>(element, soft), PsiPolyVariantReference {

    private val QUOTE_LENGTH = 2

    override fun getRangeInElement(): TextRange = TextRange.from(1, element.textLength - QUOTE_LENGTH)

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val project = myElement.project
        val enumJavaModelName = myElement.text.replace("\"".toRegex(), "")

        val javaModelClasses = PsiShortNamesCache.getInstance(project)
                .getClassesByName(enumJavaModelName, GlobalSearchScope.allScope(project))

        return PsiElementResolveResult.createResults(*javaModelClasses)
    }

    override fun resolve(): PsiElement? {
        val resolveResults = multiResolve(false)
        return if (resolveResults.size == 1) resolveResults[0].element else null
    }

    override fun getVariants(): Array<out PsiReference> = PsiReference.EMPTY_ARRAY

}
