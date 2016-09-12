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

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlRuleParser {

    private static final Logger LOG = Logger.getInstance(XmlRuleParser.class);

    public List<XmlRule> parseRules(InputStream input) throws IOException {
        SAXParser parser;
        try {
            parser = SAXParserFactory.newInstance().newSAXParser();
            RulesHandler handler = new RulesHandler();
            parser.parse(input, handler);

            List<XmlRuleImpl> rules = handler.getRules();
            List<XmlRule> result = new ArrayList<>(rules.size());
            result.addAll(rules);

            return result;
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException("Can't create SAX parser", e);
        }
    }

    private static class RulesHandler extends DefaultHandler {

        private List<XmlRuleImpl> myRules = new ArrayList<>();

        public List<XmlRuleImpl> getRules() {
            return myRules;
        }

        @Override
        public void startElement(
            final String uri,
            final String localName,
            final String qName,
            final Attributes attributes
        ) {

            if ("rule".equals(qName)) {
                String type = attributes.getValue("type");
                if ("XPATH".equals(type)) {
                    XmlRuleImpl rule = createRule(attributes);
                    if (rule != null) {
                        rule.validate(LOG);
                        myRules.add(rule);
                    }
                }
            }
        }

        private
        @Nullable
        XmlRuleImpl createRule(Attributes attrs) {
            String id = attrs.getValue("id");
            XmlRule.Priority priority = XmlRule.Priority.fromAcronym(attrs.getValue("priority"));
            if (id == null) {
                LOG.warn("XPath validation rule without ID found, ignored: " + attrs);
                return null;
            }
            if (priority == null) {
                LOG.warn("XPath validation rule without Priority found, assuming LOW, id: " + id + ": " + attrs);
                return null;
            }

            String description = attrs.getValue("description");
            if (description == null || description.isEmpty()) {
                description = "Unknown Problem";
                LOG.warn("XPath validation rule without description, assuming '" + description +
                         "', id: " + id + ": " + attrs);
            }

            XmlRuleImpl result = new XmlRuleImpl(id, priority, description);

            result.setSelectionXPath(attrs.getValue("selectionQuery"));
            result.setTestXPath(attrs.getValue("testQuery"));
            result.setNameXPath(attrs.getValue("nameQuery"));

            return result;
        }
    }

    private static class XmlRuleImpl implements XmlRule {

        private final String myId;
        private final Priority myPriority;
        private final String myDescription;
        private String myNameXPath;
        private String mySelectionXPath;
        private String myTestXPath;

        public XmlRuleImpl(@NotNull String id, @NotNull Priority priority, @NotNull String description) {
            myId = id;
            myPriority = priority;
            myDescription = description;
        }

        @NotNull
        @Override
        public Priority getPriority() {
            return myPriority;
        }

        @NotNull
        @Override
        public String getID() {
            return myId;
        }

        @NotNull
        @Override
        public String getDescription() {
            return myDescription;
        }

        @Nullable
        @Override
        public String getNameXPath() {
            return myNameXPath;
        }

        void setNameXPath(final String nameXPath) {
            myNameXPath = nameXPath;
        }

        @Nullable
        @Override
        public String getTestXPath() {
            return myTestXPath;
        }

        void setTestXPath(final String testXPath) {
            myTestXPath = testXPath;
        }

        @Nullable
        @Override
        public String getSelectionXPath() {
            return mySelectionXPath;
        }

        public void setSelectionXPath(final String selectionXPath) {
            mySelectionXPath = selectionXPath;
        }

        public boolean validate(Logger logger) {
            boolean isValid = validateNotNull("Missing name XPath", getNameXPath(), logger);
            isValid &= validateNotNull("Missing selection XPath", getSelectionXPath(), logger);
            isValid &= validateNotNull("Missing test XPath", getTestXPath(), logger);
            return isValid;
        }

        private boolean validateNotNull(@NotNull String problem, @Nullable String subj, @NotNull Logger logger) {
            boolean isValid = true;
            if (subj == null || subj.isEmpty()) {
                logger.warn(problem + ": " + this);
                isValid = false;
            }
            return isValid;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + " for :" + getID();
        }
    }


}
