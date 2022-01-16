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

public class InvalidConfigObjectLifecycleException extends Exception {

    private static final long serialVersionUID = 5962697396195277375L;

    public InvalidConfigObjectLifecycleException(String state, String action, String objectId) {
        super("Invalid Config Object Lifecycle State: [" + state + "] or Action: [" + action + "] on object id: ["
                + objectId + "]");
    }

}
