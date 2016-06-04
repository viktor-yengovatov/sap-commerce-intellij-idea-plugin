// Generated on Sun Jun 05 00:22:21 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import org.jetbrains.annotations.NotNull;

/**
 * null:enumModelType interface.
 * <pre>
 * <h3>Type null:enumModelType documentation</h3>
 * Configures a single enum model pojo.
 * </pre>
 */
public interface EnumModel extends DomElement {

    /**
     * Returns the value of the package child.
     * <pre>
     * <h3>Attribute null:package documentation</h3>
     * Defines the package for the actual enum model pojo.
     * </pre>
     *
     * @return the value of the package child.
     */
    @NotNull
    @com.intellij.util.xml.Attribute("package")
    GenericAttributeValue<String> getPackage();


}
