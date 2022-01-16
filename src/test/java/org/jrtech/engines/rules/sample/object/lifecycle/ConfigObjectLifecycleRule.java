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
package org.jrtech.engines.rules.sample.object.lifecycle;

import java.util.List;
import java.util.Map;

import org.jrtech.engines.rules.model.AbstractStateManagedObject;
import org.jrtech.engines.rules.model.GoalApplicationException;
import org.jrtech.engines.rules.model.Result;
import org.jrtech.engines.rules.model.RuleCondition;
import org.jrtech.engines.rules.model.Rule;

public class ConfigObjectLifecycleRule<T extends AbstractStateManagedObject> extends Rule<T> {
    
    private static final long serialVersionUID = 824435206778363825L;

    public static final String ATTR_ACTION = "action";
    
    public static final String ATTR_INPUT_STATE = "state";
    
    private String state;
    
    private String action;

    public ConfigObjectLifecycleRule(String id) {
        super(id);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return super.toString() + " State: [" + state + "]" + " Action: [" + action + "]" ;
    }
    
    public <S> Result apply(S sourceObject, T targetObject, Map<String, Object> contextVariables)
            throws GoalApplicationException {
        List<RuleCondition> conditions = getConditions();
        if (conditions == null || conditions.isEmpty()) {
            // No condition means apply the goal.
            applyGoals(sourceObject, targetObject, contextVariables);

            return Result.SUCCESS;
        } else {
            return super.apply(sourceObject, targetObject, contextVariables);
        }

    }
}
