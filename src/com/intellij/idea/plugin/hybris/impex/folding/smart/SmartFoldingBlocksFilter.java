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

package com.intellij.idea.plugin.hybris.impex.folding.smart;

import com.intellij.idea.plugin.hybris.impex.folding.ImpexFoldingPlaceholderBuilderFactory;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexAttribute;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexParameters;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiElementFilter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import static com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils.isLineBreak;

/**
 * Created 22:40 01 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class SmartFoldingBlocksFilter implements PsiElementFilter {

    @Override
    public boolean isAccepted(@Nullable final PsiElement eachElement) {
        return null != eachElement && (isFoldable(eachElement) && isNotFoldableParent(eachElement));
    }

    @Contract(pure = true)
    private boolean isFoldable(@Nullable final PsiElement element) {
        return null != element
               && this.isSupportedType(element)
               && (isLineBreak(element) || this.isNotBlankPlaceholder(element));
    }

    @Contract(pure = true)
    private boolean isNotBlankPlaceholder(final @Nullable PsiElement element) {
        return (null != element) && !StringUtils.isBlank(
                ImpexFoldingPlaceholderBuilderFactory.getPlaceholderBuilder().getPlaceholder(element)
        );
    }

    @Contract(pure = true)
    private boolean isSupportedType(final @Nullable PsiElement element) {
        return element instanceof ImpexAttribute
               || element instanceof ImpexParameters
               || isLineBreak(element);
    }

    @Contract(pure = true)
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