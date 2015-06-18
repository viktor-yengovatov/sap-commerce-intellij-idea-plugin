package com.intellij.idea.plugin.hybris.project.settings.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * Configures an hMC module for the extension. Required directory: /web.
 * <p/>
 * <p>Java class for webmoduleType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="webmoduleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="webroot" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="additionalclasspath" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="jspcompile" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="sourceavailable" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "webmoduleType")
public class WebModuleType {

    @XmlAttribute(required = true)
    protected String webroot;
    @XmlAttribute
    protected String additionalclasspath;
    @XmlAttribute
    protected Boolean jspcompile;
    @XmlAttribute
    protected Boolean sourceavailable;

    /**
     * Gets the value of the webroot property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getWebroot() {
        return webroot;
    }

    /**
     * Sets the value of the webroot property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setWebroot(String value) {
        this.webroot = value;
    }

    /**
     * Gets the value of the additionalclasspath property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAdditionalclasspath() {
        return additionalclasspath;
    }

    /**
     * Sets the value of the additionalclasspath property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAdditionalclasspath(String value) {
        this.additionalclasspath = value;
    }

    /**
     * Gets the value of the jspcompile property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isJspcompile() {
        return jspcompile;
    }

    /**
     * Sets the value of the jspcompile property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setJspcompile(Boolean value) {
        this.jspcompile = value;
    }

    /**
     * Gets the value of the sourceavailable property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isSourceavailable() {
        return sourceavailable;
    }

    /**
     * Sets the value of the sourceavailable property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setSourceavailable(Boolean value) {
        this.sourceavailable = value;
    }

}
