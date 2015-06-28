/*
 * Copyright 2015 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.idea.plugin.hybris.settings;

import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created 6:43 PM 28 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
@State(
    name = "HybrisProjectSettings",
    storages = {
        @Storage(file = StoragePathMacros.PROJECT_FILE, scheme = StorageScheme.DEFAULT),
        @Storage(file = StoragePathMacros.PROJECT_CONFIG_DIR + '/' + HybrisConstants.HYBRIS_PROJECT_SETTINGS_FILE_NAME, scheme = StorageScheme.DIRECTORY_BASED)
    }
)
public class HybrisProjectSettingsComponent implements PersistentStateComponent<HybrisProjectSettings> {

    protected final HybrisProjectSettings hybrisProjectSettings = new HybrisProjectSettings();

    public static HybrisProjectSettingsComponent getInstance(@NotNull final Project project) {
        Validate.notNull(project);

        return ServiceManager.getService(project, HybrisProjectSettingsComponent.class);
    }

    @Nullable
    @Override
    public HybrisProjectSettings getState() {
        return this.hybrisProjectSettings;
    }

    @Override
    public void loadState(final HybrisProjectSettings state) {
        XmlSerializerUtil.copyBean(state, this.hybrisProjectSettings);
    }
}
