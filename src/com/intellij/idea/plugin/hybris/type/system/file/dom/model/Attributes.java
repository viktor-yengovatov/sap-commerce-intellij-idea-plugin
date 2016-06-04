// Generated on Sun Jun 05 00:22:21 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTag;
import org.jetbrains.annotations.NotNull;

/**
 * null:attributesType interface.
 * <pre>
 * <h3>Type null:attributesType documentation</h3>
 * Configures a list of attributes.
 * </pre>
 */
public interface Attributes extends DomElement {

    /**
     * Returns the list of attribute children.
     * <pre>
     * <h3>Element null:attribute documentation</h3>
     * Defines a single attribute.
     * </pre>
     *
     * @return the list of attribute children.
     */
    @NotNull
    @SubTag("attribute")
    java.util.List<com.intellij.idea.plugin.hybris.type.system.file.dom.model.Attribute> getAttributes();

    /**
     * Adds new child to the list of attribute children.
     *
     * @return created child
     */
    @SubTag("attribute")
    com.intellij.idea.plugin.hybris.type.system.file.dom.model.Attribute addAttribute();


}
