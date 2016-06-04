// Generated on Sun Jun 05 00:22:21 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.GenericDomValue;
import com.intellij.util.xml.Required;
import org.jetbrains.annotations.NotNull;

/**
 * null:columntypeType interface.
 * <pre>
 * <h3>Type null:columntypeType documentation</h3>
 * Configures a persistence definition for a specific database.
 * </pre>
 */
public interface Columntype extends DomElement {

    /**
     * Returns the value of the database child.
     * <pre>
     * <h3>Attribute null:database documentation</h3>
     * The database the given definition will be used for. One of 'oracle', 'mysql', 'sqlserver' or 'hsql'. Default is empty which configures fallback for non specified databases.
     * </pre>
     *
     * @return the value of the database child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("database")
    GenericAttributeValue<String> getDatabase();


    /**
     * Returns the value of the value child.
     * <pre>
     * <h3>Element null:value documentation</h3>
     * The attribute type used in the create statement of the database table, such as 'varchar2(4000)'.
     * </pre>
     *
     * @return the value of the value child.
     */
    @NotNull
    @Required
    GenericDomValue<String> getValue();


}
