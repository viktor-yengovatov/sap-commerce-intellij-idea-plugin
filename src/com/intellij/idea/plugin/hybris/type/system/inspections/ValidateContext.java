/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2016 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.intellij.idea.plugin.hybris.type.system.inspections;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

interface ValidateContext {

    boolean isOnTheFly();

    @NotNull
    Document getDocument();

    @NotNull
    InspectionManager getManager();

    @NotNull
    MyXPath getXPath();

    @NotNull
    PsiElement mapNodeToPsi(@NotNull Node xmlNode);

    class MyXPath {

        private final XPath myXPath = XPathFactory.newInstance().newXPath();

        public XPath getXPath() {
            return myXPath;
        }

        @NotNull
        public NodeList computeNodeSet(@Nullable String xpath, @NotNull Object start) throws
                                                                                      XPathExpressionException {
            if (xpath == null) {
                return EMPTY_NODE_LIST;
            }
            return (NodeList) myXPath.evaluate(xpath, start, XPathConstants.NODESET);
        }

        @NotNull
        public List<Node> computeList(@Nullable String xpath, @NotNull Object start) throws
                                                                                     XPathExpressionException {
            return asList(computeNodeSet(xpath, start));
        }

        public boolean computeBoolean(@Nullable String xpath, @NotNull Object start) throws
                                                                                     XPathExpressionException {
            if (xpath == null) {
                return false;
            }
            Object result = myXPath.evaluate(xpath, start, XPathConstants.BOOLEAN);
            return Boolean.TRUE.equals(result);
        }

        private static List<Node> asList(@NotNull NodeList nodeList) {
            int length = nodeList.getLength();
            if (length == 0) {
                return Collections.emptyList();
            }
            List<Node> result = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                result.add(nodeList.item(i));
            }
            return result;
        }

        private static NodeList EMPTY_NODE_LIST = new NodeList() {

            @Override
            public Node item(final int index) {
                return null;
            }

            @Override
            public int getLength() {
                return 0;
            }
        };

    }

}
