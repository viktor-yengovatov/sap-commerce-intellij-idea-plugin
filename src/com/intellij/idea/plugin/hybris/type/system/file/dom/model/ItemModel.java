// Generated on Sun Jun 05 01:21:13 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.SubTagList;
import org.jetbrains.annotations.NotNull;

/**
 * null:itemModelType interface.
 * <pre>
 * <h3>Type null:itemModelType documentation</h3>
 * Allows to configure model generation for this item used at servicelayer.
 * </pre>
 */
public interface ItemModel extends DomElement {

    /**
     * Returns the value of the generate child.
     * <pre>
     * <h3>Attribute null:generate documentation</h3>
     * Whether a model for the type and models for subtypes will be generated. Default is 'true'.
     * </pre>
     *
     * @return the value of the generate child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("generate")
    GenericAttributeValue<Boolean> getGenerate();


    /**
     * Returns the list of constructor children.
     * <pre>
     * <h3>Element null:constructor documentation</h3>
     * Allows to configure model constructor signatures.
     * </pre>
     *
     * @return the list of constructor children.
     */
    @NotNull
    @SubTagList("constructor")
    java.util.List<ModelConstructor> getConstructors();

    /**
     * Adds new child to the list of constructor children.
     *
     * @return created child
     */
    @SubTagList("constructor")
    ModelConstructor addConstructor();


}
