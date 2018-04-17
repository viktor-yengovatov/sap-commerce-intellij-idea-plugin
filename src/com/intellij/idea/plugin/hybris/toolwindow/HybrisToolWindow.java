package com.intellij.idea.plugin.hybris.toolwindow;

import com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils;
import com.intellij.idea.plugin.hybris.notifications.NotificationUtil;
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettings;
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettingsComponent;
import com.intellij.idea.plugin.hybris.tools.remote.http.HybrisHacHttpClient;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class HybrisToolWindow implements ToolWindowFactory, DumbAware {
    private ToolWindow myToolWindow;
    private Project myProject;
    private JPanel myToolWindowContent;
    private JTextField projectIpTextField;
    private JLabel projectIpLabel;
    private JTextField loginTextField;
    private JPasswordField passwordField;
    private JButton testConnectionButton;
    private JLabel loginNameLabel;
    private JLabel passwordLabel;

    @Override
    public void createToolWindowContent(
        @NotNull final Project project, @NotNull final ToolWindow toolWindow
    ) {
        myToolWindow = toolWindow;
        myProject = project;
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myToolWindowContent, "", false);
        toolWindow.getContentManager().addContent(content);
        projectIpTextField.addActionListener(action->saveSettings());
        loginTextField.addActionListener(action->saveSettings());
        passwordField.addActionListener(action->saveSettings());
        testConnectionButton.addActionListener(action->testConnection());
        final HybrisProjectSettings settings = HybrisProjectSettingsComponent.getInstance(myProject).getState();
        projectIpTextField.setText(settings.getHostIP());
        loginTextField.setText(settings.getHacLogin());
        passwordField.setText(settings.getHacPassword());
    }


    private void testConnection() {
        saveSettings();
        final boolean success = HybrisHacHttpClient.getInstance(myProject).login(myProject);
        final NotificationType type;
        final String message;
        if (success) {
            message = HybrisI18NBundleUtils.message("hybris.toolwindow.hac.test.connection.success");
            type = NotificationType.INFORMATION;
        } else {
            type = NotificationType.WARNING;
            message = HybrisI18NBundleUtils.message("hybris.toolwindow.hac.test.connection.fail");
        }

        NotificationUtil.NOTIFICATION_GROUP.createNotification(
            HybrisI18NBundleUtils.message("hybris.toolwindow.hac.test.connection.title"), message, type, null
        ).notify(myProject);
    }

    private void saveSettings() {
        final HybrisProjectSettings settings = HybrisProjectSettingsComponent.getInstance(myProject).getState();
        settings.setHostIP(projectIpTextField.getText());
        settings.setHacLogin(loginTextField.getText());
        settings.setHacPassword(new String(passwordField.getPassword()));
    }

    @Override
    public boolean shouldBeAvailable(@NotNull final Project project) {
        return HybrisProjectSettingsComponent.getInstance(project).getState().isHybrisProject();
    }
}
