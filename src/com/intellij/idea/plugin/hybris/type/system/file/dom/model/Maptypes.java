// Generated on Sun Jun 05 00:22:21 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTag;
import org.jetbrains.annotations.NotNull;

/**
 * null:maptypesType interface.
 * <pre>
 * <h3>Type null:maptypesType documentation</h3>
 * Specifies a list of map types.
 * </pre>
 */
public interface Maptypes extends DomElement {

    /**
     * Returns the list of maptype children.
     * <pre>
     * <h3>Element null:maptype documentation</h3>
     * Like the java collection framework, a type, which defines map objects. Attention: When used as type for an attribute, the attribute will not be searchable and the access performance is not effective. Consider to use a relation.
     * </pre>
     *
     * @return the list of maptype children.
     */
    @NotNull
    @SubTag("maptype")
    java.util.List<com.intellij.idea.plugin.hybris.type.system.file.dom.model.Maptype> getMaptypes();

    /**
     * Adds new child to the list of maptype children.
     *
     * @return created child
     */
    @SubTag("maptype")
    com.intellij.idea.plugin.hybris.type.system.file.dom.model.Maptype addMaptype();


}
