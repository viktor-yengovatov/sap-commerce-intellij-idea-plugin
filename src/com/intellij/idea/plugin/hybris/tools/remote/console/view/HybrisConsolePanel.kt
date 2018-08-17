package com.intellij.idea.plugin.hybris.tools.remote.console.view

import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons
import com.intellij.idea.plugin.hybris.tools.remote.console.*
import com.intellij.idea.plugin.hybris.tools.remote.console.actions.HybrisClearAllAction
import com.intellij.idea.plugin.hybris.tools.remote.console.actions.HybrisExecuteImmediatelyAction
import com.intellij.idea.plugin.hybris.tools.remote.console.actions.HybrisSuspendAction
import com.intellij.idea.plugin.hybris.tools.remote.console.actions.handler.HybrisConsoleExecuteActionHandler
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.CommonShortcuts
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.JBTabsPaneImpl
import icons.JetgroovyIcons.Groovy.Groovy_16x16
import java.awt.BorderLayout
import javax.swing.Icon
import javax.swing.JPanel
import javax.swing.SwingConstants.TOP


/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
class HybrisConsolePanel(val project: Project) : SimpleToolWindowPanel(true), Disposable {
    
    override fun dispose() {
        
    }

    private val impexConsole = HybrisImpexConsole(project)
    private val groovyConsole = HybrisGroovyConsole(project)
    private val monitorConsole = HybrisImpexMonitorConsole(project)

    init {
        layout = BorderLayout()

        val toolbarActions = DefaultActionGroup()
        val actionManager = ActionManager.getInstance()
        val actionToolbar = actionManager.createActionToolbar(ActionPlaces.UNKNOWN, toolbarActions, false)

        val panel = JPanel(BorderLayout())
        
        val tabsPane = HybrisTabs(impexConsole, groovyConsole, monitorConsole, project, TOP)

        panel.add(tabsPane.component, BorderLayout.CENTER)
        actionToolbar.setTargetComponent(tabsPane.component)
        panel.add(actionToolbar.component, BorderLayout.WEST)

        val actionHandler = HybrisConsoleExecuteActionHandler(project, false)
        val executeAction = HybrisExecuteImmediatelyAction(tabsPane, actionHandler)
        executeAction.registerCustomShortcutSet(CommonShortcuts.ALT_ENTER, this.component)
        
        toolbarActions.add(executeAction)
        toolbarActions.add(HybrisSuspendAction(tabsPane, actionHandler))

        val actions = impexConsole.createConsoleActions()
        actions[5] = HybrisClearAllAction(tabsPane)
        toolbarActions.addAll(*actions)
        add(panel)
    }

    override fun getComponent() = super.getComponent()!!
}

class HybrisTabs(impexConsole: HybrisImpexConsole,
                 groovyConsole: HybrisGroovyConsole,
                 impexMonitorConsole: HybrisImpexMonitorConsole,
                 project: Project,
                 tabPlacement: Int) : JBTabsPaneImpl(project, tabPlacement, Disposable {  }) {

    private val consoles = arrayListOf<HybrisConsole>()

    init {
        addConsoleTab("Impex", HybrisIcons.IMPEX_FILE, impexConsole, "Impex Console")

        for (extension in HybrisConsoleProvider.EP_NAME.extensions) {
            val console = extension.createConsole(project)

            if (console != null) {
                addConsoleTab(extension.tabTitle, extension.icon, console, extension.tip)
            }
        }
        addConsoleTab("Groovy Scripting", Groovy_16x16, groovyConsole, "Groovy Console")
        addConsoleTab("Impex Monitor", HybrisIcons.TYPE_SYSTEM, impexMonitorConsole, "Last imported Impex files")
    }

    private fun addConsoleTab(title: String, icon: Icon?, console: HybrisConsole, tip: String) {
        insertTab(title, icon, console.component, tip, consoles.size)
        consoles.add(console)
    }

    fun activeConsole() = consoles[selectedIndex]
}