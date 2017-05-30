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
import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import com.intellij.idea.plugin.hybris.project.actions.ProjectRefreshAction;
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettings;
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettingsComponent;
import com.intellij.idea.plugin.hybris.statistics.StatsCollector;
import com.intellij.lang.ant.config.AntBuildFileBase;
import com.intellij.lang.ant.config.AntBuildListener;
import com.intellij.lang.ant.config.AntConfigurationBase;
import com.intellij.lang.ant.config.execution.ExecutionHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ex.WindowManagerEx;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.intellij.idea.plugin.hybris.common.HybrisConstants.PLATFORM_MODULE_PREFIX;

/**
 * Created by Martin Zdarsky-Jones on 15/2/17.
 * <p>
 * This implementation of AntBuildListener gets exchanged for the blank anonymous implementation of AntBuildListener.NULL
 * That way intellij ant plugin will pick up our listener and we'll kow when ant build finishes.
 * However, we will not know what project finished. We'll just know that some ant build of some project finished.
 * <p>
 * The ant logger will leave serialized class describing new/old extensions if there are any.
 * We need to search through all open projects to see if there is a "message" from ant process.
 * If there is then we also find out which project left the message.
 */
public class HybrisAntBuildListener implements AntBuildListener {

    public static final Key<STATES> STATE = new Key("hybrisAntStateMachine");
    public static final String[] antCleanAll = new String[]{"clean", "all"};

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

    static void setFinalStatic(Field field, Object newValue) throws NoSuchFieldException, IllegalAccessException {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

    /**
     * Some ant build finished and we do not know what target not which project this is.
     *
     * @param state      0==ok
     * @param errorCount
     */
    @Override
    public void buildFinished(final int state, final int errorCount) {
        final Map<Project, AntGenResult> resultMap = new HashMap<>();
        findAntResult(resultMap);
        collectStatistics();
        processNewExtensions(resultMap);
        triggerNextAction();
    }

    private void findAntResult(final Map<Project, AntGenResult> resultMap) {
        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
            final HybrisProjectSettings hybrisProjectSettings = HybrisProjectSettingsComponent.getInstance(project)
                                                                                              .getState();
            if (!hybrisProjectSettings.isHybrisProject()) {
                continue;
            }

            final File file = new File(project.getBasePath() + "/" + hybrisProjectSettings.getHybrisDirectory() + "/temp/ant.ser");
            if (file.exists()) {
                AntGenResult result = null;
                try (
                    final FileInputStream fileIn = new FileInputStream(file);
                    final ObjectInputStream in = new ObjectInputStream(fileIn);
                ) {
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

    private void collectStatistics() {
        final StatsCollector statsCollector = ServiceManager.getService(StatsCollector.class);
        statsCollector.collectStat(StatsCollector.ACTIONS.ANT);
    }

    private void processNewExtensions(final Map<Project, AntGenResult> resultMap) {
        if (resultMap.isEmpty()) {
            return;
        }
        final Project project = resultMap.keySet().iterator().next();
        final AntGenResult antGenResult = resultMap.get(project);
        modifyLocalExtensions(project, antGenResult);
        project.putUserData(STATE, STATES.CLEAN_ALL_NEEDED);
    }

    private void triggerNextAction() {
        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
            final STATES state = project.getUserData(STATE);
            if (state == null) {
                continue;
            }
            switch (state) {
                case CLEAN_ALL_NEEDED:
                    project.putUserData(STATE, STATES.REFRESH_NEEDED);
                    triggerCleanAll(project);
                    return;
                case REFRESH_NEEDED:
                    project.putUserData(STATE, null);
                    ProjectRefreshAction.triggerAction(getDataContext(project));
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
                for (XmlTag extensions : hybrisconfig.getSubTags()) {
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
                        final String name = newExtension.substring(newExtension.lastIndexOf("/") + 1);
                        final String dir = "${HYBRIS_BIN_DIR}" + newExtension.substring(newExtension.indexOf("/custom"));
                        newTag.setAttribute("dir", dir);
                        newTag.setAttribute("name", name);
                        extensions.addSubTag(newTag, false);
                    }
                }

                FileDocumentManager.getInstance().saveAllDocuments();
            }
        }.execute();
    }

    private void triggerCleanAll(final Project project) {
        final HybrisProjectSettings yProjectSettings = HybrisProjectSettingsComponent.getInstance(project).getState();
        final File platformDir = new File(project.getBasePath() + "/" +
                                          yProjectSettings.getHybrisDirectory() + PLATFORM_MODULE_PREFIX);
        final VirtualFile vfPlatformDir = VfsUtil.findFileByIoFile(platformDir, true);
        final VirtualFile vfBuildFile = VfsUtil.findRelativeFile(vfPlatformDir, HybrisConstants.ANT_BUILD_XML);
        final PsiFile psiBuildFile = PsiManager.getInstance(project).findFile(vfBuildFile);
        final AntConfigurationBase antConfiguration = AntConfigurationBase.getInstance(project);
        final AntBuildFileBase antBuildFile = antConfiguration.getAntBuildFile(psiBuildFile);
        ExecutionHandler.runBuild(
            antBuildFile,
            antCleanAll,
            null,
            getDataContext(project),
            Collections.emptyList(),
            AntBuildListener.NULL
        );
    }

    private DataContext getDataContext(final Project project) {
        final WindowManagerEx windowManager = WindowManagerEx.getInstanceEx();
        final Component focusedComponent = windowManager.getFocusedComponent(project);
        final DataContext dataContext = DataManager.getInstance().getDataContext(focusedComponent);
        return dataContext;
    }

    public enum STATES {CLEAN_ALL_NEEDED, REFRESH_NEEDED}
}
