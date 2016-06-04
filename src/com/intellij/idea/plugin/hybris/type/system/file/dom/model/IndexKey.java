// Generated on Sun Jun 05 00:22:21 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;
import org.jetbrains.annotations.NotNull;

/**
 * null:indexKeyType interface.
 * <pre>
 * <h3>Type null:indexKeyType documentation</h3>
 * Configures a single index key.
 * </pre>
 */
public interface IndexKey extends DomElement {

    /**
     * Returns the value of the attribute child.
     * <pre>
     * <h3>Attribute null:attribute documentation</h3>
     * Type attribute to be indexed.
     * </pre>
     *
     * @return the value of the attribute child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("attribute")
    @Required
    GenericAttributeValue<String> getAttribute();


    /**
     * Returns the value of the lower child.
     * <pre>
     * <h3>Attribute null:lower documentation</h3>
     * Elements will be indexed case-insensitive. Default is 'false'.
     * </pre>
     *
     * @return the value of the lower child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("lower")
    GenericAttributeValue<Boolean> getLower();


}
