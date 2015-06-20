package com.intellij.idea.plugin.hybris.project;

import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import com.intellij.idea.plugin.hybris.utils.HybrisConstants;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.projectImport.ProjectOpenProcessorBase;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created 8:57 PM 07 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisProjectOpenProcessor extends ProjectOpenProcessorBase<DefaultHybrisProjectImportBuilder> {

    private static final Logger LOG = Logger.getInstance(HybrisProjectOpenProcessor.class.getName());

    public HybrisProjectOpenProcessor(final DefaultHybrisProjectImportBuilder builder) {
        super(builder);
    }

    @Override
    public boolean doQuickImport(final VirtualFile file, final WizardContext wizardContext) {
        this.getBuilder().setRootProjectAbsolutePath(VfsUtil.virtualToIoFile(file.getParent()));

        final List<HybrisModuleDescriptor> projects = this.getBuilder().getList();
        if (null == projects || 1 != projects.size()) {
            return false;
        }

        try {
            this.getBuilder().setList(projects);
        } catch (ConfigurationException e) {
            LOG.error(e);
        }

        wizardContext.setProjectName(projects.get(0).getModuleName());

        return true;
    }

    @Nullable
    @Override
    public String[] getSupportedExtensions() {
        return new String[]{
            HybrisConstants.EXTENSION_INFO_XML
        };
//        return new String[]{
//            HybrisConstants.EXTENSION_INFO_XML,
//            HybrisConstants.LOCAL_EXTENSIONS_XML,
//            HybrisConstants.EXTENSIONS_XML
//        };
    }

}
