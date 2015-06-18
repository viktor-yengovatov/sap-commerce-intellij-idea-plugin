package com.intellij.idea.plugin.hybris.project.settings.jaxb;

import javax.xml.bind.annotation.*;


/**
 * Configures the available modules of the extension.
 * <p/>
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="extension" type="{}extensionType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "extension"
})
@XmlRootElement(name = "extensioninfo")
public class ExtensionInfo {

    @XmlElement(required = true)
    protected ExtensionType extension;

    /**
     * Gets the value of the extension property.
     *
     * @return possible object is
     * {@link ExtensionType }
     */
    public ExtensionType getExtension() {
        return extension;
    }

    /**
     * Sets the value of the extension property.
     *
     * @param value allowed object is
     *              {@link ExtensionType }
     */
    public void setExtension(ExtensionType value) {
        this.extension = value;
    }

}
