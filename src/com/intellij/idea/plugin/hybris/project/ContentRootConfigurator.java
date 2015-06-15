package com.intellij.idea.plugin.hybris.project;

import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import com.intellij.openapi.roots.ModifiableRootModel;
import org.jetbrains.annotations.NotNull;

/**
 * Created 2:05 AM 15 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public interface ContentRootConfigurator {

    void configure(@NotNull ModifiableRootModel modifiableRootModel,
                   @NotNull HybrisModuleDescriptor moduleDescriptor);

}
