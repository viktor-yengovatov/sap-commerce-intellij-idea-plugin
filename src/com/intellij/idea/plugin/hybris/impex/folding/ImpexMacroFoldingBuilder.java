package com.intellij.idea.plugin.hybris.impex.folding;

import com.intellij.idea.plugin.hybris.impex.psi.ImpexFile;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexMacroDeclaration;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexMacroNameDec;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexMacroUsageDec;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexVisitor;
import com.intellij.idea.plugin.hybris.impex.utils.ProjectPropertiesUtils;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.lang.properties.IProperty;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.util.SmartList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static com.intellij.psi.util.PsiTreeUtil.findChildrenOfAnyType;

public class ImpexMacroFoldingBuilder implements FoldingBuilder {

    private static final Logger LOG = Logger.getInstance(ImpexMacroFoldingBuilder.class);
    private static final Key<CachedValue<Map<String, MacroDescriptor>>> FILE_IMPEX_FOLDING_CACHE_KEY = Key.create("FILE_IMPEX_FOLDING_CACHE");
    public static final String CONFIG_PREFIX = "$config-";

    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(
        @NotNull final ASTNode node, @NotNull final Document document
    ) {
        if (!(node.getPsi() instanceof ImpexFile)) {
            return FoldingDescriptor.EMPTY;
        }
        ImpexFile root = (ImpexFile) node.getPsi();

        SmartList<FoldingDescriptor> results = new SmartList<>();
        SmartList<ImpexMacroUsageDec> localMacroList = new SmartList<>();

        // resolve $config- first
        findChildrenOfAnyType(root, ImpexMacroUsageDec.class).forEach(macroUsage->resolveMacroUsage(macroUsage, results, localMacroList));

        // then resolve other macros
        root.acceptChildren(new ImpexVisitor(){
            public void visitMacroDeclaration(@NotNull ImpexMacroDeclaration macroDeclaration) {
                super.visitMacroDeclaration(macroDeclaration);
                resolveMacroDeclaration(macroDeclaration, results);
            }
        });

        // resolve local macro last
        localMacroList.forEach(macroUsage->resolveLocalMacro(macroUsage, results));

        return results.toArray(FoldingDescriptor.EMPTY);
    }

    private void resolveMacroDeclaration(
        ImpexMacroDeclaration macroLine,
        final SmartList<FoldingDescriptor> results
    ) {
        PsiElement[] lineElements = macroLine.getChildren();
        String macroName = null;
        StringBuilder sb = new StringBuilder();
        Map<String, MacroDescriptor> cache = getFileCache(macroLine.getContainingFile()).getValue();
        for (PsiElement child: lineElements) {
            if (child instanceof ImpexMacroNameDec) {
                macroName = child.getText();
            } else {
                if (child instanceof ImpexMacroUsageDec) {
                    MacroDescriptor descriptor = findInCache(cache, child.getText());
                    if (descriptor != null) {
                        sb.append(descriptor.getResolvedValue());
                        int delta = child.getText().length() - descriptor.getMacroName().length();
                        if (delta > 0) {
                            sb.append(child.getText().substring(descriptor.getMacroName().length()));
                        }
                    } else {
                        sb.append(child.getText());
                    }
                } else {
                    sb.append(child.getText());
                }
            }
        }

        cache.put(macroName, new MacroDescriptor(macroName, sb.toString()));
    }

    private void resolveMacroUsage(
        final ImpexMacroUsageDec macroUsage,
        final SmartList<FoldingDescriptor> results,
        final SmartList<ImpexMacroUsageDec> localMacroList
    ) {
        String text = macroUsage.getText();
        if (text.startsWith(CONFIG_PREFIX)) {
            resolveProperty(macroUsage, results);
        } else {
            // local macro needs to be resolved later when evaluating macro declarations
            if (!(macroUsage.getParent() instanceof ImpexMacroUsageDec)) {
                localMacroList.add(macroUsage);
            }
        }
    }

    private void resolveProperty(final ImpexMacroUsageDec macroUsage, final SmartList<FoldingDescriptor> results) {
        String text = macroUsage.getText();
        if (text.length() <= CONFIG_PREFIX.length()) {
            return;
        }
        Map<String, MacroDescriptor> cache = getFileCache(macroUsage.getContainingFile()).getValue();
        MacroDescriptor descriptor = cache.get(text);
        if (descriptor == null) {
            String propertyName = text.substring(CONFIG_PREFIX.length());
            Module module = ModuleUtil.findModuleForPsiElement(macroUsage);
            if (module == null) {
                return;
            }
            IProperty property = ProjectPropertiesUtils.INSTANCE.findMacroProperty(module, propertyName);
            if (property == null) {
                return;
            }
            descriptor = new MacroDescriptor(CONFIG_PREFIX + property.getKey(), property.getValue());
            cache.put(text, descriptor);
            cache.put(CONFIG_PREFIX + property.getKey(), new MacroDescriptor(CONFIG_PREFIX + property.getKey(), property.getValue()));
        }
        int start = macroUsage.getTextRange().getStartOffset();
        TextRange range = new TextRange(start, start + descriptor.getMacroName().length());
        results.add(new FoldingDescriptor(macroUsage.getNode(), range, null));
    }

    private void resolveLocalMacro(final ImpexMacroUsageDec macroUsage, final SmartList<FoldingDescriptor> results) {
        Map<String, MacroDescriptor> cache = getFileCache(macroUsage.getContainingFile()).getValue();
        String currentKey = "";
        for (String key: cache.keySet()) {
            if (macroUsage.getText().startsWith(key)) {
                if (key.length() > currentKey.length()) {
                    currentKey = key;
                }
            }
        }
        if (currentKey.isEmpty()) {
            return;
        }
        MacroDescriptor descriptor = cache.get(currentKey);
        int start = macroUsage.getTextRange().getStartOffset();
        TextRange range = new TextRange(start, start + descriptor.getMacroName().length());
        results.add(new FoldingDescriptor(macroUsage.getNode(), range, null));

        cache.put(macroUsage.getText(), descriptor);
    }

    private MacroDescriptor findInCache(
        final Map<String, MacroDescriptor> cache,
        final String text
    ) {
        MacroDescriptor macroDescriptor = cache.get(text);
        if (macroDescriptor != null) {
            return macroDescriptor;
        }
        for (MacroDescriptor md: cache.values()) {
            if (text.startsWith(md.getMacroName())) {
                cache.put(text, md);
                return md;
            }
        }
        return null;
    }

    private CachedValue<Map<String, MacroDescriptor>> getFileCache(PsiFile impexFile) {
        CachedValue<Map<String, MacroDescriptor>> fileModelCache = impexFile.getUserData(FILE_IMPEX_FOLDING_CACHE_KEY);

        if (fileModelCache == null) {
            fileModelCache = CachedValuesManager.getManager(impexFile.getProject()).createCachedValue(
                () -> ApplicationManager.getApplication().runReadAction(
                    (Computable<CachedValueProvider.Result<Map<String, MacroDescriptor>>>) () ->
                        CachedValueProvider.Result.create(new HashMap<>(), impexFile)
                    ), false);
            impexFile.putUserData(FILE_IMPEX_FOLDING_CACHE_KEY, fileModelCache);
        }

        return fileModelCache;
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull final ASTNode node) {
        Map<String, MacroDescriptor> cache = getFileCache(node.getPsi().getContainingFile()).getValue();
        MacroDescriptor descriptor = cache.get(node.getText());
        if (descriptor != null) {
            return descriptor.getResolvedValue();
        }
        return node.getText();
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull final ASTNode node) {
        return true;
    }

    private class MacroDescriptor {
        private String macroName;
        private String resolvedValue;

        public MacroDescriptor(final String macroName, final String resolvedValue) {
            this.macroName = macroName;
            this.resolvedValue = resolvedValue;
            replaceBlank();
        }

        private void replaceBlank() {
            if (resolvedValue == null || resolvedValue.isEmpty()) {
                resolvedValue = "<blank>";
            }
        }

        public String getMacroName() {
            return macroName;
        }

        public void setMacroName(final String macroName) {
            this.macroName = macroName;
        }

        public String getResolvedValue() {
            return resolvedValue;
        }

        public void setResolvedValue(final String resolvedValue) {
            this.resolvedValue = resolvedValue;
        }
    }
}
