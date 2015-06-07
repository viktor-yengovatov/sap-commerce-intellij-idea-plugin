package com.intellij.idea.plugin.hybris.project;

import com.intellij.projectImport.ProjectOpenProcessorBase;
import org.jetbrains.annotations.Nullable;

/**
 * Created 8:57 PM 07 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisProjectOpenProcessor extends ProjectOpenProcessorBase<HybrisProjectImportBuilder> {

    protected HybrisProjectOpenProcessor(final HybrisProjectImportBuilder builder) {
        super(builder);
    }

    @Nullable
    @Override
    public String[] getSupportedExtensions() {
        return new String[0];
    }

}
