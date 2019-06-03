package com.intellij.idea.plugin.hybris.impex.utils

import com.intellij.lang.properties.IProperty
import com.intellij.lang.properties.PropertiesFileType
import com.intellij.lang.properties.psi.PropertiesFile
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.DelegatingGlobalSearchScope
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import java.util.*

object ProjectPropertiesUtils {


    fun findAllProperties(module: Module): List<IProperty> {
        val result: MutableList<IProperty> = ArrayList()
        val configModule = obtainConfigModule(module)
        val platformModule = obtainPlatformModule(module)
        val scope = createSearchScope(module, configModule, platformModule)
        val files = FileTypeIndex.getFiles(PropertiesFileType.INSTANCE, scope)
        for (virtualFile in files) {
            val file = PsiManager.getInstance(module.project).findFile(virtualFile) as PropertiesFile?
            if (file != null) {
                result.addAll(file.properties)
            }
        }
        return result
    }

    fun findAutoCompleteProperties(module: Module, query: String): List<IProperty> =
        findAllProperties(module).filter { it.key != null && it.key!!.contains(query) || query.isBlank()}

    fun findMacroProperty(module: Module, query: String): IProperty? {
        val allProps = findAllProperties(module)
        if (allProps.isEmpty()) {
            return null;
        }
        val filteredProps = allProps.filter { it.key != null && query.contains(it.key!!) || query.isBlank() }
        if (filteredProps.isEmpty()) {
            return null;
        }

        return filteredProps.reduce { one, two -> if (one.key!!.length > two.key!!.length) one else two }
    }

    private fun createSearchScope(module: Module, configModule: Module, platformModule: Module): GlobalSearchScope {
// to enable project.properties in all extensions
//        val projectPropertiesScope = getScopeRestrictedByFileTypes(everythingScope(module.project), PropertiesFileType.INSTANCE)
//            .filter { it.name == "project.properties" }

        val projectPropertiesScope = module.moduleContentScope.filter { it.name == "project.properties" }
        val advancedPropertiesScope = platformModule.moduleContentScope.filter { it.name == "advanced.properties" }
        val localPropertiesScope = configModule.moduleContentScope.filter { it.name == "local.properties" }

        return projectPropertiesScope.or(advancedPropertiesScope).or(localPropertiesScope)
    }

    private fun obtainConfigModule(module: Module) =
        ModuleManager.getInstance(module.project).modules.first { it.name == "config" }

    private fun obtainPlatformModule(module: Module) =
        ModuleManager.getInstance(module.project).modules.first { it.name == "platform" }

    fun GlobalSearchScope.filter(filter: (VirtualFile) -> Boolean) = object : DelegatingGlobalSearchScope(this) {
        override fun contains(file: VirtualFile): Boolean {
            return filter(file) && super.contains(file)
        }
    }

    fun GlobalSearchScope.or(otherScope: GlobalSearchScope): GlobalSearchScope = union(otherScope)

}