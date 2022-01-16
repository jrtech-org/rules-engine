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
package org.jrtech.engines.rules.reader;

import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jrtech.engines.rules.model.AbstractStateManagedObject;
import org.jrtech.engines.rules.ContextAwareObjectAttributeReader;

public class FinalApproveReader<T extends AbstractStateManagedObject> implements ContextAwareObjectAttributeReader<T, Boolean> {

    private static final long serialVersionUID = 7856439590774982339L;
    
    private Map<String, Object> contextData;

    @Override
    public Boolean read(T objectValue) {
        if (objectValue.getApproverList() == null || "".equals(objectValue.getApproverList())) {
            return false;
        } else {
            String[] approvers = StringUtils.split(objectValue.getApproverList(), ',');
            String currentUser = (String) getContextData().get("USER");
            if (ArrayUtils.contains(approvers, currentUser)) {
                return false; // skip
            }
        }
        
        return true;
    }

    @Override
    public void setContextData(Map<String, Object> contextData) {
        this.contextData = contextData;
    }

    @Override
    public Map<String, Object> getContextData() {
        return contextData;
    }

}
