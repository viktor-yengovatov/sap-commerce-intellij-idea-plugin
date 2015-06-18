package com.intellij.idea.plugin.hybris.project.settings;

import com.intellij.idea.plugin.hybris.project.utils.HybrisProjectUtils;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

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
    protected final Set<HybrisModuleDescriptor> alreadyOpenedModules = new HashSet<HybrisModuleDescriptor>();
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

        if (this.alreadyOpenedModules.isEmpty()) {
            this.alreadyOpenedModules.addAll(HybrisProjectUtils.getAlreadyOpenedModules(this.project));
        }

        return this.alreadyOpenedModules;
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
