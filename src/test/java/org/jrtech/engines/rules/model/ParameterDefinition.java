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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParameterDefinition implements Serializable {

    private static final long serialVersionUID = -2381774086317758358L;

    public static final String TYPE_BOOLEAN = "boolean";

    public static final String TYPE_NUMBER = "number";

    public static final String TYPE_STRING = "string";

    public static final String[] PARAM_TYPES = new String[] { TYPE_BOOLEAN, TYPE_NUMBER, TYPE_STRING };

    private String name;

    private String defaultValue = null;

    private String type;

    public ParameterDefinition() {
        this("", TYPE_STRING);
    }

    public ParameterDefinition(String name) {
        this(name, TYPE_STRING);
    }

    public ParameterDefinition(String name, String type) {
        this(name, type, null);
    }

    public ParameterDefinition(String name, String type, String defaultValue) {
        this.name = name;
        this.type = type == null ? null : (ArrayUtils.contains(PARAM_TYPES, type.toLowerCase()) ? type.toLowerCase()
                : TYPE_STRING);
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return defaultValue;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((defaultValue == null) ? 0 : defaultValue.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ParameterDefinition other = (ParameterDefinition) obj;
        if (defaultValue == null) {
            if (other.defaultValue != null)
                return false;
        } else if (!defaultValue.equals(other.defaultValue))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

    public ParameterDefinition clone() {
        return new ParameterDefinition(name, type, defaultValue);
    }

    public static String getUserFriendlyString(Collection<ParameterDefinition> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (ParameterDefinition param : parameters) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(getUserFriendlyString(param));
            i++;
        }

        return sb.toString();
    }

    public static String getUserFriendlyString(ParameterDefinition parameter) {
        if (parameter == null) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        sb.append(parameter.getName()).append("(").append(parameter.getType().substring(0, 1).toUpperCase()).append(")");

        if (parameter.getValue() != null) {
            sb.append("=");
            boolean isString = TYPE_STRING.equalsIgnoreCase(parameter.getType());
            if (isString) {
                sb.append("'");
            }
            sb.append(parameter.getValue());
            if (isString) {
                sb.append("'");
            }
        }

        return sb.toString();
    }

    public static List<ParameterDefinition> unmarshall(String stringDefinition) throws JsonMappingException,
            IOException {
        return unmarshall(new ObjectMapper(), stringDefinition);
    }

    public static List<ParameterDefinition> unmarshall(ObjectMapper jsonMapper, String stringDefinition)
            throws JsonMappingException, IOException {
        if (stringDefinition == null || stringDefinition.trim().length() < 1) {
            return new ArrayList<ParameterDefinition>();
        }

        // sync from definition
        List<ParameterDefinition> paramList = new ArrayList<>();
        @SuppressWarnings("unchecked")
        HashMap<String, Object>[] table = jsonMapper.readValue(stringDefinition, HashMap[].class);
        for (HashMap<String, Object> record : table) {
            String paramName = (String) record.get("name");
            String paramType = (String) record.get("type");
            String paramValue = (String) record.get("value");
            paramList.add(new ParameterDefinition(paramName, paramType, paramValue));
        }
        return paramList;
    }

    public static String marshall(List<ParameterDefinition> parameterList) throws JsonProcessingException {
        return marshall(new ObjectMapper(), parameterList);
    }

    public static String marshall(ObjectMapper jsonMapper, List<ParameterDefinition> parameterList)
            throws JsonProcessingException {
        if (parameterList == null) {
            return null;
        }

        if (parameterList.isEmpty()) {
            return "[]";
        }

        // sync from list object
        return jsonMapper.writeValueAsString(parameterList.toArray(new ParameterDefinition[0]));
    }
    
}
