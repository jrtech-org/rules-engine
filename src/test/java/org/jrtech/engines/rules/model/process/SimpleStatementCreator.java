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
import java.util.Map;

import org.jrtech.engines.rules.model.CallParameter;
import org.jrtech.engines.rules.model.util.ParameterUtil;

public class SimpleStatementCreator extends AbstractStatementCreator<SimpleStatement> {

    private static final long serialVersionUID = -6496628877030776761L;

    @Override
    public SimpleStatement create(Map<String, Object> record) {
        SimpleStatement st = new SimpleStatement();

        st.setLabel((String) record.get("label"));
        st.setReferenceActivityId((String) record.get("referenceActivityId"));
        @SuppressWarnings("unchecked")
        List<CallParameter> paramList = ParameterUtil.convertCallParameterRecordList((List<Map<String, Object>>) record
                .get("parameterList"));
        st.setParameterList(paramList);

        return st;
    }

}
