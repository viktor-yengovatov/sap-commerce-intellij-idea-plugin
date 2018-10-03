package com.intellij.idea.plugin.hybris.settings;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;

public class HybrisRemoteConnectionSettings implements Serializable {

    protected String uuid;
    protected String displayName;
    protected String hostIP;
    protected String port;
    protected String hacWebroot;
    protected String hacLogin;
    protected String hacPassword;
    protected String generatedURL;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public String getHostIP() {
        return hostIP;
    }

    public void setHostIP(final String hostIP) {
        this.hostIP = hostIP;
    }

    public String getPort() {
        return port;
    }

    public void setPort(final String port) {
        this.port = port;
    }

    public String getHacWebroot() {
        return hacWebroot;
    }

    public void setHacWebroot(final String hacWebroot) {
        this.hacWebroot = hacWebroot;
    }

    public String getHacLogin() {
        return hacLogin;
    }

    public void setHacLogin(final String hacLogin) {
        this.hacLogin = hacLogin;
    }

    public String getHacPassword() {
        return hacPassword;
    }

    public void setHacPassword(final String hacPassword) {
        this.hacPassword = hacPassword;
    }

    public String getGeneratedURL() {
        return generatedURL;
    }

    public void setGeneratedURL(final String generatedURL) {
        this.generatedURL = generatedURL;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HybrisRemoteConnectionSettings that = (HybrisRemoteConnectionSettings) o;
        return Objects.equals(getUuid(), that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }

    @Override
    public String toString() {
        if (!StringUtils.isBlank(getDisplayName())) {
            return getDisplayName();
        }
        return getGeneratedURL();
    }
}
