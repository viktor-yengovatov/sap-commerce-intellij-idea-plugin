// Generated on Sun Jun 05 01:21:13 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.GenericDomValue;
import com.intellij.util.xml.Required;
import com.intellij.util.xml.SubTag;
import org.jetbrains.annotations.NotNull;

/**
 * null:customPropertyType interface.
 * <pre>
 * <h3>Type null:customPropertyType documentation</h3>
 * Defines a custom property.
 * </pre>
 */
public interface CustomProperty extends DomElement {

    /**
     * Returns the value of the name child.
     * <pre>
     * <h3>Attribute null:name documentation</h3>
     * The name of the custom property.
     * </pre>
     *
     * @return the value of the name child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("name")
    @Required
    GenericAttributeValue<String> getName();


    /**
     * Returns the value of the value child.
     * <pre>
     * <h3>Element null:value documentation</h3>
     * The value of the custom property.
     * </pre>
     *
     * @return the value of the value child.
     */
    @NotNull
    @SubTag("value")
    @Required
    GenericDomValue<String> getValue();


}
