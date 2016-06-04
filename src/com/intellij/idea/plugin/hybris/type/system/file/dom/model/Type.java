// Generated on Sun Jun 05 01:21:13 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

/**
 * null:typeAttrType enumeration.
 */
public enum Type implements com.intellij.util.xml.NamedEnum {
    COLLECTION("collection"),
    LIST("list"),
    SET("set");

    private final String value;

    Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
