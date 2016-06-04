// Generated on Sun Jun 05 00:22:21 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;
import com.intellij.util.xml.SubTag;
import org.jetbrains.annotations.NotNull;

/**
 * null:persistenceType interface.
 * <pre>
 * <h3>Type null:persistenceType documentation</h3>
 * Defines how the values of the attribute will be stored. Possible values: 'cmp' (deprecated), 'jalo' (not persistent), and 'property' (persistent).
 * </pre>
 */
public interface Persistence extends DomElement {

    /**
     * Returns the value of the type child.
     * <pre>
     * <h3>Attribute null:type documentation</h3>
     * Defines how the values of the attribute will be stored. Possible values: 'cmp' (deprecated), 'jalo' (not persistent, deprecated), 'property' (persistent), 'dynamic' (not persisted).
     * </pre>
     *
     * @return the value of the type child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("type")
    @Required
    GenericAttributeValue<com.intellij.idea.plugin.hybris.type.system.file.dom.model.Type> getType();


    /**
     * Returns the value of the qualifier child.
     * <pre>
     * <h3>Attribute null:qualifier documentation</h3>
     * Deprecated. Only usable in relation with 'cmp' and 'property'(compatibility reasons) persistence type. Default is empty.
     * </pre>
     *
     * @return the value of the qualifier child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("qualifier")
    GenericAttributeValue<String> getQualifier();


    /**
     * Returns the value of the attributeHandler child.
     * <pre>
     * <h3>Attribute null:attributeHandler documentation</h3>
     * Spring bean id that handles dynamic attributes implementation.
     * </pre>
     *
     * @return the value of the attributeHandler child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("attributeHandler")
    GenericAttributeValue<String> getAttributeHandler();


    /**
     * Returns the list of columntype children.
     * <pre>
     * <h3>Element null:columntype documentation</h3>
     * Configures a persistence definition for a specific database used at create statement.
     * </pre>
     *
     * @return the list of columntype children.
     */
    @NotNull
    @SubTag("columntype")
    java.util.List<com.intellij.idea.plugin.hybris.type.system.file.dom.model.Columntype> getColumntypes();

    /**
     * Adds new child to the list of columntype children.
     *
     * @return created child
     */
    @SubTag("columntype")
    com.intellij.idea.plugin.hybris.type.system.file.dom.model.Columntype addColumntype();


}
