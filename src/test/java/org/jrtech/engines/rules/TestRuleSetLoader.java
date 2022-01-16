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
import java.util.List;

import org.jrtech.engines.rules.model.Rule;
import org.junit.Assert;
import org.junit.Test;

public class TestRuleSetLoader {
    
    private RuleSetLoader loder = new RuleSetLoader();
    
    @Test
    public void testLoad1() {
        InputStream is = getClass().getResourceAsStream("/rules/test-rule-def-1.xml");
        try {
            List<Rule<Object>> intList = loder.load(is);
            for (Rule<Object> rule : intList) {
                System.out.println(rule.getId());
            }
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

}
