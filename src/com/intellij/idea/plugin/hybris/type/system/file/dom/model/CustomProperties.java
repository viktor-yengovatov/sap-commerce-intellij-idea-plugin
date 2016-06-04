// Generated on Sun Jun 05 01:21:13 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTagList;
import org.jetbrains.annotations.NotNull;

/**
 * null:customPropertiesType interface.
 * <pre>
 * <h3>Type null:customPropertiesType documentation</h3>
 * Defines custom properties.
 * </pre>
 */
public interface CustomProperties extends DomElement {

    /**
     * Returns the list of property children.
     * <pre>
     * <h3>Element null:property documentation</h3>
     * Defines a custom property.
     * </pre>
     *
     * @return the list of property children.
     */
    @NotNull
    @SubTagList("property")
    java.util.List<CustomProperty> getProperties();

    /**
     * Adds new child to the list of property children.
     *
     * @return created child
     */
    @SubTagList("property")
    CustomProperty addProperty();


}
