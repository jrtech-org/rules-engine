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

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.jrtech.engines.rules.model.BooleanConditionalAttribute;
import org.jrtech.engines.rules.model.DateConditionalAttribute;
import org.jrtech.engines.rules.model.DatetimeConditionalAttribute;
import org.jrtech.engines.rules.generator.condition.AbstractConditionalAttributeGenerator;
import org.jrtech.engines.rules.generator.goal.AbstractGoalGenerator;
import org.jrtech.engines.rules.model.ConditionalAttribute;
import org.jrtech.engines.rules.model.GoalAttribute;
import org.jrtech.engines.rules.model.NumberConditionalAttribute;
import org.jrtech.engines.rules.model.ObjectRule;
import org.jrtech.engines.rules.model.RuleCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRuleGenerator implements Serializable {
    
    private static final long serialVersionUID = 1533977837770020537L;

    public static final String INDENT = "    ";
    
    private static Logger log = LoggerFactory.getLogger(AbstractRuleGenerator.class);

    protected static final ConcurrentHashMap<Class<?>, Class<? extends AbstractGoalGenerator<?>>> GOAL_GENERATOR_CATALOG = new ConcurrentHashMap<>();

    protected static final ConcurrentHashMap<Class<? extends ConditionalAttribute>, Class<? extends AbstractConditionalAttributeGenerator<?>>> CONDITIONAL_ATTRIBUTE_GENERATOR_CATALOG = new ConcurrentHashMap<>();

    private Map<Class<? extends ConditionalAttribute>, AbstractConditionalAttributeGenerator<ConditionalAttribute>> conditionalGeneratorInstanceCatalog = new HashMap<>();

    private Map<String, Object> generatorContextData = new HashMap<>();

    public AbstractRuleGenerator() {
        super();
    }

    public String generateGoals(List<GoalAttribute> goals) {
        StringBuffer sb = new StringBuffer();

        for (GoalAttribute goal : goals) {
            sb.append(generateGoal(goal)).append("\n");
        }

        return sb.toString();
    }

    public String generateConditions(List<RuleCondition> conditions, boolean format) {
        StringBuffer sb = new StringBuffer();

        if (conditions != null && conditions.size() > 0) {
            int i = 0;
            for (RuleCondition intCond : conditions) {
                List<ConditionalAttribute> condAttrList = intCond.getAttributes();
                if (condAttrList.isEmpty()) {
                    continue;
                }

                if (i > 0) {
                    sb.append(" ").append(getBooleanOrOperator()).append(" ");
                    if (format) {
                        sb.append("\n");
                    }
                }
                if (condAttrList.size() > 1 && conditions.size() > 1) {
                    sb.append("(");
                }
                int j = 0;
                StringBuffer andConditionBuffer = new StringBuffer();
                for (ConditionalAttribute condAttr : condAttrList) {
                    if (j > 0) {
                        andConditionBuffer.append(" ").append(getBooleanAndOperator()).append(" ");
                    }
                    
                    if (condAttrList.size() > 3 && j % 3 == 0) {
                        andConditionBuffer.append("\n").append(" ");
                    }
                    
                    if (condAttr.isNegated()) {
                        // Negate expression start
                        andConditionBuffer.append(getBooleanNegateOperator()).append("(");
                    }

                    if (condAttr instanceof BooleanConditionalAttribute) {
                        andConditionBuffer.append(generateBooleanExpression(condAttr));
                    } else if (condAttr instanceof NumberConditionalAttribute) {
                        andConditionBuffer.append(generateNumericExpression(condAttr));
                    } else if (condAttr instanceof DateConditionalAttribute) {
                        andConditionBuffer.append(generateDateExpression(condAttr));
                    } else if (condAttr instanceof DatetimeConditionalAttribute) {
                        andConditionBuffer.append(generateDatetimeExpression(condAttr));
                    } else {
                        // Assumption field value is string
                        andConditionBuffer.append(generateStringExpression(condAttr));
                    }

                    if (condAttr.isNegated()) {
                        // Negate expression end
                        andConditionBuffer.append(")");
                    }

                    j++;
                }
                
                sb.append(andConditionBuffer.toString());
                
                if (condAttrList.size() > 1 && conditions.size() > 1) {
                    if (condAttrList.size() > 3) {
                        sb.append("\n");
                    }
                    sb.append(")");
                }
                i++;
            }
            
            String condString = sb.toString();
            if (conditions.size() > 1) {
                return "\n " + StringUtils.replace(condString, "\n", "\n ") + "\n";
            } else if (conditions.size() == 1) {
                return condString;
            }
        }

        return "";
    }

    protected String removeLeadingAndTrailingQuotes(String fieldValue) {
        if (fieldValue.startsWith("\"")) {
            fieldValue = fieldValue.substring(1);
        }
        if (fieldValue.endsWith("\"")) {
            fieldValue = fieldValue.substring(0, fieldValue.length() - 1);
        }

        return fieldValue;
    }

    @SuppressWarnings("unchecked")
    protected AbstractConditionalAttributeGenerator<ConditionalAttribute> getConditionalAttributeGenerator(
            ConditionalAttribute conditionalAttribute) {
        if (conditionalGeneratorInstanceCatalog.containsKey(conditionalAttribute.getClass())) {
            return conditionalGeneratorInstanceCatalog.get(conditionalAttribute.getClass());
        }

        Class<? extends AbstractConditionalAttributeGenerator<ConditionalAttribute>> conditionalAttributeGeneratorClass = (Class<? extends AbstractConditionalAttributeGenerator<ConditionalAttribute>>) CONDITIONAL_ATTRIBUTE_GENERATOR_CATALOG
                .get(conditionalAttribute.getClass());
        if (conditionalAttributeGeneratorClass == null) {
            conditionalGeneratorInstanceCatalog.put(conditionalAttribute.getClass(), null);
            return null;
        }
        AbstractConditionalAttributeGenerator<ConditionalAttribute> conditionalAttributeGenerator = null;
        try {
            conditionalAttributeGenerator = conditionalAttributeGeneratorClass.newInstance();
            conditionalGeneratorInstanceCatalog.put(conditionalAttribute.getClass(), conditionalAttributeGenerator);
        } catch (Exception e) {
            log.info("", e);
            conditionalGeneratorInstanceCatalog.put(conditionalAttribute.getClass(), null);
            return null;
        }

        conditionalAttributeGenerator.setGeneratorContextData(generatorContextData);

        return conditionalAttributeGenerator;
    }

    protected Map<String, Object> getGeneratorContextData() {
        return generatorContextData;
    }

    public abstract String getBooleanOrOperator();

    public abstract String getBooleanAndOperator();

    public abstract String getBooleanNegateOperator();

    public abstract String generatePatternExpression(ConditionalAttribute conditionalAttribute);

    public abstract String generateStringExpression(ConditionalAttribute conditionalAttribute);

    public abstract String generateStringCaseInsensitiveExpression(ConditionalAttribute conditionalAttribute);

    public abstract String generateBooleanExpression(ConditionalAttribute conditionalAttribute);

    public abstract String generateDateExpression(ConditionalAttribute conditionalAttribute);

    public abstract String generateDatetimeExpression(ConditionalAttribute conditionalAttribute);

    public abstract String generateNumericExpression(ConditionalAttribute conditionalAttribute);

    public abstract String generateGoal(GoalAttribute goal);

    public abstract String generateRuleSet(List<ObjectRule> ruleSet);
}
