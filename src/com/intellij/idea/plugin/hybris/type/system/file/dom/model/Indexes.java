// Generated on Sun Jun 05 01:21:13 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.Required;
import com.intellij.util.xml.SubTagList;
import org.jetbrains.annotations.NotNull;

/**
 * null:indexesType interface.
 * <pre>
 * <h3>Type null:indexesType documentation</h3>
 * Configures a list of indexes.
 * </pre>
 */
public interface Indexes extends DomElement {

    /**
     * Returns the list of index children.
     * <pre>
     * <h3>Element null:index documentation</h3>
     * Configures a single index.
     * </pre>
     *
     * @return the list of index children.
     */
    @NotNull
    @SubTagList("index")
    @Required
    java.util.List<Index> getIndexes();

    /**
     * Adds new child to the list of index children.
     *
     * @return created child
     */
    @SubTagList("index")
    Index addIndex();


}
