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

package com.intellij.idea.plugin.hybris.impex.formatting;

import com.intellij.formatting.Alignment;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

/**
 * Created 12:57 01 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public interface AlignmentStrategy {

    Alignment getAlignment(@NotNull ASTNode currentNode);

    void processNode(@NotNull ASTNode currentNode);
}
