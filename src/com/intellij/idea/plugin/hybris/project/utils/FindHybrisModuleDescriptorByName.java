package com.intellij.idea.plugin.hybris.project.utils;

import com.google.common.base.Predicate;
import com.intellij.idea.plugin.hybris.project.settings.HybrisModuleDescriptor;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created 8:31 PM 20 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class FindHybrisModuleDescriptorByName implements Predicate<HybrisModuleDescriptor> {

    private final String name;

    public FindHybrisModuleDescriptorByName(@NotNull final String name) {
        Validate.notEmpty(name);

        this.name = name;
    }

    @Override
    public boolean apply(@Nullable final HybrisModuleDescriptor t) {
        return null != t && name.equalsIgnoreCase(t.getModuleName());
    }
}
