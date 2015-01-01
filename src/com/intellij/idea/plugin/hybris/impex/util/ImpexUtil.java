package com.intellij.idea.plugin.hybris.impex.util;

public class ImpexUtil {

    private ImpexUtil() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

//
//    public static List<ImpexProperty> findProperties(final Project project, final String key) {
//        List<ImpexProperty> result = null;
//
//        final Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(
//                FileTypeIndex.NAME, ImpexFileType.INSTANCE,
//                GlobalSearchScope.allScope(project)
//        );
//
//        for (VirtualFile virtualFile : virtualFiles) {
//
//            final PsiElement simpleFile = (ImpexFile) PsiManager.getInstance(project).findFile(virtualFile);
//
//            if (simpleFile != null) {
//
//                final ImpexProperty[] properties = PsiTreeUtil.getChildrenOfType(simpleFile, ImpexProperty.class);
//
//                if (properties != null) {
//
//                    for (ImpexProperty property : properties) {
//
//                        if (key.equals(property.getKey())) {
//
//                            if (result == null) {
//                                result = new ArrayList<ImpexProperty>();
//                            }
//
//                            result.add(property);
//                        }
//                    }
//                }
//            }
//        }
//
//        return result != null ? result : Collections.<ImpexProperty>emptyList();
//    }
//
//    public static List<ImpexProperty> findProperties(final Project project) {
//        final List<ImpexProperty> result = new ArrayList<ImpexProperty>();
//
//        final Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(
//                FileTypeIndex.NAME, ImpexFileType.INSTANCE,
//                GlobalSearchScope.allScope(project)
//        );
//
//        for (VirtualFile virtualFile : virtualFiles) {
//
//            final PsiElement simpleFile = (ImpexFile) PsiManager.getInstance(project).findFile(virtualFile);
//
//            if (simpleFile != null) {
//
//                final ImpexProperty[] properties = PsiTreeUtil.getChildrenOfType(simpleFile, ImpexProperty.class);
//
//                if (properties != null) {
//                    Collections.addAll(result, properties);
//                }
//            }
//        }
//
//        return result;
//    }
}
