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
package org.jrtech.engines.rules.function;

public class StringEndsWithFunctionImpl extends AbstractInternalFunctionImpl {

    private static final long serialVersionUID = 4471323498701985348L;

    @Override
    public boolean match(Object actualValue, Object expectedValue) {
        if (actualValue == null && expectedValue == null) {
            return true;
        } else if ((actualValue != null && expectedValue == null) || actualValue == null && expectedValue != null) {
            return false;
        } else if (actualValue instanceof String && expectedValue instanceof String) {
            return ((String) actualValue).endsWith((String) expectedValue);
        }

        return ("" + actualValue).endsWith("" + expectedValue);
    }

    @Override
    public String createLogicalExpression(String name, String value) {
        return name + ".endsWith(\"" + value + "\")";
    }

}
