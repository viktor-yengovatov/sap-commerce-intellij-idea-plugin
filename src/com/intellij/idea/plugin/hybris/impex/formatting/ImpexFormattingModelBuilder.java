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
                Wrap.createWrap(WrapType.ALWAYS, false),
                Alignment.createAlignment(),
                createSpaceBuilder(settings)
        );

        return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(), impexBlock, settings);
    }

    private static SpacingBuilder createSpaceBuilder(final CodeStyleSettings settings) {
        final ImpexCodeStyleSettings impexSettings = settings.getCustomSettings(ImpexCodeStyleSettings.class);

        return new SpacingBuilder(settings, ImpexLanguage.INSTANCE)
                .before(ImpexTypes.FIELD_VALUE_SEPARATOR)
                .spaceIf(impexSettings.SPACE_BEFORE_FIELD_VALUE_SEPARATOR)

                .after(ImpexTypes.FIELD_VALUE_SEPARATOR)
                .spaceIf(impexSettings.SPACE_AFTER_FIELD_VALUE_SEPARATOR)

                .before(ImpexTypes.PARAMETERS_SEPARATOR)
                .spaceIf(impexSettings.SPACE_BEFORE_PARAMETERS_SEPARATOR)

                .after(ImpexTypes.PARAMETERS_SEPARATOR)
                .spaceIf(impexSettings.SPACE_AFTER_PARAMETERS_SEPARATOR)

                .before(ImpexTypes.ATTRIBUTE_SEPARATOR)
                .spaceIf(impexSettings.SPACE_BEFORE_ATTRIBUTE_SEPARATOR)

                .after(ImpexTypes.ATTRIBUTE_SEPARATOR)
                .spaceIf(impexSettings.SPACE_AFTER_ATTRIBUTE_SEPARATOR)

                .before(ImpexTypes.FIELD_LIST_ITEM_SEPARATOR)
                .spaceIf(impexSettings.SPACE_BEFORE_FIELD_LIST_ITEM_SEPARATOR)

                .after(ImpexTypes.FIELD_LIST_ITEM_SEPARATOR)
                .spaceIf(impexSettings.SPACE_AFTER_FIELD_LIST_ITEM_SEPARATOR)
                ;
    }

    @Nullable
    @Override
    public TextRange getRangeAffectingIndent(final PsiFile file, final int offset, final ASTNode elementAtOffset) {
        return null;
    }
}
