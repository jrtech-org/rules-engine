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
package org.jrtech.engines.rules;

import java.util.List;
import java.util.Map;

import org.jrtech.engines.rules.model.GoalApplicationException;
import org.jrtech.engines.rules.model.Result;
import org.jrtech.engines.rules.model.Rule;

public abstract class AbstractTestRuleSetProcessing {
	
	protected RuleSetLoader loder = new RuleSetLoader();

	protected List<Rule<Object>> ruleSet;

	protected String ruleProcessing(Map<String, Object> sourceObject, Map<String, Object> targetObject,
			Map<String, Object> contextVariables) throws GoalApplicationException {
		for (Rule<Object> rule : ruleSet) {
			Result result = rule.apply(sourceObject, targetObject, contextVariables);
			if (Result.SUCCESS.equals(result)) {
				// match
				return rule.getId();
			}
		}

		return null;
	}
}
