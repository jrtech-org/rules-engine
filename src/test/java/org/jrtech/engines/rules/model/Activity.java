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

import java.util.List;

public interface Activity {

    public static final String NAME_ALLOWED_CHARACTERS_STRING = INamedObject.BASE_ALLOWED_CHARACTERS_STRING + "- ";

    public String getObjectId();
    
    public String getName();
    
    public void setName(String name);
    
    public String getPreConditions();
    
    public void setPreConditions(String conditions);

    public String getPostConditions();
    
    public void setPostConditions(String conditions);
    
    public List<ParameterDefinition> getParameterList();
    
    public void setParameterList(List<ParameterDefinition> parameterList);
}
