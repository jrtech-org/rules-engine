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

import org.jrtech.engines.rules.model.GoalAttribute;
import org.jrtech.engines.rules.model.ConditionalAttribute;
import org.jrtech.engines.rules.model.ObjectRule;

public class SqlRuleGenerator extends AbstractRuleGenerator {

    private static final long serialVersionUID = -8142152066362138001L;

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
        String fieldValue = removeLeadingAndTrailingQuotes(conditionalAttribute.getValue());
        // TODO: Check if LOWER is a valid function for DB used.
        return "LOWER(" + conditionalAttribute.getName() + ")='" + fieldValue.toLowerCase() + "'";
    }

    @Override
    public String generateStringExpression(ConditionalAttribute conditionalAttribute) {
        String fieldValue = removeLeadingAndTrailingQuotes(conditionalAttribute.getValue());
        return conditionalAttribute.getName() + "='" + fieldValue + "'";
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
        String fieldValue = conditionalAttribute.getValue();
        return conditionalAttribute.getName() + "=" + fieldValue + "";
    }

    @Override
    public String generateDatetimeExpression(ConditionalAttribute conditionalAttribute) {
        String fieldValue = conditionalAttribute.getValue();
        return conditionalAttribute.getName() + "=" + fieldValue + "";
    }

    @Override
    public String generateNumericExpression(ConditionalAttribute conditionalAttribute) {
        String fieldValue = conditionalAttribute.getValue();
        return conditionalAttribute.getOperatorFunctionObject().getImplementation()
                .createLogicalExpression(conditionalAttribute.getName(), fieldValue);
    }

}
