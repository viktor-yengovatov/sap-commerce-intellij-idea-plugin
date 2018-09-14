package com.intellij.idea.plugin.hybris.toolwindow;

import com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils;
import com.intellij.idea.plugin.hybris.settings.HybrisDeveloperSpecificProjectSettings;
import com.intellij.idea.plugin.hybris.settings.HybrisDeveloperSpecificProjectSettingsComponent;
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettingsComponent;
import com.intellij.idea.plugin.hybris.settings.HybrisRemoteConnectionSettings;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.AddEditDeleteListPanel;
import com.intellij.ui.ListUtil;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

public class HybrisToolWindow implements ToolWindowFactory, DumbAware {
    private ToolWindow myToolWindow;
    private Project myProject;
    private JPanel myToolWindowContent;
    private JPanel connectionPanel;

    private HybrisDeveloperSpecificProjectSettings state;
    private MyListPanel myListPanel;

    @Override
    public void createToolWindowContent(
        @NotNull final Project project, @NotNull final ToolWindow toolWindow
    ) {
        myToolWindow = toolWindow;
        myProject = project;
        state = HybrisDeveloperSpecificProjectSettingsComponent.getInstance(myProject).getState();
        myListPanel.setInitialList(state.getRemoteConnectionSettingsList());
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myToolWindowContent, "", false);
        toolWindow.getContentManager().addContent(content);
    }


    @Override
    public boolean shouldBeAvailable(@NotNull final Project project) {
        return HybrisProjectSettingsComponent.getInstance(project).getState().isHybrisProject();
    }

    private void createUIComponents() {
        myListPanel = new MyListPanel(HybrisI18NBundleUtils.message("hybris.toolwindow.remote.label"), new ArrayList<>());
        connectionPanel = myListPanel;
    }

    class MyListPanel extends AddEditDeleteListPanel<HybrisRemoteConnectionSettings> {

        public MyListPanel(
            final String title,
            final List<HybrisRemoteConnectionSettings> initialList
        ) {
            super(title, initialList);
            myList.getModel().addListDataListener(new ListDataListener() {

                @Override
                public void intervalAdded(final ListDataEvent e) {
                    saveSettings();
                }

                @Override
                public void intervalRemoved(final ListDataEvent e) {
                    saveSettings();
                }

                @Override
                public void contentsChanged(final ListDataEvent e) {
                    saveSettings();
                }
            });
        }

        @Nullable
        @Override
        protected HybrisRemoteConnectionSettings editSelectedItem(final HybrisRemoteConnectionSettings item) {
            final boolean ok = new RemoteConnectionDialog(myProject, myListPanel, item).showAndGet();
            return ok ? item : null;
        }

        @Nullable
        @Override
        protected HybrisRemoteConnectionSettings findItemToAdd() {
            HybrisRemoteConnectionSettings item = HybrisDeveloperSpecificProjectSettingsComponent.getInstance(myProject).getDefaultHybrisRemoteConnectionSettings(myProject);
            final boolean ok = new RemoteConnectionDialog(myProject, myListPanel, item).showAndGet();
            return ok ? item : null;
        }

        public void setInitialList(final List<HybrisRemoteConnectionSettings> remoteConnectionSettingsList) {
            remoteConnectionSettingsList.forEach(this::addElement);
        }

        protected void customizeDecorator(ToolbarDecorator decorator) {
            super.customizeDecorator(decorator);
            decorator.setMoveUpAction(button -> ListUtil.moveSelectedItemsUp(myList));
            decorator.setMoveDownAction(button -> ListUtil.moveSelectedItemsDown(myList));
        }

        public List<HybrisRemoteConnectionSettings> getData() {
            List<HybrisRemoteConnectionSettings> remoteConnectionSettingsList = new ArrayList<>();
            for (int index=0; index<myList.getModel().getSize(); index++) {
                remoteConnectionSettingsList.add(myList.getModel().getElementAt(index));
            }
            return remoteConnectionSettingsList;
        }
    }

    private void saveSettings() {
        state.setRemoteConnectionSettingsList(myListPanel.getData());
    }


}
