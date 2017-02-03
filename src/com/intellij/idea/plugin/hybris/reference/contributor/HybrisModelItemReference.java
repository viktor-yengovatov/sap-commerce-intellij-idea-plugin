package com.intellij.idea.plugin.hybris.reference.contributor;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.util.ArrayUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Nosov Aleksandr
 */
public class HybrisModelItemReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {

    private static final String JALO_PREFIX_MODEL = "Generated";
    private static final String JAVA_MODEL_SUFFIX = "Model";
    private static final int QUOTE_LENGTH = 2;

    public HybrisModelItemReference(final PsiElement element, final boolean soft) {
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
        final String modelName = myElement.getText().replaceAll("\"", "");

        final String javaModelName = modelName + JAVA_MODEL_SUFFIX;
        final String jaloModelName = JALO_PREFIX_MODEL + modelName;
        final PsiClass[] javaModelClasses = PsiShortNamesCache.getInstance(project).getClassesByName(
            javaModelName,
            GlobalSearchScope.allScope(
                project)
        );
        final PsiClass[] jaloModelClasses = PsiShortNamesCache.getInstance(project).getClassesByName(
            jaloModelName,
            GlobalSearchScope.allScope(project)
        );

        final PsiClass[] psiClasses = ArrayUtil.mergeArrays(javaModelClasses, jaloModelClasses);
        return PsiElementResolveResult.createResults(psiClasses);
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
