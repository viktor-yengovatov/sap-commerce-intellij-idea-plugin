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

package com.intellij.idea.plugin.hybris.type.system.meta.impl;

import com.intellij.idea.plugin.hybris.type.system.model.Attributes;
import com.intellij.idea.plugin.hybris.type.system.model.ItemType;
import com.intellij.idea.plugin.hybris.type.system.model.ItemTypes;
import com.intellij.idea.plugin.hybris.type.system.model.Items;
import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaModel;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.ProjectScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.Processor;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;
import com.intellij.util.xml.stubs.index.DomElementClassIndex;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;

/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 15/06/2016.
 */
public class TSMetaModelBuilder implements Processor<PsiFile> {

    private final Project myProject;
    private final DomManager myDomManager;

    private TSMetaModelImpl myResult;

    public TSMetaModelBuilder(final @NotNull Project project) {
        this(project, DomManager.getDomManager(project));
    }

    public TSMetaModelBuilder(final @NotNull Project project, final @NotNull DomManager domManager) {
        myProject = project;
        myDomManager = domManager;
    }

    public TSMetaModel rebuildModel() {
        myResult = new TSMetaModelImpl();
        StubIndex.getInstance().processElements(
            DomElementClassIndex.KEY,
            Items.class.getName(),
            myProject,
            ProjectScope.getAllScope(myProject),
            PsiFile.class,
            this
        );
        TSMetaModelImpl result = myResult;
        myResult = null;
        return result;
    }


    @Override
    public boolean process(final PsiFile psiFile) {
        final DomFileElement<Items> rootWrapper = myDomManager.getFileElement((XmlFile) psiFile, Items.class);
        final ItemTypes itemTypes = Optional.ofNullable(rootWrapper)
                                              .map(DomFileElement::getRootElement)
                                              .map(Items::getItemTypes)
                                              .orElse(null);

        if (itemTypes != null) {
            itemTypes.getItemTypes().stream()
                     .forEach(this::processItemType);
            itemTypes.getTypeGroups().stream()
                     .flatMap(tg -> tg.getItemTypes().stream())
                     .forEach(this::processItemType);
        }

        //continue visiting
        return true;
    }

    private void processItemType(final @NotNull ItemType itemType) {
        final TSMetaClassImpl metaclass = myResult.findOrCreateClass(itemType);
        if (metaclass == null) {
            //can't be registered, misses the code
            return;
        }

        Optional.ofNullable(itemType.getAttributes())
                .map(Attributes::getAttributes)
                .orElse(Collections.emptyList())
                .stream()
                .forEach(metaclass::createProperty);
    }
}
