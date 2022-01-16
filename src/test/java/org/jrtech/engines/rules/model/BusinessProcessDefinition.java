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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jrtech.engines.rules.model.process.Statement;
import org.jrtech.engines.rules.model.process.StatementUtil;
import org.jrtech.engines.rules.model.util.ParameterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BusinessProcessDefinition extends AbstractStateManagedObject implements RoutingGoal, Activity {

    private static final long serialVersionUID = 6551035572316769588L;

    private static Logger log = LoggerFactory.getLogger(BusinessProcessDefinition.class);

    private transient SortedSet<Activity> dependentSet;

    private transient List<ParameterDefinition> parameterList;

    private transient List<Statement> logicStatements;

    private String dependentsString;

    private String preConditions;

    private String postConditions;

    private String definition;

    public BusinessProcessDefinition() {
        super();

        super.setObjectType(getClass().getSimpleName());
        dependentSet = new TreeSet<>();
        parameterList = null;
    }

    @Override
    public String getPreConditions() {
        return preConditions;
    }

    @Override
    public void setPreConditions(String preConditions) {
        this.preConditions = preConditions;
    }

    @Override
    public String getPostConditions() {
        return postConditions;
    }

    @Override
    public void setPostConditions(String postConditions) {
        this.postConditions = postConditions;
    }

    @JsonIgnore
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
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;

        logicStatements = null;
        parameterList = null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((definition == null) ? 0 : definition.hashCode());
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
        BusinessProcessDefinition other = (BusinessProcessDefinition) obj;
        if (definition == null) {
            if (other.definition != null)
                return false;
        } else if (!definition.equals(other.definition))
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

    // Parameter
    @JsonIgnore
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
    private void unmarshallParameterList() {
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

    private void marshallParameterList() {
        // sync to definition
        try {
            Map<String, Object> defProps = getDefinitionProperties(definition);
            defProps.put(DEF_ATTRIBUTE_PARAMETERS, parameterList);

            definition = new ObjectMapper().writeValueAsString(defProps);
        } catch (Exception e) {
            log.debug("Fail to update definition from object-id: " + getObjectId() + " - " + getName() + " ("
                    + getObjectType() + ") for parameters: '" + Arrays.toString(parameterList.toArray()) + "'", e);
        }
    }

    @JsonIgnore
    public List<Statement> getLogicStatements() {
        if (logicStatements == null && definition != null) {
            unmarshallLogicStatements();
        }

        return logicStatements;
    }

    public void setLogicStatements(List<Statement> logicStatements) {
        this.logicStatements = logicStatements;

        marshallLogicStatements();
    }

    @SuppressWarnings("unchecked")
    private void unmarshallLogicStatements() {
        // sync from definition
        try {
            Map<String, Object> defProps = new ObjectMapper().readValue(definition, Map.class);
            List<Map<String, Object>> statementObjectList = (List<Map<String, Object>>) defProps
                    .get(DEF_ATTRIBUTE_LOGIC);
            logicStatements = StatementUtil.convertObjectListToStatements(statementObjectList);
        } catch (Exception e) {
            log.debug("Invalid logic definition from object-id: " + getObjectId() + " - " + getName() + " ("
                    + getObjectType() + ") from definition content: '" + definition + "'", e);
            logicStatements = null;
        }
    }

    private void marshallLogicStatements() {
        // sync to definition
        try {
            Map<String, Object> defProps = getDefinitionProperties(definition);
            defProps.put(DEF_ATTRIBUTE_LOGIC, logicStatements);

            definition = new ObjectMapper().writeValueAsString(defProps);
        } catch (Exception e) {
            log.debug("Fail to update definition from object-id: " + getObjectId() + " - " + getName() + " ("
                    + getObjectType() + ") for logic statements: '" + Arrays.toString(logicStatements.toArray()) + "'",
                    e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getDefinitionProperties(String definition) {
        Map<String, Object> defProps = null;
        if (definition == null) {
            defProps = new HashMap<>();
        } else {
            try {
                return new ObjectMapper().readValue(definition, Map.class);
            } catch (Exception e) {
                // invalid definition -> Initialize with new one.
                log.debug("Invalid definition from object-id: " + getObjectId() + " - " + getName() + " ("
                        + getObjectType() + ") for logic statements: '" + definition + "'", e);
                defProps = new HashMap<>();
            }
        }

        return defProps;
    }
}
