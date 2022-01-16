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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jrtech.engines.rules.RingBufferStore;

public class StringInFunctionImpl extends AbstractInternalFunctionImpl {

    private static final long serialVersionUID = -7005087809981352893L;

    private static final RingBufferStore<String, Set<String>> CACHE = new RingBufferStore<>(500);

    @Override
    public boolean match(Object actualValue, Object expectedValue) {
        if (actualValue == null && expectedValue == null) {
            return true;
        } else if ((actualValue != null && expectedValue == null) || actualValue == null && expectedValue != null) {
            return false;
        } else if (actualValue instanceof String && expectedValue instanceof String && !"".equals(expectedValue)) {
            Set<String> valueSet = getCache().get("" + expectedValue);
            if (valueSet == null) {
                valueSet = new HashSet<>();
                String[] expectedValueList = StringUtils.split("" + expectedValue, ",");
                for (String expectedValueItem : expectedValueList) {
                    String cleanedValue = expectedValueItem.trim();
                    if (cleanedValue.startsWith("\"") || cleanedValue.startsWith("'")) {
                        cleanedValue = cleanedValue.substring(1);
                    }
                    if (cleanedValue.endsWith("\"") || cleanedValue.endsWith("'")) {
                        cleanedValue = cleanedValue.substring(0, cleanedValue.length() - 1);
                    }
                    valueSet.add(cleanedValue);
                }
                getCache().put("" + expectedValue, valueSet);
            }
            return valueSet.contains(actualValue);
        }

        return false;
    }

    @Override
    public String createLogicalExpression(String name, String value) {
        return name + "in [" + value + "]";
    }

    private RingBufferStore<String, Set<String>> getCache() {
        return CACHE;
    }

}
