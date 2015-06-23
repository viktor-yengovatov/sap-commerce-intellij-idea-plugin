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
