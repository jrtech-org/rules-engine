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
package org.jrtech.engines.rules.writer;

import java.util.Map;

import org.jrtech.engines.rules.ObjectAttributeWriter;

public class MapIntegerValueWriter implements ObjectAttributeWriter<Map<String, Object>, String> {

    private static final long serialVersionUID = 3769896830069850253L;

    @Override
    public boolean write(Map<String, Object> object, String attributeName, String value) {
        try {
            Integer intValue = Integer.parseInt(value);
            object.put(attributeName, intValue);
            return true;
        } catch (NumberFormatException e) {
            // ignore
        }

        return false;
    }

}
