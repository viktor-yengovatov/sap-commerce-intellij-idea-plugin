package com.intellij.idea.plugin.hybris.impex.completion.provider;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Function;

public class ImpexKeywordCompletionProvider extends CompletionProvider<CompletionParameters> {

    @NotNull
    private final Set<String> keywords;

    @NotNull
    private final Function<String, LookupElement> func;

    public ImpexKeywordCompletionProvider(@NotNull final Set<String> keywords, 
                                          @NotNull final Function<String, LookupElement> func) {
        this.keywords = keywords;
        this.func = func;
    }

    @Override
    protected void addCompletions(
        @NotNull final CompletionParameters parameters,
        final ProcessingContext processingContext,
        @NotNull final CompletionResultSet completionResultSet
    ) {
        for (String keyword : keywords) {
            completionResultSet.addElement(
                func.apply(keyword)
            );
        }
    }
}
