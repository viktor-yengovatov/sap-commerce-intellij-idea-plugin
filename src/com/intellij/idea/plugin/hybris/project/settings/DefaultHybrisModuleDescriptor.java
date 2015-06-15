package com.intellij.idea.plugin.hybris.project.settings;

import com.intellij.idea.plugin.hybris.project.exceptions.HybrisConfigurationException;
import com.intellij.idea.plugin.hybris.project.utils.HybrisProjectUtils;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.File;

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

    public DefaultHybrisModuleDescriptor(@NotNull final String moduleRootAbsolutePath) throws HybrisConfigurationException {
        Validate.notEmpty(moduleRootAbsolutePath);

        this.rootDirectory = new File(moduleRootAbsolutePath);

        if (!this.rootDirectory.isDirectory()) {
            throw new HybrisConfigurationException("Can not find module directory using path: " + moduleRootAbsolutePath);
        }

        final String moduleName = HybrisProjectUtils.getModuleName(moduleRootAbsolutePath);
        if (null == moduleName) {
            throw new HybrisConfigurationException("Can not find module name using path: " + moduleRootAbsolutePath);
        }

        this.moduleName = moduleName;
        this.moduleFile = new File(moduleRootAbsolutePath, moduleName + HybrisConstants.NEW_MODULE_FILE_EXTENSION);
        this.hybrisProjectFile = new File(moduleRootAbsolutePath, HybrisConstants.EXTENSION_INFO_XML);
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
    public File getHybrisProjectFile() {
        return hybrisProjectFile;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
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

        return new EqualsBuilder()
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
