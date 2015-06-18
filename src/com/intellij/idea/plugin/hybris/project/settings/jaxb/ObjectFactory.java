package com.intellij.idea.plugin.hybris.project.settings.jaxb;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.intellij.idea.plugin.hybris.project.settings.jaxb package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.intellij.idea.plugin.hybris.project.settings.jaxb
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ExtensionInfo }
     */
    public ExtensionInfo createExtensioninfo() {
        return new ExtensionInfo();
    }

    /**
     * Create an instance of {@link RequiresExtensionType }
     */
    public RequiresExtensionType createRequiresExtensionType() {
        return new RequiresExtensionType();
    }

    /**
     * Create an instance of {@link WebModuleType }
     */
    public WebModuleType createWebmoduleType() {
        return new WebModuleType();
    }

    /**
     * Create an instance of {@link HmcModuleType }
     */
    public HmcModuleType createHmcmoduleType() {
        return new HmcModuleType();
    }

    /**
     * Create an instance of {@link CoreModuleType }
     */
    public CoreModuleType createCoremoduleType() {
        return new CoreModuleType();
    }

    /**
     * Create an instance of {@link ExtensionType }
     */
    public ExtensionType createExtensionType() {
        return new ExtensionType();
    }

    /**
     * Create an instance of {@link MetaType }
     */
    public MetaType createMetaType() {
        return new MetaType();
    }

}
