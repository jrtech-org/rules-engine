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

import org.jrtech.engines.rules.model.Activity;
import org.jrtech.engines.rules.model.BusinessProcessDefinition;
import org.jrtech.engines.rules.model.ParameterDefinition;
import org.jrtech.engines.rules.model.process.Statement;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JsonBusinessProcessDefinition extends BusinessProcessDefinition {

    private static final long serialVersionUID = 1362938751016662823L;

    public static List<JsonBusinessProcessDefinition> fromBase(Collection<BusinessProcessDefinition> baseObjects) {
        List<JsonBusinessProcessDefinition> temp = new ArrayList<>();
        for (BusinessProcessDefinition baseObject : baseObjects) {
            temp.add(new JsonBusinessProcessDefinition().fromBase(baseObject));
        }
        
        return temp;
    }

    public static List<BusinessProcessDefinition> toBase(Collection<JsonBusinessProcessDefinition> jsonObjects) {
        List<BusinessProcessDefinition> temp = new ArrayList<>();
        for (JsonBusinessProcessDefinition jsonObject : jsonObjects) {
            temp.add(jsonObject.toBase());
        }
        
        return temp;
    }
    
    public JsonBusinessProcessDefinition fromBase(BusinessProcessDefinition baseObject) {
        JsonObjectCopyUtil.copyValue(baseObject, this);
        return this;
    }
    
    public BusinessProcessDefinition toBase() {
        BusinessProcessDefinition baseObject = new BusinessProcessDefinition();
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
    public List<Statement> getLogicStatements() {
        return super.getLogicStatements();
    }
    
    @JsonIgnore
    @Override
    public void setLogicStatements(List<Statement> logicStatements) {
        super.setLogicStatements(logicStatements);
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
}
