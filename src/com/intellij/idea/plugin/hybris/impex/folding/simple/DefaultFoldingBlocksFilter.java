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

package com.intellij.idea.plugin.hybris.impex.folding.simple;

import com.intellij.idea.plugin.hybris.impex.psi.ImpexModifiers;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexParameters;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiElementFilter;
import org.jetbrains.annotations.Nullable;

import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.isLineBreak;

/**
 * Created 22:40 01 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class DefaultFoldingBlocksFilter implements PsiElementFilter {

    @Override
    public boolean isAccepted(@Nullable final PsiElement eachElement) {
        return null != eachElement && (isFoldable(eachElement) && isNotFoldableParent(eachElement));
    }

    private boolean isFoldable(@Nullable final PsiElement element) {
        if (null == element) {
            return false;
        }

        return element instanceof ImpexModifiers
               || element instanceof ImpexParameters
               || isLineBreak(element);
    }

    private boolean isNotFoldableParent(@Nullable final PsiElement element) {
        if (null == element) {
            return false;
        }

        PsiElement parent = element.getParent();
        while (null != parent) {
            if (isFoldable(parent)) {
                return false;
            }

            parent = parent.getParent();
        }

        return true;
    }
}