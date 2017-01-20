package com.intellij.idea.plugin.hybris.linemaker;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils;
import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons;
import com.intellij.idea.plugin.hybris.common.utils.PsiXmlUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.spring.gutter.SpringBeansPsiElementCellRenderer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Class for show gutter icon for navigation between *-item.xml and generated classes.
 *
 * @author Nosov Aleksandr
 */
public class HybrisItemLineMakerProvider extends RelatedItemLineMarkerProvider {

    @Override
    protected void collectNavigationMarkers(
        @NotNull final PsiElement element, final Collection<? super RelatedItemLineMarkerInfo> result
    ) {
        if (element instanceof PsiClass && (element.getText().contains("ItemModel") ||
                                            element.getText().contains("Generated"))) {
            final PsiClass psiClass = (PsiClass) element;

            final PsiClass superClass = ((PsiClass) element).getSuperClass();
            if (superClass != null && ("ItemModel".equals(superClass.getName()) ||
                                       superClass.getName().startsWith("Generated"))) {
                final Project project = element.getProject();
                final Collection<XmlTag> list = PsiXmlUtil.findTags(project, psiClass.getName());

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
                builder.setCellRenderer(SpringBeansPsiElementCellRenderer.INSTANCE);
                builder.setTooltipText(HybrisI18NBundleUtils.message(
                    "hybris.item.class.tooltip.navigate.declaration", new Object[0]
                ));
                result.add(builder.createLineMarkerInfo(psiClass.getNameIdentifier()));
            }
        }
    }
}
