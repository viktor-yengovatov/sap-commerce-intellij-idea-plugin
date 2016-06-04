// Generated on Sun Jun 05 01:21:13 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTag;
import org.jetbrains.annotations.NotNull;

/**
 * null:itemsElemType interface.
 */
public interface Items extends DomElement {

    /**
     * Returns the value of the atomictypes child.
     * <pre>
     * <h3>Element null:atomictypes documentation</h3>
     * Defines the list of AtomicType's for your extension.
     * </pre>
     *
     * @return the value of the atomictypes child.
     */
    @NotNull
    @SubTag("atomictypes")
    Atomictypes getAtomictypes();


    /**
     * Returns the value of the collectiontypes child.
     * <pre>
     * <h3>Element null:collectiontypes documentation</h3>
     * Defines the list of CollectionType's for your extension.
     * </pre>
     *
     * @return the value of the collectiontypes child.
     */
    @NotNull
    @SubTag("collectiontypes")
    Collectiontypes getCollectiontypes();


    /**
     * Returns the value of the enumtypes child.
     * <pre>
     * <h3>Element null:enumtypes documentation</h3>
     * Defines the list of EnumerationType's for your extension.
     * </pre>
     *
     * @return the value of the enumtypes child.
     */
    @NotNull
    @SubTag("enumtypes")
    Enumtypes getEnumtypes();


    /**
     * Returns the value of the maptypes child.
     * <pre>
     * <h3>Element null:maptypes documentation</h3>
     * Defines the list of MapType's for your extension.
     * </pre>
     *
     * @return the value of the maptypes child.
     */
    @NotNull
    @SubTag("maptypes")
    Maptypes getMaptypes();


    /**
     * Returns the value of the relations child.
     * <pre>
     * <h3>Element null:relations documentation</h3>
     * Defines the list of RelationType's for your extension.
     * </pre>
     *
     * @return the value of the relations child.
     */
    @NotNull
    @SubTag("relations")
    Relations getRelations();


    /**
     * Returns the value of the itemtypes child.
     * <pre>
     * <h3>Element null:itemtypes documentation</h3>
     * Defines the list of ComposedType's for your extension.
     * </pre>
     *
     * @return the value of the itemtypes child.
     */
    @NotNull
    @SubTag("itemtypes")
    Itemtypes getItemtypes();


}
