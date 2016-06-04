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
 * null:enumValueType interface.
 * <pre>
 * <h3>Type null:enumValueType documentation</h3>
 * Configures a single enum value.
 * </pre>
 */
public interface EnumValue extends DomElement {

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


    /**
     * Returns the value of the description child.
     * <pre>
     * <h3>Element null:description documentation</h3>
     * Provides possibility to add meaningfull description phrase for a generated model class.
     * </pre>
     *
     * @return the value of the description child.
     */
    @NotNull
    @SubTag("description")
    GenericDomValue<String> getDescription();


}
