// Generated on Sun Jun 05 01:21:13 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;
import org.jetbrains.annotations.NotNull;

/**
 * null:valueType interface.
 * <pre>
 * <h3>Type null:valueType documentation</h3>
 * Configures a single element.
 * </pre>
 */
public interface Value extends DomElement {

    /**
     * Returns the value of the code child.
     * <pre>
     * <h3>Attribute null:code documentation</h3>
     * The unique code of this element.
     * </pre>
     *
     * @return the value of the code child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("code")
    @Required
    GenericAttributeValue<String> getCode();


}
