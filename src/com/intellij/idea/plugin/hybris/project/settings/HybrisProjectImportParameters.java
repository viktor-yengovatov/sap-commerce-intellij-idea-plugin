package com.intellij.idea.plugin.hybris.project.settings;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created 3:55 PM 13 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisProjectImportParameters {

    protected String root;
    protected List<String> workspace;
    protected List<String> projectsToConvert = new ArrayList<String>();
    protected boolean openModuleSettings;
    @NonNls
    protected String commonModulesDirectory;
    protected Set<String> existingModuleNames;

    public String getRoot() {
        return root;
    }

    public void setRoot(final String root) {
        this.root = root;
    }

    public List<String> getWorkspace() {
        return workspace;
    }

    public void setWorkspace(final List<String> workspace) {
        this.workspace = workspace;
    }

    public List<String> getProjectsToConvert() {
        return projectsToConvert;
    }

    public void setProjectsToConvert(final List<String> projectsToConvert) {
        this.projectsToConvert = projectsToConvert;
    }

    public boolean isOpenModuleSettings() {
        return openModuleSettings;
    }

    public void setOpenModuleSettings(final boolean openModuleSettings) {
        this.openModuleSettings = openModuleSettings;
    }

    public String getCommonModulesDirectory() {
        return commonModulesDirectory;
    }

    public void setCommonModulesDirectory(final String commonModulesDirectory) {
        this.commonModulesDirectory = commonModulesDirectory;
    }

    public Set<String> getExistingModuleNames() {
        return existingModuleNames;
    }

    public void setExistingModuleNames(final Set<String> existingModuleNames) {
        this.existingModuleNames = existingModuleNames;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (null == obj || getClass() != obj.getClass()) {
            return false;
        }

        final HybrisProjectImportParameters other = (HybrisProjectImportParameters) obj;

        return new EqualsBuilder()
            .append(openModuleSettings, other.openModuleSettings)
            .append(root, other.root)
            .append(workspace, other.workspace)
            .append(projectsToConvert, other.projectsToConvert)
            .append(commonModulesDirectory, other.commonModulesDirectory)
            .append(existingModuleNames, other.existingModuleNames)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(root)
            .append(workspace)
            .append(projectsToConvert)
            .append(openModuleSettings)
            .append(commonModulesDirectory)
            .append(existingModuleNames)
            .toHashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HybrisProjectImportParameters{");
        sb.append("root='").append(root).append('\'');
        sb.append(", workspace=").append(workspace);
        sb.append(", projectsToConvert=").append(projectsToConvert);
        sb.append(", openModuleSettings=").append(openModuleSettings);
        sb.append(", commonModulesDirectory='").append(commonModulesDirectory).append('\'');
        sb.append(", existingModuleNames=").append(existingModuleNames);
        sb.append('}');
        return sb.toString();
    }
}
