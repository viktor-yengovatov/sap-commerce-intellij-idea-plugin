// Generated on Sun Jun 05 01:21:13 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.GenericDomValue;
import com.intellij.util.xml.Required;
import com.intellij.util.xml.SubTag;
import com.intellij.util.xml.SubTagList;
import org.jetbrains.annotations.NotNull;

/**
 * null:enumtypeType interface.
 * <pre>
 * <h3>Type null:enumtypeType documentation</h3>
 * An EnumerationType defines fixed value types. (The typesystem provides item enumeration only)
 * </pre>
 */
public interface EnumType extends DomElement {

    /**
     * Returns the value of the code child.
     * <pre>
     * <h3>Attribute null:code documentation</h3>
     * The unique code of this Enumeration.
     * </pre>
     *
     * @return the value of the code child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("code")
    @Required
    GenericAttributeValue<String> getCode();


    /**
     * Returns the value of the autocreate child.
     * <pre>
     * <h3>Attribute null:autocreate documentation</h3>
     * If 'true', the item will be created during initialization.
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
     * If 'false' no constants will be generated at constant class of extension as well as at corresponding servicelayer enum class. Default is 'true'.
     * </pre>
     *
     * @return the value of the generate child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("generate")
    GenericAttributeValue<Boolean> getGenerate();


    /**
     * Returns the value of the jaloclass child.
     * <pre>
     * <h3>Attribute null:jaloclass documentation</h3>
     * Specifies the name of the associated jalo class. The specified class must extend de.hybris.platform.jalo.enumeration.EnumerationValue and will not be generated. By specifying a jalo class you can change the implementation to use for the values of this enumeration. By default EnumerationValue class is used.
     * </pre>
     *
     * @return the value of the jaloclass child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("jaloclass")
    GenericAttributeValue<String> getJaloClass();


    /**
     * Returns the value of the dynamic child.
     * <pre>
     * <h3>Attribute null:dynamic documentation</h3>
     * Whether it is possible to add new values by runtime. Also results in different types of enums: 'true' results in 'classic' hybris enums, 'false' results in Java enums. Default is false. Both kinds of enums are API compatible, and switching between enum types is possible by running a system update.
     * </pre>
     *
     * @return the value of the dynamic child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("dynamic")
    GenericAttributeValue<Boolean> getDynamic();


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


    /**
     * Returns the value of the model child.
     * <pre>
     * <h3>Element null:model documentation</h3>
     * Allows changing enum model settings.
     * </pre>
     *
     * @return the value of the model child.
     */
    @NotNull
    @SubTag("model")
    EnumModel getModel();


    /**
     * Returns the list of value children.
     * <pre>
     * <h3>Element null:value documentation</h3>
     * Configures one value of this Enumeration.
     * </pre>
     *
     * @return the list of value children.
     */
    @NotNull
    @SubTagList("value")
    java.util.List<EnumValue> getValues();

    /**
     * Adds new child to the list of value children.
     *
     * @return created child
     */
    @SubTagList("value")
    EnumValue addValue();


}
