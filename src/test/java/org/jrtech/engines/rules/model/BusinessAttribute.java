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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BusinessAttribute extends AbstractStateManagedObject {

    private static final long serialVersionUID = 5532152370341650809L;

    private static Logger log = LoggerFactory.getLogger(BusinessAttribute.class);
    
    public static final String DATA_TYPE_BOOLEAN = "Boolean";
    
    public static final String DATA_TYPE_DATE = "Date";
    
    public static final String DATA_TYPE_DATETIME = "Datetime";
    
    public static final String DATA_TYPE_DECIMAL = "Decimal";
    
    public static final String DATA_TYPE_INTEGER = "Integer";
    
    public static final String DATA_TYPE_STRING = "String";
    
    public static final String DEFAULT_DATA_TYPE = DATA_TYPE_STRING;
    
    public static final String NAME_ALLOWED_CHARACTERS_STRING = INamedObject.BASE_ALLOWED_CHARACTERS_STRING + " ";
    
    private static final ConcurrentSkipListMap<String, Class<?>> DATA_TYPE_CATALOG = new ConcurrentSkipListMap<>();
    
    static {
        DATA_TYPE_CATALOG.put(DATA_TYPE_BOOLEAN, java.lang.Boolean.class);
        DATA_TYPE_CATALOG.put(DATA_TYPE_DATE, java.util.Date.class);
        DATA_TYPE_CATALOG.put(DATA_TYPE_DATETIME, java.util.Date.class);
        DATA_TYPE_CATALOG.put(DATA_TYPE_DECIMAL, java.math.BigDecimal.class);
        DATA_TYPE_CATALOG.put(DATA_TYPE_INTEGER, java.math.BigInteger.class);
        DATA_TYPE_CATALOG.put(DATA_TYPE_STRING, java.lang.String.class);
    }

    private String type;
    
    private String labelKey;
    
    private String defaultLabel;
    
    private String widgetCreator;
    
    private String extendedAttributesString;
    
    private transient Map<String, Object> extendedAttributeMap;

    public BusinessAttribute() {
        super();
        
        super.setObjectType(getClass().getSimpleName());
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabelKey() {
        return labelKey;
    }

    public void setLabelKey(String labelKey) {
        this.labelKey = labelKey;
    }

    public String getDefaultLabel() {
        return defaultLabel;
    }

    public void setDefaultLabel(String defaultLabel) {
        this.defaultLabel = defaultLabel;
    }

    public String getWidgetCreator() {
        return widgetCreator;
    }

    public void setWidgetCreator(String widgetCreator) {
        this.widgetCreator = widgetCreator;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((defaultLabel == null) ? 0 : defaultLabel.hashCode());
        result = prime * result + ((labelKey == null) ? 0 : labelKey.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((widgetCreator == null) ? 0 : widgetCreator.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        BusinessAttribute other = (BusinessAttribute) obj;
        if (defaultLabel == null) {
            if (other.defaultLabel != null)
                return false;
        } else if (!defaultLabel.equals(other.defaultLabel))
            return false;
        if (labelKey == null) {
            if (other.labelKey != null)
                return false;
        } else if (!labelKey.equals(other.labelKey))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (widgetCreator == null) {
            if (other.widgetCreator != null)
                return false;
        } else if (!widgetCreator.equals(other.widgetCreator))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return getName() + " (" + type + ")";
    }

    public String getExtendedAttributesString() {
        return extendedAttributesString;
    }

    public void setExtendedAttributesString(String extendedAttributesString) {
        this.extendedAttributesString = extendedAttributesString;
    }

    public Map<String, Object> getExtendedAttributeMap() {
        if (extendedAttributeMap == null) {
            extendedAttributeMap = new TreeMap<>();
        }
        if (extendedAttributeMap.isEmpty() && extendedAttributesString != null && extendedAttributesString.length() > 0) {
            // Load from extendedAttributesString
            ObjectMapper mapper = new ObjectMapper();
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> tempMap = mapper.readValue(extendedAttributesString, TreeMap.class);
                extendedAttributeMap.putAll(tempMap);
            } catch (Exception e) {
                log.info("Failure to parse extended attributes JSON for Business Attribute: " + getName(), e);
            }
        }
        
        return extendedAttributeMap;
    }
    
    public void syncExtendedAttributeMapToString() {
        if (extendedAttributeMap == null) {
            extendedAttributesString = null;
            return; // Nothing to sync -> skip
        }

        // Load from extendedAttributesString
        ObjectMapper mapper = new ObjectMapper();
        try {
            extendedAttributesString = mapper.writeValueAsString(extendedAttributeMap);
        } catch (Exception e) {
            log.info("Fail to synchronize extended attributes to JSON string for Business Attribute: " + getName(), e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getExtendedAttributeValue(String attributeName) {
        return (T) getExtendedAttributeMap().get(attributeName);
    }
    
    public static final Collection<String> getDataTypes() {
        return Collections.unmodifiableCollection(DATA_TYPE_CATALOG.keySet());
    }
    
    public static final Class<?> getDataTypeClass(String dataType) {
        if (dataType == null || !DATA_TYPE_CATALOG.containsKey(dataType)) {
            return null;
        }
        return DATA_TYPE_CATALOG.get(dataType);
    }
    
}
