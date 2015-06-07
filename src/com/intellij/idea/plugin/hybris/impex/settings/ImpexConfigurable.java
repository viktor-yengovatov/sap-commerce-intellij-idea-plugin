package com.intellij.idea.plugin.hybris.impex.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created 16:02 29 March 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexConfigurable implements Configurable {

    protected final ImpexSettingsForm settingsForm = new ImpexSettingsForm();

    @Nls
    @Override
    public String getDisplayName() {
        return "Impex";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "Impex plugin configuration.";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        final ImpexSettingsManager settingsManager = ApplicationManager.getApplication().getComponent(
                ImpexSettingsManager.class
        );

        this.settingsForm.setData(settingsManager.getImpexSettingsData());

        return this.settingsForm.getMainPanel();
    }

    @Override
    public boolean isModified() {
        final ImpexSettingsManager settingsManager = ApplicationManager.getApplication().getComponent(
                ImpexSettingsManager.class
        );

        return this.settingsForm.isModified(settingsManager.getImpexSettingsData());
    }

    @Override
    public void apply() throws ConfigurationException {
        final ImpexSettingsManager settingsManager = ApplicationManager.getApplication().getComponent(
                ImpexSettingsManager.class
        );

        this.settingsForm.getData(settingsManager.getImpexSettingsData());

        settingsManager.saveImpexSettingsData();
    }

    @Override
    public void reset() {
        final ImpexSettingsManager settingsManager = ApplicationManager.getApplication().getComponent(
                ImpexSettingsManager.class
        );

        this.settingsForm.setData(settingsManager.getImpexSettingsData());
    }

    @Override
    public void disposeUIResources() {
    }
}
