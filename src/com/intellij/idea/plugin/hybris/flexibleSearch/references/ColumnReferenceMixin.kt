package com.intellij.idea.plugin.hybris.flexibleSearch.references

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchColumnReference
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchFromClause
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchQuerySpecification
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTableExpression
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTableName
import com.intellij.idea.plugin.hybris.psi.references.TypeSystemReferenceBase
import com.intellij.idea.plugin.hybris.type.system.model.Attribute
import com.intellij.idea.plugin.hybris.type.system.model.RelationElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.ResolveResult
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.xml.DomElement
import java.util.Optional

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
abstract class ColumnReferenceMixin(node: ASTNode) : ASTWrapperPsiElement(node), FlexibleSearchColumnReference {

    private var reference: TypeSystemAttributeReference? = null

    override fun getReferences(): Array<PsiReference?> {
        if (reference == null) {
            reference = TypeSystemAttributeReference(this)
        }
        return arrayOf(reference)
    }

    override fun clone(): Any {
        val result = super.clone() as ColumnReferenceMixin
        result.reference = null
        return result
    }

}

internal class TypeSystemAttributeReference(owner: FlexibleSearchColumnReference) : TypeSystemReferenceBase<FlexibleSearchColumnReference>(owner) {

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val meta = typeSystemMeta
        val featureName = element.text
        val metaClass = findItemTypeReference()
                .map { it.text }
                .map({ meta.findMetaClassByName(it) })

        if (!metaClass.isPresent) {
            return ResolveResult.EMPTY_ARRAY
        }

        val result = metaClass.get()
                .findPropertiesByName(featureName, true)
                .mapNotNull { it.retrieveDom() }
                .map { AttributeResolveResult(it) }.toList()

        metaClass.get().findReferenceEndsByRole(featureName, true)
                .mapNotNull { it.retrieveDom() }
                .map { RelationElementResolveResult(it) }

        return result.toTypedArray()
    }

    private fun findItemTypeReference(): Optional<FlexibleSearchTableName> {
        return Optional.ofNullable(PsiTreeUtil.getParentOfType(element, FlexibleSearchQuerySpecification::class.java))
                .map { PsiTreeUtil.findChildOfType(it, FlexibleSearchFromClause::class.java) }
                .map { it!!.tableReferenceList }
                .map { PsiTreeUtil.findChildOfType(it, FlexibleSearchTableName::class.java) }
    }

    private class AttributeResolveResult(private val myDomAttribute: Attribute) : TypeSystemReferenceBase.TypeSystemResolveResult {
        override fun getElement(): PsiElement? = myDomAttribute.qualifier.xmlAttributeValue
        override fun isValidResult() = element != null
        override fun getSemanticDomElement() = myDomAttribute
    }

    private class RelationElementResolveResult(private val myDomRelationEnd: RelationElement) : TypeSystemReferenceBase.TypeSystemResolveResult {
        override fun getElement(): PsiElement? = myDomRelationEnd.qualifier.xmlAttributeValue
        override fun isValidResult() = element != null
        override fun getSemanticDomElement(): DomElement = myDomRelationEnd
    }

}
