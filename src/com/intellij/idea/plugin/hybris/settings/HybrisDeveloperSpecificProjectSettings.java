package com.intellij.idea.plugin.hybris.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HybrisDeveloperSpecificProjectSettings {

    protected int activeRemoteConnectionHash;
    protected List<HybrisRemoteConnectionSettings> remoteConnectionSettingsList = new ArrayList<>();

    public List<HybrisRemoteConnectionSettings> getRemoteConnectionSettingsList() {
        return remoteConnectionSettingsList;
    }

    public void setRemoteConnectionSettingsList(final List<HybrisRemoteConnectionSettings> remoteConnectionSettingsList) {
        this.remoteConnectionSettingsList = remoteConnectionSettingsList;
    }

    public int getActiveRemoteConnectionHash() {
        return activeRemoteConnectionHash;
    }

    public void setActiveRemoteConnectionHash(final int activeRemoteConnectionHash) {
        this.activeRemoteConnectionHash = activeRemoteConnectionHash;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HybrisDeveloperSpecificProjectSettings that = (HybrisDeveloperSpecificProjectSettings) o;
        return getActiveRemoteConnectionHash() == that.getActiveRemoteConnectionHash() &&
               Objects.equals(getRemoteConnectionSettingsList(), that.getRemoteConnectionSettingsList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getActiveRemoteConnectionHash(), getRemoteConnectionSettingsList());
    }
}
