package com.intellij.idea.plugin.hybris.project;

import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.idea.plugin.hybris.util.HybrisConstants;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.projectImport.ProjectOpenProcessorBase;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created 8:57 PM 07 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisProjectOpenProcessor extends ProjectOpenProcessorBase<HybrisProjectImportBuilder> {

    public HybrisProjectOpenProcessor(final HybrisProjectImportBuilder builder) {
        super(builder);
    }

    @Nullable
    @Override
    public String[] getSupportedExtensions() {
        return new String[]{
            HybrisConstants.EXTENSION_INFO_XML
        };
    }

    public boolean doQuickImport(VirtualFile file, final WizardContext wizardContext) {
//        getBuilder().setRootDirectory(file.getParent().getPath());
//
//        final List<String> projects = getBuilder().getList();
//        if (projects == null || projects.size() != 1) {
//            return false;
//        }
//        getBuilder().setList(projects);
//        wizardContext.setProjectName(EclipseProjectFinder.findProjectName(projects.get(0)));
        return true;
    }

}
