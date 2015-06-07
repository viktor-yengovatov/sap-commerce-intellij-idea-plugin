package com.intellij.idea.plugin.hybris.project.settings;

import com.intellij.openapi.externalSystem.model.ProjectSystemId;
import com.intellij.openapi.externalSystem.service.settings.AbstractImportFromExternalSystemControl;
import com.intellij.openapi.externalSystem.util.ExternalSystemSettingsControl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created 8:59 PM 07 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImportFromHybrisControl extends AbstractImportFromExternalSystemControl<HybrisProjectSettings, HybrisSettingsListener, HybrisSettings> {

    protected ImportFromHybrisControl(@NotNull final ProjectSystemId externalSystemId,
                                      @NotNull final HybrisSettings systemSettings,
                                      @NotNull final HybrisProjectSettings projectSettings) {
        super(externalSystemId, systemSettings, projectSettings);
    }

    @Override
    protected void onLinkedProjectPathChange(@NotNull final String path) {

    }

    @NotNull
    @Override
    protected ExternalSystemSettingsControl<HybrisProjectSettings> createProjectSettingsControl(@NotNull final HybrisProjectSettings settings) {
        return null;
    }

    @Nullable
    @Override
    protected ExternalSystemSettingsControl<HybrisSettings> createSystemSettingsControl(@NotNull final HybrisSettings settings) {
        return null;
    }
}
