package com.intellij.idea.plugin.hybris.project.utils;

import org.apache.commons.lang.Validate;

import java.util.Comparator;

/**
 * Created 6:18 PM 13 June 2015.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ProjectRootsComparator implements Comparator<String> {

    //public static final int LESS = -1;
    public static final int EQUALS = 0;
    //public static final int GREATER = 1;

    @Override
    public int compare(final String o1, final String o2) {
        Validate.notEmpty(o1);
        Validate.notEmpty(o2);

        if (o1.equals(o2)) {
            return EQUALS;
        }

        final String projectName1 = HybrisProjectUtils.getModuleName(o1);
        final String projectName2 = HybrisProjectUtils.getModuleName(o2);

        return ((null != projectName1) && (null != projectName2))
               ? projectName1.compareToIgnoreCase(projectName2)
               : EQUALS;
    }
}
