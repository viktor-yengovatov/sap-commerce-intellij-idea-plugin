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

package com.intellij.idea.plugin.hybris.impex.completion.provider;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexFullHeaderType;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexHeaderLine;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexHeaderTypeName;
import com.intellij.idea.plugin.hybris.impex.utils.CommonPsiUtils;
import com.intellij.idea.plugin.hybris.indexer.ItemTypesIndexService;
import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaClass;
import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaModel;
import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaModelAccess;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PropertyUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created 22:13 14 May 2016
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexHeaderItemTypeAttributeNameCompletionProvider extends CompletionProvider<CompletionParameters> {

    protected static final boolean GRAYED = true;
    protected static final String UNNAMED = "<unnamed>";

    @NotNull
    public static CompletionProvider<CompletionParameters> getInstance() {
        return ServiceManager.getService(ImpexHeaderItemTypeAttributeNameCompletionProvider.class);
    }

    @Override
    public void addCompletions(
        @NotNull final CompletionParameters parameters,
        final ProcessingContext context,
        @NotNull final CompletionResultSet result
    ) {
        Validate.notNull(parameters);
        Validate.notNull(result);

        final Project project = this.getProject(parameters);
        if (null == project) {
            return;
        }

        final PsiElement psiElementUnderCaret = parameters.getPosition();
        final ImpexHeaderTypeName headerTypeName = this.getHeaderTypeNamePsiElementOfAttribute(psiElementUnderCaret);

        if (headerTypeName != null) {
            this.fillJavaTypeFieldsCompletionResultsSet(project, headerTypeName, result);
            this.fillDomAttributesCompletions(project, headerTypeName, result);
        }
    }

    protected void fillDomAttributesCompletions(
        @NotNull final Project project,
        @NotNull final ImpexHeaderTypeName headerTypeName,
        @NotNull final CompletionResultSet resultSet
    ) {

        final TSMetaModel metaModel = TSMetaModelAccess.getInstance(project).getTypeSystemMeta();
        final String itemTypeCode = headerTypeName.getText();
        final Optional<TSMetaClass> metaClass = Optional.ofNullable(metaModel.findMetaClassByName(itemTypeCode));

        metaClass
            .map(meta -> meta.getPropertiesStream(true))
            .orElse(Stream.empty())
            .filter(prop -> prop.getName() != null)
            .map(prop -> createLookup(prop.getName()))
            .forEach(resultSet::addElement);

        metaClass
            .map(meta -> meta.getReferenceEndsStream(true))
            .orElse(Stream.empty())
            .map(ref -> createLookup(ref.getRole()))
            .forEach(resultSet::addElement);
    }

    protected LookupElement createLookup(final @Nullable String name) {
        //FIXME: add type
        return LookupElementBuilder
            .create(Optional.ofNullable(name).orElse("<unnamed>")) //should not happen
            .withIcon(HybrisIcons.TYPE_SYSTEM)
            //.withTypeText(propertyType.getPresentableText(), GRAYED)
            ;
    }

    protected void fillJavaTypeFieldsCompletionResultsSet(
        @NotNull final Project project,
        @NotNull final PsiElement headerTypeNamePsiElement,
        @NotNull final CompletionResultSet resultSet
    ) {
        Validate.notNull(project);
        Validate.notNull(resultSet);

        final ItemTypesIndexService itemTypesIndexService = ServiceManager.getService(
            project, ItemTypesIndexService.class
        );

        final PsiClass psiClass = itemTypesIndexService.getPsiClassByTypeCode(headerTypeNamePsiElement.getText());

        if (null == psiClass) {
            return;
        }

        for (PsiMethod psiMethod : psiClass.getAllMethods()) {
            if (!CommonPsiUtils.isSetter(psiMethod)) {
                continue;
            }

            final String propertyName = PropertyUtil.getPropertyNameBySetter(psiMethod);
            final PsiType propertyType = PropertyUtil.getPropertyType(psiMethod);

            if (null == propertyType) {
                continue;
            }

            final LookupElementBuilder element = LookupElementBuilder
                .create(propertyName)
                .withStrikeoutness(psiMethod.isDeprecated()) // marks deprecation
                .withTypeText(propertyType.getPresentableText(), GRAYED);

            resultSet.addElement(element);
        }
    }

    @Nullable
    private Project getProject(final @NotNull CompletionParameters parameters) {
        Validate.notNull(parameters);

        return parameters.getEditor().getProject();
    }

    @Nullable
    @Contract("null -> null")
    protected ImpexHeaderTypeName getHeaderTypeNamePsiElementOfAttribute(@Nullable final PsiElement headerAttributePsiElement) {
        if (null == headerAttributePsiElement || null == headerAttributePsiElement.getNode()) {
            return null;
        }

        final ImpexHeaderLine impexHeaderLine = PsiTreeUtil.getParentOfType(
            headerAttributePsiElement,
            ImpexHeaderLine.class
        );

        if (null == impexHeaderLine) {
            return null;
        }

        final ImpexFullHeaderType impexFullHeaderType = impexHeaderLine.getFullHeaderType();

        return null == impexFullHeaderType ? null : impexFullHeaderType.getHeaderTypeName();
    }
}
