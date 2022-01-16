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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.jrtech.engines.rules.model.GoalApplicationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestRuleSetProcessing extends AbstractTestRuleSetProcessing {

	@Before
	public void testLoad1() {
		InputStream is = getClass().getResourceAsStream("/rules/test-rule-def-1.xml");
		try {
			ruleSet = loder.load(is);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}

	@Test
	public void simpleProcessing() {
		String[][] inputData = new String[][] {
				// // @formatter:off
				{ "USD", "O", "103", "TRUE", "My Rule 1" }, { "USD", "O", "200", "TRUE", "My Rule 1" },
				{ "USD", "O", "531", "TRUE", "My Rule 2" }, { "USD", "O", "540", "TRUE", "My Rule 2" },
				// @formatter:on
		};

		for (String[] record : inputData) {
			Map<String, Object> sourceObject = new HashMap<>();
			sourceObject.put("Currency", record[0]);
			sourceObject.put("Direction", record[1]);
			sourceObject.put("MessageType", record[2]);

			Map<String, Object> targetObject = new HashMap<>();
			Map<String, Object> contextVariables = new HashMap<>();
			String matchedRuleId = null;
			try {
				matchedRuleId = ruleProcessing(sourceObject, targetObject, contextVariables);
			} catch (GoalApplicationException e) {
				e.printStackTrace();
				Assert.fail("Exception: " + e.getMessage());
			}

			Assert.assertEquals("Applied rule does not match expected rule.", record[4], matchedRuleId);
			if (matchedRuleId == null) {
				System.out.println("No rule applied!");
			}
		}
	}

}
