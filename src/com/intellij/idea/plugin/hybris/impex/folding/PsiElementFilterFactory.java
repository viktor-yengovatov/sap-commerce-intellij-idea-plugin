/*
 * This file is part of "Hybris Integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2015 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
