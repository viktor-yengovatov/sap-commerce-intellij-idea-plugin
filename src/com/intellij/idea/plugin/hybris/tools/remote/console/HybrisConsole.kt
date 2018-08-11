package com.intellij.idea.plugin.hybris.tools.remote.console

import com.intellij.execution.console.ConsoleHistoryController
import com.intellij.execution.console.ConsoleRootType
import com.intellij.execution.console.LanguageConsoleImpl
import com.intellij.idea.plugin.hybris.common.HybrisConstants
import com.intellij.idea.plugin.hybris.flexibleSearch.FlexibleSearchLanguage
import com.intellij.idea.plugin.hybris.impex.ImpexLanguage
import com.intellij.idea.plugin.hybris.settings.HybrisProjectSettingsComponent
import com.intellij.lang.Language
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.util.io.FileUtil.toCanonicalPath
import com.intellij.ui.ListCellRendererWrapper
import com.intellij.ui.components.JBLabel
import org.apache.batik.ext.swing.GridBagConstants
import org.jetbrains.plugins.groovy.GroovyLanguage
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.io.File
import java.util.concurrent.TimeUnit
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */
abstract class HybrisConsole(project: Project, title: String, language: Language) : LanguageConsoleImpl(project, title, language)

class HybrisImpexConsole(project: Project) : HybrisConsole(project, "Hybris Impex Console", ImpexLanguage.getInstance()) {
    object MyConsoleRootType : ConsoleRootType("hybris.impex.shell", null)

    init {
        ConsoleHistoryController(MyConsoleRootType, "hybris.impex.shell", this).install()
    }
}

class HybrisFSConsole(project: Project) : HybrisConsole(project, "Hybris FS Console", FlexibleSearchLanguage.getInstance()) {
    object MyConsoleRootType : ConsoleRootType("hybris.fs.shell", null)

    init {
        ConsoleHistoryController(MyConsoleRootType, "hybris.fs.shell", this).install()
    }
}

class HybrisGroovyConsole(project: Project) : HybrisConsole(project, "Hybris Groovy Console", GroovyLanguage) {
    object MyConsoleRootType : ConsoleRootType("hybris.groovy.shell", null)

    init {
        ConsoleHistoryController(MyConsoleRootType, "hybris.groovy.shell", this).install()
    }
}

class HybrisImpexMonitorConsole(project: Project) : HybrisConsole(project, "Hybris Monitor Console", ImpexLanguage.getInstance()) {
    object MyConsoleRootType : ConsoleRootType("hybris.impex.monitor.shell", null)

    private val panel = JPanel()
    private val timeComboBox = ComboBox(arrayOf(
            TimeOption("in the last 5 minutes", 5, TimeUnit.MINUTES),
            TimeOption("in the last 10 minutes", 10, TimeUnit.MINUTES),
            TimeOption("in the last 15 minutes", 15, TimeUnit.MINUTES),
            TimeOption("in the last 30 minutes", 30, TimeUnit.MINUTES),
            TimeOption("in the last 1 hour", 1, TimeUnit.HOURS)
    ))
    private val workingDirLabel = JBLabel("Hybris Data Folder: ${obtainDataFolder(project)}")
    private val timeOptionLabel = JBLabel("Imported Impex")

    init {
        createUI()
        ConsoleHistoryController(MyConsoleRootType, "hybris.impex.monitor.shell", this).install()
    }

    private fun createUI() {
        timeComboBox.renderer = object : ListCellRendererWrapper<TimeOption>() {
            override fun customize(list: JList<*>?, value: TimeOption, index: Int, selected: Boolean, hasFocus: Boolean) {
                setText(value.name)
            }
        }
        isConsoleEditorEnabled = false
        panel.layout = GridBagLayout()
        val constraints = GridBagConstraints()
        constraints.weightx = 0.0
        timeOptionLabel.border = EmptyBorder(0, 10, 0, 5)
        panel.add(timeOptionLabel)
        panel.add(timeComboBox, constraints)
        constraints.weightx = 1.0
        constraints.fill = GridBagConstants.HORIZONTAL
        workingDirLabel.border = EmptyBorder(0, 10, 0, 10)
        panel.add(workingDirLabel, constraints)
        add(panel, BorderLayout.NORTH)
        isEditable = true
    }

    private fun obtainDataFolder(project: Project): String {
        val settings = HybrisProjectSettingsComponent.getInstance(project).state
        return toCanonicalPath("${project.basePath}${File.separatorChar}${settings.hybrisDirectory}${File.separatorChar}${HybrisConstants.HYBRIS_DATA_DIRECTORY}")
    }

    fun timeOption() = (timeComboBox.selectedItem as TimeOption)

    fun workingDir() = obtainDataFolder(project)
}

data class TimeOption(val name: String, val value: Int, val unit: TimeUnit)