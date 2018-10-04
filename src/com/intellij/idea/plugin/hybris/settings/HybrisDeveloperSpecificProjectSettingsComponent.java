package com.intellij.idea.plugin.hybris.settings;

import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import com.intellij.idea.plugin.hybris.common.services.CommonIdeaService;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.intellij.idea.plugin.hybris.common.HybrisConstants.HYBRIS_DEVELOPER_SPECIFIC_PROJECT_SETTINGS_COMPONENT_NAME;
import static com.intellij.idea.plugin.hybris.common.HybrisConstants.HYBRIS_DEVELOPER_SPECIFIC_PROJECT_SETTINGS_FILE_NAME;

@State(name = HYBRIS_DEVELOPER_SPECIFIC_PROJECT_SETTINGS_COMPONENT_NAME, storages = {@Storage(HYBRIS_DEVELOPER_SPECIFIC_PROJECT_SETTINGS_FILE_NAME)})
public class HybrisDeveloperSpecificProjectSettingsComponent implements PersistentStateComponent<HybrisDeveloperSpecificProjectSettings> {

    private HybrisDeveloperSpecificProjectSettings state = new HybrisDeveloperSpecificProjectSettings();

    public static HybrisDeveloperSpecificProjectSettingsComponent getInstance(@NotNull final Project project) {
        return ServiceManager.getService(project, HybrisDeveloperSpecificProjectSettingsComponent.class);
    }

    @Nullable
    @Override
    public HybrisDeveloperSpecificProjectSettings getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull final HybrisDeveloperSpecificProjectSettings state) {
        XmlSerializerUtil.copyBean(state, this.state);
    }

    public HybrisRemoteConnectionSettings getActiveHybrisRemoteConnectionSettings(final Project project) {
        HybrisDeveloperSpecificProjectSettings state = getState();
        if (state == null) {
            return getDefaultHybrisRemoteConnectionSettings(project);
        }
        List<HybrisRemoteConnectionSettings> remoteList = state.getRemoteConnectionSettingsList();
        if (remoteList.isEmpty()) {
            return getDefaultHybrisRemoteConnectionSettings(project);
        }
        String id = state.getActiveRemoteConnectionID();

        return remoteList.stream().filter(e-> Objects.equals(id, e.getUuid())).findFirst().orElse(remoteList.get(0));
    }

    @NotNull
    public HybrisRemoteConnectionSettings getDefaultHybrisRemoteConnectionSettings(final Project project) {
        HybrisRemoteConnectionSettings item = new HybrisRemoteConnectionSettings();
        item.setUuid(UUID.randomUUID().toString());
        item.setHostIP("localhost");
        item.setPort(HybrisConstants.DEFAULT_TOMCAT_SSL_PORT);
        item.setHacLogin("admin");
        item.setHacPassword("nimda");
        item.setGeneratedURL(CommonIdeaService.getInstance().getHostHacUrl(project, item));
        return item;
    }
}
