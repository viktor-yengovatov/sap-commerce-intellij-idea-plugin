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
    protected final File moduleRootDirectory;
    @NotNull
    protected final HybrisProjectDescriptor rootProjectDescriptor;
    @NotNull
    protected final Set<HybrisModuleDescriptor> dependenciesTree = new HashSet<HybrisModuleDescriptor>(0);

    public AbstractHybrisModuleDescriptor(@NotNull final File moduleRootDirectory,
                                          @NotNull final HybrisProjectDescriptor rootProjectDescriptor
    ) throws HybrisConfigurationException {
        Validate.notNull(moduleRootDirectory);
        Validate.notNull(rootProjectDescriptor);

        this.moduleRootDirectory = moduleRootDirectory;
        this.rootProjectDescriptor = rootProjectDescriptor;

        if (!this.moduleRootDirectory.isDirectory()) {
            throw new HybrisConfigurationException("Can not find module directory using path: " + moduleRootDirectory);
        }
    }

    @Override
    public int compareTo(@NotNull final HybrisModuleDescriptor o) {
        return this.getModuleName().compareToIgnoreCase(o.getModuleName());
    }

    @NotNull
    @Override
    public File getModuleRootDirectory() {
        return moduleRootDirectory;
    }

    @NotNull
    @Override
    public String getModuleRelativePath() {
        final File rootDirectory = this.getRootProjectDescriptor().getRootDirectory();
        if (null == rootDirectory) {
            return this.getModuleRootDirectory().getPath();
        } else {
            return this.getModuleRootDirectory().getPath().replaceFirst(rootDirectory.getPath(), "");
        }
    }

    @NotNull
    @Override
    public HybrisProjectDescriptor getRootProjectDescriptor() {
        return rootProjectDescriptor;
    }

    @NotNull
    @Override
    public File getIdeaModuleFile() {
        return new File(this.moduleRootDirectory, this.getModuleName() + HybrisConstants.NEW_IDEA_MODULE_FILE_EXTENSION);
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
            .append(moduleRootDirectory)
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
            .append(moduleRootDirectory, other.moduleRootDirectory)
            .isEquals();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ConfigHybrisModuleDescriptor{");
        sb.append("moduleName='").append(this.getModuleName()).append('\'');
        sb.append(", moduleRootDirectory=").append(moduleRootDirectory);
        sb.append(", moduleFile=").append(this.getIdeaModuleFile());
        sb.append('}');
        return sb.toString();
    }
}
