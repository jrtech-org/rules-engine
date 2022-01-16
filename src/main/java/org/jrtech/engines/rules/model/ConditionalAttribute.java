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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import org.jrtech.engines.rules.function.InternalFunctionDefinition;
import org.jrtech.engines.rules.ContextAwareObjectAttributeReader;
import org.jrtech.engines.rules.ObjectAttributeReader;

public class ConditionalAttribute extends AbstractAttribute {

    private static final long serialVersionUID = -7936796477326904062L;

    public static final String ATTR_NEGATE = "negate";
    public static final String ATTR_OWNER = "owner";
    public static final String ATTR_TYPE = "type";
    public static final String ATTR_OPERATOR_FUNCTION = "operatorFunction";

    protected static final ConcurrentSkipListMap<String, InternalFunctionDefinition> INTERNAL_FUNCTIONS_BY_NAME = new ConcurrentSkipListMap<>();

    private boolean negate = false;

    private String operatorFunction = "";

    private transient InternalFunctionDefinition operatorFunctionObject = null;

    public ConditionalAttribute(String name, String value) {
        this(name, value, false);
    }

    public ConditionalAttribute(String name, String value, boolean negate) {
        this(name, value, negate, Owner.SOURCE);
    }

    public ConditionalAttribute(String name, String value, boolean negate, Owner owner) {
        super(name, value, owner);
        this.negate = negate;
    }

    public ConditionalAttribute(String name, String value, boolean negate, Owner owner, String operatorFunction) {
        super(name, value, owner);
        this.negate = negate;
        this.operatorFunction = operatorFunction;
    }

    public boolean isNegated() {
        return negate;
    }

    @Override
    public String toString() {
        return (negate ? "NOT (" : "") + getOperatorFunctionObject().createLogicalExpression(getName(), getValue()) + (negate ? ")" : "");
    }

    public <S, T> boolean match(S sourceObject, T targetObject, Map<String, Object> contextVariables) {
        ActualValueResolution avr = resolveActualValue(sourceObject, targetObject, contextVariables);
        if (avr == null) {
            return false;
        }
        
        if (avr.getActualValue() == null) {
            return "".equals(getValue()) || "[NULL]".equalsIgnoreCase(getValue()) || getValue() == null;
        }

        String expectedValue = getValue();

        Boolean result = new Boolean(false);
        boolean hasValidOperator = false;
        if (getOperatorFunctionObject() != null) {
            try {
                result = getOperatorFunctionObject().getImplementation().match(avr.getActualValue(), expectedValue);
                hasValidOperator = true;
            } catch (Exception e) {
                // Fall back to old way
                hasValidOperator = false;
                result = null;
            }
        }

        if (!hasValidOperator) {
            // Primitive string equals
            result = avr.getActualValue().equals(expectedValue);
        }

        return result == null ? false : (isNegated() ? !result : result);
    }

    @Override
    protected Owner getDefaultOwner() {
        return Owner.SOURCE;
    }

    public String getOperatorFunction() {
        return operatorFunction;
    }

    public void setOperatorFunction(String operatorFunction) {
        this.operatorFunction = operatorFunction;
        this.operatorFunctionObject = null;
    }

    public InternalFunctionDefinition getOperatorFunctionObject() {
        if (operatorFunctionObject == null) {
            if (operatorFunction != null && !operatorFunction.equals("")) {
                operatorFunctionObject = getOperatorFunctionByName(getDataType(), operatorFunction);
            } else {
                operatorFunctionObject = getOperatorFunctionByName(getDataType(), getDefaultOperatorFunctionName());
            }
        }
        
        return operatorFunctionObject;
    }
    
    public String getDefaultOperatorFunctionName() {
        return "";
    }
    
    public String getDataType() {
        return String.class.getSimpleName();
    }
    
    protected static String formulateFunctionKey(String dataType, String functionName) {
        return dataType + "-" + functionName;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <S, T> ActualValueResolution resolveActualValue(S sourceObject, T targetObject, Map<String, Object> contextVariables) {
        if (sourceObject == null || targetObject == null)
            return null;

        Object objectValue = null;
        if (KEYWORD_SELF.equals(getName())) {
            objectValue = sourceObject;
        } else if (KEYWORD_SOURCE_OBJECT.equals(getName())) {
            objectValue = sourceObject;
        } else if (KEYWORD_TARGET_OBJECT.equals(getName())) {
            objectValue = targetObject;
        } else {
            objectValue = retrieveObjectValue(sourceObject, targetObject, getName(), getOwner());
        }

        String value = null;
        ObjectAttributeReader<Object, Object> reader = getReader();
        if (reader != null) {
            if (reader instanceof ContextAwareObjectAttributeReader) {
                ((ContextAwareObjectAttributeReader) reader).setContextData(contextVariables);
            }
            value = "" + reader.read(objectValue);
        } else {
            if (objectValue != null) {
                value = "" + objectValue;
            }
        }

        String conditionValue = getValue();
        boolean variableValue = false;
        if (isVariableValue()) {
            conditionValue = resolveVariableValue(sourceObject, targetObject, contextVariables, conditionValue);
            variableValue = true;
        }

        return new ActualValueResolution(true, value, variableValue);
    }

    public static InternalFunctionDefinition getOperatorFunctionByName(String dataType, String operatorFunctionName) {
        return INTERNAL_FUNCTIONS_BY_NAME.get(formulateFunctionKey(dataType, operatorFunctionName));
    }
    
    public static List<InternalFunctionDefinition> getOperatorFunctionList() {
        return Collections.unmodifiableList(new ArrayList<>(INTERNAL_FUNCTIONS_BY_NAME.values()));
    }

    protected static class ActualValueResolution implements Serializable {

        private static final long serialVersionUID = 8278551921039666245L;
        
        private boolean status;
        
        private boolean variableValue = false;
        
        private String actualValue;

        public ActualValueResolution(boolean status, String actualValue, boolean variableValue) {
            this.status = status;
            this.actualValue = actualValue;
            this.variableValue = variableValue;
        }
        
        public boolean isStatus() {
            return status;
        }

        public String getActualValue() {
            return actualValue;
        }
        
        public boolean isVariableValue() {
            return variableValue;
        }
    }
}
