// Generated on Sun Jun 05 01:21:13 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

/**
 * null:cardinalityAttrType enumeration.
 */
public enum Cardinality implements com.intellij.util.xml.NamedEnum {
    MANY("many"),
    ONE("one");

    private final String value;

    Cardinality(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
