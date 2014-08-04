package com.intellij.idea.plugin.hybris.impex;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class ImpexColorSettingsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Round Brackets", ImpexSyntaxHighlighter.ROUND_BRACKETS),
            new AttributesDescriptor("Square Brackets", ImpexSyntaxHighlighter.SQUARE_BRACKETS),
            new AttributesDescriptor("Semicolon", ImpexSyntaxHighlighter.SEMICOLON),
            new AttributesDescriptor("Comma", ImpexSyntaxHighlighter.COMMA),
            new AttributesDescriptor("Insert Update", ImpexSyntaxHighlighter.INSERT_UPDATE),
            new AttributesDescriptor("Table Name", ImpexSyntaxHighlighter.TABLE_NAME),
            new AttributesDescriptor("Table Name", ImpexSyntaxHighlighter.COMMENT),
            new AttributesDescriptor("Single Quoted String", ImpexSyntaxHighlighter.SINGLE_QUOTED_STRING),
            new AttributesDescriptor("String", ImpexSyntaxHighlighter.STRING),
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return ImpexIcons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new ImpexSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return "# You are reading the \".properties\" entry.\n" +
                "! The exclamation mark can also mark text as comments.\n" +
                "website = http://en.wikipedia.org/\n" +
                "language = English\n" +
                "# The backslash below tells the application to continue reading\n" +
                "# the value onto the next line.\n" +
                "message = Welcome to \\\n" +
                "          Wikipedia!\n" +
                "# Add spaces to the key\n" +
                "key\\ with\\ spaces = This is the value that could be looked up with the key \"key with spaces\".\n" +
                "# Unicode\n" +
                "tab : \\u0009";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Impex";
    }
}
