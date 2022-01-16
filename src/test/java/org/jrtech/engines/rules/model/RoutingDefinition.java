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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;

import org.jrtech.engines.rules.model.util.ParameterUtil;
import org.jrtech.engines.rules.RuleSetLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RoutingDefinition extends AbstractRule implements RoutingGoal, Activity {

    private static final long serialVersionUID = -4754605978479372497L;

    public static final String RULE_TYPE = "Routing-";
    
    private static Logger log = LoggerFactory.getLogger(RoutingDefinition.class);

    private transient List<ObjectRule> ruleItemList;

    private transient SortedSet<Activity> dependentSet;
    
    private transient List<ParameterDefinition> parameterList; 

    private String dependentsString;

    private String preConditions;

    private String postConditions;

    private String definition;

    private transient RuleSetLoader ruleLoader;
    
    public RoutingDefinition() {
        super();
        
        super.setObjectType(getClass().getSimpleName());

        // ruleItemList = new ArrayList<>();
        // dependentSet = new TreeSet<>();
    }

    public List<ObjectRule> getRuleItemList() {
        if ((ruleItemList == null || ruleItemList.isEmpty()) && (definition != null && definition.trim().length() > 0)) {
            unmarshallRoutingRuleItemList();
        }
        
        if (ruleItemList == null) {
            ruleItemList = new ArrayList<>();
        }
        
        return ruleItemList;
    }

    private final List<ObjectRule> convertRoutingRuleFromString(String routingRuleDefinition) {
        List<Rule<Object>> interpretationList;
        try {
            interpretationList = getRuleLoader().load(routingRuleDefinition);
            return ObjectRule.fromBaseInterpretation(interpretationList);
        } catch (Exception e) {
            log.info("Fail to create object from definition:\n" + definition, e);
        }
        
        return new ArrayList<>();
    }
    
    private final String convertRuleItemListToString(List<ObjectRule> routingRuleItemList) {
        try {
            return getRuleLoader().writeAsString(ObjectRule.toBaseInterpretation(ruleItemList), "interpretationList", false);
        } catch (Exception e) {
            log.info("Fail to write string definition: " + ruleItemList, e);
        }
        
        return "";
    }

    public String getPreConditions() {
        return preConditions;
    }

    public void setPreConditions(String preConditions) {
        this.preConditions = preConditions;
    }

    public String getPostConditions() {
        return postConditions;
    }

    public void setPostConditions(String postConditions) {
        this.postConditions = postConditions;
    }

    public SortedSet<Activity> getDependentSet() {
        return dependentSet;
    }

    public void addDependent(Activity element) {
        dependentSet.add(element);
    }

    public void removeDependent(Activity element) {
        dependentSet.remove(element);
    }

    public String getDefinition() {
        if (definition == null) {
            if (parameterList != null) {
                marshallParameterList();
            }
            
            if (ruleItemList != null) {
                marshallRoutingRuleItemList();
            }
        }
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
        
        ruleItemList = null;
        parameterList = null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((definition == null) ? 0 : definition.hashCode());
        result = prime * result + ((getDependentsString() == null) ? 0 : getDependentsString().hashCode());
        result = prime * result + ((postConditions == null) ? 0 : postConditions.hashCode());
        result = prime * result + ((preConditions == null) ? 0 : preConditions.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        RoutingDefinition other = (RoutingDefinition) obj;
        if (definition == null) {
            if (other.definition != null)
                return false;
        } else if (!definition.equals(other.definition))
            return false;
        if (getDependentsString() == null) {
            if (other.getDependentsString() != null)
                return false;
        } else if (!getDependentsString().equals(other.getDependentsString()))
            return false;
        if (postConditions == null) {
            if (other.postConditions != null)
                return false;
        } else if (!postConditions.equals(other.postConditions))
            return false;
        if (preConditions == null) {
            if (other.preConditions != null)
                return false;
        } else if (!preConditions.equals(other.preConditions))
            return false;
        return true;
    }

    public String getDependentsString() {
        return dependentsString;
    }

    public void setDependentsString(String dependentsString) {
        this.dependentsString = dependentsString;
    }

    public void setRuleItemList(List<ObjectRule> ruleItemList) {
        this.ruleItemList = ruleItemList;
        
        marshallRoutingRuleItemList();
    }
    
    private RuleSetLoader getRuleLoader() {
        if (ruleLoader == null) {
            ruleLoader = new RuleSetLoader();
        }
        
        return ruleLoader;
    }

    @Override
    protected String getRuleType() {
        return RULE_TYPE;
    }

    @Override
    public List<ParameterDefinition> getParameterList() {
        if (parameterList == null && definition != null) {
            unmarshallParameterList();
        }
        return parameterList;
    }

    @Override
    public void setParameterList(List<ParameterDefinition> parameterList) {
        this.parameterList = parameterList;
        
        marshallParameterList();
    }
    
    @SuppressWarnings("unchecked")
    public void unmarshallParameterList() {
        // sync from definition
        try {
            Map<String, Object> defProps = new ObjectMapper().readValue(definition, Map.class);
            List<Map<String, Object>> paramRecordList = (List<Map<String, Object>>) defProps
                    .get(DEF_ATTRIBUTE_PARAMETERS);
            parameterList = ParameterUtil.convertParameterDefinitionRecordList(paramRecordList);
        } catch (Exception e) {
            log.debug("Invalid parameter definition from object-id: " + getObjectId() + " - " + getName() + " ("
                    + getObjectType() + ") from definition content: '" + definition + "'", e);
            parameterList = null;
        }
    }

    @SuppressWarnings("unchecked")
    public void marshallParameterList() {
        // sync to definition
        try {
            ObjectMapper jsonMapper = new ObjectMapper();
            Map<String, Object> defProps = null;
            if (definition == null) {
                defProps = new TreeMap<>();
            } else {
                defProps = jsonMapper.readValue(definition, Map.class);
            }
            defProps.put(DEF_ATTRIBUTE_PARAMETERS, parameterList);

            definition = jsonMapper.writeValueAsString(defProps);
        } catch (Exception e) {
            log.debug("Failto update definition from object-id: " + getObjectId() + " - " + getName() + " ("
                    + getObjectType() + ") for parameters: '" + Arrays.toString(parameterList.toArray()) + "'", e);
        }
    }

    @SuppressWarnings("unchecked")
    public void unmarshallRoutingRuleItemList() {
        // sync from definition
        try {
            Map<String, Object> defProps = new ObjectMapper().readValue(definition, Map.class);
            String routingRuleString = (String) defProps.get(DEF_ATTRIBUTE_LOGIC);
            ruleItemList = convertRoutingRuleFromString(routingRuleString);
        } catch (Exception e) {
            log.debug("Invalid routing rule definition from object-id: " + getObjectId() + " - " + getName() + " ("
                    + getObjectType() + ") from definition content: '" + definition + "'", e);
            ruleItemList = null;
        }
    }

    @SuppressWarnings("unchecked")
    public void marshallRoutingRuleItemList() {
        // sync to definition
        try {
            String routingRuleString = convertRuleItemListToString(ruleItemList);
            
            ObjectMapper jsonMapper = new ObjectMapper();
            Map<String, Object> defProps = null;
            if (definition == null) {
                defProps = new TreeMap<>();
            } else {
                defProps = jsonMapper.readValue(definition, Map.class);
            }
            defProps.put(DEF_ATTRIBUTE_LOGIC, routingRuleString);

            definition = jsonMapper.writeValueAsString(defProps);
        } catch (Exception e) {
            log.debug("Fail to update definition from object-id: " + getObjectId() + " - " + getName() + " ("
                    + getObjectType() + ") for routing rules: '" + Arrays.toString(ruleItemList.toArray()) + "'",
                    e);
        }
    }

}
