package com.intellij.idea.plugin.hybris.project.settings;

import com.intellij.idea.plugin.hybris.project.exceptions.HybrisConfigurationException;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created 12:46 PM 20 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public abstract class AbstractHybrisModuleDescriptor implements HybrisModuleDescriptor {

    @NotNull
    protected final File rootDirectory;
    @NotNull
    protected final Set<HybrisModuleDescriptor> dependenciesTree = new HashSet<HybrisModuleDescriptor>(0);

    public AbstractHybrisModuleDescriptor(@NotNull final File moduleRootDirectory) throws HybrisConfigurationException {
        Validate.notNull(moduleRootDirectory);

        this.rootDirectory = moduleRootDirectory;

        if (!this.rootDirectory.isDirectory()) {
            throw new HybrisConfigurationException("Can not find module directory using path: " + moduleRootDirectory);
        }
    }

    @Override
    public int compareTo(@NotNull final HybrisModuleDescriptor o) {
        return this.getModuleName().compareToIgnoreCase(o.getModuleName());
    }

    @NotNull
    @Override
    public File getRootDirectory() {
        return rootDirectory;
    }

    @NotNull
    @Override
    public File getIdeaModuleFile() {
        return new File(this.rootDirectory, this.getModuleName() + HybrisConstants.NEW_IDEA_MODULE_FILE_EXTENSION);
    }

    @NotNull
    @Override
    public Set<HybrisModuleDescriptor> getDependenciesTree() {
        return dependenciesTree;
    }

    @Override
    public void setDependenciesTree(@NotNull final Set<HybrisModuleDescriptor> moduleDescriptors) {
        Validate.notNull(moduleDescriptors);

        this.dependenciesTree.clear();
        this.dependenciesTree.addAll(moduleDescriptors);
    }

    @NotNull
    @Override
    public Set<HybrisModuleDescriptor> getDependenciesPlainList() {
        return Collections.unmodifiableSet(this.recursivelyCollectDependenciesPlainSet(
            this, new HashSet<HybrisModuleDescriptor>()
        ));
    }

    @NotNull
    protected Set<HybrisModuleDescriptor> recursivelyCollectDependenciesPlainSet(
        @NotNull final HybrisModuleDescriptor descriptor,
        @NotNull final Set<HybrisModuleDescriptor> dependenciesSet
    ) {
        Validate.notNull(descriptor);
        Validate.notNull(dependenciesSet);

        if (CollectionUtils.isEmpty(descriptor.getDependenciesTree())) {
            return Collections.emptySet();
        }

        for (HybrisModuleDescriptor moduleDescriptor : descriptor.getDependenciesTree()) {
            if (dependenciesSet.contains(moduleDescriptor)) {
                continue;
            }

            dependenciesSet.add(moduleDescriptor);
            dependenciesSet.addAll(recursivelyCollectDependenciesPlainSet(moduleDescriptor, dependenciesSet));
        }

        return dependenciesSet;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(this.getModuleName())
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

        final AbstractHybrisModuleDescriptor other = (AbstractHybrisModuleDescriptor) obj;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
            .append(this.getModuleName(), other.getModuleName())
            .append(rootDirectory, other.rootDirectory)
            .isEquals();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ConfigHybrisModuleDescriptor{");
        sb.append("moduleName='").append(this.getModuleName()).append('\'');
        sb.append(", rootDirectory=").append(rootDirectory);
        sb.append(", moduleFile=").append(this.getIdeaModuleFile());
        sb.append('}');
        return sb.toString();
    }
}
