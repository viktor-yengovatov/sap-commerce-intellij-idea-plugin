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

package com.intellij.idea.plugin.hybris.impex.psi;

import com.intellij.idea.plugin.hybris.impex.ImpexLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.lowerCase;

public class ImpexTokenType extends IElementType {

    private static final Pattern PATTERN = Pattern.compile("[_]");

    public ImpexTokenType(@NotNull @NonNls final String debugName) {
        super(debugName, ImpexLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        final String name = super.toString();

        if (isBlank(name)) return name;

        final String fixedName = PATTERN.matcher(lowerCase(name)).replaceAll(" ");

        return new StringBuilder("<").append(fixedName).append(">").toString();
    }
}