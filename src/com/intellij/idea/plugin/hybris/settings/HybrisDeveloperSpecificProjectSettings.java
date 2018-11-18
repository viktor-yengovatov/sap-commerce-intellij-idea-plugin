package com.intellij.idea.plugin.hybris.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HybrisDeveloperSpecificProjectSettings {

    protected String activeRemoteConnectionID;
    protected String activeSolrConnectionID;
    protected List<HybrisRemoteConnectionSettings> remoteConnectionSettingsList = new ArrayList<>();
    protected List<SolrConnectionSettings> solrConnectionSettingsList = new ArrayList<>();

    public List<HybrisRemoteConnectionSettings> getRemoteConnectionSettingsList() {
        return remoteConnectionSettingsList;
    }

    public void setRemoteConnectionSettingsList(final List<HybrisRemoteConnectionSettings> remoteConnectionSettingsList) {
        this.remoteConnectionSettingsList = remoteConnectionSettingsList;
    }

    public String getActiveRemoteConnectionID() {
        return activeRemoteConnectionID;
    }

    public void setActiveRemoteConnectionID(final String activeRemoteConnectionID) {
        this.activeRemoteConnectionID = activeRemoteConnectionID;
    }

    public String getActiveSolrConnectionID() {
        return activeSolrConnectionID;
    }

    public void setActiveSolrConnectionID(final String activeSolrConnectionID) {
        this.activeSolrConnectionID = activeSolrConnectionID;
    }

    public List<SolrConnectionSettings> getSolrConnectionSettingsList() {
        return solrConnectionSettingsList;
    }

    public void setSolrConnectionSettingsList(final List<SolrConnectionSettings> solrConnectionSettingsList) {
        this.solrConnectionSettingsList = solrConnectionSettingsList;
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
        return getActiveRemoteConnectionID().equals(that.getActiveRemoteConnectionID()) &&
               getActiveSolrConnectionID().equals(that.getActiveSolrConnectionID()) &&
               Objects.equals(getRemoteConnectionSettingsList(), that.getRemoteConnectionSettingsList()) &&
               Objects.equals(getSolrConnectionSettingsList(), that.getSolrConnectionSettingsList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            getActiveRemoteConnectionID(),
            getRemoteConnectionSettingsList(),
            getSolrConnectionSettingsList(),
            getActiveSolrConnectionID()
        );
    }
}
