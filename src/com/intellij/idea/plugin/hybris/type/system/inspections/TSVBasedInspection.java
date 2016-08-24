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

import com.hybris.ps.tsv.results.IResult;
import com.hybris.ps.tsv.rules.executable.XPathRule;
import com.hybris.ps.tsv.utils.LocationRecordingHandler;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 15/06/2016.
 */
public class TSVBasedInspection extends LocalInspectionTool {

    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    @Nullable
    @Override
    public ProblemDescriptor[] checkFile(
        final @NotNull PsiFile file,
        final @NotNull InspectionManager manager,
        final boolean isOnTheFly
    ) {
        if (false == file instanceof XmlFile) {
            return null;
        }
        final XmlFile xmlFile = (XmlFile) file;

        final File ioFile = VfsUtilCore.virtualToIoFile(file.getVirtualFile());
        if (!ioFile.exists()) {
            return null;
        }

        final Document w3cDocument;
        final InputSource inputSource;
        try {
            inputSource = new InputSource(new StringReader(file.getText()));
            w3cDocument = createDocument(inputSource);
        } catch (SAXException e) {
            //cause will be probably reported by xml integration
            return null;
        } catch (IOException e) {
            return null;
        }

        final List<XPathRule> xpathRules = TSVRuleSetAccess.getInstance().getXPathRules();
        final List<ProblemDescriptor> results = new LinkedList<ProblemDescriptor>();

        for (XPathRule next : xpathRules) {
            List<IResult> problems = next.check(ioFile, w3cDocument);
            if (problems == null) {
                problems = Collections.emptyList();
            }
            for (IResult nextProblem : problems) {
                switch (nextProblem.getState()) {
                    case FAIL:
                    case ERROR:
                        PsiElement owner = findByEncodedLineAndColumn(xmlFile, nextProblem.getLine());
                        if (owner == null) {
                            owner = xmlFile;
                        }
                        final ProblemDescriptor nextResult = manager.createProblemDescriptor(
                            owner,
                            nextProblem.getDescription(),
                            true,
                            ProblemHighlightType.ERROR,
                            isOnTheFly
                        );
                        results.add(nextResult);
                }
            }
        }

        return results.toArray(new ProblemDescriptor[results.size()]);
    }

    private PsiElement findByEncodedLineAndColumn(
        final @NotNull XmlFile file,
        @Nullable final Integer encodedLineAndColumn
    ) {
        if (encodedLineAndColumn == null) {
            return file;
        }
        int line = RecordingEncodedColumnAndLineHandler.decodeLine(encodedLineAndColumn) - 1;
        int column = RecordingEncodedColumnAndLineHandler.decodeColumn(encodedLineAndColumn) - 1;

        PsiElement leaf = findByLineAndColumn(file, line, column);

        if (leaf instanceof PsiWhiteSpace) {
            leaf = PsiTreeUtil.prevVisibleLeaf(leaf);
        }

        PsiElement tag = leaf instanceof XmlTag ? leaf : PsiTreeUtil.getParentOfType(leaf, XmlTag.class);
        return tag == null ? leaf : tag;
    }

    private static String safeGetText(@Nullable PsiElement psi) {
        return psi == null ? "" : psi.getText();
    }

    private PsiElement findByLineAndColumn(XmlFile file, int line, int column) {
        String fullText = file.getText();
        int offset = StringUtil.lineColToOffset(fullText, line, column);
        if (offset < 0) {
            offset = StringUtil.lineColToOffset(fullText, line, 0);
        }
        PsiElement result = file.findElementAt(offset);
        return result;
    }

    private Document createDocument(InputSource source) throws SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);

        Document doc;
        try {
            SAXParser sp = factory.newSAXParser();
            sp.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            sp.parse(source, new RecordingEncodedColumnAndLineHandler(doc));
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException("Invalid Parser Configuration", e);
        }
        return doc;
    }

    private static class RecordingEncodedColumnAndLineHandler extends LocationRecordingHandler {

        private static int MAX_COLUMN = 1000;

        public RecordingEncodedColumnAndLineHandler(final Document doc) {
            super(doc);
        }

        @Override
        public void setDocumentLocator(final Locator locator) {
            super.setDocumentLocator(locator);
        }

        @Override
        protected void setLocationData(Node n) {
            final Locator locator = getDocumentLocator();
            if (locator != null) {
                final int encodedLineAndCol = encodeLineAndColumn(locator.getLineNumber(), locator.getColumnNumber());
                n.setUserData(KEY_LIN_NO, encodedLineAndCol, null);
            }
        }

        public static int encodeLineAndColumn(int line, int column) {
            return column + line * MAX_COLUMN;
        }

        public static int decodeColumn(int encoded) {
            return encoded % MAX_COLUMN;
        }

        public static int decodeLine(int encoded) {
            return encoded / MAX_COLUMN;
        }

    }


}
