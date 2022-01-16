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

import org.jrtech.engines.rules.function.BooleanEqualsFunctionImpl;
import org.jrtech.common.utils.model.LabelDefinition;
import org.jrtech.engines.rules.function.InternalFunctionDefinition;

public class BooleanConditionalAttribute extends ConditionalAttribute {
    
    private static final long serialVersionUID = 6736837552614006972L;

    public static final String INTERNAL_FUNCTION_EQUALS = "equals";
    
    private static final List<InternalFunctionDefinition> INTERNAL_FUNCTION_LIST = Arrays
            .asList(new InternalFunctionDefinition[] {
                    // @formatter:off
                    // Numeric
                    new InternalFunctionDefinition(INTERNAL_FUNCTION_EQUALS, new LabelDefinition(
                            "internal.function.boolean." + INTERNAL_FUNCTION_EQUALS, "="), Boolean.class, 
                            new BooleanEqualsFunctionImpl()),
                    // @formatter:on
            });

    static {
        for (InternalFunctionDefinition ifd : INTERNAL_FUNCTION_LIST) {
            INTERNAL_FUNCTIONS_BY_NAME.put(formulateFunctionKey(ifd.getDataType().getSimpleName(), ifd.getName()), ifd);
        }
    }

    public BooleanConditionalAttribute(String name, String value) {
        this(name, value, false, Owner.SOURCE);
    }

    public BooleanConditionalAttribute(String name, String value, boolean negate) {
        this(name, value, negate, Owner.SOURCE);
    }

    public BooleanConditionalAttribute(String name, String value, boolean negate, Owner owner) {
        super(name, value, negate, owner);
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
        return Boolean.class.getSimpleName();
    }
}
