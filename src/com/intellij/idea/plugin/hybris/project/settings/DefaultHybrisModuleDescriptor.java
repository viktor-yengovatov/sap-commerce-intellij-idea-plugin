package com.intellij.idea.plugin.hybris.project.settings;

import com.intellij.idea.plugin.hybris.project.exceptions.HybrisConfigurationException;
import com.intellij.idea.plugin.hybris.project.settings.jaxb.ExtensionInfo;
import com.intellij.idea.plugin.hybris.project.utils.HybrisProjectUtils;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created 3:55 PM 13 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class DefaultHybrisModuleDescriptor implements HybrisModuleDescriptor {

    @NotNull
    protected final String moduleName;
    @NotNull
    protected final File rootDirectory;
    @NotNull
    protected final File moduleFile;
    @NotNull
    protected final File hybrisProjectFile;
    @NotNull
    protected final ExtensionInfo extensionInfo;
    @NotNull
    protected final Set<HybrisModuleDescriptor> dependenciesTree = new HashSet<HybrisModuleDescriptor>(0);

    public DefaultHybrisModuleDescriptor(@NotNull final File moduleRootDirectory) throws HybrisConfigurationException {
        Validate.notNull(moduleRootDirectory);

        this.rootDirectory = moduleRootDirectory;

        if (!this.rootDirectory.isDirectory()) {
            throw new HybrisConfigurationException("Can not find module directory using path: " + moduleRootDirectory);
        }

        this.hybrisProjectFile = new File(moduleRootDirectory, HybrisConstants.EXTENSION_INFO_XML);

        final ExtensionInfo extensionInfo = HybrisProjectUtils.unmarshalExtensionInfo(this.hybrisProjectFile);
        if (null == extensionInfo) {
            throw new HybrisConfigurationException("Can not unmarshal " + this.hybrisProjectFile);
        }

        this.extensionInfo = extensionInfo;

        if (null == extensionInfo.getExtension() || isBlank(extensionInfo.getExtension().getName())) {
            throw new HybrisConfigurationException("Can not find module name using path: " + moduleRootDirectory);
        }

        this.moduleName = extensionInfo.getExtension().getName();
        this.moduleFile = new File(moduleRootDirectory, this.moduleName + HybrisConstants.NEW_MODULE_FILE_EXTENSION);
    }

    @Override
    public int compareTo(@NotNull final HybrisModuleDescriptor o) {
        return this.getModuleName().compareToIgnoreCase(o.getModuleName());
    }

    @Override
    @NotNull
    public String getModuleName() {
        return moduleName;
    }

    @Override
    @NotNull
    public File getRootDirectory() {
        return rootDirectory;
    }

    @Override
    @NotNull
    public File getModuleFile() {
        return moduleFile;
    }

    @Override
    @NotNull
    public ExtensionInfo getExtensionInfo() {
        return extensionInfo;
    }

    @Override
    @NotNull
    public Set<HybrisModuleDescriptor> getDependenciesTree() {
        return dependenciesTree;
    }

    @Override
    @NotNull
    public Set<HybrisModuleDescriptor> getDependenciesPlainList() {
        return HybrisProjectUtils.getDependenciesPlainSet(this);
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
            .append(moduleName)
            .append(rootDirectory)
            .toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (null == obj || getClass() != obj.getClass()) {
            return false;
        }

        final DefaultHybrisModuleDescriptor other = (DefaultHybrisModuleDescriptor) obj;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
            .append(moduleName, other.moduleName)
            .append(rootDirectory, other.rootDirectory)
            .isEquals();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultHybrisModuleDescriptor{");
        sb.append("moduleName='").append(moduleName).append('\'');
        sb.append(", rootDirectory=").append(rootDirectory);
        sb.append(", moduleFile=").append(moduleFile);
        sb.append(", hybrisProjectFile=").append(hybrisProjectFile);
        sb.append('}');
        return sb.toString();
    }
}
