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
package org.jrtech.engines.rules.generator.condition;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.jrtech.engines.rules.model.ConditionalAttribute;

public abstract class AbstractConditionalAttributeGenerator<T extends ConditionalAttribute> implements Serializable {

    private static final long serialVersionUID = 6009321296984230173L;

    public static final String CONTEXT_ATTRIBUTE_DECLARATION_CATALOG = "attributeDeclarationCatalog";

    public static final String CONTEXT_PHYSICAL_NAME_CATALOG = "physicalNameCatalog";

    public static final String CONTEXT_RELATED_ATTRIBUTE_CATALOG = "relatedAttributeCatalog";

    protected static final ConcurrentMap<String, Class<? extends AbstractFunctionExpressionGenerator>> FUNCTION_EXPPRESSION_GENERATOR_CATALOG = new ConcurrentHashMap<>();

    private Map<String, AbstractFunctionExpressionGenerator> functionExpressionGeneratorInstanceCatalog = new HashMap<>();

    private Map<String, Object> generatorContextData;
    
    protected String generatePhysicalName(String logicalName) {
        String physicalName = logicalName;
        physicalName = StringUtils.remove(physicalName, ' ');
        physicalName = physicalName.substring(0, 1).toLowerCase() + physicalName.substring(1);
        return physicalName;
    }

    public Map<String, Object> getGeneratorContextData() {
        return generatorContextData;
    }

    public void setGeneratorContextData(Map<String, Object> generatorContext) {
        this.generatorContextData = generatorContext;
    }

    public Map<String, String> getPhysicalNameCatalog() {
        if (generatorContextData == null) {
            throw new NullGeneratorContextDataException();
        }

        @SuppressWarnings("unchecked")
        Map<String, String> physicalNameCatalog = (Map<String, String>) generatorContextData
                .get(CONTEXT_PHYSICAL_NAME_CATALOG);
        if (physicalNameCatalog == null) {
            physicalNameCatalog = new TreeMap<>();
            generatorContextData.put(CONTEXT_PHYSICAL_NAME_CATALOG, physicalNameCatalog);
        }

        return physicalNameCatalog;
    }

    public Map<String, String> getAttributeDeclarationCatalog() {
        if (generatorContextData == null) {
            throw new NullGeneratorContextDataException();
        }

        @SuppressWarnings("unchecked")
        Map<String, String> attrDeclCatalog = (Map<String, String>) generatorContextData
                .get(CONTEXT_ATTRIBUTE_DECLARATION_CATALOG);
        if (attrDeclCatalog == null) {
            attrDeclCatalog = new TreeMap<>();
            generatorContextData.put(CONTEXT_ATTRIBUTE_DECLARATION_CATALOG, attrDeclCatalog);
        }

        return attrDeclCatalog;
    }

    protected void registerAttributeDeclaration(String logicalAttributeName, String attributeDeclarationString) {
        if (logicalAttributeName == null || logicalAttributeName.length() < 1 || attributeDeclarationString == null
                || attributeDeclarationString.length() < 1) {
            return; // ignore
        }

        Map<String, String> catalog = getAttributeDeclarationCatalog();
        if (!catalog.containsKey(logicalAttributeName)) {
            catalog.put(logicalAttributeName, attributeDeclarationString);
        }
    }

    public Set<String> getRelatedAttributeCatalog() {
        if (generatorContextData == null) {
            throw new NullGeneratorContextDataException();
        }

        @SuppressWarnings("unchecked")
        Set<String> relatedAttributeCatalog = (Set<String>) generatorContextData.get(CONTEXT_RELATED_ATTRIBUTE_CATALOG);
        if (relatedAttributeCatalog == null) {
            relatedAttributeCatalog = new TreeSet<>();
            generatorContextData.put(CONTEXT_RELATED_ATTRIBUTE_CATALOG, relatedAttributeCatalog);
        }

        return relatedAttributeCatalog;
    }

    protected void registerRelatedAttribute(String attributeName) {
        if (attributeName == null) {
            return; // ignore
        }

        Set<String> catalog = getRelatedAttributeCatalog();
        if (!catalog.contains(attributeName)) {
            catalog.add(attributeName);
        }
    }

    public String generatePhysicalName(T attribute) {
        String logicalName = attribute.getName();

        String physicalName = getPhysicalNameCatalog().get(logicalName);

        if (physicalName != null) {
            return physicalName; // return value from existing catalog
        }

        physicalName = generatePhysicalName(logicalName);
        
        getPhysicalNameCatalog().put(logicalName, physicalName);

        return physicalName;
    }

    protected AbstractFunctionExpressionGenerator getFunctionExpressionGeneratorInstance(String scopedFunctionName) {
        if (functionExpressionGeneratorInstanceCatalog.containsKey(scopedFunctionName)) {
            return functionExpressionGeneratorInstanceCatalog.get(scopedFunctionName);
        }

        Class<? extends AbstractFunctionExpressionGenerator> functionExpressionGeneratorClass = (Class<? extends AbstractFunctionExpressionGenerator>) FUNCTION_EXPPRESSION_GENERATOR_CATALOG
                .get(scopedFunctionName);
        if (functionExpressionGeneratorClass == null) {
            functionExpressionGeneratorInstanceCatalog.put(scopedFunctionName, null);
            return null;
        }
        AbstractFunctionExpressionGenerator functionExpressionGeneratorInstance = null;
        try {
            functionExpressionGeneratorInstance = functionExpressionGeneratorClass.newInstance();
            functionExpressionGeneratorInstanceCatalog.put(scopedFunctionName, functionExpressionGeneratorInstance);
        } catch (Exception e) {
            functionExpressionGeneratorInstanceCatalog.put(scopedFunctionName, null);
            return null;
        }

        return functionExpressionGeneratorInstance;
    }
    
    /**
     * Generates positive function expression.<br>
     * 
     * @param attribute
     * @return
     */
    public String generateFunctionExpression(T attribute) {
        AbstractFunctionExpressionGenerator functionExpressionGeneratorInstance = getFunctionExpressionGeneratorInstance(getGeneratorPrefix() + attribute.getOperatorFunction());
        if (functionExpressionGeneratorInstance == null) {
            functionExpressionGeneratorInstance = getFunctionExpressionGeneratorInstance(getGeneratorPrefix() + attribute.getDefaultOperatorFunctionName());
        }
        
        String attributePhysicalName = generatePhysicalName(attribute);
        
        if (functionExpressionGeneratorInstance == null) {
            return attributePhysicalName + " = " + attribute.getValue();
        }
        
        String expression = functionExpressionGeneratorInstance.generate(attributePhysicalName, attribute.getValue());
        return expression;
    }
    
    protected String removeLeadingAndTrailingQuotes(String fieldValue) {
        if (fieldValue.startsWith("\"")) {
            fieldValue = fieldValue.substring(1);
        }
        if (fieldValue.endsWith("\"")) {
            fieldValue = fieldValue.substring(0, fieldValue.length() - 1);
        }

        return fieldValue;
    }
    
    protected abstract String getGeneratorPrefix();
    
    public static abstract class AbstractFunctionExpressionGenerator {
        public abstract String generate(String attributePhysicalName, String attributeValue);
    }

}
