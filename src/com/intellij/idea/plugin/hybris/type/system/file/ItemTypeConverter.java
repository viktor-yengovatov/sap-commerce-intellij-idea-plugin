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

package com.intellij.idea.plugin.hybris.type.system.file;

import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaClass;
import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaModel;
import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaModelAccess;
import com.intellij.idea.plugin.hybris.type.system.model.ItemType;
import com.intellij.psi.PsiElement;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.ResolvingConverter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 15/06/2016.
 */
public class ItemTypeConverter extends ResolvingConverter<ItemType> {

    @Nullable
    @Override
    public ItemType fromString(
        @Nullable @NonNls final String s, final ConvertContext context
    ) {
        final TSMetaModel meta = getTypeSystemMeta(context);
        return Optional.ofNullable(meta.findMetaClassByName(s))
                       .map(TSMetaClass::getAllDomsStream)
                       .orElse(Stream.empty())
                       .findFirst()
                       .orElse(null);
    }

    @NotNull
    @Override
    public Collection<? extends ItemType> getVariants(final ConvertContext context) {
        final TSMetaModel meta = getTypeSystemMeta(context);
        return meta.getMetaClassesStream()
                   .map(TSMetaClass::getAllDomsStream)
                   .map(Stream::findFirst)
                   .filter(Optional::isPresent)
                   .map(Optional::get)
                   .collect(Collectors.toList());
    }

    @Nullable
    @Override
    public String toString(@Nullable final ItemType t, final ConvertContext context) {
        return Optional.ofNullable(t)
                       .map(ItemType::getCode)
                       .map(GenericAttributeValue::getValue)
                       .orElse(null);
    }

    @Nullable
    @Override
    public PsiElement getPsiElement(@Nullable final ItemType resolvedValue) {
        return Optional.ofNullable(resolvedValue)
                       .map(ItemType::getCode)
                       .map(GenericAttributeValue::getXmlAttributeValue)
                       .orElse(null);
    }

    private TSMetaModel getTypeSystemMeta(@NotNull final ConvertContext convertContext) {
        return TSMetaModelAccess.getInstance(convertContext.getProject()).getTypeSystemMeta();
    }
}
