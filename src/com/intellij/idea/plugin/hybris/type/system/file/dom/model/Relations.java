// Generated on Sun Jun 05 00:22:21 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTag;
import org.jetbrains.annotations.NotNull;

/**
 * null:relationsType interface.
 * <pre>
 * <h3>Type null:relationsType documentation</h3>
 * Defines a list of relation types.
 * </pre>
 */
public interface Relations extends DomElement {

    /**
     * Returns the list of relation children.
     * <pre>
     * <h3>Element null:relation documentation</h3>
     * A RelationType defines a n-m or 1-n relation between types.
     * </pre>
     *
     * @return the list of relation children.
     */
    @NotNull
    @SubTag("relation")
    java.util.List<com.intellij.idea.plugin.hybris.type.system.file.dom.model.Relation> getRelations();

    /**
     * Adds new child to the list of relation children.
     *
     * @return created child
     */
    @SubTag("relation")
    com.intellij.idea.plugin.hybris.type.system.file.dom.model.Relation addRelation();


}
