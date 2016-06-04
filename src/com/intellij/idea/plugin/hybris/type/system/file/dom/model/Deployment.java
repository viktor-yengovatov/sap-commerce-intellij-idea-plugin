// Generated on Sun Jun 05 00:22:21 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;
import org.jetbrains.annotations.NotNull;

/**
 * null:deploymentType interface.
 * <pre>
 * <h3>Type null:deploymentType documentation</h3>
 * A deployment defines how a (generic) item or relation is mapped onto the database.
 * </pre>
 */
public interface Deployment extends DomElement {

    /**
     * Returns the value of the table child.
     * <pre>
     * <h3>Attribute null:table documentation</h3>
     * The mapped database table. Must be globally unique.
     * </pre>
     *
     * @return the value of the table child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("table")
    @Required
    GenericAttributeValue<String> getTable();


    /**
     * Returns the value of the typecode child.
     * <pre>
     * <h3>Attribute null:typecode documentation</h3>
     * The mapped item type code. Must be globally unique
     * </pre>
     *
     * @return the value of the typecode child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("typecode")
    @Required
    GenericAttributeValue<String> getTypecode();


    /**
     * Returns the value of the propertytable child.
     * <pre>
     * <h3>Attribute null:propertytable documentation</h3>
     * The mapped dump property database table to be used for this item. Default is 'props'.
     * </pre>
     *
     * @return the value of the propertytable child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("propertytable")
    GenericAttributeValue<String> getPropertytable();


}
