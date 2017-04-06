/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2016 Alexander Bartash <AlexanderBartash@gmail.com>
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

package com.intellij.idea.plugin.hybris.impex.find.findUsages;

import com.intellij.idea.plugin.hybris.impex.ImpexLexerAdapter;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils;
import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

import static com.intellij.psi.tree.TokenSet.create;
import static com.intellij.psi.tree.TokenSet.orSet;

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
public class ImpexFindUsagesProvider implements FindUsagesProvider {

    @Nullable
    @Override
    public WordsScanner getWordsScanner() {
        return new DefaultWordsScanner(
            new ImpexLexerAdapter(),
            orSet(
                create(ImpexTypes.MACRO_NAME_DECLARATION),
                create(ImpexTypes.MACRO_DECLARATION),
                create(ImpexTypes.MACRO_USAGE)
            ),
            create(ImpexTypes.COMMENT),
            getTokenSetAny()
        );
    }

    private TokenSet getTokenSetAny() {
        final TokenSet[] any = Arrays.stream(ImpexTypes.class.getDeclaredFields())
                                     .filter(field -> field.getType().equals(IElementType.class))
                                     .map(field -> {
                                         try {
                                             return (IElementType) field.get(null);
                                         } catch (IllegalAccessException e) {
                                             return null;
                                         }
                                     })
                                     .filter(element -> element instanceof IElementType)
                                     .map(element -> create(element))
                                     .toArray(TokenSet[]::new);
        return orSet(any);
    }

    @Override
    public boolean canFindUsagesFor(@NotNull final PsiElement psiElement) {
        return psiElement instanceof PsiNamedElement;
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull final PsiElement psiElement) {
        return null;
    }

    @NotNull
    @Override
    public String getType(@NotNull final PsiElement element) {
        if (ImpexPsiUtils.isMacroUsage(element)) {
            return "macros usage";
        }
        if (ImpexPsiUtils.isMacroNameDeclaration(element)) {
            return "macros declaration";
        }
        return "unknown";
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull final PsiElement element) {
        return element.getText() + " desc";
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull final PsiElement element, final boolean useFullName) {
        return element.getText();
    }
}
