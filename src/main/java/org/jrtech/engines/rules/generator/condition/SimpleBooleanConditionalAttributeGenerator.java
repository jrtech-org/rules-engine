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

import org.jrtech.engines.rules.model.BooleanConditionalAttribute;

public class SimpleBooleanConditionalAttributeGenerator extends
        AbstractConditionalAttributeGenerator<BooleanConditionalAttribute> {

    private static final long serialVersionUID = 8361060502095991662L;
    
    protected static final String GENERATOR_PREFIX = "Simple.Boolean.";

    static {
        FUNCTION_EXPPRESSION_GENERATOR_CATALOG.put(GENERATOR_PREFIX
                + BooleanConditionalAttribute.INTERNAL_FUNCTION_EQUALS, SimpleBooleanFunctionExpressionGenerator.class);
    }

    @Override
    protected String getGeneratorPrefix() {
        return GENERATOR_PREFIX;
    }

    public static class SimpleBooleanFunctionExpressionGenerator extends AbstractFunctionExpressionGenerator {
        @Override
        public String generate(String attributePhysicalName, String attributeValue) {
            return attributePhysicalName + " = " + attributeValue;
        }
    }

    @Override
    protected AbstractFunctionExpressionGenerator getFunctionExpressionGeneratorInstance(String scopedFunctionName) {
        AbstractFunctionExpressionGenerator functionExpressionGenerator = super
                .getFunctionExpressionGeneratorInstance(scopedFunctionName);
        if (!(functionExpressionGenerator instanceof SimpleBooleanFunctionExpressionGenerator)) {
            functionExpressionGenerator = new SimpleBooleanFunctionExpressionGenerator();
        }

        return functionExpressionGenerator;
    }

}
