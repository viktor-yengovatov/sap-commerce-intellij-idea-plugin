// Generated on Sun Jun 05 00:22:21 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTag;
import org.jetbrains.annotations.NotNull;

/**
 * null:enumtypesType interface.
 * <pre>
 * <h3>Type null:enumtypesType documentation</h3>
 * Defines a list of enumeration types.
 * </pre>
 */
public interface Enumtypes extends DomElement {

    /**
     * Returns the list of enumtype children.
     * <pre>
     * <h3>Element null:enumtype documentation</h3>
     * An EnumerationType defines fixed value types. (The typesystem provides item enumeration only)
     * </pre>
     *
     * @return the list of enumtype children.
     */
    @NotNull
    @SubTag("enumtype")
    java.util.List<com.intellij.idea.plugin.hybris.type.system.file.dom.model.Enumtype> getEnumtypes();

    /**
     * Adds new child to the list of enumtype children.
     *
     * @return created child
     */
    @SubTag("enumtype")
    com.intellij.idea.plugin.hybris.type.system.file.dom.model.Enumtype addEnumtype();


}
