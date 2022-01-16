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

public class RoutingGoalAttribute extends GoalAttribute implements CallParameterizable {

    private static final long serialVersionUID = 1371951363426832051L;

    public RoutingGoalAttribute(String name, String value) {
        super(name, value);
    }

    public RoutingGoalAttribute(String name, String value, String method) {
        super(name, value, method, Owner.TARGET);
    }

    public RoutingGoalAttribute(String name, String value, String method, Owner owner) {
        super(name, value, method, owner, null);
    }

    public static final RoutingGoalAttribute fromGoalAttribute(GoalAttribute goalAttribute) {
        if (goalAttribute == null)
            return null;

        RoutingGoalAttribute rga = new RoutingGoalAttribute(goalAttribute.getName(), goalAttribute.getValue(), goalAttribute.getMethod(), goalAttribute.getOwner());
        rga.setReaderClass(goalAttribute.getReaderClass());
        rga.setWriterClass(goalAttribute.getWriterClass());
        rga.setParameterList(goalAttribute.getParameterList());
        
        return rga;
    }
    
}
