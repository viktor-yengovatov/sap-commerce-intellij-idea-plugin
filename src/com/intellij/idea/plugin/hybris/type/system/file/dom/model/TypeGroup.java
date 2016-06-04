// Generated on Sun Jun 05 01:21:13 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.SubTagList;
import org.jetbrains.annotations.NotNull;

/**
 * null:typeGroupType interface.
 */
public interface TypeGroup extends DomElement {

    /**
     * Returns the value of the name child.
     * <pre>
     * <h3>Attribute null:name documentation</h3>
     * Defines the name of this group. Only for structural purpose, will have no effect on runtime. Default is empty.
     * </pre>
     *
     * @return the value of the name child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("name")
    GenericAttributeValue<String> getName();


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
    java.util.List<Itemtype> getItemtypes();

    /**
     * Adds new child to the list of itemtype children.
     *
     * @return created child
     */
    @SubTagList("itemtype")
    Itemtype addItemtype();


}
