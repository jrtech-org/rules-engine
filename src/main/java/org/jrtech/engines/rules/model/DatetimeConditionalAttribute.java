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

public class DatetimeConditionalAttribute extends DateConditionalAttribute {

    private static final long serialVersionUID = 7137437000320580236L;

    public DatetimeConditionalAttribute(String name, String value) {
        this(name, value, false, Owner.SOURCE);
    }
    
    public DatetimeConditionalAttribute(String name, String value, boolean negate) {
        this(name, value, negate, Owner.SOURCE);
    }

    public DatetimeConditionalAttribute(String name, String value, boolean negate, Owner owner) {
        this(name, value, negate, owner, "");
    }

    public DatetimeConditionalAttribute(String name, String value, boolean negate, String operatorFunction) {
        this(name, value, negate, Owner.SOURCE, operatorFunction);
    }

    public DatetimeConditionalAttribute(String name, String value, boolean negate, Owner owner, String operatorFunction) {
        super(name, value, negate, owner, operatorFunction);
    }

}
