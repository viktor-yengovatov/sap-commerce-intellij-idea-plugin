/*
 * Copyright 2015 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.idea.plugin.hybris.project.settings;

import com.google.common.collect.Sets;
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
import java.util.*;

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

    public DefaultHybrisModuleDescriptor(@NotNull final File moduleRootDirectory,
                                         @NotNull final HybrisProjectDescriptor rootProjectDescriptor
    ) throws HybrisConfigurationException {
        super(moduleRootDirectory, rootProjectDescriptor);

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
    public String getName() {
        return moduleName;
    }

    @NotNull
    @Override
    public Set<String> getRequiredExtensionNames() {
        if (null == this.extensionInfo.getExtension()) {
            return Collections.unmodifiableSet(Sets.newHashSet(HybrisConstants.PLATFORM_EXTENSION_NAME));
        }

        final List<RequiresExtensionType> requiresExtensions = this.extensionInfo.getExtension().getRequiresExtension();

        if (isEmpty(requiresExtensions)) {
            return Collections.unmodifiableSet(Sets.newHashSet(HybrisConstants.PLATFORM_EXTENSION_NAME));
        }

        final Set<String> requiredExtensionNames = new HashSet<String>(requiresExtensions.size());

        for (RequiresExtensionType requiresExtension : requiresExtensions) {
            requiredExtensionNames.add(requiresExtension.getName());
        }

        requiredExtensionNames.add(HybrisConstants.PLATFORM_EXTENSION_NAME);

        return Collections.unmodifiableSet(requiredExtensionNames);
    }

    @NotNull
    @Override
    public List<JavaLibraryDescriptor> getLibraryDescriptors() {
        return Arrays.<JavaLibraryDescriptor>asList(
            new DefaultJavaLibraryDescriptor(
                new File(this.getRootDirectory(), HybrisConstants.BIN_DIRECTORY), true
            ),
            new DefaultJavaLibraryDescriptor(
                new File(this.getRootDirectory(), HybrisConstants.WEB_INF_LIB_DIRECTORY)
            ),
            new DefaultJavaLibraryDescriptor(
                new File(this.getRootDirectory(), HybrisConstants.HMC_LIB_DIRECTORY)
            ),
            new DefaultJavaLibraryDescriptor(
                new File(this.getRootDirectory(), HybrisConstants.BACKOFFICE_LIB_DIRECTORY)
            ),
            new DefaultJavaLibraryDescriptor(
                new File(this.getRootDirectory(), HybrisConstants.WEB_INF_CLASSES_DIRECTORY), true, true
            )
        );
    }

    @Override
    public boolean isPreselected() {
        //TODO check if this extension name is stated in hybris/config/localextensions.xml
        return getRelativePath().contains("bin"+File.separator+"custom");
    }
}
