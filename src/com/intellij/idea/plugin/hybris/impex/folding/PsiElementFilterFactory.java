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

import com.intellij.idea.plugin.hybris.impex.folding.simple.DefaultFoldingBlocksFilter;
import com.intellij.idea.plugin.hybris.impex.folding.smart.SmartFoldingBlocksFilter;
import com.intellij.idea.plugin.hybris.impex.settings.ImpexSettingsManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.util.PsiElementFilter;

/**
 * Created 22:32 29 March 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class PsiElementFilterFactory {

    private static final PsiElementFilter DEFAULT_FOLDING_BLOCKS_FILTER = new DefaultFoldingBlocksFilter();
    private static final PsiElementFilter SMART_FOLDING_BLOCKS_FILTER = new SmartFoldingBlocksFilter();

    public static PsiElementFilter getPsiElementFilter() {
        return isUseSmartFolding() ? SMART_FOLDING_BLOCKS_FILTER : DEFAULT_FOLDING_BLOCKS_FILTER;
    }

    private static boolean isUseSmartFolding() {
        final ImpexSettingsManager settingsManager = ApplicationManager.getApplication().getComponent(
                ImpexSettingsManager.class
        );

        return settingsManager.getImpexSettingsData().isUseSmartFolding();
    }

}
