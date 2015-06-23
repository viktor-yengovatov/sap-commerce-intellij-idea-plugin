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

package com.intellij.idea.plugin.hybris.impex.assistance;

import com.intellij.codeInsight.highlighting.HighlightManager;
import com.intellij.codeInsight.highlighting.HighlightUsagesHandler;
import com.intellij.idea.plugin.hybris.impex.ImpexLanguage;
import com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils;
import com.intellij.lang.Language;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilBase;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultImpexHeaderNameHighlighterService implements ImpexHeaderNameHighlighterService {

    protected final Map<Editor, PsiElement> highlightedBlocks = new ConcurrentHashMap<Editor, PsiElement>();

    @Override
    @Contract(pure = false)
    public void highlightCurrentHeader(@NotNull final Editor editor) {
        Validate.notNull(editor);

        final Project project = editor.getProject();

        if (null == project) {
            return;
        }

        if (project.isDisposed()) {
            return;
        }

        final Language languageInEditor = PsiUtilBase.getLanguageInEditor(editor, project);

        if (languageInEditor instanceof ImpexLanguage) {
            this.highlightHeaderOfValueUnderCaret(editor);
        }
    }

    @Contract(pure = false)
    protected void highlightHeaderOfValueUnderCaret(@NotNull final Editor editor) {
        Validate.notNull(editor);

        final PsiElement header = ImpexPsiUtils.getHeaderOfValueGroupUnderCaret(editor);

        if (null == header) {
            this.clearHighlightedArea(editor);
        } else {
            this.highlightArea(editor, header);
        }
    }

    @Contract(pure = false)
    protected void highlightArea(@NotNull final Editor editor,
                                 @NotNull final PsiElement impexFullHeaderParameter) {
        Validate.notNull(editor);
        Validate.notNull(impexFullHeaderParameter);

        if (isAlreadyHighlighted(editor, impexFullHeaderParameter)) {
            return;
        }

        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                final PsiElement currentHighlightedElement = highlightedBlocks.remove(editor);
                if (null != currentHighlightedElement) {
                    modifyHighlightedArea(editor, currentHighlightedElement, true);
                }

                highlightedBlocks.put(editor, impexFullHeaderParameter);
                modifyHighlightedArea(editor, impexFullHeaderParameter, false);
            }
        });
    }

    @Contract(pure = false)
    protected void clearHighlightedArea(@NotNull final Editor editor) {
        Validate.notNull(editor);

        if (!highlightedBlocks.isEmpty()) {
            final PsiElement impexFullHeaderParameter = highlightedBlocks.remove(editor);

            if (null != impexFullHeaderParameter) {
                ApplicationManager.getApplication().invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        modifyHighlightedArea(editor, impexFullHeaderParameter, true);
                    }
                });
            }
        }
    }

    @Contract(pure = true)
    protected boolean isAlreadyHighlighted(@NotNull final Editor editor,
                                           @Nullable final PsiElement impexFullHeaderParameter) {
        Validate.notNull(editor);

        return this.highlightedBlocks.get(editor) == impexFullHeaderParameter;
    }

    @Contract(pure = false)
    protected void modifyHighlightedArea(@NotNull final Editor editor,
                                         @NotNull final PsiElement impexFullHeaderParameter,
                                         final boolean clear) {
        Validate.notNull(editor);
        Validate.notNull(impexFullHeaderParameter);

        if (null == editor.getProject()) {
            return;
        }

        if (editor.getProject().isDisposed()) {
            return;
        }

        // This list must be modifiable
        // https://bitbucket.org/AlexanderBartash/impex-editor-intellij-idea-plugin/issue/11/unsupportedoperationexception-null
        final List<TextRange> ranges = new ArrayList<TextRange>();
        ranges.add(impexFullHeaderParameter.getTextRange());

        HighlightUsagesHandler.highlightRanges(
                HighlightManager.getInstance(editor.getProject()),
                editor,
                EditorColorsManager.getInstance().getGlobalScheme().getAttributes(EditorColors.SEARCH_RESULT_ATTRIBUTES),
                clear,
                ranges
        );
    }

    @Override
    @Contract(pure = false)
    public void releaseEditorData(@NotNull final Editor editor) {
        Validate.notNull(editor);

        this.highlightedBlocks.remove(editor);
    }
}