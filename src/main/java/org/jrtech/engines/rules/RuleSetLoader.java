/*
 * Copyright (c) 2016-2026 Jumin Rubin
 * LinkedIn: https://www.linkedin.com/in/juminrubin/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jrtech.engines.rules;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.jrtech.common.utils.ResourceLocatorUtil;
import org.jrtech.common.xmlutils.XmlUtils;
import org.jrtech.engines.rules.model.AbstractAttribute;
import org.jrtech.engines.rules.model.BooleanConditionalAttribute;
import org.jrtech.engines.rules.model.CallParameter;
import org.jrtech.engines.rules.model.ConditionalAttribute;
import org.jrtech.engines.rules.model.DateConditionalAttribute;
import org.jrtech.engines.rules.model.DatetimeConditionalAttribute;
import org.jrtech.engines.rules.model.GoalAttribute;
import org.jrtech.engines.rules.model.NumberConditionalAttribute;
import org.jrtech.engines.rules.model.Rule;
import org.jrtech.engines.rules.model.RuleCondition;
import org.jrtech.engines.rules.model.StringConditionalAttribute;
import org.jrtech.engines.rules.model.TimeConditionalAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RuleSetLoader {

    public static final String TAG_INCLUDE = "include";

    public static final String ATTR_NAME = "name";

    public static final String ATTR_SCOPE_INDEX = "scopeIndex";

    public static final String ATTR_TERMINATING = "terminating";

    public static final String ATTR_URI = "uri";

    public static final String SCOPE_INDEX_SEPARATOR = "-";

    private static Logger log = LoggerFactory.getLogger(RuleSetLoader.class);

    private static final String DEFAULT_CONDITIONAL_ATTRIBUTE_SUFFIX = ConditionalAttribute.class.getSimpleName()
            .toLowerCase();

    protected static final ConcurrentMap<String, ConditionalAttributeBuilder<? extends ConditionalAttribute>> BUILDER_CATALOG = new ConcurrentHashMap<>();

    private Map<String, List<Rule<?>>> scopeIndexedRuleSetCatalog = null;

    private String ruleSetName = null;

    public static RuleSetLoader newInstance() {
        return new RuleSetLoader();
    }

    public <T> List<Rule<T>> load(String xmlString) throws Exception {
        Document xmlDocument = XmlUtils.createDocument(xmlString);
        return load(xmlDocument);
    }

    public <T> List<Rule<T>> load(InputStream xmlStream) throws Exception {
        Document xmlDocument = XmlUtils.createDocument(xmlStream);
        return load(xmlDocument);
    }

    public <T> List<Rule<T>> load(Document xmlDocument) throws Exception {
        List<Rule<T>> result = new ArrayList<Rule<T>>();
        Set<String> loadedRuleSet = new HashSet<String>();

        ruleSetName = null;
        scopeIndexedRuleSetCatalog = null;

        int ruleIndex = 0;
        Element xmlRootElement = xmlDocument.getDocumentElement();
        if (xmlRootElement != null) {
            ruleSetName = xmlRootElement.getAttribute(ATTR_NAME);
            scopeIndexedRuleSetCatalog = new ConcurrentHashMap<>();

            ResourceLocatorUtil locationUtil = ResourceLocatorUtil.newInstance();
            List<Element> xmlNodeList = XmlUtils.getChildElementList(xmlRootElement);
            for (int i = 0; i < xmlNodeList.size(); i++) {
                Element xmlElement = xmlNodeList.get(i);
                if (Rule.TAG_RULE.equals(xmlElement.getTagName())) {
                    Rule<T> rule = xmlToRule(xmlElement);
                    if (rule.getId() == null || rule.getId().trim().length() < 1) {
                        rule.setId("" + (ruleIndex++));
                    }
                    if (rule != null && !loadedRuleSet.contains(rule.getId())) {
                        loadedRuleSet.add(rule.getId());
                        result.add(rule);
                        indexRule(scopeIndexedRuleSetCatalog, rule);
                    }
                } else if (TAG_INCLUDE.equals(xmlElement.getTagName())) {
                    String includeDocLocation = xmlElement.getAttribute(ATTR_URI).trim();
                    if (includeDocLocation.length() > 0) {
                        URL includeDocUrl = locationUtil.resolveUrlFromLocationString(includeDocLocation);
                        InputStream subStructureStream = null;
                        try {
                            subStructureStream = includeDocUrl.openStream();
                            Document subStructureConfigDoc = XmlUtils.createDocument(subStructureStream);
                            List<Rule<T>> includeRuleList = load(subStructureConfigDoc);
                            for (Rule<T> includeIntepretation : includeRuleList) {
                                if (includeIntepretation.getId() == null
                                        || includeIntepretation.getId().trim().length() < 1) {
                                    includeIntepretation.setId("" + (ruleIndex++));
                                }
                                if (!loadedRuleSet.contains(includeIntepretation.getId())) {
                                    result.add(includeIntepretation);
                                    loadedRuleSet.add(includeIntepretation.getId());
                                    indexRule(scopeIndexedRuleSetCatalog, includeIntepretation);
                                }
                            }
                        } catch (Exception e) {
                            log.warn("Failure in loading include rule document: '" + includeDocLocation + "'", e);
                        } finally {
                            if (subStructureStream != null) {
                                try {
                                    subStructureStream.close();
                                } catch (Exception e) {
                                    // do nothing
                                }
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    public String writeAsString(List<Rule<?>> ruleSet) throws Exception {
        return writeAsString(ruleSet, "root");
    }

    public String writeAsString(List<Rule<?>> ruleSet, String rootTagName) throws Exception {
        return writeAsString(ruleSet, rootTagName, false);
    }

    public String writeAsString(List<Rule<?>> ruleSet, String rootTagName, String ruleSetName) throws Exception {
        return writeAsString(ruleSet, rootTagName, false, ruleSetName);
    }

    public String writeAsString(List<Rule<?>> ruleSet, String rootTagName, boolean format) throws Exception {
        return writeAsString(ruleSet, rootTagName, format, ruleSetName);
    }

    public String writeAsString(List<Rule<?>> ruleSet, String rootTagName, boolean format, String ruleSetName)
            throws Exception {
        Document xmlDoc = XmlUtils.createDocument("<" + rootTagName + "/>");

        for (Rule<?> rule : ruleSet) {
            Element xmlRuleElement = XmlUtils.createSubElement(xmlDoc.getDocumentElement(), Rule.TAG_RULE);
            xmlFromRule(xmlRuleElement, rule);
        }

        return XmlUtils.nodeToString(xmlDoc, format, true);
    }

    protected <T> void xmlFromRule(Element xmlRuleElement, Rule<T> rule) {
        if (xmlRuleElement == null)
            return;

        xmlRuleElement.setAttribute(XmlUtils.XML_ATTRIBUTE_ID, rule.getId());
        if (rule.getScopeIndex() != null || rule.getScopeIndex().trim().length() > 0) {
            xmlRuleElement.setAttribute(Rule.ATTR_SCOPE_INDEX, rule.getScopeIndex());
        }
        xmlRuleElement.setAttribute(ATTR_TERMINATING, "" + rule.isTerminating());

        // Goals
        Element xmlGoalsElement = XmlUtils.getChildByTagName(xmlRuleElement, Rule.TAG_GOALS);
        if (xmlGoalsElement == null) {
            xmlGoalsElement = XmlUtils.createSubElement(xmlRuleElement, Rule.TAG_GOALS);
        } else {
            // Clean Child
            while (xmlGoalsElement.hasChildNodes()) {
                xmlGoalsElement.removeChild(xmlGoalsElement.getLastChild());
            }
        }
        for (GoalAttribute goalAttribute : rule.getGoals()) {
            Element xmlGoalAttributeElement = XmlUtils.createSubElement(xmlGoalsElement, AbstractAttribute.TAG);
            xmlFromGoalAttribute(xmlGoalAttributeElement, goalAttribute);
        }

        // Conditions
        Element xmlConditionsElement = XmlUtils.getChildByTagName(xmlRuleElement, Rule.TAG_CONDITIONS);
        if (xmlConditionsElement == null) {
            xmlConditionsElement = XmlUtils.createSubElement(xmlRuleElement, Rule.TAG_CONDITIONS);
        } else {
            // Clean Child
            while (xmlConditionsElement.hasChildNodes()) {
                xmlConditionsElement.removeChild(xmlConditionsElement.getLastChild());
            }
        }
        for (RuleCondition ruleCondition : rule.getConditions()) {
            Element xmlRuleConditionElement = XmlUtils.createSubElement(xmlConditionsElement, Rule.TAG_CONDITION);
            for (ConditionalAttribute conditionalAttribute : ruleCondition.getAttributes()) {
                Element xmlConditionAttributeElement = XmlUtils.createSubElement(xmlRuleConditionElement,
                        AbstractAttribute.TAG);
                xmlFromConditionalAttribute(xmlConditionAttributeElement, conditionalAttribute);
            }
        }
    }

    protected <T> Rule<T> xmlToRule(Element xmlRuleElement) {
        if (xmlRuleElement == null)
            return null;

        Rule<T> rule = newRule(xmlRuleElement.getAttribute(XmlUtils.XML_ATTRIBUTE_ID));

        if (xmlRuleElement.hasAttribute(Rule.ATTR_SCOPE_INDEX)) {
            rule.setScopeIndex(xmlRuleElement.getAttribute(Rule.ATTR_SCOPE_INDEX));
        }
        boolean terminating = "true".equalsIgnoreCase(xmlRuleElement.getAttribute(ATTR_TERMINATING)) ? true : false;
        rule.setTerminating(terminating);

        Element xmlGoalsElement = XmlUtils.getChildByTagName(xmlRuleElement, Rule.TAG_GOALS);
        List<Element> xmlGoalAttributeElementList = XmlUtils.getChildElementListByTagName(xmlGoalsElement,
                AbstractAttribute.TAG);
        for (Element xmlGoalAttributeElement : xmlGoalAttributeElementList) {
            GoalAttribute ga = xmlToGoalAttribute(xmlGoalAttributeElement);
            rule.getGoals().add(ga);
        }

        Element xmlConditionsElement = XmlUtils.getChildByTagName(xmlRuleElement, Rule.TAG_CONDITIONS);
        List<Element> xmlConditionElementList = XmlUtils.getChildElementListByTagName(xmlConditionsElement,
                Rule.TAG_CONDITION);
        for (Element xmlConditionElement : xmlConditionElementList) {
            List<ConditionalAttribute> caList = new ArrayList<ConditionalAttribute>();
            List<Element> xmlConditionAttributeElementList = XmlUtils.getChildElementListByTagName(xmlConditionElement,
                    AbstractAttribute.TAG);
            for (Element xmlConditionAttributeElement : xmlConditionAttributeElementList) {
                ConditionalAttribute ca = xmlToConditionalAttribute(xmlConditionAttributeElement);
                caList.add(ca);
            }
            if (!caList.isEmpty()) {
                rule.getConditions().add(new RuleCondition(caList));
            }
        }

        return rule;
    }

    protected void xmlFromConditionalAttribute(Element xmlConditionAttributeElement,
            ConditionalAttribute conditionalAttribute) {
        xmlConditionAttributeElement.setAttribute(ConditionalAttribute.ATTR_NEGATE,
                "" + conditionalAttribute.isNegated());
        xmlConditionAttributeElement.setAttribute(ConditionalAttribute.ATTR_TYPE,
                conditionalAttribute.getClass().getSimpleName());
        xmlConditionAttributeElement.setAttribute(ConditionalAttribute.ATTR_OPERATOR_FUNCTION,
                conditionalAttribute.getOperatorFunction() == null ? "" : conditionalAttribute.getOperatorFunction());

        xmlConditionAttributeElement.setAttribute(AbstractAttribute.ATTR_NAME, conditionalAttribute.getName());
        xmlConditionAttributeElement.setAttribute(AbstractAttribute.ATTR_VALUE, conditionalAttribute.getValue());
        xmlConditionAttributeElement.setAttribute(AbstractAttribute.ATTR_OWNER,
                conditionalAttribute.getOwner().toString());

        xmlConditionAttributeElement.setAttribute(AbstractAttribute.ATTR_READER_CLASS,
                conditionalAttribute.getReaderClass());
    }

    protected void xmlFromConditionalAttribute(Element xmlConditionAttributeElement,
            StringConditionalAttribute conditionalAttribute) {
        xmlFromConditionalAttribute(xmlConditionAttributeElement, (ConditionalAttribute) conditionalAttribute);
        xmlConditionAttributeElement.setAttribute(StringConditionalAttribute.ATTR_CASE_SENSITIVE,
                "" + conditionalAttribute.isCaseSensitive());
        xmlConditionAttributeElement.setAttribute(StringConditionalAttribute.ATTR_REGEX,
                "" + conditionalAttribute.isRegex());
    }

    protected ConditionalAttribute xmlToConditionalAttribute(Element xmlConditionAttributeElement) {
        String type = xmlConditionAttributeElement.getAttribute(ConditionalAttribute.ATTR_TYPE);

        ConditionalAttribute ca = null;
        String builderKey = type.toLowerCase();
        if (builderKey.endsWith(DEFAULT_CONDITIONAL_ATTRIBUTE_SUFFIX.toLowerCase())) {
            builderKey = builderKey.substring(0, builderKey.length() - DEFAULT_CONDITIONAL_ATTRIBUTE_SUFFIX.length());
        }

        if (builderKey == null || builderKey.trim().length() < 1) {
            builderKey = "string";
        }

        ConditionalAttributeBuilder<?> builder = getConcreteAttributeBuilder(builderKey);
        if (builder == null) {
            builder = new StringConditionalAttributeBuilder();
        }
        ca = builder.xmlToConcreteConditionalAttribute(xmlConditionAttributeElement);

        if (ca != null) {
            ca.setOperatorFunction(
                    xmlConditionAttributeElement.getAttribute(ConditionalAttribute.ATTR_OPERATOR_FUNCTION));
        }

        return ca;
    }

// @formatter:off
//    protected StringConditionalAttribute xmlToStringConditionalAttribute(Element xmlConditionAttributeElement) {
//        boolean negate = "true".equalsIgnoreCase(
//                xmlConditionAttributeElement.getAttribute(ConditionalAttribute.ATTR_NEGATE)) ? true : false;
//
//        StringConditionalAttribute ca = new StringConditionalAttribute(
//                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_NAME),
//                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_VALUE), negate, Owner.fromString(
//                        xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_OWNER), Owner.SOURCE));
//
//        ca.setReaderClass(xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_READER_CLASS));
//
//        boolean regex = false;
//        if (xmlConditionAttributeElement.hasAttribute(StringConditionalAttribute.ATTR_REGEX)) {
//            // For Backward compatibility
//            regex = "true".equalsIgnoreCase(
//                    xmlConditionAttributeElement.getAttribute(StringConditionalAttribute.ATTR_REGEX)) ? true : false;
//            if (regex) {
//                ca.setOperatorFunction(StringConditionalAttribute.INTERNAL_FUNCTION_REGEX);
//            }
//        }
//
//        if (!regex && xmlConditionAttributeElement.hasAttribute(StringConditionalAttribute.ATTR_CASE_SENSITIVE)) {
//            // For Backward compatibility
//            boolean caseSensitive = "false".equalsIgnoreCase(
//                    xmlConditionAttributeElement.getAttribute(StringConditionalAttribute.ATTR_CASE_SENSITIVE)) ? false
//                            : true;
//            if (!caseSensitive) {
//                ca.setOperatorFunction(StringConditionalAttribute.INTERNAL_FUNCTION_IGNORE_CASE);
//            }
//        }
//
//        return ca;
//    }
//
//    protected NumberConditionalAttribute xmlToNumericConditionalAttribute(Element xmlConditionAttributeElement) {
//        boolean negate = "true".equalsIgnoreCase(
//                xmlConditionAttributeElement.getAttribute(ConditionalAttribute.ATTR_NEGATE)) ? true : false;
//
//        NumberConditionalAttribute ca = new NumberConditionalAttribute(
//                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_NAME),
//                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_VALUE), negate, Owner.fromString(
//                        xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_OWNER), Owner.SOURCE));
//
//        ca.setReaderClass(xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_READER_CLASS));
//
//        return ca;
//    }
//
//    protected BooleanConditionalAttribute xmlToBooleanConditionalAttribute(Element xmlConditionAttributeElement) {
//        boolean negate = "true".equalsIgnoreCase(
//                xmlConditionAttributeElement.getAttribute(ConditionalAttribute.ATTR_NEGATE)) ? true : false;
//        boolean value = "true".equalsIgnoreCase(
//                xmlConditionAttributeElement.getAttribute(ConditionalAttribute.ATTR_VALUE)) ? true : false;
//
//        BooleanConditionalAttribute ca = new BooleanConditionalAttribute(
//                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_NAME), "" + value, negate,
//                Owner.fromString(xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_OWNER),
//                        Owner.SOURCE));
//
//        ca.setReaderClass(xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_READER_CLASS));
//
//        return ca;
//    }
//
//    protected DateConditionalAttribute xmlToDateConditionalAttribute(Element xmlConditionAttributeElement) {
//        boolean negate = "true".equalsIgnoreCase(
//                xmlConditionAttributeElement.getAttribute(ConditionalAttribute.ATTR_NEGATE)) ? true : false;
//
//        DateConditionalAttribute ca = new DateConditionalAttribute(
//                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_NAME),
//                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_VALUE), negate, Owner.fromString(
//                        xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_OWNER), Owner.SOURCE));
//
//        ca.setReaderClass(xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_READER_CLASS));
//
//        return ca;
//    }
//
//    protected TimeConditionalAttribute xmlToTimeConditionalAttribute(Element xmlConditionAttributeElement) {
//        boolean negate = "true".equalsIgnoreCase(
//                xmlConditionAttributeElement.getAttribute(ConditionalAttribute.ATTR_NEGATE)) ? true : false;
//
//        TimeConditionalAttribute ca = new TimeConditionalAttribute(
//                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_NAME),
//                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_VALUE), negate, Owner.fromString(
//                        xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_OWNER), Owner.SOURCE));
//
//        ca.setReaderClass(xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_READER_CLASS));
//
//        return ca;
//    }
//
//    protected DatetimeConditionalAttribute xmlToDatetimeConditionalAttribute(Element xmlConditionAttributeElement) {
//        boolean negate = "true".equalsIgnoreCase(
//                xmlConditionAttributeElement.getAttribute(ConditionalAttribute.ATTR_NEGATE)) ? true : false;
//
//        DatetimeConditionalAttribute ca = new DatetimeConditionalAttribute(
//                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_NAME),
//                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_VALUE), negate, Owner.fromString(
//                        xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_OWNER), Owner.SOURCE));
//
//        ca.setReaderClass(xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_READER_CLASS));
//
//        return ca;
//    }
// @formatter:on
    protected void xmlFromGoalAttribute(Element xmlGoalAttributeElement, GoalAttribute goalAttribute) {
        xmlGoalAttributeElement.setAttribute(AbstractAttribute.ATTR_NAME, goalAttribute.getName());
        xmlGoalAttributeElement.setAttribute(AbstractAttribute.ATTR_VALUE, goalAttribute.getValue());
        xmlGoalAttributeElement.setAttribute(GoalAttribute.ATTR_METHOD, goalAttribute.getMethod());
        xmlGoalAttributeElement.setAttribute(AbstractAttribute.ATTR_OWNER, goalAttribute.getOwner().toString());

        xmlGoalAttributeElement.setAttribute(AbstractAttribute.ATTR_READER_CLASS, goalAttribute.getReaderClass());
        xmlGoalAttributeElement.setAttribute(GoalAttribute.ATTR_WRITER_CLASS, goalAttribute.getWriterClass());

        // Parameters
        if (goalAttribute.getParameterList() != null) {
            for (CallParameter parameter : goalAttribute.getParameterList()) {
                Element xmlParameterElement = XmlUtils.createSubElement(xmlGoalAttributeElement,
                        GoalAttribute.TAG_PARAMETER);
                xmlParameterElement.setAttribute(AbstractAttribute.ATTR_NAME, parameter.getName());
                if (parameter.getValue() != null && parameter.getValue().length() > 0) {
                    xmlParameterElement.setAttribute(GoalAttribute.ATTR_VALUE, parameter.getValue());
                }
            }
        }
    }

    protected GoalAttribute xmlToGoalAttribute(Element xmlGoalAttributeElement) {
        GoalAttribute ga = new GoalAttribute(xmlGoalAttributeElement.getAttribute(AbstractAttribute.ATTR_NAME),
                xmlGoalAttributeElement.getAttribute(AbstractAttribute.ATTR_VALUE),
                xmlGoalAttributeElement.getAttribute(GoalAttribute.ATTR_METHOD),
                AbstractAttribute.Owner.fromString(xmlGoalAttributeElement.getAttribute(AbstractAttribute.ATTR_OWNER), AbstractAttribute.Owner.TARGET));

        ga.setReaderClass(xmlGoalAttributeElement.getAttribute(AbstractAttribute.ATTR_READER_CLASS));
        ga.setWriterClass(xmlGoalAttributeElement.getAttribute(GoalAttribute.ATTR_WRITER_CLASS));

        // Parameters
        List<Element> xmlParameterElementList = XmlUtils.getChildElementListByTagName(xmlGoalAttributeElement,
                GoalAttribute.TAG_PARAMETER);
        if (xmlParameterElementList != null) {
            List<CallParameter> parameterList = new ArrayList<>();
            for (Element xmlParameterElement : xmlParameterElementList) {
                String paramName = xmlParameterElement.getAttribute(AbstractAttribute.ATTR_NAME);
                if (paramName.trim().length() < 1) {
                    // invalid parameter XML -> skip
                    continue;
                }
                String paramValue = "";
                if (xmlParameterElement.hasAttribute(AbstractAttribute.ATTR_VALUE)) {
                    paramValue = xmlParameterElement.getAttribute(AbstractAttribute.ATTR_VALUE);
                } else {
                    paramValue = xmlParameterElement.getTextContent();
                }
                CallParameter parameter = new CallParameter(paramName, paramValue);
                parameterList.add(parameter);
            }

            if (parameterList.size() > 0) {
                ga.setParameterList(parameterList);
            }
        }

        return ga;
    }

    protected <T> Rule<T> newRule(String id) {
        return new Rule<T>(id);
    }

    public String getRuleSetName() {
        return ruleSetName;
    }

    public void setRuleSetName(String ruleSetName) {
        this.ruleSetName = ruleSetName;
    }

    protected <T> void indexRule(Map<String, List<Rule<?>>> scopeIndexedRuleSetCatalog, Rule<T> rule) {
        if (rule.getScopeIndex() == null || rule.getScopeIndex().length() < 1) {
            return;
        }

        // Index the rule
        String scopeIndexValue = rule.getScopeIndex();
        List<Rule<?>> scopedRuleSet = scopeIndexedRuleSetCatalog.get(scopeIndexValue);
        if (scopedRuleSet == null) {
            scopedRuleSet = new ArrayList<>();
        }
        scopedRuleSet.add(rule);
        scopeIndexedRuleSetCatalog.put(scopeIndexValue, scopedRuleSet);
    }

    public Map<String, List<Rule<?>>> getScopeIndexedRuleSetCatalog() {
        return scopeIndexedRuleSetCatalog;
    }

    @SuppressWarnings("unchecked")
    protected static final <CA extends ConditionalAttribute> ConditionalAttributeBuilder<CA> getConcreteAttributeBuilder(
            String builderKey) {
        if (BUILDER_CATALOG.isEmpty()) {
            BUILDER_CATALOG.put(
                    StringUtils.remove(DatetimeConditionalAttribute.class.getSimpleName().toLowerCase(),
                            DEFAULT_CONDITIONAL_ATTRIBUTE_SUFFIX),
                    (Element xmlConditionAttributeElement) -> {
                        boolean negate = "true".equalsIgnoreCase(
                                xmlConditionAttributeElement.getAttribute(ConditionalAttribute.ATTR_NEGATE)) ? true
                                        : false;

                        DatetimeConditionalAttribute ca = new DatetimeConditionalAttribute(
                                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_NAME),
                                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_VALUE), negate,
                                AbstractAttribute.Owner.fromString(
                                        xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_OWNER),
                                        AbstractAttribute.Owner.SOURCE));

                        ca.setReaderClass(
                                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_READER_CLASS));

                        return ca;
                    });
            BUILDER_CATALOG.put(
                    StringUtils.remove(DateConditionalAttribute.class.getSimpleName().toLowerCase(),
                            DEFAULT_CONDITIONAL_ATTRIBUTE_SUFFIX),
                    (Element xmlConditionAttributeElement) -> {
                        boolean negate = "true".equalsIgnoreCase(
                                xmlConditionAttributeElement.getAttribute(ConditionalAttribute.ATTR_NEGATE)) ? true
                                        : false;

                        DateConditionalAttribute ca = new DateConditionalAttribute(
                                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_NAME),
                                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_VALUE), negate,
                                AbstractAttribute.Owner.fromString(
                                        xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_OWNER),
                                        AbstractAttribute.Owner.SOURCE));

                        ca.setReaderClass(
                                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_READER_CLASS));

                        return ca;
                    });
            BUILDER_CATALOG.put(
                    StringUtils.remove(TimeConditionalAttribute.class.getSimpleName().toLowerCase(),
                            DEFAULT_CONDITIONAL_ATTRIBUTE_SUFFIX),
                    (Element xmlConditionAttributeElement) -> {
                        boolean negate = "true".equalsIgnoreCase(
                                xmlConditionAttributeElement.getAttribute(ConditionalAttribute.ATTR_NEGATE)) ? true
                                        : false;

                        TimeConditionalAttribute ca = new TimeConditionalAttribute(
                                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_NAME),
                                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_VALUE), negate,
                                AbstractAttribute.Owner.fromString(
                                        xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_OWNER),
                                        AbstractAttribute.Owner.SOURCE));

                        ca.setReaderClass(
                                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_READER_CLASS));

                        return ca;
                    });
            BUILDER_CATALOG.put(
                    StringUtils.remove(BooleanConditionalAttribute.class.getSimpleName().toLowerCase(),
                            DEFAULT_CONDITIONAL_ATTRIBUTE_SUFFIX),
                    (Element xmlConditionAttributeElement) -> {
                        boolean negate = "true".equalsIgnoreCase(
                                xmlConditionAttributeElement.getAttribute(ConditionalAttribute.ATTR_NEGATE)) ? true
                                        : false;
                        boolean value = "true".equalsIgnoreCase(
                                xmlConditionAttributeElement.getAttribute(ConditionalAttribute.ATTR_VALUE)) ? true
                                        : false;

                        BooleanConditionalAttribute ca = new BooleanConditionalAttribute(
                                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_NAME), "" + value,
                                negate,
                                AbstractAttribute.Owner.fromString(
                                        xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_OWNER),
                                        AbstractAttribute.Owner.SOURCE));

                        ca.setReaderClass(
                                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_READER_CLASS));

                        return ca;
                    });
            BUILDER_CATALOG.put(
                    StringUtils.remove(NumberConditionalAttribute.class.getSimpleName().toLowerCase(),
                            DEFAULT_CONDITIONAL_ATTRIBUTE_SUFFIX),
                    (Element xmlConditionAttributeElement) -> {
                        boolean negate = "true".equalsIgnoreCase(
                                xmlConditionAttributeElement.getAttribute(ConditionalAttribute.ATTR_NEGATE)) ? true
                                        : false;

                        NumberConditionalAttribute ca = new NumberConditionalAttribute(
                                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_NAME),
                                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_VALUE), negate,
                                AbstractAttribute.Owner.fromString(
                                        xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_OWNER),
                                        AbstractAttribute.Owner.SOURCE));

                        ca.setReaderClass(
                                xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_READER_CLASS));

                        return ca;
                    });
            BUILDER_CATALOG.put(
                    StringUtils.remove(StringConditionalAttribute.class.getSimpleName().toLowerCase(),
                            DEFAULT_CONDITIONAL_ATTRIBUTE_SUFFIX),
                    new StringConditionalAttributeBuilder());
        }

        return (ConditionalAttributeBuilder<CA>) BUILDER_CATALOG.get(builderKey);
    }

    public static class StringConditionalAttributeBuilder
            implements ConditionalAttributeBuilder<StringConditionalAttribute> {
        @Override
        public StringConditionalAttribute xmlToConcreteConditionalAttribute(Element xmlConditionAttributeElement) {
            boolean negate = "true".equalsIgnoreCase(
                    xmlConditionAttributeElement.getAttribute(ConditionalAttribute.ATTR_NEGATE)) ? true : false;

            StringConditionalAttribute ca = new StringConditionalAttribute(
                    xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_NAME),
                    xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_VALUE), negate, AbstractAttribute.Owner.fromString(
                            xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_OWNER), AbstractAttribute.Owner.SOURCE));

            ca.setReaderClass(xmlConditionAttributeElement.getAttribute(AbstractAttribute.ATTR_READER_CLASS));

            boolean regex = false;
            if (xmlConditionAttributeElement.hasAttribute(StringConditionalAttribute.ATTR_REGEX)) {
                // For Backward compatibility
                regex = "true".equalsIgnoreCase(
                        xmlConditionAttributeElement.getAttribute(StringConditionalAttribute.ATTR_REGEX)) ? true
                                : false;
                if (regex) {
                    ca.setOperatorFunction(StringConditionalAttribute.INTERNAL_FUNCTION_REGEX);
                }
            }

            if (!regex && xmlConditionAttributeElement.hasAttribute(StringConditionalAttribute.ATTR_CASE_SENSITIVE)) {
                // For Backward compatibility
                boolean caseSensitive = "false".equalsIgnoreCase(
                        xmlConditionAttributeElement.getAttribute(StringConditionalAttribute.ATTR_CASE_SENSITIVE))
                                ? false
                                : true;
                if (!caseSensitive) {
                    ca.setOperatorFunction(StringConditionalAttribute.INTERNAL_FUNCTION_IGNORE_CASE);
                }
            }

            return ca;
        }
    }

    public static interface ConditionalAttributeBuilder<CA extends ConditionalAttribute> {
        CA xmlToConcreteConditionalAttribute(Element xmlConditionAttributeElement);
    }
}
