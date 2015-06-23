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

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;

/**
 * Created 23:42 21 December 2014
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexCodeStyleSettings extends CustomCodeStyleSettings {
    public boolean SPACE_AFTER_FIELD_VALUE_SEPARATOR = true;
    public boolean SPACE_BEFORE_FIELD_VALUE_SEPARATOR = true;

    public boolean SPACE_AFTER_PARAMETERS_SEPARATOR = true;
    public boolean SPACE_BEFORE_PARAMETERS_SEPARATOR = false;

    public boolean SPACE_AFTER_COMMA = true;
    public boolean SPACE_BEFORE_COMMA = false;

    public boolean SPACE_AFTER_ATTRIBUTE_SEPARATOR = true;
    public boolean SPACE_BEFORE_ATTRIBUTE_SEPARATOR = false;

    public boolean SPACE_AFTER_FIELD_LIST_ITEM_SEPARATOR = true;
    public boolean SPACE_BEFORE_FIELD_LIST_ITEM_SEPARATOR = false;

    public boolean SPACE_AFTER_ASSIGN_VALUE = true;
    public boolean SPACE_BEFORE_ASSIGN_VALUE = true;

    public boolean SPACE_AFTER_LEFT_ROUND_BRACKET = false;
//    public boolean SPACE_BEFORE_LEFT_ROUND_BRACKET = false;

//    public boolean SPACE_AFTER_RIGHT_ROUND_BRACKET = false;
    public boolean SPACE_BEFORE_RIGHT_ROUND_BRACKET = false;

    public boolean SPACE_AFTER_LEFT_SQUARE_BRACKET = false;
//    public boolean SPACE_BEFORE_LEFT_SQUARE_BRACKET = false;

//    public boolean SPACE_AFTER_RIGHT_SQUARE_BRACKET = false;
    public boolean SPACE_BEFORE_RIGHT_SQUARE_BRACKET = false;

    public boolean SPACE_AFTER_ALTERNATIVE_PATTERN = true;
    public boolean SPACE_BEFORE_ALTERNATIVE_PATTERN = true;

    public ImpexCodeStyleSettings(final CodeStyleSettings settings) {
        super("ImpexCodeStyleSettings", settings);
    }
}
