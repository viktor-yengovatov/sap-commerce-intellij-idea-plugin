package com.intellij.idea.plugin.hybris.project;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.ProjectWizardStepFactory;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.idea.plugin.hybris.project.wizard.HybrisWorkspaceRootStep;
import com.intellij.idea.plugin.hybris.project.wizard.SelectHybrisImportedProjectsStep;
import com.intellij.idea.plugin.hybris.utils.HybrisI18NBundleUtils;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.projectImport.ProjectImportProvider;
import org.jetbrains.annotations.Nullable;

/**
 * Created 12:31 AM 10 June 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class HybrisProjectImportProvider extends ProjectImportProvider {

    private final HybrisProjectOpenProcessor myProcessor;

    public HybrisProjectImportProvider(final DefaultHybrisProjectImportBuilder builder) {
        super(builder);
        this.myProcessor = new HybrisProjectOpenProcessor(builder);
    }

    @Override
    protected boolean canImportFromFile(final VirtualFile file) {
        return this.myProcessor.canOpenProject(file);
    }

    @Override
    public ModuleWizardStep[] createSteps(final WizardContext context) {
        final ProjectWizardStepFactory stepFactory = ProjectWizardStepFactory.getInstance();

        return new ModuleWizardStep[]{
            new HybrisWorkspaceRootStep(context),
            new SelectHybrisImportedProjectsStep(context),
            stepFactory.createProjectJdkStep(context)
        };
    }

    @Nullable
    @Override
    public String getFileSample() {
        return HybrisI18NBundleUtils.message("hybris.project.import.dialog.message");
    }
}
