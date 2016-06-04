// Generated on Sun Jun 05 00:22:21 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTag;
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
    @SubTag("itemtype")
    java.util.List<com.intellij.idea.plugin.hybris.type.system.file.dom.model.Itemtype> getItemtypes();

    /**
     * Adds new child to the list of itemtype children.
     *
     * @return created child
     */
    @SubTag("itemtype")
    com.intellij.idea.plugin.hybris.type.system.file.dom.model.Itemtype addItemtype();


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
    @SubTag("typegroup")
    java.util.List<com.intellij.idea.plugin.hybris.type.system.file.dom.model.TypeGroup> getTypegroups();

    /**
     * Adds new child to the list of typegroup children.
     *
     * @return created child
     */
    @SubTag("typegroup")
    com.intellij.idea.plugin.hybris.type.system.file.dom.model.TypeGroup addTypegroup();


}
