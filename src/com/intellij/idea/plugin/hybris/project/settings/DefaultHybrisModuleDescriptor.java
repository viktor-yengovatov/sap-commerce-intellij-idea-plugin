/*
 * This file is part of "Hybris Integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2015 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.intellij.idea.plugin.hybris.project.settings;

import com.google.common.collect.Sets;
import com.intellij.idea.plugin.hybris.project.exceptions.HybrisConfigurationException;
import com.intellij.idea.plugin.hybris.project.settings.jaxb.extensioninfo.ExtensionInfo;
import com.intellij.idea.plugin.hybris.project.settings.jaxb.extensioninfo.RequiresExtensionType;
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
        final List<JavaLibraryDescriptor> libs = new ArrayList<JavaLibraryDescriptor>(5);

        libs.add(new DefaultJavaLibraryDescriptor(new File(this.getRootDirectory(), HybrisConstants.BIN_DIRECTORY), true));
        libs.add(new DefaultJavaLibraryDescriptor(new File(this.getRootDirectory(), HybrisConstants.WEB_INF_LIB_DIRECTORY)));
        libs.add(new DefaultJavaLibraryDescriptor(new File(this.getRootDirectory(), HybrisConstants.HMC_LIB_DIRECTORY)));
        libs.add(new DefaultJavaLibraryDescriptor(new File(this.getRootDirectory(), HybrisConstants.BACKOFFICE_LIB_DIRECTORY)));

        final File webSrcDir = new File(this.getRootDirectory(), HybrisConstants.WEB_SRC_DIRECTORY);
        if (!webSrcDir.exists()) {
            libs.add(new DefaultJavaLibraryDescriptor(
                new File(this.getRootDirectory(), HybrisConstants.WEB_INF_CLASSES_DIRECTORY), true, true
            ));
        }

        return Collections.unmodifiableList(libs);
    }

    public boolean isInCustomDir() {
        return getRelativePath().contains("bin"+File.separator+"custom");
    }

    @Override
    public boolean isPreselected() {
        return isInLocalExtensions() && isInCustomDir();
    }
}
