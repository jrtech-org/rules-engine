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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rule<T> implements Serializable {
    private static final long serialVersionUID = -7949963709540315623L;

    public static final String TAG_GOALS = "goals";
    public static final String TAG_CONDITION = "condition";
    public static final String TAG_CONDITIONS = "conditions";
    public static final String TAG_RULE = "rule";
    
    public static final String ATTR_SCOPE_INDEX = "scopeIndex";
    
    private static Logger log = LoggerFactory.getLogger(Rule.class);

    private String id;
    
    private String scopeIndex;

    private List<GoalAttribute> goals;

    private List<RuleCondition> conditions;
    
    private boolean terminating = true;

    public Rule() {
        this(null);
    }
    
    public Rule(String id) {
        this.id = id;
        goals = new ArrayList<GoalAttribute>();
        conditions = new ArrayList<RuleCondition>();
    }

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    public List<GoalAttribute> getGoals() {
        return goals;
    }

    public List<RuleCondition> getConditions() {
        return conditions;
    }

    public <S> Result apply(S sourceObject, T targetObject, Map<String, Object> contextVariables)
            throws GoalApplicationException {
        if (conditions == null || conditions.isEmpty()) {
            return Result.SUCCESS; // No conditions provided -> always TRUE
        }
        
        for (RuleCondition cond : conditions) {
            if (cond.match(sourceObject, targetObject, contextVariables)) {
                applyGoals(sourceObject, targetObject, contextVariables);
                return Result.SUCCESS;
            }
        }

        return Result.NOT_APPLICABLE;
    }

    protected <S> void applyGoals(S sourceObject, T targetObject, Map<String, Object> contextVariables)
            throws GoalApplicationException {
        log.debug("Applying rule: '" + id + "'");
        for (GoalAttribute goal : goals) {
            goal.apply(sourceObject, targetObject, contextVariables);
        }
    }

    public boolean isTerminating() {
        return terminating;
    }

    public void setTerminating(boolean terminating) {
        this.terminating = terminating;
    }

    public String getScopeIndex() {
        return scopeIndex;
    }

    public void setScopeIndex(String scopeIndex) {
        this.scopeIndex = scopeIndex;
    }
}
