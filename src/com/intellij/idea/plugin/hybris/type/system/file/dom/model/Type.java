// Generated on Sun Jun 05 00:22:21 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

/**
 * null:typeAttrType enumeration.
 */
public enum Type implements com.intellij.util.xml.NamedEnum {
    COLLECTION("collection"),
    LIST("list"),
    SET("set");

    private final java.lang.String value;

    private Type(java.lang.String value) {
        this.value = value;
    }

    public java.lang.String getValue() {
        return value;
    }

}
