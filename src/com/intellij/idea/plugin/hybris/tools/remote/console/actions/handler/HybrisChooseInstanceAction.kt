package com.intellij.idea.plugin.hybris.tools.remote.console.actions.handler

import com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils.message
import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons.HYBRIS_REMOTE_ICON
import com.intellij.idea.plugin.hybris.settings.HybrisDeveloperSpecificProjectSettingsComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages


class HybrisChooseInstanceAction : AnAction(
        message("action.choose.hybris.instance.message.text"),
        message("action.choose.hybris.instance.message.title"),
        HYBRIS_REMOTE_ICON) {

    override fun actionPerformed(e: AnActionEvent?) {
        val project = getEventProject(e) ?: return
        val state = HybrisDeveloperSpecificProjectSettingsComponent.getInstance(project).state ?: return
        val list = state.remoteConnectionSettingsList
        val options:Array<String> = list.stream().map { e -> e.toString() }.toArray<String> { length -> arrayOfNulls(length)}
        val active = HybrisDeveloperSpecificProjectSettingsComponent.getInstance(project).getActiveHybrisRemoteConnectionSettings(project)
        val ret =  Messages.showChooseDialog(project,
                message("action.choose.hybris.instance.message.text"),
                message("action.choose.hybris.instance.message.title"),
                HYBRIS_REMOTE_ICON, options, active.toString())
        if (ret == -1)
            return
        state.activeRemoteConnectionHash= list[ret].hashCode()
    }

    override fun update(e: AnActionEvent) {
        val project = e.getData(CommonDataKeys.PROJECT)
        if (project == null) {
            e.presentation.isEnabledAndVisible=false
            return
        }
        val state = HybrisDeveloperSpecificProjectSettingsComponent.getInstance(project).state
        if (state == null) {
            e.presentation.isEnabledAndVisible=false
            return
        }
        e.presentation.isEnabledAndVisible = state.remoteConnectionSettingsList.size > 1
    }
}
