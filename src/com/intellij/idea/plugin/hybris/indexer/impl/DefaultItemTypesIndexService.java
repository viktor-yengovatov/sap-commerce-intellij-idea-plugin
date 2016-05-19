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

package com.intellij.idea.plugin.hybris.indexer.impl;

import com.intellij.idea.plugin.hybris.indexer.ItemTypesIndexService;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.impl.source.PsiExtensibleClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import static com.intellij.idea.plugin.hybris.common.utils.CollectionUtils.emptySetIfNull;

/**
 * Created 22:56 14 May 2016
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class DefaultItemTypesIndexService implements ItemTypesIndexService {

    private static final Logger LOG = Logger.getInstance(DefaultItemTypesIndexService.class);

    protected static final Pattern DOUBLE_QUOTE_PATTERN = Pattern.compile("\"");

    protected Map<String, PsiClass> TYPE_CODES = new ConcurrentHashMap<String, PsiClass>();

    private final Project project;

    public DefaultItemTypesIndexService(@NotNull final Project project) {
        Validate.notNull(project);

        this.project = project;
    }

    @Override
    public void indexItemTypes() {

        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            TYPE_CODES = new ConcurrentHashMap<String, PsiClass>();
                            final PsiClass itemRootClass = JavaPsiFacade.getInstance(project).findClass(
                                "de.hybris.platform.core.model.ItemModel",
                                GlobalSearchScope.allScope(project)
                            );

                            if (null == itemRootClass) {
                                return;
                            }

                            final Collection<PsiClass> inheritedClasses = ClassInheritorsSearch.search(itemRootClass)
                                                                                               .findAll();
                            // adding Item class itself
                            inheritedClasses.add(itemRootClass);

                            for (PsiClass itemClass : inheritedClasses) {
                                String typeCode = null;

                                // typecode -> the "hybris type"-uid within the typesystem of hybris
                                for (PsiField field : ((PsiExtensibleClass) itemClass).getOwnFields()) {
                                    if ("_TYPECODE".equals(field.getName())) {
                                        if (null != field.getInitializer()) {
                                            typeCode = DOUBLE_QUOTE_PATTERN.matcher(field.getInitializer().getText())
                                                                           .replaceAll("");
                                        }
                                    }
                                }
                                if (typeCode != null) {
                                    TYPE_CODES.put(typeCode, itemClass);
                                }
                            }

                        } catch (IndexNotReadyException e) {
                            LOG.warn(e);
                            showDumbModeNotification(project);
                        }
                    }
                });
            }
        };
        CommandProcessor.getInstance().executeCommand(project, runnable, "Indexing hybris typesystem", null);
    }

    @NotNull
    @Override
    public Set<String> getAllTypeCodes() {
        return emptySetIfNull(TYPE_CODES.keySet());
    }

    @Nullable
    @Override
    public PsiClass getPsiClassByTypeCode(@NotNull final String typeCode) {
        Validate.notEmpty(typeCode);

        return TYPE_CODES.get(typeCode);
    }

    private static void showDumbModeNotification(final Project project) {
        DumbService.getInstance(project).showDumbModeNotification(
            "[y]-typesystem information is not available during index update." +
            " Trigger 'Tools -> Index [y]-types' after IntelliJ-Index is build!"
        );
    }
}
