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
