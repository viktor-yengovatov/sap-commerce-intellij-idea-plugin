// Generated on Sun Jun 05 01:21:13 EEST 2016
// DTD/Schema  :    null

package com.intellij.idea.plugin.hybris.type.system.file.dom.model;

/**
 * null:creationmodeAttrType enumeration.
 */
public enum CreationMode implements com.intellij.util.xml.NamedEnum {
    ALL("all"),
    FORCE("force"),
    HSQLDB("hsqldb"),
    MYSQL("mysql"),
    ORACLE("oracle"),
    SAP("sap"),
    SQLSERVER("sqlserver");

    private final String value;

    CreationMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
