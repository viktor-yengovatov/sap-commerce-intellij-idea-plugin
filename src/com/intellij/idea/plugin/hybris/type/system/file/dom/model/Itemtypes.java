// Generated on Sun Jun 05 01:21:13 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTagList;
import org.jetbrains.annotations.NotNull;

/**
 * null:itemtypesType interface.
 * <pre>
 * <h3>Type null:itemtypesType documentation</h3>
 * Defines a grouping of item types.
 * </pre>
 */
public interface Itemtypes extends DomElement {

    /**
     * Returns the list of itemtype children.
     * <pre>
     * <h3>Element null:itemtype documentation</h3>
     * Specifies a specific ComposedType.
     * </pre>
     *
     * @return the list of itemtype children.
     */
    @NotNull
    @SubTagList("itemtype")
    java.util.List<ItemType> getItemTypes();

    /**
     * Adds new child to the list of itemtype children.
     *
     * @return created child
     */
    @SubTagList("itemtype")
    ItemType addItemType();


    /**
     * Returns the list of typegroup children.
     * <pre>
     * <h3>Element null:typegroup documentation</h3>
     * Specifies a group of ComposedTypes to allow better structuring within the items.xml file.
     * </pre>
     *
     * @return the list of typegroup children.
     */
    @NotNull
    @SubTagList("typegroup")
    java.util.List<TypeGroup> getTypeGroups();

    /**
     * Adds new child to the list of typegroup children.
     *
     * @return created child
     */
    @SubTagList("typegroup")
    TypeGroup addTypeGroup();


}
