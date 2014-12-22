package com.intellij.idea.plugin.hybris.impex.formatting;

import com.intellij.application.options.CodeStyleAbstractConfigurable;
import com.intellij.application.options.CodeStyleAbstractPanel;
import com.intellij.application.options.TabbedLanguageCodeStylePanel;
import com.intellij.idea.plugin.hybris.impex.ImpexLanguage;
import com.intellij.openapi.options.Configurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created 23:42 21 December 2014
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexCodeStyleSettingsProvider extends CodeStyleSettingsProvider {

    @Override
    public CustomCodeStyleSettings createCustomSettings(final CodeStyleSettings settings) {
        return new ImpexCodeStyleSettings(settings);
    }

    @Nullable
    @Override
    public String getConfigurableDisplayName() {
        return "Impex";
    }

    @NotNull
    @Override
    public Configurable createSettingsPage(final CodeStyleSettings settings, final CodeStyleSettings originalSettings) {

        return new CodeStyleAbstractConfigurable(settings, originalSettings, "Impex") {

            @Override
            protected CodeStyleAbstractPanel createPanel(final CodeStyleSettings settings) {
                return new SimpleCodeStyleMainPanel(getCurrentSettings(), settings);
            }

            @Nullable
            @Override
            public String getHelpTopic() {
                return null;
            }
        };
    }

    private static class SimpleCodeStyleMainPanel extends TabbedLanguageCodeStylePanel {

        public SimpleCodeStyleMainPanel(final CodeStyleSettings currentSettings, final CodeStyleSettings settings) {
            super(ImpexLanguage.INSTANCE, currentSettings, settings);
        }
    }
}
