package com.intellij.idea.plugin.hybris.flexibleSearch.highlighting;

import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

import static com.intellij.idea.plugin.hybris.flexibleSearch.highlighting.FlexibleSearchHighlighterColors.*;

public class FlexibleSearchColorSettingsPage implements ColorSettingsPage {

    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
        new AttributesDescriptor("Comment", FS_COMMENT),
        new AttributesDescriptor("Keywords", FS_KEYWORD),
        new AttributesDescriptor("Column", FS_COLUMN),
        new AttributesDescriptor("Comma", FS_SYMBOL),
        new AttributesDescriptor("Number", FS_NUMBER),
        new AttributesDescriptor("Braces", FS_BRACES),
        new AttributesDescriptor("Brackets", FS_BRACKETS),
        new AttributesDescriptor("Parentheses", FS_PARENTHESES),
        new AttributesDescriptor("Bad character", HighlighterColors.BAD_CHARACTER)
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return HybrisIcons.FS_FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return ServiceManager.getService(FlexibleSearchSyntaxHighlighter.class);
    }

    @NotNull
    @Override
    public String getDemoText() {
        return "SELECT {cat:pk} FROM {Category AS cat} WHERE NOT EXISTS (\n" +
               "   {{ SELECT * FROM {CategoryCategoryRelation} WHERE {target}={cat:pk} }}\n" +
               ")\n" +
               "\n" +
               "SELECT * FROM {Product} WHERE {code} LIKE '%al%'\n" +
               "\n" +
               "\n" +
               "SELECT * FROM {Product} WHERE {code} LIKE '%al%' AND {code} LIKE '%15%'\n" +
               "\n" +
               "SELECT * FROM {Product} WHERE {code} IS NULL\n" +
               "\n" +
               "SELECT * FROM {Product} WHERE {code} NOT LIKE '%al%' AND {code} NOT LIKE '%15%' OR {code} IS NULL\n" +
               "\n" +
               "SELECT * FROM {Product} WHERE {code} LIKE '%al%' AND {code} NOT LIKE '%15%'\n" +
               "\n" +
               "SELECT * FROM {Product} WHERE {code} IS NOT NULL\n" +
               "\n" +
               "SELECT {cat:pk} FROM {Category AS cat} WHERE NOT EXISTS (\n" +
               "   {{ SELECT * FROM {CategoryCategoryRelation} WHERE {target}={cat.spk} }}\n" +
               ")\n" +
               "\n" +
               "SELECT {code},{pk} FROM  {Product} ORDER BY {code} DESC\n" +
               "\n" +
               "SELECT {pk} FROM {Product} WHERE {modifiedtime} >= ?startDate AND {modifiedtime} <= ?endDate\n" +
               "\n" +
               "SELECT {p:PK}\n" +
               "   FROM {Product AS p}\n" +
               "   WHERE {p:code} LIKE '%myProduct'\n" +
               "      OR {p:name} LIKE '%myProduct'\n" +
               "   ORDER BY {p:code} ASC\n" +
               "\n" +
               "@@@@@\n";
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
        return "FlexibleSearch";
    }
}
