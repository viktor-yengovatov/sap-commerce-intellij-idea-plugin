/*
 * Copyright 2015 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.idea.plugin.hybris.project;

import com.intellij.idea.plugin.hybris.project.settings.HybrisProjectDescriptor;
import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import com.intellij.projectImport.ProjectImportBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created 5:20 PM 13 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public abstract class AbstractHybrisProjectImportBuilder extends ProjectImportBuilder<HybrisModuleDescriptor> {

    public abstract void setRootProjectDirectory(@NotNull final File directory);

    @NotNull
    public abstract HybrisProjectDescriptor getHybrisProjectDescriptor();

}
