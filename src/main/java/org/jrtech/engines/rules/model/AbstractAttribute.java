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
import java.util.Calendar;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.beanutils.BeanUtils;
import org.jrtech.common.utils.InvalidMethodPathExpression;
import org.jrtech.common.utils.ObjectPropertyUtil;
import org.jrtech.engines.rules.ObjectAttributeReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAttribute implements Serializable {

	private static final long serialVersionUID = -6306279283342745772L;

	public static final String KEYWORD_SELF = "${SELF}";
	public static final String KEYWORD_SOURCE_OBJECT = "${SOUCE_OBJECT}";
	public static final String KEYWORD_TARGET_OBJECT = "${TARGET_OBJECT}";
	
	public static final String INTERNAL_VARIABLE_SYSTEM_NOW = "now"; 
    public static final String INTERNAL_VARIABLE_SYSTEM_TODAY = "today"; 
	
	public static final String TAG = "attribute";

	public static final String ATTR_NAME = "name";
	public static final String ATTR_OWNER = "owner";
	public static final String ATTR_READER_CLASS = "readerClass";
	public static final String ATTR_VALUE = "value";

	private static Logger log = LoggerFactory.getLogger(AbstractAttribute.class);

	private String name;

	private String value;

	private Owner owner = Owner.SOURCE;

	private ObjectAttributeReader<Object, Object> reader = null;

	private String readerClass;

	private ObjectPropertyUtil objPropUtil = null;

	public AbstractAttribute(String name, String value, Owner owner) {
		this(name, value, owner, null);
	}

	public AbstractAttribute(String name, String value, Owner owner, String readerClass) {
		super();
		this.name = name;
		this.value = value;
		this.owner = owner;
		this.readerClass = readerClass;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public Owner getOwner() {
		return owner;
	}

	@SuppressWarnings("unchecked")
	public ObjectAttributeReader<Object, Object> getReader() {
		if (reader != null)
			return reader;

		if (readerClass != null && readerClass.trim().length() > 0) {
			try {
				@SuppressWarnings({ "rawtypes" })
				Class<? extends ObjectAttributeReader> clzz = (Class<? extends ObjectAttributeReader>) Class
				        .forName(readerClass);
				reader = clzz.newInstance();

				return reader;
			} catch (Exception e) {
				log.warn("Cannot load interpreter reader: '" + readerClass + "'", e);
			}

		}

		return null;
	}

	public String getReaderClass() {
		return readerClass;
	}

	public void setReaderClass(String readerClass) {
		this.readerClass = readerClass;
	}

	public boolean isVariableValue() {
		if (value == null || value.trim().length() < 1)
			return false;

		int startIndex = value.indexOf("${");
		if (startIndex >= 0) {
			int endIndex =  value.indexOf('}', startIndex + 2);
			if (endIndex > startIndex + 2) {
				return true;
			}
		}

		return false;
	}

	protected <S, T> Object retrieveObjectValue(S sourceObject, T targetObject, String attributeName,
	        Owner attributeOwner) {
		Object workingObject = sourceObject;
		if (Owner.TARGET.equals(attributeOwner)) {
			workingObject = targetObject;
		}
		if (workingObject instanceof Map) {
			Map<?, ?> mapWorkingObject = (Map<?, ?>) workingObject;
			if (mapWorkingObject.containsKey(attributeName)) {
				return mapWorkingObject.get(attributeName);
			}
		} else {
			try {
				return getObjectPropertyUtil().getPropertyValue(workingObject, attributeName,
				        ObjectPropertyUtil.RETRIEVING_METHOD_DEFAULT);
			} catch (InvalidMethodPathExpression e) {
				log.warn("Invalid method: '" + attributeName + "' for object type: '"
				        + workingObject.getClass().getName() + "' for attribute definition: " + this);
			}
		}

		return null;
	}

	private ObjectPropertyUtil getObjectPropertyUtil() {
		if (objPropUtil == null) {
			objPropUtil = ObjectPropertyUtil.getInstance();
		}

		return objPropUtil;
	}

	protected <S, T> String resolveVariableValue(S sourceObject, T targetObject, Map<String, Object> contextVariables,
	        String value) {
		String resolvedValue = value;

		int startPos = resolvedValue.indexOf("${");
		while (startPos >= 0) {
			int endPos = resolvedValue.indexOf("}", startPos + 2);
			String variableName = resolvedValue.substring(startPos + 2, endPos);
			String scope = null;
			if (variableName.indexOf('@') >= 0) {
				scope = variableName.substring(variableName.indexOf('@') + 1);
				variableName = variableName.substring(0, variableName.indexOf('@'));
			}

			String variableValue = null;
			Owner variableOwner = Owner.fromString(scope, getDefaultOwner());
			if (Owner.CONTEXT.equals(variableOwner)) {
				if (contextVariables == null || contextVariables.isEmpty()) {
					variableValue = "";
				} else {
					variableValue = "" + contextVariables.get(variableName);
				}
			} else if (Owner.SYSTEM.equals(variableOwner)) {
				if (INTERNAL_VARIABLE_SYSTEM_NOW.equalsIgnoreCase(variableName)) {
					variableValue = DatatypeConverter.printDateTime(Calendar.getInstance());
				} else if (INTERNAL_VARIABLE_SYSTEM_TODAY.equalsIgnoreCase(variableName)) {
					variableValue = DatatypeConverter.printDate(Calendar.getInstance());
				} else {
					variableValue = "";
				}
			} else if (variableOwner == null) {
				// Default from target
				try {
					variableValue = BeanUtils.getProperty(targetObject, variableName);
				} catch (Exception e) {
					throw new RuntimeException("Fail to resolve variable: '" + variableName + "' on target object.", e);
				}
			} else {
				variableValue = (String) retrieveObjectValue(sourceObject, targetObject, variableName, variableOwner);
			}
			resolvedValue = resolvedValue.substring(0, startPos) + (variableValue == null ? "" : escapeResolvedValue(variableValue))
			        + resolvedValue.substring(endPos + 1);

			startPos = resolvedValue.indexOf("${", endPos + 1);
		}
		return resolvedValue;
	}

	protected String escapeResolvedValue(String resolvedValue) {
	    return resolvedValue;
    }

	protected abstract  Owner getDefaultOwner();

	public static enum Owner {
		SOURCE, TARGET, CONTEXT, SYSTEM;

		public static Owner fromString(String value) {
			return fromString(value, Owner.SOURCE);
		}

		public static Owner fromString(String value, Owner defaultOwner) {
			for (Owner owner : values()) {
				if (owner.name().equalsIgnoreCase(value)) {
					return owner;
				}
			}

			return defaultOwner;
		}
	}

}
