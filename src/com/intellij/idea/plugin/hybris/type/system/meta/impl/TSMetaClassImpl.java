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
import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaProperty;
import com.intellij.idea.plugin.hybris.type.system.model.Attribute;
import com.intellij.idea.plugin.hybris.type.system.model.ItemType;
import com.intellij.util.containers.MultiMap;
import com.intellij.util.xml.GenericAttributeValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 15/06/2016.
 */
class TSMetaClassImpl extends TSMetaEntityImpl<ItemType> implements TSMetaClass {

    private final MultiMap<String, TSMetaPropertyImpl> myProperties = MultiMap.create();

    private final Set<ItemType> myAllDoms = new LinkedHashSet<>();

    private final TSMetaModelImpl myMetaModel;

    private Optional<String> myExtendedMetaClassName = Optional.empty();

    public TSMetaClassImpl(
        final @NotNull TSMetaModelImpl model,
        final @NotNull String name,
        final @NotNull ItemType dom
    ) {
        super(name, dom);
        myMetaModel = model;
        myAllDoms.add(dom);
        registerExtends(dom);
    }

    public void addDomRepresentation(final @NotNull ItemType anotherDom) {
        myAllDoms.add(anotherDom);
        registerExtends(anotherDom);
    }

    private void registerExtends(final @NotNull ItemType dom) {
        //only one extends is allowed
        if (!myExtendedMetaClassName.isPresent()) {
            myExtendedMetaClassName = Optional.ofNullable(dom.getExtends())
                                              .map(GenericAttributeValue::getRawText);
        }
    }

    @NotNull
    @Override
    public Stream<? extends ItemType> getAllDomsStream() {
        return myAllDoms.stream();
    }

    @NotNull
    TSMetaModelImpl getMetaModel() {
        return myMetaModel;
    }

    @NotNull
    TSMetaPropertyImpl createProperty(final @NotNull Attribute domAttribute) {
        final TSMetaPropertyImpl result = new TSMetaPropertyImpl(this, domAttribute);
        myProperties.putValue(result.getName(), result);
        return result;
    }

    @NotNull
    @Override
    public String getName() {
        //noinspection NullableProblems
        return super.getName();
    }

    @Override
    @NotNull
    public Iterable<? extends TSMetaProperty> getProperties() {
        return myProperties.values();
    }

    @Override
    @NotNull
    public Stream<? extends TSMetaProperty> getPropertiesStream() {
        return myProperties.values().stream();
    }

    @NotNull
    @Override
    public Collection<? extends TSMetaProperty> findPropertiesByName(@NotNull final String name) {
        //FIXME: does not consider inheritance as of yet
        return myProperties.get(name);
    }

    @Nullable
    @Override
    public String getExtendedMetaClassName() {
        return myExtendedMetaClassName.orElse(null);
    }

    @Nullable
    static String extractMetaClassName(@NotNull final ItemType dom) {
        final GenericAttributeValue<String> code = dom.getCode();
        return code == null ? null : code.getValue();
    }

}
