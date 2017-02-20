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

package com.intellij.idea.plugin.hybris.type.system.structure.view;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.idea.plugin.hybris.type.system.model.Attribute;
import com.intellij.idea.plugin.hybris.type.system.model.CollectionType;
import com.intellij.idea.plugin.hybris.type.system.model.ColumnType;
import com.intellij.idea.plugin.hybris.type.system.model.CustomProperty;
import com.intellij.idea.plugin.hybris.type.system.model.Deployment;
import com.intellij.idea.plugin.hybris.type.system.model.EnumType;
import com.intellij.idea.plugin.hybris.type.system.model.EnumValue;
import com.intellij.idea.plugin.hybris.type.system.model.Index;
import com.intellij.idea.plugin.hybris.type.system.model.ItemType;
import com.intellij.idea.plugin.hybris.type.system.model.MapType;
import com.intellij.idea.plugin.hybris.type.system.model.Persistence;
import com.intellij.idea.plugin.hybris.type.system.model.Relation;
import com.intellij.idea.plugin.hybris.type.system.model.RelationElement;
import com.intellij.idea.plugin.hybris.type.system.model.TypeGroup;
import com.intellij.idea.plugin.hybris.type.system.model.Value;
import com.intellij.util.Function;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomElementNavigationProvider;
import com.intellij.util.xml.DomService;
import com.intellij.util.xml.structure.DomStructureTreeElement;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 20/2/17.
 */
public class TSStructureTreeElement extends DomStructureTreeElement {
    private final Function<DomElement, DomService.StructureViewMode> myDescriptor;
    private final DomElementNavigationProvider myNavigationProvider;

    public <T extends DomElement> TSStructureTreeElement(
        final T stableCopy,
        final Function<DomElement, DomService.StructureViewMode> myDescriptor,
        final DomElementNavigationProvider myNavigationProvider
    ) {
        super(stableCopy, myDescriptor, myNavigationProvider);
        this.myDescriptor = myDescriptor;
        this.myNavigationProvider = myNavigationProvider;
    }

    protected StructureViewTreeElement createChildElement(final DomElement element) {
        return new TSStructureTreeElement(element, myDescriptor, myNavigationProvider);
    }

    /**
     * Returns the name of the object to be presented in most renderers across the program.
     *
     * @return the object name.
     */
    @Nullable
    public String getPresentableText() {
        final DomElement dom = getElement();
        if (dom instanceof Attribute) {
            return ((Attribute) dom).getQualifier().getXmlAttributeValue().getValue();
        }
        if (dom instanceof CollectionType) {
            return ((CollectionType) dom).getCode().getValue();
        }
        if (dom instanceof ColumnType) {
            return ((ColumnType) dom).getDatabase().getValue();
        }
        if (dom instanceof CustomProperty) {
            return ((CustomProperty) dom).getName().getValue();
        }
        if (dom instanceof Deployment) {
            return ((Deployment) dom).getTable().getValue();
        }
        if (dom instanceof EnumType) {
            return ((EnumType) dom).getCode().getValue();
        }
        if (dom instanceof EnumValue) {
            return ((EnumValue) dom).getCode().getValue();
        }
        if (dom instanceof Index) {
            return ((Index) dom).getName().getValue();
        }
        if (dom instanceof ItemType) {
            return ((ItemType) dom).getCode().getValue();
        }
        if (dom instanceof MapType) {
            return ((MapType) dom).getCode().getValue();
        }
        if (dom instanceof Persistence) {
            return ((Persistence) dom).getType().getValue().getValue();
        }
        if (dom instanceof Relation) {
            return ((Relation) dom).getCode().getValue();
        }
        if (dom instanceof RelationElement) {
            return ((RelationElement) dom).getQualifier().getXmlAttributeValue().getValue();
        }
        if (dom instanceof TypeGroup) {
            return ((TypeGroup) dom).getName().getValue();
        }
        if (dom instanceof Value) {
            ((Value) dom).getCode().getXmlAttributeValue();
        }
        return super.getPresentableText();
    }

    /**
     * Returns the location of the object (for example, the package of a class). The location
     * string is used by some renderers and usually displayed as grayed text next to the item name.
     *
     * @return the location description, or null if none is applicable.
     */
    @Nullable
    public String getLocationString() {
        final DomElement dom = getElement();
        final String xmlElementName = dom.getXmlElementName();
        if (xmlElementName == null) {
            return null;
        }
        if (xmlElementName.equalsIgnoreCase(getPresentableText())) {
            return null;
        }
        return xmlElementName;
    }
}
