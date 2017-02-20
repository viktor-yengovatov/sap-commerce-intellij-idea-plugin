package com.intellij.idea.plugin.hybris.linemaker;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils;
import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons;
import com.intellij.idea.plugin.hybris.common.utils.PsiItemXmlUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.intellij.idea.plugin.hybris.common.utils.PsiItemXmlUtil.ENUM_TYPE_TAG_NAME;
import static com.intellij.idea.plugin.hybris.common.utils.PsiItemXmlUtil.ITEM_TYPE_TAG_NAME;

/**
 * Class for show gutter icon for navigation between *-item.xml and generated classes.
 *
 * @author Nosov Aleksandr
 */
public class HybrisItemLineMakerProvider extends RelatedItemLineMarkerProvider {

    @Override
    protected void collectNavigationMarkers(
        @NotNull final PsiElement element,
        final Collection<? super RelatedItemLineMarkerInfo> result
    ) {
        if (element instanceof PsiClass) {
            final PsiClass psiClass = (PsiClass) element;
            if (psiClass.getName() != null && (psiClass.getName().contains("Model") ||
                                               psiClass.getSuperClass().getName().contains("Generated"))) {

                final Collection<XmlElement> list = PsiItemXmlUtil.findTags(psiClass, ITEM_TYPE_TAG_NAME);
                if (!list.isEmpty()) {
                    createTargetsWithGutterIcon(result, psiClass, list);
                }
            } else if (psiClass.getImplementsListTypes().length > 0) {
                final boolean anyMatch = ContainerUtil.newHashSet(psiClass.getImplementsListTypes()).stream()
                                                      .anyMatch(psiClassType -> "HybrisEnumValue".equals(psiClassType.getClassName()));
                if (anyMatch) {
                    final Collection<XmlElement> list = PsiItemXmlUtil.findTags(psiClass, ENUM_TYPE_TAG_NAME);
                    
                    if (!list.isEmpty()) {
                        createTargetsWithGutterIcon(result, psiClass, list);
                    }
                }
            }
        }
    }

    private void createTargetsWithGutterIcon(
        final Collection<? super RelatedItemLineMarkerInfo> result,
        final PsiClass psiClass,
        final Collection<XmlElement> list
    ) {
        final NavigationGutterIconBuilder builder
            = NavigationGutterIconBuilder.create(HybrisIcons.TYPE_SYSTEM).setTargets(list);

        builder.setEmptyPopupText(HybrisI18NBundleUtils.message(
            "hybris.gutter.navigate.no.matching.beans",
            new Object[0]
        ));

        builder.setPopupTitle(HybrisI18NBundleUtils.message(
            "hybris.bean.class.navigate.choose.class.title",
            new Object[0]
        ));
        builder.setTooltipText(HybrisI18NBundleUtils.message(
            "hybris.item.class.tooltip.navigate.declaration", new Object[0]
        ));
        result.add(builder.createLineMarkerInfo(psiClass.getNameIdentifier()));
    }
}
