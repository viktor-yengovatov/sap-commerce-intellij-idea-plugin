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

package com.intellij.idea.plugin.hybris.impex.formatting;

import com.intellij.formatting.*;
import com.intellij.idea.plugin.hybris.impex.ImpexLanguage;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created 22:33 21 December 2014
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexFormattingModelBuilder implements FormattingModelBuilder {

    @NotNull
    @Override
    public FormattingModel createModel(final PsiElement element, final CodeStyleSettings settings) {

        final Block impexBlock = new ImpexBlock(
                element.getNode(),
                null,
                Alignment.createAlignment(),
                createSpaceBuilder(settings)
        );

        return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(), impexBlock, settings);
    }

    private static SpacingBuilder createSpaceBuilder(final CodeStyleSettings settings) {
        final ImpexCodeStyleSettings impexSettings = settings.getCustomSettings(ImpexCodeStyleSettings.class);

        return new SpacingBuilder(settings, ImpexLanguage.INSTANCE)
                .before(ImpexTypes.VALUE_GROUP)
                .spaceIf(impexSettings.SPACE_BEFORE_FIELD_VALUE_SEPARATOR)

                .after(ImpexTypes.FIELD_VALUE_SEPARATOR)
                .spaceIf(impexSettings.SPACE_AFTER_FIELD_VALUE_SEPARATOR)

                .before(ImpexTypes.PARAMETERS_SEPARATOR)
                .spaceIf(impexSettings.SPACE_BEFORE_PARAMETERS_SEPARATOR)

                .after(ImpexTypes.PARAMETERS_SEPARATOR)
                .spaceIf(impexSettings.SPACE_AFTER_PARAMETERS_SEPARATOR)

                .before(ImpexTypes.ATTRIBUTE_SEPARATOR)
                .spaceIf(impexSettings.SPACE_BEFORE_ATTRIBUTE_SEPARATOR)

                .after(ImpexTypes.COMMA)
                .spaceIf(impexSettings.SPACE_AFTER_COMMA)

                .before(ImpexTypes.COMMA)
                .spaceIf(impexSettings.SPACE_BEFORE_COMMA)

                .after(ImpexTypes.ATTRIBUTE_SEPARATOR)
                .spaceIf(impexSettings.SPACE_AFTER_ATTRIBUTE_SEPARATOR)

                .before(ImpexTypes.FIELD_LIST_ITEM_SEPARATOR)
                .spaceIf(impexSettings.SPACE_BEFORE_FIELD_LIST_ITEM_SEPARATOR)

                .after(ImpexTypes.FIELD_LIST_ITEM_SEPARATOR)
                .spaceIf(impexSettings.SPACE_AFTER_FIELD_LIST_ITEM_SEPARATOR)

                .after(ImpexTypes.ASSIGN_VALUE)
                .spaceIf(impexSettings.SPACE_AFTER_ASSIGN_VALUE)

                .before(ImpexTypes.ASSIGN_VALUE)
                .spaceIf(impexSettings.SPACE_BEFORE_ASSIGN_VALUE)

                .after(ImpexTypes.LEFT_ROUND_BRACKET)
                .spaceIf(impexSettings.SPACE_AFTER_LEFT_ROUND_BRACKET)

//                .before(ImpexTypes.LEFT_ROUND_BRACKET)
//                .spaceIf(impexSettings.SPACE_BEFORE_LEFT_ROUND_BRACKET)

//                .after(ImpexTypes.RIGHT_ROUND_BRACKET)
//                .spaceIf(impexSettings.SPACE_AFTER_RIGHT_ROUND_BRACKET)

                .before(ImpexTypes.RIGHT_ROUND_BRACKET)
                .spaceIf(impexSettings.SPACE_BEFORE_RIGHT_ROUND_BRACKET)

                .after(ImpexTypes.LEFT_SQUARE_BRACKET)
                .spaceIf(impexSettings.SPACE_AFTER_LEFT_SQUARE_BRACKET)

//                .before(ImpexTypes.LEFT_SQUARE_BRACKET)
//                .spaceIf(impexSettings.SPACE_BEFORE_LEFT_SQUARE_BRACKET)

//                .after(ImpexTypes.RIGHT_SQUARE_BRACKET)
//                .spaceIf(impexSettings.SPACE_AFTER_RIGHT_SQUARE_BRACKET)

                .before(ImpexTypes.RIGHT_SQUARE_BRACKET)
                .spaceIf(impexSettings.SPACE_BEFORE_RIGHT_SQUARE_BRACKET)

                .after(ImpexTypes.ALTERNATIVE_PATTERN)
                .spaceIf(impexSettings.SPACE_AFTER_ALTERNATIVE_PATTERN)

                .before(ImpexTypes.ALTERNATIVE_PATTERN)
                .spaceIf(impexSettings.SPACE_BEFORE_ALTERNATIVE_PATTERN)
                ;
    }

    @Nullable
    @Override
    public TextRange getRangeAffectingIndent(final PsiFile file, final int offset, final ASTNode elementAtOffset) {
        return null;
    }
}
