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

import org.jrtech.engines.rules.model.StringConditionalAttribute;

public class SimpleStringConditionalAttributeGenerator extends
        AbstractConditionalAttributeGenerator<StringConditionalAttribute> {
    
    private static final long serialVersionUID = 2664742319299776989L;
    
    protected static final String GENERATOR_PREFIX = "Simple.String.";
    
    static {
        FUNCTION_EXPPRESSION_GENERATOR_CATALOG.put(GENERATOR_PREFIX + StringConditionalAttribute.INTERNAL_FUNCTION_CONTAINS,
                SimpleStringContainsFunctionExpressionGenerator.class);
        FUNCTION_EXPPRESSION_GENERATOR_CATALOG.put(GENERATOR_PREFIX + StringConditionalAttribute.INTERNAL_FUNCTION_ENDS_WITH,
                SimpleStringEndsWithFunctionExpressionGenerator.class);
        FUNCTION_EXPPRESSION_GENERATOR_CATALOG.put(GENERATOR_PREFIX + StringConditionalAttribute.INTERNAL_FUNCTION_EQUALS,
                SimpleStringEqualsFunctionExpressionGenerator.class);
        FUNCTION_EXPPRESSION_GENERATOR_CATALOG.put(GENERATOR_PREFIX + StringConditionalAttribute.INTERNAL_FUNCTION_IGNORE_CASE,
                SimpleStringIgnoreCaseFunctionExpressionGenerator.class);
        FUNCTION_EXPPRESSION_GENERATOR_CATALOG.put(GENERATOR_PREFIX + StringConditionalAttribute.INTERNAL_FUNCTION_IN,
                SimpleStringInFunctionExpressionGenerator.class);
        FUNCTION_EXPPRESSION_GENERATOR_CATALOG.put(GENERATOR_PREFIX + StringConditionalAttribute.INTERNAL_FUNCTION_REGEX,
                SimpleStringRegexFunctionExpressionGenerator.class);
        FUNCTION_EXPPRESSION_GENERATOR_CATALOG.put(GENERATOR_PREFIX + StringConditionalAttribute.INTERNAL_FUNCTION_STARTS_WITH,
                SimpleStringStartsWithFunctionExpressionGenerator.class);
    }
    
    @Override
    protected String getGeneratorPrefix() {
        return GENERATOR_PREFIX;
    }

    public static class SimpleStringContainsFunctionExpressionGenerator extends AbstractFunctionExpressionGenerator {
        @Override
        public String generate(String attributePhysicalName, String attributeValue) {
            return attributePhysicalName + " = *" + attributeValue + "*";
        }
    }
    
    public static class SimpleStringEndsWithFunctionExpressionGenerator extends AbstractFunctionExpressionGenerator {
        @Override
        public String generate(String attributePhysicalName, String attributeValue) {
            return attributePhysicalName + " = *" + attributeValue;
        }
    }

    public static class SimpleStringEqualsFunctionExpressionGenerator extends AbstractFunctionExpressionGenerator {
        @Override
        public String generate(String attributePhysicalName, String attributeValue) {
            return attributePhysicalName + " = " + attributeValue;
        }
    }

    public static class SimpleStringIgnoreCaseFunctionExpressionGenerator extends AbstractFunctionExpressionGenerator {
        @Override
        public String generate(String attributePhysicalName, String attributeValue) {
            return "LOWER(" + attributePhysicalName + ") = LOWER(" + attributeValue + ")";
        }
    }

    public static class SimpleStringRegexFunctionExpressionGenerator extends AbstractFunctionExpressionGenerator {
        @Override
        public String generate(String attributePhysicalName, String attributeValue) {
            return attributePhysicalName + " REGEX " + attributeValue;
        }
    }

    public static class SimpleStringInFunctionExpressionGenerator extends AbstractFunctionExpressionGenerator {
        @Override
        public String generate(String attributePhysicalName, String attributeValue) {
            return attributePhysicalName + " IN [" + attributeValue + "]";
        }
    }

    public static class SimpleStringStartsWithFunctionExpressionGenerator extends AbstractFunctionExpressionGenerator {
        @Override
        public String generate(String attributePhysicalName, String attributeValue) {
            return attributePhysicalName + " = " + attributeValue + "*";
        }
    }
}
