package com.intellij.idea.plugin.hybris.project.settings;

import com.intellij.openapi.externalSystem.settings.AbstractExternalSystemSettings;
import com.intellij.openapi.externalSystem.settings.ExternalSystemSettingsListener;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;

/**
 * Created 9:09 PM 07 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisSettings extends AbstractExternalSystemSettings<HybrisSettings, HybrisProjectSettings, HybrisSettingsListener> {

    protected HybrisSettings(final Topic<HybrisSettingsListener> topic, final Project project) {
        super(topic, project);
    }

    @Override
    public void subscribe(@NotNull final ExternalSystemSettingsListener<HybrisProjectSettings> listener) {

    }

    @Override
    protected void copyExtraSettingsFrom(@NotNull final HybrisSettings settings) {

    }

    @Override
    protected void checkSettings(@NotNull final HybrisProjectSettings old, @NotNull final HybrisProjectSettings current) {

    }
}
