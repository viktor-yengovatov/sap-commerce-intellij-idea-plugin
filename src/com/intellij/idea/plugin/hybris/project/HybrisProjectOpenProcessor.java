package com.intellij.idea.plugin.hybris.project;

import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.idea.plugin.hybris.project.utils.HybrisProjectUtils;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.projectImport.ProjectOpenProcessorBase;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created 8:57 PM 07 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisProjectOpenProcessor extends ProjectOpenProcessorBase<AbstractHybrisProjectImportBuilder> {

    private static final Logger LOG = Logger.getInstance(HybrisProjectOpenProcessor.class.getName());

    public HybrisProjectOpenProcessor(final AbstractHybrisProjectImportBuilder builder) {
        super(builder);
    }

    @Nullable
    @Override
    public String[] getSupportedExtensions() {
        return new String[]{
            HybrisConstants.EXTENSION_INFO_XML
        };
    }

    @Override
    public boolean doQuickImport(final VirtualFile file, final WizardContext wizardContext) {
        this.getBuilder().setRootDirectory(file.getParent().getPath());

        final List<String> projects = getBuilder().getList();
        if (null == projects || 1 != projects.size()) {
            return false;
        }

        try {
            this.getBuilder().setList(projects);
        } catch (ConfigurationException e) {
            LOG.error(e);
        }

        wizardContext.setProjectName(HybrisProjectUtils.findProjectName(projects.get(0)));
        return true;
    }

}
