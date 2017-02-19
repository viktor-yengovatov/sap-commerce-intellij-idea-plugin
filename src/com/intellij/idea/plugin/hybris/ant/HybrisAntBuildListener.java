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

package com.intellij.idea.plugin.hybris.ant;

import com.intellij.ide.DataManager;
import com.intellij.idea.plugin.hybris.project.actions.ProjectRefreshAction;
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettings;
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettingsComponent;
import com.intellij.lang.ant.config.AntBuildListener;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martin Zdarsky-Jones on 15/2/17.
 *
 * This implementation of AntBuildListener gets exchanged for the blank anonymous implementation of AntBuildListener.NULL
 * That way intellij ant plugin will pick up our listener and we'll kow when ant build finishes.
 * However, we will not know what project finished. We'll just know that some ant build of some project finished.
 *
 * The ant logger will leave serialized class describing new/old extensions if there are any.
 * We need to search through all open projects to see if there is a "message" from ant process.
 * If there is then we also find out which project left the message.
 */
public class HybrisAntBuildListener implements AntBuildListener {

    /**
     * Injecting our listener implementation into ant integration interface.
     */
    public static void registerAntListener() {
        try {
            final Field field = AntBuildListener.class.getField("NULL");
            setFinalStatic(field, new HybrisAntBuildListener());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Some ant build finished and we do not know what target not which project this is.
     *
     * @param state 0==ok
     * @param errorCount
     */
    @Override
    public void buildFinished(final int state, final int errorCount) {
        final Map<Project, AntGenResult> resultMap = new HashMap<>();

        findAntResult(resultMap);

        if (resultMap.isEmpty()) {
            return;
        }
        final Project project = resultMap.keySet().iterator().next();
        final AntGenResult antGenResult = resultMap.get(project);
        modifyLocalExtensions(project, antGenResult);
        triggerRefreshProjectAction();
    }

    private void findAntResult(final Map<Project, AntGenResult> resultMap) {
        for (Project project: ProjectManager.getInstance().getOpenProjects()) {
            final HybrisProjectSettings hybrisProjectSettings = HybrisProjectSettingsComponent.getInstance(project)
                                                                                              .getState();
            if (!hybrisProjectSettings.isHybisProject()) {
                continue;
            }

            final File file = new File(project.getBasePath() + "/" + hybrisProjectSettings.getHybrisDirectory() + "/temp/ant.ser");
            if (file.exists()) {
                AntGenResult result = null;
                try (
                    final FileInputStream fileIn = new FileInputStream(file);
                    final ObjectInputStream in = new ObjectInputStream(fileIn);
                ){
                    result = (AntGenResult) in.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                file.delete();
                resultMap.put(project, result);
                return;
            }
        }
    }

    private void modifyLocalExtensions(final Project project, final AntGenResult result) {
        final HybrisProjectSettings hybrisProjectSettings = HybrisProjectSettingsComponent.getInstance(project)
                                                                                          .getState();
        final File file = new File(project.getBasePath() + "/" +
                                   hybrisProjectSettings.getHybrisDirectory() + "/config/localextensions.xml");
        final VirtualFile vf = VfsUtil.findFileByIoFile(file, true);
        final XmlFile xmlFile = (XmlFile) PsiManager.getInstance(project).findFile(vf);
        new WriteCommandAction.Simple(project, xmlFile) {
            @Override
            protected void run() throws Throwable {

                final XmlTag hybrisconfig = xmlFile.getRootTag();
                if (hybrisconfig == null) {
                    return;
                }
                for (XmlTag extensions: hybrisconfig.getSubTags()) {
                    if (!extensions.getName().equals("extensions")) {
                        continue;
                    }
                    for (XmlTag extension : extensions.getSubTags()) {
                        if (!extension.getName().equals("extension")) {
                            continue;
                        }
                        if (result.getExtensionsToRemove().contains(extension.getAttributeValue("name"))) {
                            extension.delete();
                        }
                    }

                    for (String newExtension : result.getExtensionsToAdd()) {
                        final XmlTag newTag = extensions.createChildTag("extension", null, null, false);
                        newTag.setAttribute("dir", newExtension);
                        extensions.addSubTag(newTag, false);
                    }
                }

                FileDocumentManager.getInstance().saveAllDocuments();
            }
        }.execute();
    }

    private void triggerRefreshProjectAction() {
        ApplicationManager.getApplication().invokeLater(() -> {
            final DataContext dataContext = DataManager.getInstance().getDataContextFromFocus().getResult();
            final AnAction action = new ProjectRefreshAction();
            final AnActionEvent actionEvent = AnActionEvent.createFromAnAction(action, null, ActionPlaces.UNKNOWN, dataContext);
            action.actionPerformed(actionEvent);
        }, ModalityState.NON_MODAL);
    }

    static void setFinalStatic(Field field, Object newValue) throws NoSuchFieldException, IllegalAccessException {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }
}
