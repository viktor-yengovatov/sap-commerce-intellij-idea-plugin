package com.intellij.idea.plugin.hybris.impex.settings;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Created 19:39 29 March 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexSettingsManagerComponent implements ApplicationComponent, ImpexSettingsManager {

    protected final ImpexSettingsData settingsData = new ImpexSettingsData();

    @Override
    public void initComponent() {
        PropertiesComponent.getInstance().loadFields(this.settingsData);
    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return this.getClass().getName();
    }

    @NotNull
    @Override
    public ImpexSettingsData getImpexSettingsData() {
        return this.settingsData;
    }

    @Override
    public void saveImpexSettingsData() {
        PropertiesComponent.getInstance().saveFields(this.settingsData);
    }
}
