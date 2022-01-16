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
package org.jrtech.engines.rules.generator;

import java.util.List;

import org.jrtech.engines.rules.generator.condition.AbstractConditionalAttributeGenerator;
import org.jrtech.engines.rules.generator.condition.SimpleBooleanConditionalAttributeGenerator;
import org.jrtech.engines.rules.generator.condition.SimpleDateConditionalAttributeGenerator;
import org.jrtech.engines.rules.generator.condition.SimpleNumberConditionalAttributeGenerator;
import org.jrtech.engines.rules.generator.condition.SimpleStringConditionalAttributeGenerator;
import org.jrtech.engines.rules.model.BooleanConditionalAttribute;
import org.jrtech.engines.rules.model.ConditionalAttribute;
import org.jrtech.engines.rules.model.DateConditionalAttribute;
import org.jrtech.engines.rules.model.GoalAttribute;
import org.jrtech.engines.rules.model.NumberConditionalAttribute;
import org.jrtech.engines.rules.model.ObjectRule;
import org.jrtech.engines.rules.model.StringConditionalAttribute;

public class SimpleRuleGenerator extends AbstractRuleGenerator {

    private static final long serialVersionUID = 8458107219380972039L;

    static {
        CONDITIONAL_ATTRIBUTE_GENERATOR_CATALOG.put(StringConditionalAttribute.class,
                SimpleStringConditionalAttributeGenerator.class);
        CONDITIONAL_ATTRIBUTE_GENERATOR_CATALOG.put(NumberConditionalAttribute.class,
                SimpleNumberConditionalAttributeGenerator.class);
        CONDITIONAL_ATTRIBUTE_GENERATOR_CATALOG.put(DateConditionalAttribute.class,
                SimpleDateConditionalAttributeGenerator.class);
        CONDITIONAL_ATTRIBUTE_GENERATOR_CATALOG.put(BooleanConditionalAttribute.class,
                SimpleBooleanConditionalAttributeGenerator.class);
    }

    @Override
    public String getBooleanOrOperator() {
        return "OR";
    }

    @Override
    public String getBooleanAndOperator() {
        return "AND";
    }

    @Override
    public String getBooleanNegateOperator() {
        return "NOT";
    }

    @Override
    public String generatePatternExpression(ConditionalAttribute conditionalAttribute) {
        String fieldValue = removeLeadingAndTrailingQuotes(conditionalAttribute.getValue());
        return conditionalAttribute.getName() + " LIKE '" + fieldValue + "'";
    }

    @Override
    public String generateStringCaseInsensitiveExpression(ConditionalAttribute conditionalAttribute) {
        return generateCommonStringExpression(conditionalAttribute, false);
    }

    @Override
    public String generateStringExpression(ConditionalAttribute conditionalAttribute) {
        return generateCommonStringExpression(conditionalAttribute, true);
    }

    protected String generateCommonStringExpression(ConditionalAttribute conditionalAttribute, boolean caseSensitive) {
        StringConditionalAttribute sca = null;

        if (conditionalAttribute instanceof StringConditionalAttribute) {
            sca = (StringConditionalAttribute) conditionalAttribute;
        } else {
            sca = new StringConditionalAttribute(conditionalAttribute.getName(), conditionalAttribute.getValue(),
                    conditionalAttribute.isNegated(), conditionalAttribute.getOwner(),
                    conditionalAttribute.getOperatorFunction());
            sca.setReaderClass(conditionalAttribute.getReaderClass());
        }

        if (sca.getOperatorFunction() == null || sca.getOperatorFunction().length() < 1) {
            if (caseSensitive) {
                sca.setOperatorFunction(StringConditionalAttribute.INTERNAL_FUNCTION_EQUALS);
            } else {
                sca.setOperatorFunction(StringConditionalAttribute.INTERNAL_FUNCTION_IGNORE_CASE);
            }
        }

        AbstractConditionalAttributeGenerator<ConditionalAttribute> condAttrGenerator = getConditionalAttributeGenerator(sca);
        if (condAttrGenerator == null) {
            String fieldValue = removeLeadingAndTrailingQuotes(conditionalAttribute.getValue());
            String fieldPhysicalName = conditionalAttribute.getName();
            String operatorString = ") = LOWER(";
            if (caseSensitive) {
                operatorString = " = ";
            }
            return (caseSensitive ? "LOWER(" : "") + fieldPhysicalName + operatorString + fieldValue
                    + (caseSensitive ? ")" : "");
        }

        return condAttrGenerator.generateFunctionExpression(sca);
    }

    @Override
    public String generateGoal(GoalAttribute goal) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String generateRuleSet(List<ObjectRule> ruleSet) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String generateBooleanExpression(ConditionalAttribute conditionalAttribute) {
        String fieldValue = conditionalAttribute.getValue();
        return conditionalAttribute.getName() + "=" + fieldValue + "";
    }

    @Override
    public String generateDateExpression(ConditionalAttribute conditionalAttribute) {
        return generateDatetimeExpression(conditionalAttribute);
    }

    @Override
    public String generateDatetimeExpression(ConditionalAttribute conditionalAttribute) {
        DateConditionalAttribute dca = null;

        if (conditionalAttribute instanceof DateConditionalAttribute) {
            dca = (DateConditionalAttribute) conditionalAttribute;
        } else {
            dca = new DateConditionalAttribute(conditionalAttribute.getName(), conditionalAttribute.getValue(),
                    conditionalAttribute.isNegated(), conditionalAttribute.getOwner(),
                    conditionalAttribute.getOperatorFunction());
            dca.setReaderClass(conditionalAttribute.getReaderClass());
        }

        if (dca.getOperatorFunction() == null || dca.getOperatorFunction().length() < 1) {
            dca.setOperatorFunction(DateConditionalAttribute.INTERNAL_FUNCTION_AFTER);
        }

        AbstractConditionalAttributeGenerator<ConditionalAttribute> condAttrGenerator = getConditionalAttributeGenerator(dca);
        if (condAttrGenerator == null) {
            // Fallback scenario
            String fieldValue = conditionalAttribute.getValue();

            if (DateConditionalAttribute.INTERNAL_FUNCTION_AFTER.equals(conditionalAttribute.getOperatorFunction())) {
                return conditionalAttribute.getName() + " After " + fieldValue;
            } else if (DateConditionalAttribute.INTERNAL_FUNCTION_SINCE.equals(conditionalAttribute
                    .getOperatorFunction())) {
                return conditionalAttribute.getName() + " Since " + fieldValue;
            } else if (DateConditionalAttribute.INTERNAL_FUNCTION_BEFORE.equals(conditionalAttribute
                    .getOperatorFunction())) {
                return conditionalAttribute.getName() + " Before " + fieldValue;
            } else if (DateConditionalAttribute.INTERNAL_FUNCTION_UNTIL.equals(conditionalAttribute
                    .getOperatorFunction())) {
                return conditionalAttribute.getName() + " Until " + fieldValue;
            }

            return conditionalAttribute.getName() + " At " + fieldValue;
        }

        return condAttrGenerator.generateFunctionExpression(dca);
    }

    @Override
    public String generateNumericExpression(ConditionalAttribute conditionalAttribute) {
        NumberConditionalAttribute nca = null;

        if (conditionalAttribute instanceof NumberConditionalAttribute) {
            nca = (NumberConditionalAttribute) conditionalAttribute;
        } else {
            nca = new NumberConditionalAttribute(conditionalAttribute.getName(), conditionalAttribute.getValue(),
                    conditionalAttribute.isNegated(), conditionalAttribute.getOwner(),
                    conditionalAttribute.getOperatorFunction());
            nca.setReaderClass(conditionalAttribute.getReaderClass());
        }

        if (nca.getOperatorFunction() == null || nca.getOperatorFunction().length() < 1) {
            nca.setOperatorFunction(NumberConditionalAttribute.INTERNAL_FUNCTION_EQUALS);
        }

        AbstractConditionalAttributeGenerator<ConditionalAttribute> condAttrGenerator = getConditionalAttributeGenerator(nca);
        if (condAttrGenerator == null) {
            // Fallback scenario
            String fieldValue = removeLeadingAndTrailingQuotes(conditionalAttribute.getValue());
            String fieldPhysicalName = conditionalAttribute.getName();
            String operatorString = ".compareTo(";
            return fieldPhysicalName + operatorString + fieldValue + "B) == 0";
        }

        return condAttrGenerator.generateFunctionExpression(nca);
    }

}
