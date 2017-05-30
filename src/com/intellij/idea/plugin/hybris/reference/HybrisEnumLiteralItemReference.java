package com.intellij.idea.plugin.hybris.reference;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.compiled.ClsClassImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Nosov Aleksandr
 */
public class HybrisEnumLiteralItemReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {

    private static final int QUOTE_LENGTH = 2;

    public HybrisEnumLiteralItemReference(final PsiElement element, final boolean soft) {
        super(element, soft);
    }

    @Override
    public final TextRange getRangeInElement() {
        return TextRange.from(1, getElement().getTextLength() - QUOTE_LENGTH);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(final boolean incompleteCode) {
        Project project = myElement.getProject();
        final String enumLiteralJavaModelName = myElement.getText().replaceAll("\"", "").toUpperCase();

        final PsiShortNamesCache psiShortNamesCache = PsiShortNamesCache.getInstance(project);
        final PsiField[] javaEnumLiteralFields = psiShortNamesCache.getFieldsByName(
            enumLiteralJavaModelName, GlobalSearchScope.allScope(project)
        );

        final Set<PsiField> enumFields = stream(javaEnumLiteralFields)
            .filter(literal -> literal.getParent() != null)
            .filter(literal -> literal.getParent() instanceof ClsClassImpl)
            .filter(literal -> ((ClsClassImpl) literal.getParent()).isEnum())
            .collect(Collectors.toSet());

        return PsiElementResolveResult.createResults(enumFields);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        final ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return EMPTY_ARRAY;
    }
}
