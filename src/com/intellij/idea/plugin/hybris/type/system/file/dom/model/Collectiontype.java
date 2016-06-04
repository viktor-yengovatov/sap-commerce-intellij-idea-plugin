// Generated on Sun Jun 05 01:21:13 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;
import org.jetbrains.annotations.NotNull;

/**
 * null:collectiontypeType interface.
 * <pre>
 * <h3>Type null:collectiontypeType documentation</h3>
 * A CollectionType defines a collection of typed elements. Attention: If using a collection type for persistent attributes (not jalo) you can not search on that attribute and you are limited in size of collection. Consider to use a relation instead.
 * </pre>
 */
public interface CollectionType extends DomElement {

    /**
     * Returns the value of the code child.
     * <pre>
     * <h3>Attribute null:code documentation</h3>
     * The code (that is, qualifier) of the CollectionType.
     * </pre>
     *
     * @return the value of the code child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("code")
    @Required
    GenericAttributeValue<String> getCode();


    /**
     * Returns the value of the elementtype child.
     * <pre>
     * <h3>Attribute null:elementtype documentation</h3>
     * The type of elements of this CollectionType.
     * </pre>
     *
     * @return the value of the elementtype child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("elementtype")
    @Required
    GenericAttributeValue<String> getElementType();


    /**
     * Returns the value of the autocreate child.
     * <pre>
     * <h3>Attribute null:autocreate documentation</h3>
     * If 'true', the CollectionType will be created during initialization.
     * </pre>
     *
     * @return the value of the autocreate child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("autocreate")
    GenericAttributeValue<Boolean> getAutoCreate();


    /**
     * Returns the value of the generate child.
     * <pre>
     * <h3>Attribute null:generate documentation</h3>
     * Deprecated. Has no effect for collection types. Default is 'true'.
     * </pre>
     *
     * @return the value of the generate child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("generate")
    GenericAttributeValue<Boolean> getGenerate();


    /**
     * Returns the value of the type child.
     * <pre>
     * <h3>Attribute null:type documentation</h3>
     * Configures the type of this collection: 'set', 'list', 'collection'. The getter / setter methods will use corresponding Java collection interfaces. Default is 'collection'.
     * </pre>
     *
     * @return the value of the type child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("type")
    GenericAttributeValue<Type> getType();


}
