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
package org.jrtech.engines.rules.generator.condition;

import org.jrtech.engines.rules.model.NumberConditionalAttribute;

public class SimpleNumberConditionalAttributeGenerator extends
        AbstractConditionalAttributeGenerator<NumberConditionalAttribute> {

    private static final long serialVersionUID = 7020910691635020118L;
    
    protected static final String GENERATOR_PREFIX = "Simple.Number.";
    
    static {
        FUNCTION_EXPPRESSION_GENERATOR_CATALOG.put(GENERATOR_PREFIX + NumberConditionalAttribute.INTERNAL_FUNCTION_EQUALS,
                SimpleNumberEqualsFunctionExpressionGenerator.class);
        FUNCTION_EXPPRESSION_GENERATOR_CATALOG.put(GENERATOR_PREFIX + NumberConditionalAttribute.INTERNAL_FUNCTION_GREATER_OR_EQUALS,
                SimpleNumberGreaterOrEqualsFunctionExpressionGenerator.class);
        FUNCTION_EXPPRESSION_GENERATOR_CATALOG.put(GENERATOR_PREFIX + NumberConditionalAttribute.INTERNAL_FUNCTION_GREATER_THAN,
                SimpleNumberGreatherThanFunctionExpressionGenerator.class);
        FUNCTION_EXPPRESSION_GENERATOR_CATALOG.put(GENERATOR_PREFIX + NumberConditionalAttribute.INTERNAL_FUNCTION_LESS_OR_EQUALS,
                SimpleNumberLessOrEqualsFunctionExpressionGenerator.class);
        FUNCTION_EXPPRESSION_GENERATOR_CATALOG.put(GENERATOR_PREFIX + NumberConditionalAttribute.INTERNAL_FUNCTION_LESS_THAN,
                SimpleNumberLessThanFunctionExpressionGenerator.class);
    }
    
    @Override
    protected String getGeneratorPrefix() {
        return GENERATOR_PREFIX;
    }

    public static class SimpleNumberGreaterOrEqualsFunctionExpressionGenerator extends AbstractFunctionExpressionGenerator {
        @Override
        public String generate(String attributePhysicalName, String attributeValue) {
            return attributePhysicalName + " >= " + attributeValue;
        }
    }
    
    public static class SimpleNumberGreatherThanFunctionExpressionGenerator extends AbstractFunctionExpressionGenerator {
        @Override
        public String generate(String attributePhysicalName, String attributeValue) {
            return attributePhysicalName + " > " + attributeValue;
        }
    }

    public static class SimpleNumberEqualsFunctionExpressionGenerator extends AbstractFunctionExpressionGenerator {
        @Override
        public String generate(String attributePhysicalName, String attributeValue) {
            return attributePhysicalName + " = " + attributeValue;
        }
    }

    public static class SimpleNumberLessThanFunctionExpressionGenerator extends AbstractFunctionExpressionGenerator {
        @Override
        public String generate(String attributePhysicalName, String attributeValue) {
            return attributePhysicalName + " < " + attributeValue;
        }
    }

    public static class SimpleNumberLessOrEqualsFunctionExpressionGenerator extends AbstractFunctionExpressionGenerator {
        @Override
        public String generate(String attributePhysicalName, String attributeValue) {
            return attributePhysicalName + " <= " + attributeValue;
        }
    }

}
