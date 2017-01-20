package com.intellij.idea.plugin.hybris.common.utils;

import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Nosov Aleksandr
 */
public final class PsiXmlUtil {

    private PsiXmlUtil() {
    }

    public static List<XmlTag> findTags(final Project project, final String key) {
        List<XmlTag> result = null;
        final Collection<VirtualFile> virtualFiles =
            FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, XmlFileType.INSTANCE,
                                                            GlobalSearchScope.allScope(project)
            );
        final List<VirtualFile> itemsFile = virtualFiles.stream()
                                                        .filter(xmlFile -> xmlFile.getName().endsWith("-items.xml"))
                                                        .collect(Collectors.toList());
        for (VirtualFile virtualFile : itemsFile) {
            final XmlFile xmlFile = (XmlFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (xmlFile != null) {
                final DomManager manager = DomManager.getDomManager(project);
                final DomFileElement<DomElement> domFile = manager.getFileElement(xmlFile);
                final DomElement root = domFile.getRootElement();
                if (root.getXmlElement() != null) {
                    final PsiElement[] children = root.getXmlElement().getChildren();
                    if (result == null) {
                        result = new ArrayList<>();
                    }
                    final Optional<PsiElement> itemtypes = Arrays.stream(children)
                                                                 .filter(psiElement -> psiElement instanceof XmlTag &&
                                                                                       ((XmlTag) psiElement)
                                                                                           .getName()
                                                                                           .equals("itemtypes"))
                                                                 .findFirst();

                    if (itemtypes.isPresent()) {
                        final List<XmlTag> itemtype = Arrays.stream(itemtypes.get().getChildren())
                                                            .filter(psiElement -> psiElement instanceof XmlTag)
                                                            .map(psiElement -> (XmlTag) psiElement)
                                                            .filter(psiElement -> psiElement.getName()
                                                                                            .equals("itemtype"))
                                                            .filter(psiElement ->
                                                                        Objects.equals(
                                                                            psiElement.getAttributeValue("code"),
                                                                            key.replaceFirst("Model", "")
                                                                        ) || Objects.equals(
                                                                            psiElement.getAttributeValue("code"),
                                                                            key.replaceFirst("Generated", "")
                                                                        ))
                                                            .collect(Collectors.toList());
                        result.addAll(itemtype);
                    }
                }
            }
        }
        return result != null ? result : Collections.emptyList();
    }

}