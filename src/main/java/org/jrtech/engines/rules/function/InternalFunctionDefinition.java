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

import java.io.Serializable;

import org.jrtech.common.utils.model.LabelDefinition;

public class InternalFunctionDefinition implements Serializable {

    private static final long serialVersionUID = 8288197136765649076L;

    private String name;

    private LabelDefinition labelDefinition;

    private AbstractInternalFunctionImpl implementation;

    private Class<?> dataType;

    public InternalFunctionDefinition() {
        this(null, null, String.class, null);
    }

    public InternalFunctionDefinition(String name, LabelDefinition labelDefinition) {
        this(name, labelDefinition, String.class, null);
    }

    public InternalFunctionDefinition(String name, LabelDefinition labelDefinition, Class<?> dataType) {
        this(name, labelDefinition, dataType, null);
    }

    public InternalFunctionDefinition(String name, LabelDefinition labelDefinition, Class<?> dataType,
            AbstractInternalFunctionImpl implementation) {
        this.name = name;
        this.labelDefinition = labelDefinition;
        this.dataType = dataType;
        this.implementation = implementation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LabelDefinition getLabelDefinition() {
        return labelDefinition;
    }

    public void setLabelDefinition(LabelDefinition labelDefinition) {
        this.labelDefinition = labelDefinition;
    }

    public AbstractInternalFunctionImpl getImplementation() {
        return implementation;
    }

    public void setImplementation(AbstractInternalFunctionImpl implementation) {
        this.implementation = implementation;
    }

    @Override
    public String toString() {
        return name;
    }

    public Class<?> getDataType() {
        return dataType;
    }

    public void setDataType(Class<?> dataType) {
        this.dataType = dataType;
    }

    public String createLogicalExpression(String attributeName, String attributeValue) {
        if (implementation == null) {
            return null;
        }
        
        return implementation.createLogicalExpression(attributeName, attributeValue);
    }

}
