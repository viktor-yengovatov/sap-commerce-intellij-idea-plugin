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
//            new AttributesDescriptor("Round Brackets", ImpexSyntaxHighlighter.ROUND_BRACKETS),
//            new AttributesDescriptor("Square Brackets", ImpexSyntaxHighlighter.SQUARE_BRACKETS),
//            new AttributesDescriptor("Semicolon", ImpexSyntaxHighlighter.SEMICOLON),
//            new AttributesDescriptor("Comma", ImpexSyntaxHighlighter.COMMA),
//            new AttributesDescriptor("Insert Update", ImpexSyntaxHighlighter.INSERT_UPDATE),
//            new AttributesDescriptor("Table Name", ImpexSyntaxHighlighter.TABLE_NAME),
            new AttributesDescriptor("Comment", ImpexSyntaxHighlighter.COMMENT),
//            new AttributesDescriptor("Single Quoted String", ImpexSyntaxHighlighter.SINGLE_QUOTED_STRING),
//            new AttributesDescriptor("String", ImpexSyntaxHighlighter.STRING),
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
        return "#This is a comment\n" +
                "INSERT Product;code[unique=true];name[lang=en]; name[lang=de];someList(code)\n" +
                ";myProduct1;myProduct1's localized name;lokalisierter Name von myProduct1;value1,value2,value3\n" +
                ";myProduct2;myProduct2's localized name;lokalisierter Name von myProduct2\n" +
                "\n" +
                "INSERT_UPDATE Address;firstname;owner( Principal.uid | AbstractOrder.code )\n" +
                ";Hans;\"Double quoted string\"\n" +
                ";Klaus;'Single quoted string'\n";
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
