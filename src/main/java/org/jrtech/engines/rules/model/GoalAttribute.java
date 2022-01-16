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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.jrtech.engines.rules.ObjectAttributeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoalAttribute extends AbstractAttribute {

    private static final long serialVersionUID = 555748055742080587L;

    public static final String ATTR_METHOD = "method";

    public static final String ATTR_WRITER_CLASS = "writerClass";

    public static final String TAG_PARAMETER = "parameter";

    private static Logger log = LoggerFactory.getLogger(GoalAttribute.class);

    private String method;

    private String writerClass;

    private ObjectAttributeWriter<Object, Object> writer = null;
    
    private List<CallParameter> parameterList;
    
    private transient Map<String, CallParameter> parameterCatalog = new HashMap<>();

    public GoalAttribute(String name, String value) {
        this(name, value, null);
    }

    public GoalAttribute(String name, String value, String method) {
        this(name, value, method, Owner.TARGET);
    }

    public GoalAttribute(String name, String value, String method, Owner owner) {
        this(name, value, method, owner, null);
    }

    public GoalAttribute(String name, String value, String method, Owner owner, String writerClass) {
        super(name, value, owner);
        this.method = method;
        this.writerClass = writerClass;
        this.parameterList = new ArrayList<>();
    }

    public String getMethod() {
        return "".equals(method) ? null : method;
    }

    public String getWriterClass() {
        return writerClass;
    }

    public void setWriterClass(String writerClass) {
        this.writerClass = writerClass;
    }

    @SuppressWarnings("unchecked")
    public ObjectAttributeWriter<Object, Object> getWriter() {
        if (writer != null)
            return writer;

        if (writerClass != null && writerClass.trim().length() > 0) {
            try {
                @SuppressWarnings({ "rawtypes" })
                Class<? extends ObjectAttributeWriter> clzz = (Class<? extends ObjectAttributeWriter>) Class
                        .forName(writerClass);
                writer = clzz.newInstance();

                return writer;
            } catch (Exception e) {
                log.warn("Cannot load interpreter writer: '" + writerClass + "'", e);
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return getName() + "(" + (method == null ? "" : method) + ")" + "->" + getValue();
    }

    public <S, T> T apply(S sourceObject, T targetObject, Map<String, Object> contextVariables)
            throws GoalApplicationException {
        if (targetObject != null && sourceObject != null) {
            Object objectValue = null;
            if (!isVariableValue()) {
                objectValue = getValue();
            } else {
                // Resolve variable
                objectValue = resolveVariableValue(sourceObject, targetObject, contextVariables, getValue());
            }
            if (getWriter() != null) {
                getWriter().write(targetObject, getName(), objectValue);
            } else {
                setValue(targetObject, "" + objectValue);
            }
        }

        return targetObject;
    }

    private <T> void setValue(T targetObject, String value) throws GoalApplicationException {
        if (getMethod() == null) {
            try {
                BeanUtils.setProperty(targetObject, getName(), value);
            } catch (Exception e) {
                throw new GoalApplicationException(this, e);
            }
        } else {
            try {
                MethodUtils.invokeExactMethod(targetObject, getMethod(), new Object[] { getName(), value },
                        new Class[] { String.class, Object.class });
            } catch (Exception e) {
                throw new GoalApplicationException(this, e);
            }
        }

    }

    @Override
    protected Owner getDefaultOwner() {
        return Owner.TARGET;
    }
    
    public List<CallParameter> getParameterList() {
        return Collections.unmodifiableList(parameterList);
    }
    
    public void setParameterList(List<CallParameter> parameterList) {
        this.parameterList = parameterList;
        
        parameterCatalog.clear();
        for (CallParameter parameter : parameterList) {
            parameterCatalog.put(parameter.getName(), parameter);
        }
    }
    
    public CallParameter getParameterByName(String parameterName) {
        CallParameter param = parameterCatalog.get(parameterName);
        if (param != null) {
            param = param.clone();
        }
        return param;
    }
    
}
