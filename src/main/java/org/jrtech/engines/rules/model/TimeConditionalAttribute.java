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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jrtech.engines.rules.function.DateAfterFunctionImpl;
import org.jrtech.engines.rules.function.DateAtFunctionImpl;
import org.jrtech.common.utils.model.LabelDefinition;
import org.jrtech.engines.rules.function.DateBeforeFunctionImpl;
import org.jrtech.engines.rules.function.DateSinceFunctionImpl;
import org.jrtech.engines.rules.function.DateUntilFunctionImpl;
import org.jrtech.engines.rules.function.InternalFunctionDefinition;

public class TimeConditionalAttribute extends ConditionalAttribute {

    private static final long serialVersionUID = 6302724737990945522L;

    public static final String INTERNAL_FUNCTION_AFTER = "after";

    public static final String INTERNAL_FUNCTION_AT = "at";

    public static final String INTERNAL_FUNCTION_BEFORE = "before";

    public static final String INTERNAL_FUNCTION_SINCE = "since";

    public static final String INTERNAL_FUNCTION_UNTIL = "until";

    protected static final List<InternalFunctionDefinition> INTERNAL_FUNCTION_LIST = Arrays
            .asList(new InternalFunctionDefinition[] {
                    // @formatter:off
                    // Date/Time
                    new InternalFunctionDefinition(INTERNAL_FUNCTION_AFTER, new LabelDefinition(
                            "internal.function.datetime." + INTERNAL_FUNCTION_AFTER, "After"), Date.class, 
                            new DateAfterFunctionImpl()),
                    new InternalFunctionDefinition(INTERNAL_FUNCTION_AT, new LabelDefinition(
                            "internal.function.datetime." + INTERNAL_FUNCTION_AT, "At"), Date.class, 
                            new DateAtFunctionImpl()),
                    new InternalFunctionDefinition(INTERNAL_FUNCTION_BEFORE, new LabelDefinition(
                            "internal.function.datetime." + INTERNAL_FUNCTION_BEFORE, "Before"), Date.class, 
                            new DateBeforeFunctionImpl()),
                    new InternalFunctionDefinition(INTERNAL_FUNCTION_SINCE, new LabelDefinition(
                            "internal.function.datetime." + INTERNAL_FUNCTION_SINCE, "Since"), Date.class, 
                            new DateSinceFunctionImpl()),
                    new InternalFunctionDefinition(INTERNAL_FUNCTION_UNTIL, new LabelDefinition(
                            "internal.function.datetime." + INTERNAL_FUNCTION_UNTIL, "Until"), Date.class, 
                            new DateUntilFunctionImpl()),
                    // @formatter:on
            });

    static {
        for (InternalFunctionDefinition ifd : INTERNAL_FUNCTION_LIST) {
            String functionKey = formulateFunctionKey(ifd.getDataType().getSimpleName(), ifd.getName());
            if (!INTERNAL_FUNCTIONS_BY_NAME.containsKey(functionKey)) {
                INTERNAL_FUNCTIONS_BY_NAME.put(functionKey, ifd);
            }
        }
    }

    public TimeConditionalAttribute(String name, String value) {
        this(name, value, false, Owner.SOURCE);
    }

    public TimeConditionalAttribute(String name, String value, boolean negate) {
        this(name, value, negate, Owner.SOURCE);
    }

    public TimeConditionalAttribute(String name, String value, boolean negate, Owner owner) {
        this(name, value, negate, owner, "");
    }

    public TimeConditionalAttribute(String name, String value, boolean negate, String operatorFunction) {
        this(name, value, negate, Owner.SOURCE, operatorFunction);
    }

    public TimeConditionalAttribute(String name, String value, boolean negate, Owner owner, String operatorFunction) {
        super(name, value, negate, owner, operatorFunction);
    }

    @Override
    public String getDefaultOperatorFunctionName() {
        return INTERNAL_FUNCTION_AT;
    }

    @Override
    public String getDataType() {
        return Date.class.getSimpleName();
    }
}
