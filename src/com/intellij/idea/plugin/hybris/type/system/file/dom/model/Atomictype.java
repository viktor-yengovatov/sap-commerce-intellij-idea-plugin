// Generated on Sun Jun 05 00:22:21 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;
import org.jetbrains.annotations.NotNull;

/**
 * null:atomictypeType interface.
 * <pre>
 * <h3>Type null:atomictypeType documentation</h3>
 * An AtomicType represents a simple java object. (The name 'atomic' just means 'non-composed' objects.)
 * </pre>
 */
public interface Atomictype extends DomElement {

    /**
     * Returns the value of the class child.
     * <pre>
     * <h3>Attribute null:class documentation</h3>
     * Corresponding Java class in the hybris Suite; will also be used as the code of the atomic type.
     * </pre>
     *
     * @return the value of the class child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("class")
    @Required
    GenericAttributeValue<String> getClazz();


    /**
     * Returns the value of the autocreate child.
     * <pre>
     * <h3>Attribute null:autocreate documentation</h3>
     * If 'true', the AtomicType will be created during initialization.
     * </pre>
     *
     * @return the value of the autocreate child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("autocreate")
    GenericAttributeValue<Boolean> getAutocreate();


    /**
     * Returns the value of the generate child.
     * <pre>
     * <h3>Attribute null:generate documentation</h3>
     * Deprecated. Has no effect for atomic types. Default is 'true'.
     * </pre>
     *
     * @return the value of the generate child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("generate")
    GenericAttributeValue<Boolean> getGenerate();


    /**
     * Returns the value of the extends child.
     * <pre>
     * <h3>Attribute null:extends documentation</h3>
     * Defines the class which will be extended. Default is 'java.lang.Object'.
     * </pre>
     *
     * @return the value of the extends child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("extends")
    GenericAttributeValue<String> getExtends();


}
