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
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jrtech.engines.rules.model.CallParameter;
import org.jrtech.engines.rules.model.ParameterDefinition;

public class ParameterUtil {

    public static String getUserFriendlyString(Collection<CallParameter> parameters) {
        if (parameters == null) {
            return null;
        }

        if (parameters.isEmpty()) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (CallParameter param : parameters) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(param.getName()).append("=");
            
            String valueString = param.getValue();
            if (valueString == null) {
                valueString = "";
            }
            
            sb.append(valueString);
            i++;
        }

        return sb.toString();
    }

    public static List<CallParameter> convertCallParameterRecordList(List<Map<String, Object>> parameterRecordList) {
        if (parameterRecordList == null)
            return null;
        
        List<CallParameter> paramList = new ArrayList<>();
        for (Map<String, Object> paramRecord : parameterRecordList) {
            String paramName = (String) paramRecord.get("name");
            String paramValue = (String) paramRecord.get("value");
            paramList.add(new CallParameter(paramName, paramValue));
        }
        

        return paramList;
    }

    public static List<ParameterDefinition> convertParameterDefinitionRecordList(List<Map<String, Object>> parameterRecordList) {
        if (parameterRecordList == null)
            return null;
        
        List<ParameterDefinition> paramList = new ArrayList<>();
        for (Map<String, Object> paramRecord : parameterRecordList) {
            String paramName = (String) paramRecord.get("name");
            String paramType = (String) paramRecord.get("type");
            String paramValue = (String) paramRecord.get("defaultValue");
            paramList.add(new ParameterDefinition(paramName, paramType, paramValue));
        }
        

        return paramList;
    }
}
