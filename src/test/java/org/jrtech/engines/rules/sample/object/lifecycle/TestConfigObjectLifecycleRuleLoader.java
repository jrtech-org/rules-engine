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
package org.jrtech.engines.rules.sample.object.lifecycle;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jrtech.engines.rules.model.AbstractStateManagedObject;
import org.jrtech.engines.rules.model.GoalAttribute;
import org.jrtech.engines.rules.model.RuleCondition;
import org.jrtech.engines.rules.RuleSetLoader;
import org.jrtech.engines.rules.model.Rule;
import org.junit.Before;
import org.junit.Test;

public class TestConfigObjectLifecycleRuleLoader {

    private RuleSetLoader loader;

    @Before
    public void init() {
        loader = ConfigObjectLifecycleRuleLoader.newInstance();
    }

    @Test
    public void testLoad() {
        try {
            long startTime = System.currentTimeMillis();
            List<Rule<AbstractStateManagedObject>> interpretationList = loader.load(getClass()
                    .getResourceAsStream("/data/sample-config-object-lifecycle.xml"));
            long endTime = System.currentTimeMillis() - startTime;
            
            System.out.println("Loading elapsed time: " + endTime + " ms.");
            for (Rule<AbstractStateManagedObject> rule : interpretationList) {
                ConfigObjectLifecycleRule<AbstractStateManagedObject> eli = (ConfigObjectLifecycleRule<AbstractStateManagedObject>) rule;
                System.out.println("interpretation id: '" + rule.getId() + "' State: '" + eli.getState() + "' Action: '" + eli.getAction() + "'");

                System.out.println("Goals:");
                for (GoalAttribute ga : rule.getGoals()) {
                    System.out.println("    " + StringUtils.replace(ga.toString(), "\n", "\n    "));
                }

                System.out.println("Conditions:");
                if (rule.getConditions().size() > 1) {
                    System.out.println("OR {");
                }
                for (int i = 0; i < rule.getConditions().size(); i++) {
                    RuleCondition ic = rule.getConditions().get(i);
                    System.out.println(rule.getConditions().size() > 1 ? StringUtils.replace(ic.toString(),
                            "\n", "\n   ") : ic.toString());
                    if (rule.getConditions().size() > 1) {
                        System.out.println(",");
                    }
                }
                if (rule.getConditions().size() > 1) {
                    System.out.println("   }");
                }

                System.out.println("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
