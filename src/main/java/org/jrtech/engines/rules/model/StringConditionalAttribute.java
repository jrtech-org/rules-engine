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
package org.jrtech.engines.rules.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jrtech.engines.rules.function.StringEndsWithFunctionImpl;
import org.jrtech.engines.rules.function.StringEqualsFunctionImpl;
import org.jrtech.engines.rules.function.StringRegexFunctionImpl;
import org.jrtech.common.utils.model.LabelDefinition;
import org.jrtech.engines.rules.function.InternalFunctionDefinition;
import org.jrtech.engines.rules.function.StringContainsFunctionImpl;
import org.jrtech.engines.rules.function.StringIgnoreCaseFunctionImpl;
import org.jrtech.engines.rules.function.StringInFunctionImpl;
import org.jrtech.engines.rules.function.StringStartsWithFunctionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringConditionalAttribute extends ConditionalAttribute {

    private static final long serialVersionUID = 7220566370985892868L;

    private static Logger log = LoggerFactory.getLogger(StringConditionalAttribute.class);

    public static final String ATTR_CASE_SENSITIVE = "caseSensitive";
    public static final String ATTR_NEGATE = "negate";
    public static final String ATTR_REGEX = "regex";
    public static final String ATTR_OWNER = "owner";

    public static final String INTERNAL_FUNCTION_CONTAINS = "contains";

    public static final String INTERNAL_FUNCTION_ENDS_WITH = "endsWith";

    public static final String INTERNAL_FUNCTION_EQUALS = "equals";

    public static final String INTERNAL_FUNCTION_IGNORE_CASE = "ignoreCase";

    public static final String INTERNAL_FUNCTION_IN = "in";

    public static final String INTERNAL_FUNCTION_REGEX = "regex";

    public static final String INTERNAL_FUNCTION_STARTS_WITH = "startsWith";

    private static final List<InternalFunctionDefinition> INTERNAL_FUNCTION_LIST = Arrays
            .asList(new InternalFunctionDefinition[] {
                    // @formatter:off
                    new InternalFunctionDefinition(INTERNAL_FUNCTION_CONTAINS, new LabelDefinition(
                            "internal.function.string." + INTERNAL_FUNCTION_CONTAINS, "Contains"), String.class, 
                            new StringContainsFunctionImpl()),
                    new InternalFunctionDefinition(INTERNAL_FUNCTION_ENDS_WITH, new LabelDefinition(
                            "internal.function.string." + INTERNAL_FUNCTION_ENDS_WITH, "Ends With"), String.class, 
                            new StringEndsWithFunctionImpl()),
                    new InternalFunctionDefinition(INTERNAL_FUNCTION_EQUALS, new LabelDefinition(
                            "internal.function.string." + INTERNAL_FUNCTION_EQUALS, "Equals"), String.class, 
                            new StringEqualsFunctionImpl()),
                    new InternalFunctionDefinition(INTERNAL_FUNCTION_IGNORE_CASE, new LabelDefinition(
                            "internal.function.string." + INTERNAL_FUNCTION_IGNORE_CASE, "Ignore Case"), String.class, 
                            new StringIgnoreCaseFunctionImpl()),
                        new InternalFunctionDefinition(INTERNAL_FUNCTION_IN, new LabelDefinition(
                                "internal.function.string." + INTERNAL_FUNCTION_IN, "In [\"x\", \"y\", \"z\"]"), String.class, 
                                new StringInFunctionImpl()),
                    new InternalFunctionDefinition(INTERNAL_FUNCTION_REGEX, new LabelDefinition(
                            "internal.function.string." + INTERNAL_FUNCTION_REGEX, "Regular Expression"), String.class, 
                            new StringRegexFunctionImpl()),
                    new InternalFunctionDefinition(INTERNAL_FUNCTION_STARTS_WITH, new LabelDefinition(
                            "internal.function.string." + INTERNAL_FUNCTION_STARTS_WITH, "Starts With"), String.class, 
                            new StringStartsWithFunctionImpl()),
                    // @formatter:on
            });

    static {
        for (InternalFunctionDefinition ifd : INTERNAL_FUNCTION_LIST) {
            INTERNAL_FUNCTIONS_BY_NAME.put(formulateFunctionKey(ifd.getDataType().getSimpleName(), ifd.getName()), ifd);
        }
    }

    public StringConditionalAttribute(String name, String value) {
        this(name, value, false);
    }

    public StringConditionalAttribute(String name, String value, boolean negate) {
        this(name, value, negate, Owner.SOURCE);
    }

    public StringConditionalAttribute(String name, String value, boolean negate, Owner owner) {
        this(name, value, negate, owner, "");
    }

    public StringConditionalAttribute(String name, String value, boolean negate, String operationFunction) {
        this(name, value, negate, Owner.SOURCE, operationFunction);
    }

    public StringConditionalAttribute(String name, String value, boolean negate, Owner owner, String operationFunction) {
        super(name, value, negate, owner, operationFunction);
    }

    public boolean isRegex() {
        return INTERNAL_FUNCTION_REGEX.equals(getOperatorFunction());
    }

    public boolean isCaseSensitive() {
        return !INTERNAL_FUNCTION_IGNORE_CASE.equals(getOperatorFunction());
    }

    public <S, T> boolean match(S sourceObject, T targetObject, Map<String, Object> contextVariables) {
        ActualValueResolution avr = resolveActualValue(sourceObject, targetObject, contextVariables);
        if (avr == null) {
            return false;
        }

        String expectedValue = getValue();

        boolean result = false;
        if (getOperatorFunctionObject() != null) {
            try {
                result = getOperatorFunctionObject().getImplementation().match(avr.getActualValue(), expectedValue);
            } catch (Exception e) {
                log.debug(
                        "Failure in match processing Operator Function Object: '"
                                + getOperatorFunctionObject().getName() + "' with value [actual vs expected]: ["
                                + avr.getActualValue() + "/" + expectedValue + "]", e);
                return false;
            }
        }

        return isNegated() ? !result : result;
    }

    @Override
    protected String escapeResolvedValue(String resolvedValue) {
        if (isRegex()) {
            resolvedValue = StringUtils.replace(resolvedValue, ".", "[.]");
            resolvedValue = StringUtils.replace(resolvedValue, "+", "[+]");
            resolvedValue = StringUtils.replace(resolvedValue, "?", "[?]");
            resolvedValue = StringUtils.replace(resolvedValue, "*", "[*]");
            resolvedValue = StringUtils.replace(resolvedValue, "-", "\\-");
            resolvedValue = StringUtils.replace(resolvedValue, "(", "\\(");
            resolvedValue = StringUtils.replace(resolvedValue, ")", "\\)");
            resolvedValue = StringUtils.replace(resolvedValue, "|", "[|]");

            return resolvedValue;
        }
        return super.escapeResolvedValue(resolvedValue);
    }

    @Override
    public String getDefaultOperatorFunctionName() {
        return INTERNAL_FUNCTION_EQUALS;
    }

    @Override
    public String getDataType() {
        return String.class.getSimpleName();
    }
}
