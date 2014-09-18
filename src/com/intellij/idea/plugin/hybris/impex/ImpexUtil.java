package com.intellij.idea.plugin.hybris.impex;

import com.intellij.idea.plugin.hybris.impex.psi.ImpexFile;
//import com.intellij.idea.plugin.hybris.impex.psi.ImpexProperty;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ImpexUtil {

//    public static List<ImpexProperty> findProperties(Project project, String key) {
//        List<ImpexProperty> result = null;
//        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, ImpexFileType.INSTANCE,
//                GlobalSearchScope.allScope(project));
//        for (VirtualFile virtualFile : virtualFiles) {
//            ImpexFile simpleFile = (ImpexFile) PsiManager.getInstance(project).findFile(virtualFile);
//            if (simpleFile != null) {
//                ImpexProperty[] properties = PsiTreeUtil.getChildrenOfType(simpleFile, ImpexProperty.class);
//                if (properties != null) {
//                    for (ImpexProperty property : properties) {
//                        if (key.equals(property.getKey())) {
//                            if (result == null) {
//                                result = new ArrayList<ImpexProperty>();
//                            }
//                            result.add(property);
//                        }
//                    }
//                }
//            }
//        }
//        return result != null ? result : Collections.<ImpexProperty>emptyList();
//    }
//
//    public static List<ImpexProperty> findProperties(Project project) {
//        List<ImpexProperty> result = new ArrayList<ImpexProperty>();
//        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, ImpexFileType.INSTANCE,
//                GlobalSearchScope.allScope(project));
//        for (VirtualFile virtualFile : virtualFiles) {
//            ImpexFile simpleFile = (ImpexFile) PsiManager.getInstance(project).findFile(virtualFile);
//            if (simpleFile != null) {
//                ImpexProperty[] properties = PsiTreeUtil.getChildrenOfType(simpleFile, ImpexProperty.class);
//                if (properties != null) {
//                    Collections.addAll(result, properties);
//                }
//            }
//        }
//        return result;
//    }
}
