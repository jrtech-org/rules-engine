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

import org.jrtech.engines.rules.model.AbstractStateManagedObject;
import org.jrtech.engines.rules.RuleSetLoader;
import org.jrtech.engines.rules.model.Rule;
import org.w3c.dom.Element;

public class ConfigObjectLifecycleRuleLoader extends RuleSetLoader {

    public static ConfigObjectLifecycleRuleLoader newInstance() {
        return new ConfigObjectLifecycleRuleLoader();
    }
    
    @Override
    protected <T> Rule<T> xmlToRule(Element xmlRuleElement) {
        Rule<T> interpretation = super.xmlToRule(xmlRuleElement);
        
        ((ConfigObjectLifecycleRule<?>) interpretation).setAction(xmlRuleElement.getAttribute(ConfigObjectLifecycleRule.ATTR_ACTION));
        ((ConfigObjectLifecycleRule<?>) interpretation).setState(xmlRuleElement.getAttribute(ConfigObjectLifecycleRule.ATTR_INPUT_STATE));
        
        return interpretation;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected <T> Rule<T> newRule(String id) {
        ConfigObjectLifecycleRule<? extends AbstractStateManagedObject> newInt = new ConfigObjectLifecycleRule<>(id);
        return (Rule<T>) newInt;
    }
}
