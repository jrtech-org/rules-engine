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

import java.math.BigDecimal;

public class NumberGreaterOrEqualsFunctionImpl extends AbstractInternalFunctionImpl {

    private static final long serialVersionUID = -6099907699542566104L;

    @Override
    public boolean match(Object actualValue, Object expectedValue) {
        if (actualValue == null && expectedValue == null) {
            // No expectation -> match!
            return true;
        } else if ((actualValue != null && expectedValue == null) || actualValue == null && expectedValue != null) {
            return false;
        }

        BigDecimal actualBd = new BigDecimal("" + actualValue);
        BigDecimal expectedBd = new BigDecimal("" + expectedValue);
        return actualBd.compareTo(expectedBd) >= 0;
    }

    @Override
    public String createLogicalExpression(String name, String value) {
        return name + " >= " + value;
    }

}
