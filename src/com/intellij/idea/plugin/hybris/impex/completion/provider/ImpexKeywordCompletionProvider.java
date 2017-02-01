package com.intellij.idea.plugin.hybris.impex.completion.provider;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ImpexKeywordCompletionProvider extends CompletionProvider<CompletionParameters> {

    @NotNull
    private final Set<String> keywords;

    public ImpexKeywordCompletionProvider(@NotNull final Set<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    protected void addCompletions(
        @NotNull final CompletionParameters v,
        final ProcessingContext processingContext,
        @NotNull final CompletionResultSet completionResultSet
    ) {
        for (String keyword : keywords) {
            completionResultSet.addElement(
                LookupElementBuilder.create(keyword)
            );
        }
    }
}
