package com.intellij.idea.plugin.hybris.impex.constants;

import com.intellij.util.containers.ContainerUtil;

import java.util.Set;

/**
 * Class contains array of keywords for use to completion on Impex files.
 * keywords got from <a href="https://help.hybris.com/6.0.0/hcd/8bef9530866910148e6cff59d9afa127.html#impexsyntax-header">here</a>
 *
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public final class ImpexKeywords {

    /**
     * @return set of keywords.
     */
    public static Set<String> keywords() {
        return ContainerUtil.newHashSet(
            "INSERT",
            "UPDATE",
            "INSERT_UPDATE",
            "REMOVE"
        );
    }
}
