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

package com.intellij.idea.plugin.hybris.type.system.meta.impl;

import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaClass;
import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaCollection;
import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaEnum;
import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaModel;
import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaReference;
import com.intellij.idea.plugin.hybris.type.system.meta.impl.CaseInsensitive.NoCaseMap;
import com.intellij.idea.plugin.hybris.type.system.meta.impl.CaseInsensitive.NoCaseMultiMap;
import com.intellij.idea.plugin.hybris.type.system.model.CollectionType;
import com.intellij.idea.plugin.hybris.type.system.model.EnumType;
import com.intellij.idea.plugin.hybris.type.system.model.ItemType;
import com.intellij.idea.plugin.hybris.type.system.model.Relation;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 15/06/2016.
 */
class TSMetaModelImpl implements TSMetaModel {

    private final NoCaseMap<TSMetaClassImpl> myClasses = new NoCaseMap<>();
    private final NoCaseMap<TSMetaEnumImpl> myEnums = new NoCaseMap<>();
    private final NoCaseMap<TSMetaCollectionImpl> myCollections = new NoCaseMap<>();
    private final NoCaseMultiMap<TSMetaReference.ReferenceEnd> myReferencesBySourceTypeName = new NoCaseMultiMap<>();

    @Nullable
    TSMetaClassImpl findOrCreateClass(final @NotNull ItemType domItemType) {
        final String name = TSMetaClassImpl.extractMetaClassName(domItemType);
        if (name == null) {
            return null;
        }
        TSMetaClassImpl impl = myClasses.get(name);
        if (impl == null) {
            impl = new TSMetaClassImpl(this, name, domItemType);
            myClasses.put(name, impl);
        } else {
            impl.addDomRepresentation(domItemType);
        }
        return impl;
    }

    @Nullable
    TSMetaEnumImpl findOrCreateEnum(final @NotNull EnumType domEnumType) {
        final String name = TSMetaEnumImpl.extractName(domEnumType);
        if (StringUtil.isEmpty(name)) {
            return null;
        }
        TSMetaEnumImpl impl = myEnums.get(name);
        if (impl == null) {
            impl = new TSMetaEnumImpl(name, domEnumType);
            myEnums.put(name, impl);
        } else {
            //report a problem
        }
        return impl;
    }

    @Nullable
    TSMetaCollectionImpl findOrCreateCollection(@NotNull final CollectionType domCollectionType) {
        final String name = TSMetaCollectionImpl.extractName(domCollectionType);
        if (StringUtil.isEmpty(name)) {
            return null;
        }
        TSMetaCollectionImpl impl = myCollections.get(name);
        if (impl == null) {
            impl = new TSMetaCollectionImpl(this, domCollectionType);
            myCollections.put(name, impl);
        }
        return impl;
    }

    @Nullable
    TSMetaReference createReference(@NotNull final Relation domRelation) {
        final TSMetaReferenceImpl result = new TSMetaReferenceImpl(this, domRelation);

        registerReferenceEnd(result.getSource(), result.getTarget());
        registerReferenceEnd(result.getTarget(), result.getSource());

        return result;
    }

    private void registerReferenceEnd(
        @NotNull final TSMetaReference.ReferenceEnd ownerEnd,
        @NotNull final TSMetaReference.ReferenceEnd targetEnd
    ) {
        if (!targetEnd.isNavigable()) {
            return;
        }
        final String ownerTypeName = ownerEnd.getTypeName();
        if (!StringUtil.isEmpty(ownerTypeName)) {
            myReferencesBySourceTypeName.putValue(ownerTypeName, targetEnd);
        }
    }

    void collectReferencesForSourceType(
        final @NotNull TSMetaClassImpl source,
        final @NotNull Collection<TSMetaReference.ReferenceEnd> out
    ) {

        out.addAll(myReferencesBySourceTypeName.get(source.getName()));
    }

    @NotNull
    @Override
    public Stream<? extends TSMetaClass> getMetaClassesStream() {
        return myClasses.values().stream();
    }

    @Nullable
    @Override
    public TSMetaClass findMetaClassByName(@NotNull final String name) {
        return myClasses.get(name);
    }

    @Nullable
    @Override
    public TSMetaClass findMetaClassForDom(@NotNull final ItemType dom) {
        return Optional.ofNullable(TSMetaClassImpl.extractMetaClassName(dom))
                       .map(this::findMetaClassByName)
                       .orElse(null);
    }

    @NotNull
    @Override
    public Stream<? extends TSMetaEnum> getMetaEnumsStream() {
        return myEnums.values().stream();
    }

    @Nullable
    @Override
    public TSMetaEnum findMetaEnumByName(@NotNull final String name) {
        return myEnums.get(name);
    }

    @NotNull
    @Override
    public Stream<? extends TSMetaCollection> getMetaCollectionsStream() {
        return myCollections.values().stream();
    }

    @Nullable
    @Override
    public TSMetaCollection findMetaCollectionByName(@NotNull final String name) {
        return myCollections.get(name);
    }


}
