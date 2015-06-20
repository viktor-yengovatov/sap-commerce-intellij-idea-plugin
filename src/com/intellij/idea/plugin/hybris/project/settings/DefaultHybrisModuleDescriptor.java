package com.intellij.idea.plugin.hybris.project.settings;

import com.intellij.idea.plugin.hybris.project.exceptions.HybrisConfigurationException;
import com.intellij.idea.plugin.hybris.project.settings.jaxb.ExtensionInfo;
import com.intellij.idea.plugin.hybris.project.settings.jaxb.RequiresExtensionType;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import com.intellij.openapi.diagnostic.Logger;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created 3:55 PM 13 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class DefaultHybrisModuleDescriptor extends AbstractHybrisModuleDescriptor {

    private static final Logger LOG = Logger.getInstance(DefaultHybrisModuleDescriptor.class);

    @Nullable
    public static final JAXBContext EXTENSION_INFO_JAXB_CONTEXT = getExtensionInfoJaxbContext();

    @NotNull
    protected final String moduleName;
    @NotNull
    protected final ExtensionInfo extensionInfo;

    public DefaultHybrisModuleDescriptor(@NotNull final File moduleRootDirectory) throws HybrisConfigurationException {
        super(moduleRootDirectory);

        Validate.notNull(moduleRootDirectory);

        final File hybrisProjectFile = new File(moduleRootDirectory, HybrisConstants.EXTENSION_INFO_XML);

        final ExtensionInfo extensionInfo = this.unmarshalExtensionInfo(hybrisProjectFile);
        if (null == extensionInfo) {
            throw new HybrisConfigurationException("Can not unmarshal " + hybrisProjectFile);
        }

        this.extensionInfo = extensionInfo;

        if (null == extensionInfo.getExtension() || isBlank(extensionInfo.getExtension().getName())) {
            throw new HybrisConfigurationException("Can not find module name using path: " + moduleRootDirectory);
        }

        this.moduleName = extensionInfo.getExtension().getName();
    }

    @Nullable
    private ExtensionInfo unmarshalExtensionInfo(@NotNull final File file) {
        Validate.notNull(file);

        try {
            if (null == EXTENSION_INFO_JAXB_CONTEXT) {
                LOG.error(String.format(
                    "Can not unmarshal '%s' because JAXBContext has not been created.", file.getAbsolutePath()
                ));

                return null;
            }

            final Unmarshaller jaxbUnmarshaller = EXTENSION_INFO_JAXB_CONTEXT.createUnmarshaller();

            return (ExtensionInfo) jaxbUnmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            LOG.error("Can not unmarshal " + file.getAbsolutePath(), e);
        }

        return null;
    }

    @Nullable
    private static JAXBContext getExtensionInfoJaxbContext() {
        try {
            return JAXBContext.newInstance(ExtensionInfo.class);
        } catch (JAXBException e) {
            LOG.error("Can not create JAXBContext for ExtensionInfo.", e);
        }

        return null;
    }

    @Override
    @NotNull
    public String getModuleName() {
        return moduleName;
    }

    @NotNull
    @Override
    public Set<String> getRequiredExtensionNames() {
        if (null == this.extensionInfo.getExtension()) {
            return Collections.emptySet();
        }

        final List<RequiresExtensionType> requiresExtensions = this.extensionInfo.getExtension().getRequiresExtension();

        if (isEmpty(requiresExtensions)) {
            return Collections.emptySet();
        }

        final Set<String> requiredExtensionNames = new HashSet<String>(requiresExtensions.size());

        for (RequiresExtensionType requiresExtension : requiresExtensions) {
            requiredExtensionNames.add(requiresExtension.getName());
        }

        requiredExtensionNames.add(HybrisConstants.PLATFORM_EXTENSION_NAME);

        return Collections.unmodifiableSet(requiredExtensionNames);
    }
}
