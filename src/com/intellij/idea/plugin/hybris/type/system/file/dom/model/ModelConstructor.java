// Generated on Sun Jun 05 00:22:21 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;
import org.jetbrains.annotations.NotNull;

/**
 * null:modelConstructorType interface.
 * <pre>
 * <h3>Type null:modelConstructorType documentation</h3>
 * Allows to configure model constructor signatures.
 * </pre>
 */
public interface ModelConstructor extends DomElement {

    /**
     * Returns the value of the signature child.
     * <pre>
     * <h3>Attribute null:signature documentation</h3>
     * Add here, as comma separated list, the attribute qualifiers for the constructor signature in the model.
     * </pre>
     *
     * @return the value of the signature child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("signature")
    @Required
    GenericAttributeValue<String> getSignature();


}
