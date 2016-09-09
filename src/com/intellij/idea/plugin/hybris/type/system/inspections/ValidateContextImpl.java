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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;

class ValidateContextImpl implements ValidateContext {

    private final InspectionManager myManager;
    @NotNull
    private final XmlFile myPsiFile;
    private final Document myDocument;
    private final boolean myIsOnTheFly;
    private final MyXPath myXPath = new MyXPath();

    public ValidateContextImpl(
        @NotNull InspectionManager manager,
        @NotNull XmlFile psiFile,
        @NotNull Document document,
        boolean isOnTheFly
    ) {
        myManager = manager;
        myPsiFile = psiFile;
        myDocument = document;
        myIsOnTheFly = isOnTheFly;
    }

    @Nullable
    public static ValidateContext createFileContext(
        @NotNull InspectionManager manager,
        boolean isOnTheFly,
        @SuppressWarnings("TypeMayBeWeakened") @NotNull XmlFile psiFile
    ) {
        Document mappedDocument;
        try (StringReader reader = new StringReader(psiFile.getText())) {
            mappedDocument = buildMappedDocument(new InputSource(reader));
        } catch (SAXException | IOException e) {
            //cause will be probably reported by xml integration
            return null;
        }
        return new ValidateContextImpl(manager, psiFile, mappedDocument, isOnTheFly);
    }

    @NotNull
    @Override
    public InspectionManager getManager() {
        return myManager;
    }

    @NotNull
    @Override
    public MyXPath getXPath() {
        return myXPath;
    }

    @Override
    public boolean isOnTheFly() {
        return myIsOnTheFly;
    }

    @NotNull
    @Override
    public Document getDocument() {
        return myDocument;
    }

    @NotNull
    @Override
    public PsiElement mapNodeToPsi(@NotNull final Node xmlNode) {
        PsiElement result = findByLineAndColumn(myPsiFile, MappedDocumentBuilder.START_LOC.get(xmlNode));
        return result == null ? myPsiFile : result;
    }

    @Nullable
    private static PsiElement findByLineAndColumn(
        @NotNull XmlFile file,
        @Nullable Point columnAndLine
    ) {
        if (columnAndLine == null) {
            return file;
        }
        int line = columnAndLine.y - 1;
        int column = columnAndLine.x - 1;

        PsiElement leaf = findByLineAndColumn(file, line, column);

        if (leaf instanceof PsiWhiteSpace) {
            leaf = PsiTreeUtil.prevVisibleLeaf(leaf);
        }

        PsiElement tag = leaf instanceof XmlTag ? leaf : PsiTreeUtil.getParentOfType(leaf, XmlTag.class);
        return tag == null ? leaf : tag;
    }

    private static PsiElement findByLineAndColumn(@NotNull XmlFile file, int line, int column) {
        String fullText = file.getText();
        int offset = StringUtil.lineColToOffset(fullText, line, column);
        if (offset < 0) {
            offset = StringUtil.lineColToOffset(fullText, line, 0);
        }
        PsiElement result = file.findElementAt(offset);
        return result;
    }

    private static Document buildMappedDocument(InputSource source) throws SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);

        Document result;
        try {
            SAXParser sp = factory.newSAXParser();
            result = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            sp.parse(source, new MappedDocumentBuilder(result));
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException("Invalid Parser Configuration", e);
        }
        return result;
    }

    public static class NodeKey<T> {

        private final String myName;

        public NodeKey(@NotNull Class<?> contextClazz, @NotNull String name) {
            this(contextClazz.getName() + ':' + name);
        }

        public NodeKey(@NotNull String name) {
            myName = name;
        }

        public T get(@NotNull Node node) {
            //noinspection unchecked
            return (T) node.getUserData(myName);
        }

        public void put(@NotNull Node node, T value) {
            node.setUserData(myName, value, null);
        }

    }

    public static class MappedDocumentBuilder extends DefaultHandler {

        public static final NodeKey<Point> START_LOC = new NodeKey<>(MappedDocumentBuilder.class, "Start");
        public static final NodeKey<Point> END_LOC = new NodeKey<>(MappedDocumentBuilder.class, "End");

        private Document myDoc;
        private Locator myLocator;
        private LinkedList<Node> myElements = new LinkedList<>();

        public MappedDocumentBuilder(Document doc) {
            myDoc = doc;
            myElements.addFirst(myDoc);
        }

        @Override
        public void setDocumentLocator(Locator locator) {
            myLocator = locator;
        }

        protected Locator getLocator() {
            return myLocator;
        }

        protected void markLocation(@NotNull Node node, @NotNull NodeKey<Point> key) {
            if (myLocator != null) {
                int line = myLocator.getLineNumber();
                int column = myLocator.getColumnNumber();
                Point point = new Point(column, line);
                key.put(node, point);
            }
        }

        @Override
        public void startElement(
            String uri, String localName, String qName, Attributes attributes
        ) {

            Element currentElement;
            if (localName != null && !localName.isEmpty()) {
                currentElement = myDoc.createElementNS(uri, localName);
            } else {
                currentElement = myDoc.createElement(qName);
            }

            markLocation(currentElement, START_LOC);
            appendElement(currentElement);

            if (attributes != null) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    Attr nextAttr = createAndAppendAttribute(currentElement, attributes, i);
                    nextAttr.setValue(attributes.getValue(i));
                    markLocation(nextAttr, START_LOC);
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            Node last = myElements.removeLast();
            markLocation(last, END_LOC);

            if (myElements.isEmpty()) {
                last.normalize();
            }
        }

        @Override
        public void characters(@SuppressWarnings("StandardVariableNames") char[] ch, int start, int length) {
            if (myElements.isEmpty()) {
                //should never happen ?
                return;
            }
            Text text = myDoc.createTextNode(new String(ch, start, length));
            markLocation(text, START_LOC);
            myElements.peekLast().appendChild(text);
        }

        private Attr createAndAppendAttribute(@NotNull Element owner, @NotNull Attributes attrs, int idx) {
            String localName = attrs.getLocalName(idx);
            Attr result;
            if (StringUtil.isEmpty(localName)) {
                result = myDoc.createAttribute(attrs.getQName(idx));
                owner.setAttributeNode(result);
            } else {
                result = myDoc.createAttributeNS(attrs.getURI(idx), localName);
                owner.setAttributeNodeNS(result);
            }
            return result;
        }

        @SuppressWarnings("TypeMayBeWeakened")
        private void appendElement(@NotNull Element childElement) {
            Node last = myElements.getLast();
            last.appendChild(childElement);
            myElements.addLast(childElement);
        }

    }

}