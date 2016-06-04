// Generated on Sun Jun 05 00:22:21 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTag;
import org.jetbrains.annotations.NotNull;

/**
 * null:collectiontypesType interface.
 * <pre>
 * <h3>Type null:collectiontypesType documentation</h3>
 * Defines a list of collection types.
 * </pre>
 */
public interface Collectiontypes extends DomElement {

    /**
     * Returns the list of collectiontype children.
     * <pre>
     * <h3>Element null:collectiontype documentation</h3>
     * A CollectionType defines a collection of typed elements.
     * </pre>
     *
     * @return the list of collectiontype children.
     */
    @NotNull
    @SubTag("collectiontype")
    java.util.List<com.intellij.idea.plugin.hybris.type.system.file.dom.model.Collectiontype> getCollectiontypes();

    /**
     * Adds new child to the list of collectiontype children.
     *
     * @return created child
     */
    @SubTag("collectiontype")
    com.intellij.idea.plugin.hybris.type.system.file.dom.model.Collectiontype addCollectiontype();


}
