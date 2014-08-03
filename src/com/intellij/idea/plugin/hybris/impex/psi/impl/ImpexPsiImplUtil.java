package com.intellij.idea.plugin.hybris.impex.psi.impl;

import com.intellij.idea.plugin.hybris.impex.psi.ImpexProperty;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.lang.ASTNode;

public class ImpexPsiImplUtil {

    public static String getKey(ImpexProperty element) {
        ASTNode keyNode = element.getNode().findChildByType(ImpexTypes.KEY);
        if (keyNode != null) {
            return keyNode.getText();
        } else {
            return null;
        }
    }

    public static String getValue(ImpexProperty element) {
        ASTNode valueNode = element.getNode().findChildByType(ImpexTypes.VALUE);
        if (valueNode != null) {
            return valueNode.getText();
        } else {
            return null;
        }
    }
}
