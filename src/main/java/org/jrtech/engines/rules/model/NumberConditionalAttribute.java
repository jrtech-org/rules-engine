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

import org.jrtech.engines.rules.function.NumberEqualsFunctionImpl;
import org.jrtech.engines.rules.function.NumberLessThanFunctionImpl;
import org.jrtech.common.utils.model.LabelDefinition;
import org.jrtech.engines.rules.function.InternalFunctionDefinition;
import org.jrtech.engines.rules.function.NumberGreaterOrEqualsFunctionImpl;
import org.jrtech.engines.rules.function.NumberGreaterThanFunctionImpl;
import org.jrtech.engines.rules.function.NumberLessOrEqualsFunctionImpl;

public class NumberConditionalAttribute extends ConditionalAttribute {

    private static final long serialVersionUID = -351753506895070889L;

    public static final String INTERNAL_FUNCTION_EQUALS = "equals";

    public static final String INTERNAL_FUNCTION_GREATER_THAN = "greaterThan";

    public static final String INTERNAL_FUNCTION_GREATER_OR_EQUALS = "greaterOrEquals";

    public static final String INTERNAL_FUNCTION_LESS_THAN = "lessThan";

    public static final String INTERNAL_FUNCTION_LESS_OR_EQUALS = "lessOrEquals";

    private static final List<InternalFunctionDefinition> INTERNAL_FUNCTION_LIST = Arrays
            .asList(new InternalFunctionDefinition[] {
                    // @formatter:off
                    // Numeric
                    new InternalFunctionDefinition(INTERNAL_FUNCTION_EQUALS, new LabelDefinition(
                            "internal.function.number." + INTERNAL_FUNCTION_EQUALS, "="), Number.class, 
                            new NumberEqualsFunctionImpl()),
                    new InternalFunctionDefinition(INTERNAL_FUNCTION_GREATER_THAN, new LabelDefinition(
                            "internal.function.number." + INTERNAL_FUNCTION_GREATER_THAN, ">"), Number.class, 
                            new NumberGreaterThanFunctionImpl()),
                    new InternalFunctionDefinition(INTERNAL_FUNCTION_GREATER_OR_EQUALS, new LabelDefinition(
                            "internal.function.number." + INTERNAL_FUNCTION_GREATER_OR_EQUALS, ">="), Number.class, 
                            new NumberGreaterOrEqualsFunctionImpl()),
                    new InternalFunctionDefinition(INTERNAL_FUNCTION_LESS_THAN, new LabelDefinition(
                            "internal.function.string." + INTERNAL_FUNCTION_LESS_THAN , "<"), Number.class, 
                            new NumberLessThanFunctionImpl()),
                    new InternalFunctionDefinition(INTERNAL_FUNCTION_LESS_OR_EQUALS, new LabelDefinition(
                            "internal.function.string." + INTERNAL_FUNCTION_LESS_OR_EQUALS , "<="), Number.class, 
                            new NumberLessOrEqualsFunctionImpl()),
                    // @formatter:on
            });

    static {
        for (InternalFunctionDefinition ifd : INTERNAL_FUNCTION_LIST) {
            INTERNAL_FUNCTIONS_BY_NAME.put(formulateFunctionKey(ifd.getDataType().getSimpleName(), ifd.getName()), ifd);
        }
    }

    public NumberConditionalAttribute(String name, String value) {
        this(name, value, false, Owner.SOURCE);
    }

    public NumberConditionalAttribute(String name, String value, boolean negate) {
        this(name, value, negate, Owner.SOURCE);
    }

    public NumberConditionalAttribute(String name, String value, boolean negate, Owner owner) {
        this(name, value, negate, owner, "");
    }

    public NumberConditionalAttribute(String name, String value, boolean negate, String operatorFunction) {
        this(name, value, negate, Owner.SOURCE, operatorFunction);
    }

    public NumberConditionalAttribute(String name, String value, boolean negate, Owner owner, String operatorFunction) {
        super(name, value, negate, owner, operatorFunction);
    }

    @Override
    public <S, T> boolean match(S sourceObject, T targetObject, Map<String, Object> contextVariables) {
        ActualValueResolution avr = resolveActualValue(sourceObject, targetObject, contextVariables);
        if (avr == null) {
            return false;
        }

        String expectedValue = getValue();

        Boolean result = new Boolean(false);
        boolean hasValidOperator = false;
        if (getOperatorFunctionObject() != null) {
            try {
                result = getOperatorFunctionObject().getImplementation().match(avr.getActualValue(), expectedValue);
                hasValidOperator = true;
            } catch (Exception e) {
                // Fall back to old way
                hasValidOperator = false;
                result = null;
            }
        }

        if (!hasValidOperator) {
            // Primitive string equals
            result = avr.getActualValue().equals(expectedValue);
        }

        return result == null ? false : (isNegated() ? !result : result);
    }

    @Override
    public String getDefaultOperatorFunctionName() {
        return INTERNAL_FUNCTION_EQUALS;
    }

    @Override
    public String getDataType() {
        return Number.class.getSimpleName();
    }
}
