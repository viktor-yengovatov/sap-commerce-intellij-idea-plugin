/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2016 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.intellij.idea.plugin.hybris.type.system.inspections.fix;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.PsiNavigateUtil;
import org.jetbrains.annotations.NotNull;

public class XmlAddAttributeQuickFix implements LocalQuickFix {

    private final String fixName;
    private final String tagName;
    private final String attributeName;
    private final String attributeValue;

    public XmlAddAttributeQuickFix(
            final String tagName,
            final String attributeName,
            final String attributeValue
    ) {
        this.fixName = "Add/update attribute " + attributeName + "=" + attributeValue;
        this.tagName = tagName;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }


    @NotNull
    @Override
    public String getFamilyName() {
        return fixName;
    }

    @Override
    public void applyFix(@NotNull final Project project, @NotNull final ProblemDescriptor descriptor) {
        final PsiElement currentElement = descriptor.getPsiElement();

        if (currentElement instanceof XmlTag) {
            final XmlTag currentTag = (XmlTag) currentElement;
            final XmlAttribute xmlAttribute;
            XmlTag modifiersTag = currentTag.findFirstSubTag(tagName);
            if (modifiersTag == null) {
                // Create tag
                final XmlTag tagToInsert = currentTag.createChildTag(
                        tagName,
                        currentTag.getNamespace(),
                        null,
                        false
                );

                // Insert tag
                modifiersTag = currentTag.addSubTag(tagToInsert, true);
            }
            xmlAttribute = modifiersTag.setAttribute(attributeName, attributeValue);
            PsiNavigateUtil.navigate(xmlAttribute);
        }
    }
}
