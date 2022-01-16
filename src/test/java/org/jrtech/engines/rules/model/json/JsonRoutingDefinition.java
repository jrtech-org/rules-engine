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
package org.jrtech.engines.rules.model.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

import org.jrtech.engines.rules.model.ParameterDefinition;
import org.jrtech.engines.rules.model.Activity;
import org.jrtech.engines.rules.model.ObjectRule;
import org.jrtech.engines.rules.model.RoutingDefinition;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JsonRoutingDefinition extends RoutingDefinition {

    private static final long serialVersionUID = 4067726577051692292L;

    public static List<JsonRoutingDefinition> fromBase(Collection<RoutingDefinition> baseObjects) {
        List<JsonRoutingDefinition> temp = new ArrayList<>();
        for (RoutingDefinition baseObject : baseObjects) {
            temp.add(new JsonRoutingDefinition().fromBase(baseObject));
        }

        return temp;
    }

    public static List<RoutingDefinition> toBase(Collection<JsonRoutingDefinition> jsonObjects) {
        List<RoutingDefinition> temp = new ArrayList<>();
        for (JsonRoutingDefinition jsonObject : jsonObjects) {
            temp.add(jsonObject.toBase());
        }

        return temp;
    }

    public JsonRoutingDefinition fromBase(RoutingDefinition baseObject) {
        JsonObjectCopyUtil.copyValue(baseObject, this);
        return this;
    }

    public RoutingDefinition toBase() {
        RoutingDefinition baseObject = new RoutingDefinition();
        JsonObjectCopyUtil.copyValue(this, baseObject);
        return baseObject;
    }

    @JsonIgnore
    @Override
    public SortedSet<Activity> getDependentSet() {
        return super.getDependentSet();
    }

    @JsonIgnore
    @Override
    public List<ObjectRule> getRuleItemList() {
        return super.getRuleItemList();
    }

    @JsonIgnore
    @Override
    public void setRuleItemList(List<ObjectRule> ruleItemList) {
        super.setRuleItemList(ruleItemList);
    }

    @JsonIgnore
    @Override
    public List<ParameterDefinition> getParameterList() {
        return super.getParameterList();
    }

    @JsonIgnore
    @Override
    public void setParameterList(List<ParameterDefinition> parameterList) {
        super.setParameterList(parameterList);
    }
    
    @JsonIgnore
    @Override
    protected String getRuleType() {
        return super.getRuleType();
    }
}
