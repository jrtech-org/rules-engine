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
package org.jrtech.engines.rules.model.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jrtech.engines.rules.model.RuleCondition;
import org.jrtech.engines.rules.model.ObjectRule;
import org.jrtech.engines.rules.model.RoutingDefinition;

public class BusinessRuleDefinitionUtil {

    public String simulateBusinessRule(RoutingDefinition businessRuleDefinition,
            List<Map<String, Object>> workingObjectList, Map<String, Object> contextData) {
        StringBuffer sb = new StringBuffer();
        
        for (Map<String, Object> workingObject : workingObjectList) {
            sb.append(simulateBusinessRule(businessRuleDefinition, workingObject, contextData)).append("\n");
        }
        
        return sb.toString();   
    }
    
    public List<String> simulateBusinessRuleBrief(RoutingDefinition businessRuleDefinition,
            List<Map<String, Object>> testData, Map<String, Object> contextData) {
        List<String> result = new ArrayList<>();
        for (Map<String, Object> testDataRecord : testData) {
            String actualTestResult = simulateBusinessRuleBrief(businessRuleDefinition, testDataRecord, contextData); 
            result.add(actualTestResult);
        }
        return result;
    }
    
    public String simulateBusinessRule(RoutingDefinition businessRuleDefinition,
            Map<String, Object> workingObject, Map<String, Object> contextData) {
        StringBuffer sb = new StringBuffer();

        for (ObjectRule objInt : businessRuleDefinition.getRuleItemList()) {
            sb.append("Goal: ").append(StringUtils.rightPad("" + objInt.getGoals(), 60));

            boolean success = false;
            int i = 0;
            for (RuleCondition cond : objInt.getConditions()) {
                i++;
                if (cond.match(workingObject, workingObject, contextData)) {
                    sb.append("\t-> applicable at condition index: " + i);
                    success = true;
                    break;
                }
            }

            if (success && objInt.isTerminating()) {
                sb.append("\n");
                break;
            } else if (!success) {
                sb.append("\t-> NOT applicable\n");
            } else {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    
    public String simulateBusinessRuleBrief(RoutingDefinition businessRuleDefinition,
            Map<String, Object> workingObject, Map<String, Object> contextData) {
        String result = "";
        for (ObjectRule objInt : businessRuleDefinition.getRuleItemList()) {
            boolean success = false;
            int i = 0;
            for (RuleCondition cond : objInt.getConditions()) {
                i++;
                if (cond.match(workingObject, workingObject, contextData)) {
                    if (!result.equals("")) {
                        result += " / ";
                    }
                    result = objInt.getGoals() + " condition index: " + i;
                    success = true;
                    break;
                }
            }

            if (success && objInt.isTerminating()) {
                break;
            }
        }
        return result;
    }
}
