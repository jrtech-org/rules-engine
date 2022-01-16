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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class RuleCondition implements Serializable {

	private static final long serialVersionUID = 4473572856854989472L;
	
    private List<ConditionalAttribute> attributes;
	
	public RuleCondition(List<ConditionalAttribute> attributes) {
		super();
		this.attributes = attributes;
		if (this.attributes == null) {
		    this.attributes = new ArrayList<>();
		}
	}
	
	public <S, T> boolean match(S sourceObject, T targetObject, Map<String, Object> contextVariables) {
		for (ConditionalAttribute attr : attributes) {
			if (!attr.match(sourceObject, targetObject, contextVariables)) return false;
		}
		
		return true;
	}
	
	public List<ConditionalAttribute> getAttributes() {
	    return Collections.unmodifiableList(attributes);
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("AND {");
		for (int i = 0; i < attributes.size(); i++) {
			ConditionalAttribute ca = attributes.get(i);
			
			if (i > 0) {
				sb.append(",");
			} 
				
			if (attributes.size() > 1) {
				sb.append("\n");
			}
			
			sb.append(ca.toString());
		}
		if (attributes.size() > 1) {
			sb.append("\n");
		}
		sb.append("}");
		
	    return StringUtils.replace(sb.toString(), "\n", "\n    ");
	}

}
