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
import java.util.Map;

import org.jrtech.engines.rules.model.BusinessAttribute;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JsonBusinessAttribute extends BusinessAttribute {

    private static final long serialVersionUID = 7976630012802400681L;

    public static List<JsonBusinessAttribute> fromBase(Collection<BusinessAttribute> baseObjects) {
        List<JsonBusinessAttribute> temp = new ArrayList<>();
        for (BusinessAttribute baseObject : baseObjects) {
            temp.add(new JsonBusinessAttribute().fromBase(baseObject));
        }
        
        return temp;
    }

    public static List<BusinessAttribute> toBase(Collection<JsonBusinessAttribute> jsonObjects) {
        List<BusinessAttribute> temp = new ArrayList<>();
        for (JsonBusinessAttribute jsonObject : jsonObjects) {
            temp.add(jsonObject.toBase());
        }
        
        return temp;
    }
    
    public JsonBusinessAttribute fromBase(BusinessAttribute baseObject) {
        JsonObjectCopyUtil.copyValue(baseObject, this);
        return this;
    }
    
    public BusinessAttribute toBase() {
        BusinessAttribute baseObject = new BusinessAttribute();
        JsonObjectCopyUtil.copyValue(this, baseObject);
        return baseObject;
    }
    
    @JsonIgnore
    @Override
    public Map<String, Object> getExtendedAttributeMap() {
        return super.getExtendedAttributeMap();
    }
    
}
