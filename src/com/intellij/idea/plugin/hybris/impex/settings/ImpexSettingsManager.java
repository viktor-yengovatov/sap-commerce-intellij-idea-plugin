package com.intellij.idea.plugin.hybris.impex.settings;

import org.jetbrains.annotations.NotNull;

/**
 * Created 19:58 29 March 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public interface ImpexSettingsManager {

    @NotNull
    ImpexSettingsData getImpexSettingsData();

    void saveImpexSettingsData();
}
