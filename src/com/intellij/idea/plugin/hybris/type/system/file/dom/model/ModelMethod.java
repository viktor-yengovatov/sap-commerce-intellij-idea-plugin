// Generated on Sun Jun 05 00:22:21 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;
import org.jetbrains.annotations.NotNull;

/**
 * null:modelMethodType interface.
 * <pre>
 * <h3>Type null:modelMethodType documentation</h3>
 * Allows to configure alternative methods at generated model.
 * </pre>
 */
public interface ModelMethod extends DomElement {

    /**
     * Returns the value of the name child.
     * <pre>
     * <h3>Attribute null:name documentation</h3>
     * Name of the alternative getter method.
     * </pre>
     *
     * @return the value of the name child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("name")
    @Required
    GenericAttributeValue<String> getName();


    /**
     * Returns the value of the deprecated child.
     * <pre>
     * <h3>Attribute null:deprecated documentation</h3>
     * Will the method be marked deprecated? Default is
     * 					false.
     * </pre>
     *
     * @return the value of the deprecated child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("deprecated")
    GenericAttributeValue<Boolean> getDeprecated();


    /**
     * Returns the value of the default child.
     * <pre>
     * <h3>Attribute null:default documentation</h3>
     * Will this method be the default method and replace the original one instead of adding it additional? Default is false.
     * </pre>
     *
     * @return the value of the default child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("default")
    GenericAttributeValue<Boolean> getDefault();


}
