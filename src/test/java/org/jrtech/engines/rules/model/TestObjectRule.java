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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

public class TestObjectRule {

    private List<ObjectRule> intList;

    @Before
    public void init() {
        intList = new ArrayList<>();
        ObjectRule rule = new ObjectRule("test1a");
        rule.getGoals().add(new GoalAttribute("call", "FIN Payment USD or CHF small."));

        List<ConditionalAttribute> caList = new ArrayList<>();
        caList.add(new StringConditionalAttribute("ApplicationType", "FIN", false,
                StringConditionalAttribute.INTERNAL_FUNCTION_IGNORE_CASE));
        caList.add(new StringConditionalAttribute("MT", "[1-4][0-8][0-9].*", false,
                StringConditionalAttribute.INTERNAL_FUNCTION_REGEX));
        caList.add(new StringConditionalAttribute("Currency", "USD", false));
        caList.add(new NumberConditionalAttribute("Amount", "10000", false, AbstractAttribute.Owner.SOURCE,
                NumberConditionalAttribute.INTERNAL_FUNCTION_LESS_THAN));
        rule.getConditions().add(new RuleCondition(caList));

        caList = new ArrayList<>();
        caList.add(new StringConditionalAttribute("ApplicationType", "FIN", false,
                StringConditionalAttribute.INTERNAL_FUNCTION_IGNORE_CASE));
        caList.add(new StringConditionalAttribute("MT", "[1-4][0-8][0-9].*", false,
                StringConditionalAttribute.INTERNAL_FUNCTION_REGEX));
        caList.add(new StringConditionalAttribute("Currency", "CHF", false));
        caList.add(new NumberConditionalAttribute("Amount", "12000", false, AbstractAttribute.Owner.SOURCE,
                NumberConditionalAttribute.INTERNAL_FUNCTION_LESS_THAN));
        rule.getConditions().add(new RuleCondition(caList));
        intList.add(rule);

        rule = new ObjectRule("test1b");
        rule.getGoals().add(new GoalAttribute("call", "FIN Payment USD or CHF moderate."));

        caList = new ArrayList<>();
        caList.add(new StringConditionalAttribute("ApplicationType", "FIN", false,
                StringConditionalAttribute.INTERNAL_FUNCTION_IGNORE_CASE));
        caList.add(new StringConditionalAttribute("MT", "[1-4][0-8][0-9].*", false,
                StringConditionalAttribute.INTERNAL_FUNCTION_REGEX));
        caList.add(new StringConditionalAttribute("Currency", "USD", false));
        caList.add(new NumberConditionalAttribute("Amount", "10000", false, AbstractAttribute.Owner.SOURCE,
                NumberConditionalAttribute.INTERNAL_FUNCTION_GREATER_OR_EQUALS));
        caList.add(new NumberConditionalAttribute("Amount", "1000000", false, AbstractAttribute.Owner.SOURCE,
                NumberConditionalAttribute.INTERNAL_FUNCTION_LESS_THAN));
        rule.getConditions().add(new RuleCondition(caList));

        caList = new ArrayList<>();
        caList.add(new StringConditionalAttribute("ApplicationType", "FIN", false,
                StringConditionalAttribute.INTERNAL_FUNCTION_IGNORE_CASE));
        caList.add(new StringConditionalAttribute("MT", "[1-4][0-8][0-9].*", false,
                StringConditionalAttribute.INTERNAL_FUNCTION_REGEX));
        caList.add(new StringConditionalAttribute("Currency", "CHF", false));
        caList.add(new NumberConditionalAttribute("Amount", "12000", false, AbstractAttribute.Owner.SOURCE,
                NumberConditionalAttribute.INTERNAL_FUNCTION_GREATER_OR_EQUALS));
        caList.add(new NumberConditionalAttribute("Amount", "1200000", false, AbstractAttribute.Owner.SOURCE,
                NumberConditionalAttribute.INTERNAL_FUNCTION_LESS_THAN));
        rule.getConditions().add(new RuleCondition(caList));
        intList.add(rule);

        rule = new ObjectRule("test1c");
        rule.getGoals().add(new GoalAttribute("call", "FIN Payment USD or CHF large."));

        caList = new ArrayList<>();
        caList.add(new StringConditionalAttribute("ApplicationType", "FIN", false,
                StringConditionalAttribute.INTERNAL_FUNCTION_IGNORE_CASE));
        caList.add(new StringConditionalAttribute("MT", "[1-4][0-8][0-9].*", false,
                StringConditionalAttribute.INTERNAL_FUNCTION_REGEX));
        caList.add(new StringConditionalAttribute("Currency", "USD", false));
        caList.add(new NumberConditionalAttribute("Amount", "1000000", false, AbstractAttribute.Owner.SOURCE,
                NumberConditionalAttribute.INTERNAL_FUNCTION_GREATER_OR_EQUALS));
        rule.getConditions().add(new RuleCondition(caList));

        caList = new ArrayList<>();
        caList.add(new StringConditionalAttribute("ApplicationType", "FIN", false,
                StringConditionalAttribute.INTERNAL_FUNCTION_IGNORE_CASE));
        caList.add(new StringConditionalAttribute("MT", "[1-4][0-8][0-9].*", false,
                StringConditionalAttribute.INTERNAL_FUNCTION_REGEX));
        caList.add(new StringConditionalAttribute("Currency", "CHF", false));
        caList.add(new NumberConditionalAttribute("Amount", "1200000", false, AbstractAttribute.Owner.SOURCE,
                NumberConditionalAttribute.INTERNAL_FUNCTION_GREATER_OR_EQUALS));
        rule.getConditions().add(new RuleCondition(caList));
        intList.add(rule);

        rule = new ObjectRule("test2");
        rule.getGoals().add(new GoalAttribute("call", "FIN Payment EUR."));
        rule.setTerminating(false);

        caList = new ArrayList<>();
        caList.add(new StringConditionalAttribute("ApplicationType", "FIN", false,
                StringConditionalAttribute.INTERNAL_FUNCTION_IGNORE_CASE));
        caList.add(new StringConditionalAttribute("MT", "[1-4][0-8][0-9].*", false,
                StringConditionalAttribute.INTERNAL_FUNCTION_REGEX));
        caList.add(new StringConditionalAttribute("Currency", "EUR", false));
        rule.getConditions().add(new RuleCondition(caList));
        intList.add(rule);

        rule = new ObjectRule("test3");
        rule.getGoals().add(new GoalAttribute("call", "FIN Securities."));

        caList = new ArrayList<>();
        caList.add(new StringConditionalAttribute("ApplicationType", "FIN", false,
                StringConditionalAttribute.INTERNAL_FUNCTION_IGNORE_CASE));
        caList.add(new StringConditionalAttribute("MT", "5[0-8][0-9].*", false,
                StringConditionalAttribute.INTERNAL_FUNCTION_REGEX));
        rule.getConditions().add(new RuleCondition(caList));
        intList.add(rule);
    }

    @Test
    public void showLogicalExpression() {
        for (ObjectRule objInt : intList) {
            System.out.println("Goal: " + objInt.getGoals());
            for (RuleCondition cond : objInt.getConditions()) {
                System.out.println(cond);
            }
            System.out.println();
        }
    }

    @Test
    public void simulateInterpretations() {
        Map<String, Object> contextData = new HashMap<>();

        String[] inputFiles = new String[] {
                // @formatter:off
                "/data/FIN-103-CHF-small.properties",
                "/data/FIN-103-USD-small.properties", 
                "/data/FIN-103-CHF-moderate.properties",
                "/data/FIN-103-USD-moderate.properties", 
                "/data/FIN-103-CHF-large.properties",
                "/data/FIN-103-USD-large.properties", 
                "/data/FIN-5xx.properties",
                // @formatter:on
        };

        for (String inputFile : inputFiles) {
            System.out.println("Input file: " + inputFile);
            Properties workingObject = new Properties();
            InputStream is = null;
            try {
                is = getClass().getResourceAsStream(inputFile);
                workingObject.load(is);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("Input: " + workingObject);

            for (ObjectRule objInt : intList) {
                System.out.print("Goal: " + StringUtils.rightPad("" +objInt.getGoals(), 60));

                boolean success = false;
                int i = 0;
                for (RuleCondition cond : objInt.getConditions()) {
                    i++;
                    if (cond.match(workingObject, workingObject, contextData)) {
                        System.out.print("\t-> applicable at condition index: " + i);
                        success = true;
                        break;
                    }
                }

                if (success && objInt.isTerminating()) {
                    System.out.println();
                    break;
                } else if (!success) {
                    System.out.println("\t-> NOT applicable");
                } else {
                    System.out.println();
                }
            }
            System.out.println();
        }
        System.out.println("Processing finish.");
    }
}
