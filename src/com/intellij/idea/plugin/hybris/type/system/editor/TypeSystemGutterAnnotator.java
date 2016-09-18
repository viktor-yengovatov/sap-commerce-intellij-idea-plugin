/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2016 Alexander Bartash <AlexanderBartash@gmail.com>
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

package com.intellij.idea.plugin.hybris.type.system.editor;

import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaClass;
import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaModel;
import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaModelAccess;
import com.intellij.idea.plugin.hybris.type.system.model.ItemType;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomManager;
import com.intellij.util.xml.GenericAttributeValue;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.intellij.idea.plugin.hybris.type.system.utils.TypeSystemUtils.isTypeSystemXmlFile;


/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 15/06/2016.
 */
public class TypeSystemGutterAnnotator implements Annotator {

    @Override
    public void annotate(
        @NotNull final PsiElement psiElement, @NotNull final AnnotationHolder annotationHolder
    ) {
        if (psiElement instanceof XmlTag && isTypeSystemXmlFile(psiElement.getContainingFile())) {
            final Project project = psiElement.getProject();
            final DomElement dom = DomManager.getDomManager(project).getDomElement((XmlTag) psiElement);

            if (dom instanceof ItemType) {
                final ItemType itemType = (ItemType) dom;
                if (itemType.getCode() == null || itemType.getCode().getXmlAttributeValue() == null) {
                    return;
                }

                final Collection<? extends PsiElement> alternativeDoms = findAlternativeDoms(itemType);
                if (!alternativeDoms.isEmpty()) {
                    NavigationGutterIconBuilder
                        .create(AllIcons.Actions.Nextfile, TypeSystemGutterAnnotator::findAlternativeDoms)
                        .setTarget(itemType)
                        .setTooltipText(alternativeDoms.size() > 1
                                            ? "Alternative Definitions"
                                            : "Alternative Definition")
                        .setAlignment(GutterIconRenderer.Alignment.RIGHT)
                        .install(annotationHolder, itemType.getCode().getXmlAttributeValue());
                }

                final Optional<TSMetaClass> firstExtender = findFirstExtendingMetaClass(itemType);
                if (firstExtender.isPresent()) {
                    NavigationGutterIconBuilder
                        .create(
                            AllIcons.Gutter.OverridenMethod,
                            TypeSystemGutterAnnotator::findAllExtendingXmlAttributes
                        )
                        .setTarget(itemType)
                        .setAlignment(GutterIconRenderer.Alignment.LEFT)
                        .setTooltipText("Has subtypes")
                        .install(annotationHolder, itemType.getCode().getXmlAttributeValue());
                }
            }
        }
    }

    private static Collection<XmlAttributeValue> findAlternativeDoms(
        @NotNull final ItemType source
    ) {
        final TSMetaModel metaModel = TSMetaModelAccess.getInstance(source).getTypeSystemMeta();
        return Optional.ofNullable(metaModel.findMetaClassForDom(source))
                       .map(TSMetaClass::getAllDomsStream)
                       .orElse(Stream.empty())
                       .filter(dom -> !dom.equals(source))
                       .map(ItemType::getCode)
                       .map(GenericAttributeValue::getXmlAttributeValue)
                       .collect(Collectors.toList());
    }

    @NotNull
    private static Optional<TSMetaClass> findFirstExtendingMetaClass(
        @NotNull final ItemType source
    ) {
        return getExtendingMetaClassNamesStream(source)
            .findAny();
    }

    private static Collection<PsiElement> findAllExtendingXmlAttributes(
        @NotNull final ItemType source
    ) {
        return getExtendingMetaClassNamesStream(source)
            .flatMap(TSMetaClass::getAllDomsStream)
            .filter(dom -> dom.getExtends() != null)
            .map(ItemType::getExtends)
            .map(GenericAttributeValue::getXmlAttributeValue)
            .filter(psi -> psi != null)
            .collect(Collectors.toList());
    }

    @NotNull
    private static Stream<TSMetaClass> getExtendingMetaClassNamesStream(
        @NotNull final ItemType source
    ) {
        final TSMetaModel metaModel = TSMetaModelAccess.getInstance(source).getTypeSystemMeta();
        final TSMetaClass sourceMeta = metaModel.findMetaClassForDom(source);
        if (sourceMeta == null) {
            return Stream.empty();
        }
        return metaModel
            .getMetaClassesStream()
            .map(TSMetaClass.class::cast)
            .filter(meta -> sourceMeta.getName().equals(meta.getExtendedMetaClassName()));
    }


}
