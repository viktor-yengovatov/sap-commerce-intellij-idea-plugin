package com.intellij.idea.plugin.hybris.project.settings;

import com.intellij.idea.plugin.hybris.project.utils.HybrisProjectUtils;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.GuardedBy;
import java.io.File;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created 3:55 PM 13 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class DefaultHybrisImportParameters implements HybrisImportParameters {

    @Nullable
    protected final Project project;
    @NotNull
    protected final List<HybrisModuleDescriptor> foundModules = new ArrayList<HybrisModuleDescriptor>();
    @NotNull
    protected final List<HybrisModuleDescriptor> modulesChosenForImport = new ArrayList<HybrisModuleDescriptor>();
    @NotNull
    @GuardedBy("lock")
    protected final Set<HybrisModuleDescriptor> alreadyOpenedModules = new HashSet<HybrisModuleDescriptor>();
    protected final Lock lock = new ReentrantLock();
    @Nullable
    protected File rootDirectory;
    protected boolean openProjectSettingsAfterImport;

    public DefaultHybrisImportParameters() {
        this.project = null;
    }

    public DefaultHybrisImportParameters(@Nullable final Project project) {
        this.project = project;
    }

    @Override
    @Nullable
    public Project getProject() {
        return project;
    }

    @Override
    @NotNull
    public List<HybrisModuleDescriptor> getFoundModules() {
        return foundModules;
    }

    @Override
    @NotNull
    public List<HybrisModuleDescriptor> getModulesChosenForImport() {
        return modulesChosenForImport;
    }

    @Override
    @NotNull
    public Set<HybrisModuleDescriptor> getAlreadyOpenedModules() {
        if (null == this.project) {
            return Collections.emptySet();
        }

        this.lock.lock();

        try {
            if (this.alreadyOpenedModules.isEmpty()) {
                this.alreadyOpenedModules.addAll(HybrisProjectUtils.getAlreadyOpenedModules(this.project));
            }
        } finally {
            this.lock.unlock();
        }

        return Collections.unmodifiableSet(this.alreadyOpenedModules);
    }

    @Override
    @Nullable
    public File getRootDirectory() {
        return rootDirectory;
    }

    @Override
    public void setRootDirectory(@Nullable final File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    @Override
    public boolean isOpenProjectSettingsAfterImport() {
        return openProjectSettingsAfterImport;
    }

    @Override
    public void setOpenProjectSettingsAfterImport(final boolean openProjectSettingsAfterImport) {
        this.openProjectSettingsAfterImport = openProjectSettingsAfterImport;
    }
}
