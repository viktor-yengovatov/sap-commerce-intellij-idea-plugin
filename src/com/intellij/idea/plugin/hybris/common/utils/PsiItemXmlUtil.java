package com.intellij.idea.plugin.hybris.common.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.intellij.util.containers.ContainerUtil.newArrayList;

/**
 * TODO Good solve will be a create index between items.xml and java classes
 * @author Nosov Aleksandr
 */
public final class PsiItemXmlUtil {

    public static final String ITEM_TYPE_TAG_NAME = "itemtype";
    public static final String ENUM_TYPE_TAG_NAME = "enumtype";

    private PsiItemXmlUtil() {
    }

    public static List<XmlTag> findTags(final PsiClass psiClass, final String tagName) {
        final Project project = psiClass.getProject();
        final String searchName = cleanSearchName(psiClass.getName());

        final Collection<VirtualFile> files =
            FilenameIndex.getAllFilesByExt(project, "xml", GlobalSearchScope.allScope(project)).stream()
                         .filter(file -> file.getName().endsWith("-items.xml"))
                         .collect(Collectors.toList());

        final List<XmlTag> result = newArrayList();

        for (VirtualFile file : files) {
            final XmlFile xmlFile = (XmlFile) PsiManager.getInstance(project).findFile(file);

            if (xmlFile != null) {
                final DomManager manager = DomManager.getDomManager(project);
                final DomFileElement<DomElement> domFile = manager.getFileElement(xmlFile);
                final DomElement root = domFile.getRootElement();

                if (root.getXmlElement() != null) {
                    final Collection<XmlTag> childrenOfRoot = PsiTreeUtil.getChildrenOfTypeAsList(
                        root.getXmlElement(),
                        XmlTag.class
                    );

                    // find tag by searchName
                    final Optional<XmlTag> tagOfCollection
                        = childrenOfRoot.stream()
                                        .filter(psiElement -> psiElement != null &&
                                                              psiElement.getName().equals(tagName + "s"))
                                        // search element itemtypeS or enumtypeS 
                                        .findFirst();

                    if (tagOfCollection.isPresent()) {
                        final Collection<XmlTag> collection = PsiTreeUtil.getChildrenOfTypeAsList(
                            tagOfCollection.get(),
                            XmlTag.class
                        );
                        final List<XmlTag> it =
                            collection.stream()
                                      .filter(psiElement -> psiElement.getName().equals(tagName))
                                      .filter(psiElement -> searchName.equals(psiElement.getAttributeValue("code")))
                                      .collect(Collectors.toList());
                        result.addAll(it);
                    }
                }
            }
        }
        return result;
    }

    private static String cleanSearchName(@NotNull final String searchName) {
        final int idx = searchName.lastIndexOf("Model");
        if (idx == -1) {
            return searchName;
        }
        return searchName.substring(0, idx);
    }
}