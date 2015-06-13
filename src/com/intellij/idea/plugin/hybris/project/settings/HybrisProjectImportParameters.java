package com.intellij.idea.plugin.hybris.project.settings;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/**
 * Created 3:55 PM 13 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisProjectImportParameters {

    @Nullable
    protected String rootProjectAbsolutePath;
    @Nullable
    protected List<String> foundModulesRootsAbsolutePaths;
    @Nullable
    protected List<String> chosenForImportModulesRootsAbsolutePaths;
    @Nullable
    protected Set<String> alreadyOpenedModulesNames;
    protected boolean openProjectSettingsAfterImport;

    @Nullable
    public String getRootProjectAbsolutePath() {
        return rootProjectAbsolutePath;
    }

    public void setRootProjectAbsolutePath(@Nullable final String rootProjectAbsolutePath) {
        this.rootProjectAbsolutePath = rootProjectAbsolutePath;
    }

    @Nullable
    public List<String> getFoundModulesRootsAbsolutePaths() {
        return foundModulesRootsAbsolutePaths;
    }

    public void setFoundModulesRootsAbsolutePaths(@Nullable final List<String> foundModulesRootsAbsolutePaths) {
        this.foundModulesRootsAbsolutePaths = foundModulesRootsAbsolutePaths;
    }

    @Nullable
    public List<String> getChosenForImportModulesRootsAbsolutePaths() {
        return chosenForImportModulesRootsAbsolutePaths;
    }

    public void setChosenForImportModulesRootsAbsolutePaths(@Nullable final List<String> chosenForImportModulesRootsAbsolutePaths) {
        this.chosenForImportModulesRootsAbsolutePaths = chosenForImportModulesRootsAbsolutePaths;
    }

    @Nullable
    public Set<String> getAlreadyOpenedModulesNames() {
        return alreadyOpenedModulesNames;
    }

    public void setAlreadyOpenedModulesNames(@Nullable final Set<String> alreadyOpenedModulesNames) {
        this.alreadyOpenedModulesNames = alreadyOpenedModulesNames;
    }

    public boolean isOpenProjectSettingsAfterImport() {
        return openProjectSettingsAfterImport;
    }

    public void setOpenProjectSettingsAfterImport(final boolean openProjectSettingsAfterImport) {
        this.openProjectSettingsAfterImport = openProjectSettingsAfterImport;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(rootProjectAbsolutePath)
            .append(foundModulesRootsAbsolutePaths)
            .append(chosenForImportModulesRootsAbsolutePaths)
            .append(alreadyOpenedModulesNames)
            .append(openProjectSettingsAfterImport)
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

        final HybrisProjectImportParameters other = (HybrisProjectImportParameters) obj;

        return new EqualsBuilder()
            .append(openProjectSettingsAfterImport, other.openProjectSettingsAfterImport)
            .append(rootProjectAbsolutePath, other.rootProjectAbsolutePath)
            .append(foundModulesRootsAbsolutePaths, other.foundModulesRootsAbsolutePaths)
            .append(chosenForImportModulesRootsAbsolutePaths, other.chosenForImportModulesRootsAbsolutePaths)
            .append(alreadyOpenedModulesNames, other.alreadyOpenedModulesNames)
            .isEquals();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HybrisProjectImportParameters{");
        sb.append("rootProjectAbsolutePath='").append(rootProjectAbsolutePath).append('\'');
        sb.append(", foundModulesRootsAbsolutePaths=").append(foundModulesRootsAbsolutePaths);
        sb.append(", chosenForImportModulesRootsAbsolutePaths=").append(chosenForImportModulesRootsAbsolutePaths);
        sb.append(", alreadyOpenedModulesNames=").append(alreadyOpenedModulesNames);
        sb.append(", openProjectSettingsAfterImport=").append(openProjectSettingsAfterImport);
        sb.append('}');
        return sb.toString();
    }
}
