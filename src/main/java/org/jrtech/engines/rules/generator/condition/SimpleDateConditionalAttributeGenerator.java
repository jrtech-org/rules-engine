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

import org.jrtech.engines.rules.model.DateConditionalAttribute;

public class SimpleDateConditionalAttributeGenerator extends
        AbstractConditionalAttributeGenerator<DateConditionalAttribute> {

    private static final long serialVersionUID = 7188025483076594573L;
    
    protected static final String GENERATOR_PREFIX = "Simple.Date.";
    
    static {
        FUNCTION_EXPPRESSION_GENERATOR_CATALOG.put(GENERATOR_PREFIX + DateConditionalAttribute.INTERNAL_FUNCTION_AFTER,
                SimpleDateAfterFunctionExpressionGenerator.class);
        FUNCTION_EXPPRESSION_GENERATOR_CATALOG.put(GENERATOR_PREFIX + DateConditionalAttribute.INTERNAL_FUNCTION_AT,
                SimpleDateAtFunctionExpressionGenerator.class);
        FUNCTION_EXPPRESSION_GENERATOR_CATALOG.put(GENERATOR_PREFIX + DateConditionalAttribute.INTERNAL_FUNCTION_BEFORE,
                SimpleDateBeforeFunctionExpressionGenerator.class);
        FUNCTION_EXPPRESSION_GENERATOR_CATALOG.put(GENERATOR_PREFIX + DateConditionalAttribute.INTERNAL_FUNCTION_SINCE,
                SimpleDateSinceFunctionExpressionGenerator.class);
        FUNCTION_EXPPRESSION_GENERATOR_CATALOG.put(GENERATOR_PREFIX + DateConditionalAttribute.INTERNAL_FUNCTION_UNTIL,
                SimpleDateUntilFunctionExpressionGenerator.class);
    }
    
    @Override
    protected String getGeneratorPrefix() {
        return GENERATOR_PREFIX;
    }

    public static class SimpleDateAfterFunctionExpressionGenerator extends AbstractFunctionExpressionGenerator {
        @Override
        public String generate(String attributePhysicalName, String attributeValue) {
            return attributePhysicalName + " After " + attributeValue;
        }
    }

    public static class SimpleDateAtFunctionExpressionGenerator extends AbstractFunctionExpressionGenerator {
        @Override
        public String generate(String attributePhysicalName, String attributeValue) {
            return attributePhysicalName + " At " + attributeValue;
        }
    }
    
    public static class SimpleDateBeforeFunctionExpressionGenerator extends AbstractFunctionExpressionGenerator {
        @Override
        public String generate(String attributePhysicalName, String attributeValue) {
            return attributePhysicalName + " Before " + attributeValue;
        }
    }
    
    public static class SimpleDateSinceFunctionExpressionGenerator extends AbstractFunctionExpressionGenerator {
        @Override
        public String generate(String attributePhysicalName, String attributeValue) {
            return attributePhysicalName + " Since " + attributeValue;
        }
    }

    public static class SimpleDateUntilFunctionExpressionGenerator extends AbstractFunctionExpressionGenerator {
        @Override
        public String generate(String attributePhysicalName, String attributeValue) {
            return attributePhysicalName + " Until " + attributeValue;
        }
    }

}
