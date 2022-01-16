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
package org.jrtech.engines.rules.model.process;

import java.util.List;

import org.jrtech.engines.rules.model.CallParameter;
import org.jrtech.engines.rules.model.CallParameterizable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SimpleStatement implements Statement, CallParameterizable {

    private static final long serialVersionUID = 1574562978155210443L;

    private String label;

    private String referenceActivityId;

    private List<CallParameter> parameterList;

    @JsonIgnore
    private transient StringBuffer logicalStringBuffer = null;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;

        logicalStringBuffer = null;
    }

    public String getReferenceActivityId() {
        return referenceActivityId;
    }

    public void setReferenceActivityId(String referenceActivityId) {
        this.referenceActivityId = referenceActivityId;

        logicalStringBuffer = null;
    }

    public List<CallParameter> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<CallParameter> parameterList) {
        this.parameterList = parameterList;

        logicalStringBuffer = null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        result = prime * result + ((parameterList == null) ? 0 : parameterList.hashCode());
        result = prime * result + ((referenceActivityId == null) ? 0 : referenceActivityId.hashCode());
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
        SimpleStatement other = (SimpleStatement) obj;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        if (parameterList == null) {
            if (other.parameterList != null)
                return false;
        } else if (!parameterList.equals(other.parameterList))
            return false;
        if (referenceActivityId == null) {
            if (other.referenceActivityId != null)
                return false;
        } else if (!referenceActivityId.equals(other.referenceActivityId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return label + " (" + referenceActivityId + ")";
    }

    @Override
    public String toLogicalString() {
        return toLogicalString(false);
    }

    @Override
    public String toLogicalString(boolean forceRefresh) {
        if (logicalStringBuffer == null || forceRefresh) {
            logicalStringBuffer = new StringBuffer();
            logicalStringBuffer.append(label).append("(");
            if (parameterList != null) {
                int i = 0;
                for (CallParameter param : parameterList) {
                    if (i > 0) {
                        logicalStringBuffer.append(", ");
                    }
                    logicalStringBuffer.append(param.getName()).append("=").append(param.getValue());
                    i++;
                }
            }
            logicalStringBuffer.append("");
        }
        return logicalStringBuffer == null ? "" : logicalStringBuffer.toString();
    }

    public void resetLogicalStringBuffer() {
        logicalStringBuffer = null;
    }

    @Override
    public String getType() {
        return getClass().getSimpleName();
    }

}
