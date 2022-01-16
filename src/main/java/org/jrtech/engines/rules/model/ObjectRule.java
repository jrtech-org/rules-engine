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

import java.util.ArrayList;
import java.util.List;

public class ObjectRule extends Rule<Object> {

    private static final long serialVersionUID = -4540280811510662364L;
//
//    public ObjectInterpretation() {
//        super();
//    }
    
    public ObjectRule(String id) {
        super(id);
    }

    public static final ObjectRule fromBaseInterpretation(Rule<Object> baseInterpretation) {
        ObjectRule result = new ObjectRule(baseInterpretation.getId());
        result.setTerminating(baseInterpretation.isTerminating());
        result.getGoals().addAll(baseInterpretation.getGoals());
        result.getConditions().addAll(baseInterpretation.getConditions());
        
        return result;
    }

    public static final List<ObjectRule> fromBaseInterpretation(List<Rule<Object>> baseInterpretationList) {
        List<ObjectRule> result = new ArrayList<>();
        for (Rule<Object> baseInterpretation : baseInterpretationList) {
            result.add(fromBaseInterpretation(baseInterpretation));
        }
        
        return result;
    }
    
    public static final List<Rule<?>> toBaseInterpretation(List<ObjectRule> objectInterpretationList) {
        List<Rule<?>> result = new ArrayList<>();
        result.addAll(objectInterpretationList);
        
        return result;
    }

}
