package com.intellij.idea.plugin.hybris.notifications;

import com.intellij.util.messages.Topic;

public interface SolrConfigurationChangeListener {
    Topic<SolrConfigurationChangeListener> TOPIC = Topic.create("SolrConfigurationChangeListener", SolrConfigurationChangeListener.class);

    void configurationChanged();
}
