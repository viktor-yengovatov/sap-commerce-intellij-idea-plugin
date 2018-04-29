package com.intellij.idea.plugin.hybris.inspections.util

import com.intellij.codeInsight.daemon.impl.analysis.XmlHighlightVisitor
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.UserDataCache
import com.intellij.psi.XmlRecursiveElementWalkingVisitor
import com.intellij.psi.templateLanguages.OuterLanguageElement
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlFile
import com.intellij.xml.XmlAttributeDescriptor
import com.intellij.xml.util.XmlUtil
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
class BeanDefinitionCountHolder {

    private val beanClass2Attribute = HashMap<String, MutableList<XmlAttributeValue>>()
    private val possiblyDuplicateBeans = HashSet<XmlAttributeValue>()

    fun isDuplicateBeanDefinition(value: XmlAttributeValue): Boolean {
        return possiblyDuplicateBeans.contains(value)
    }

    private fun registerId(id: String, attributeValue: XmlAttributeValue) {
        var list: MutableList<XmlAttributeValue>? = beanClass2Attribute[id]
        if (list == null) {
            list = ArrayList()
            beanClass2Attribute[id] = list
        } else {
            // mark as duplicate
            if (list.isNotEmpty()) {
                possiblyDuplicateBeans.addAll(list)
                possiblyDuplicateBeans.add(attributeValue)
            }
        }

        list.add(attributeValue)
    }

    private class BeanClassGatheringRecursiveVisitor internal constructor(private val countHolder: BeanDefinitionCountHolder) : XmlRecursiveElementWalkingVisitor(true) {

        override fun visitXmlAttributeValue(value: XmlAttributeValue) {
            val element = value.parent as? XmlAttribute ?: return

            val tag = element.parent ?: return

            val descriptor = tag.descriptor ?: return

            val attributeDescriptor = descriptor.getAttributeDescriptor(element)
            if (attributeDescriptor != null) {
                if (isClassAttr(attributeDescriptor)) {
                    updateMap(element, value)
                }
            }

            super.visitXmlAttributeValue(value)
        }

        private fun isClassAttr(attributeDescriptor: XmlAttributeDescriptor) = attributeDescriptor.name == "class"

        private fun updateMap(attribute: XmlAttribute, value: XmlAttributeValue) {
            val id = XmlHighlightVisitor.getUnquotedValue(value, attribute.parent)
            if (XmlUtil.isSimpleValue(id, value) && PsiTreeUtil.getChildOfType(value, OuterLanguageElement::class.java) == null) {
                countHolder.registerId(id, value)
            }
        }
    }

    companion object {
        private val xmlRefCountHolderKey = Key.create<CachedValue<BeanDefinitionCountHolder>>("bean xml count holder")

        private val internalCache = object : UserDataCache<CachedValue<BeanDefinitionCountHolder>, XmlFile, Any?>() {
            override fun compute(file: XmlFile, p: Any?) =
                    CachedValuesManager.getManager(file.project).createCachedValue({
                        val holder = BeanDefinitionCountHolder()
                        val viewProvider = file.viewProvider
                        val language = viewProvider.baseLanguage
                        val psiFile = viewProvider.getPsi(language)
                        psiFile.accept(BeanClassGatheringRecursiveVisitor(holder))
                        CachedValueProvider.Result(holder, file)
                    }, false)
        }

        fun getCountHolder(file: XmlFile): BeanDefinitionCountHolder? {
            return internalCache.get(xmlRefCountHolderKey, file, null).value
        }
    }

}
