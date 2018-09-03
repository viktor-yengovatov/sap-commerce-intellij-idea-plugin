package com.intellij.idea.plugin.hybris.project.utils;

import com.intellij.idea.plugin.hybris.project.configurators.GroupModuleConfigurator;
import com.intellij.idea.plugin.hybris.project.descriptors.RootModuleDescriptor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static com.intellij.util.containers.ContainerUtil.newHashMap;

public class ModuleGroupUtils {

    private ModuleGroupUtils(){}

    @NotNull
    public static Map<String, String[]> fetchGroupMapping(final GroupModuleConfigurator groupModuleConfigurator, final List<? extends RootModuleDescriptor> modules) {
        Map<String, String[]> groupMapping = newHashMap();
        modules.forEach(module ->{
            final String[] path = groupModuleConfigurator.getGroupName(module);
            if (path != null) {
                groupMapping.put(module.getName(), path);
            }
        });
        return groupMapping;
    }
}
