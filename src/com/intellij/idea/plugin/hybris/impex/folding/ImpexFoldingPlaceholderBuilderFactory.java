package com.intellij.idea.plugin.hybris.impex.folding;

import com.intellij.idea.plugin.hybris.impex.folding.simple.DefaultImpexFoldingPlaceholderBuilder;
import com.intellij.idea.plugin.hybris.impex.folding.smart.SmartImpexFoldingPlaceholderBuilder;
import com.intellij.idea.plugin.hybris.impex.settings.ImpexSettingsManager;
import com.intellij.openapi.application.ApplicationManager;

/**
 * Created 22:45 29 March 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexFoldingPlaceholderBuilderFactory {

    private static final ImpexFoldingPlaceholderBuilder DEFAULT_IMPEX_FOLDING_PLACEHOLDER_BUILDER = new DefaultImpexFoldingPlaceholderBuilder();
    private static final ImpexFoldingPlaceholderBuilder SMART_IMPEX_FOLDING_PLACEHOLDER_BUILDER = new SmartImpexFoldingPlaceholderBuilder();

    public static ImpexFoldingPlaceholderBuilder getPlaceholderBuilder() {
        return isUseSmartFolding() ? SMART_IMPEX_FOLDING_PLACEHOLDER_BUILDER : DEFAULT_IMPEX_FOLDING_PLACEHOLDER_BUILDER;
    }

    private static boolean isUseSmartFolding() {
        final ImpexSettingsManager settingsManager = ApplicationManager.getApplication().getComponent(
                ImpexSettingsManager.class
        );

        return settingsManager.getImpexSettingsData().isUseSmartFolding();
    }

}
