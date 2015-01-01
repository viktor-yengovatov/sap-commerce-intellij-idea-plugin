package com.intellij.idea.plugin.hybris.impex.util;

public class ImpexPsiImplUtil {

//    public static String getKey(final PsiElement element) {
//        final ASTNode keyNode = element.getNode().findChildByType(ImpexTypes.HEADER_TYPE);
//        if (keyNode != null) {
//            return keyNode.getText();
//        } else {
//            return null;
//        }
//    }
//
//    public static String getValue(final PsiElement element) {
//        final ASTNode valueNode = element.getNode().findChildByType(ImpexTypes.HEADER_MODE_INSERT);
//        if (valueNode != null) {
//            return valueNode.getText();
//        } else {
//            return null;
//        }
//    }
//
//    public static String getName(final PsiElement element) {
//        return getKey(element);
//    }
//
//    public static PsiElement setName(final PsiElement element, final String newName) {
//        final ASTNode keyNode = element.getNode().findChildByType(ImpexTypes.HEADER_TYPE);
//        if (keyNode != null) {
//
//            final ImpexProperty property = ImpexElementFactory.createProperty(element.getProject(), newName);
//            final ASTNode newKeyNode = property.getFirstChild().getNode();
//            element.getNode().replaceChild(keyNode, newKeyNode);
//        }
//        return element;
//    }
//
//    public static PsiElement getNameIdentifier(final PsiElement element) {
//        final ASTNode keyNode = element.getNode().findChildByType(ImpexTypes.HEADER_TYPE);
//        if (keyNode != null) {
//            return keyNode.getPsi();
//        } else {
//            return null;
//        }
//    }
//
//    public static ItemPresentation getPresentation(final ImpexProperty element) {
//        return new ItemPresentation() {
//            @Nullable
//            @Override
//            public String getPresentableText() {
//                return element.getKey();
//            }
//
//            @Nullable
//            @Override
//            public String getLocationString() {
//                return element.getContainingFile().getName();
//            }
//
//            @Nullable
//            @Override
//            public Icon getIcon(final boolean unused) {
//                return ImpexIcons.FILE;
//            }
//        };
//    }
}
