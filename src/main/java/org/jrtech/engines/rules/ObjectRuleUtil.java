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
package org.jrtech.engines.rules;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jrtech.engines.rules.model.RuleCondition;
import org.jrtech.engines.rules.generator.SimpleRuleGenerator;
import org.jrtech.engines.rules.model.AbstractAttribute;
import org.jrtech.engines.rules.model.AbstractAttribute.Owner;
import org.jrtech.engines.rules.model.ConditionalAttribute;
import org.jrtech.engines.rules.model.GoalAttribute;
import org.jrtech.engines.rules.model.ObjectRule;
import org.jrtech.engines.rules.model.StringConditionalAttribute;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ObjectRuleUtil {

    public static final String conditionsToString(ObjectRule objectRule) {
        return conditionsToString(objectRule, false);
    }

    public static final String conditionsToString(ObjectRule objectRule, boolean format) {
        return conditionsToString(objectRule.getConditions(), format);
    }

    public static final String conditionsToString(List<RuleCondition> conditions, boolean format) {
        SimpleRuleGenerator generator = new SimpleRuleGenerator();
        return generator.generateConditions(conditions, format);
    }

    public static final Set<String> collectUsedAttributes(List<ObjectRule> objectRuleList) {
        Set<String> attributeSet = new HashSet<String>();
        
        for (ObjectRule objInt : objectRuleList) {
            attributeSet.addAll(collectUsedAttributes(objInt));
        }
        
        return attributeSet;
    }

    public static final Set<String> collectUsedAttributes(ObjectRule objectRule) {
        Set<String> attributeSet = new HashSet<String>();
        
        for (RuleCondition intCond : objectRule.getConditions()) {
            attributeSet.addAll(collectUsedAttributes(intCond));
        }
        
        return attributeSet;
    }

    public static final Set<String> collectUsedAttributes(RuleCondition condition) {
        Set<String> attributeSet = new HashSet<String>();
        
        for (ConditionalAttribute condAttr : condition.getAttributes()) {
            if (!attributeSet.contains(condAttr.getName())) {
                attributeSet.add(condAttr.getName());
            }
        }
        
        return attributeSet;
    }
    public static final Set<String> collectTargetGoals(List<ObjectRule> objectRuleList) {
        Set<String> goalSet = new HashSet<String>();
        
        for (ObjectRule objInt : objectRuleList) {
            goalSet.addAll(collectTargetGoals(objInt));
        }
        
        return goalSet;
    }

    public static final Set<String> collectTargetGoals(ObjectRule objectRule) {
        Set<String> goalSet = new HashSet<String>();
        
        for (GoalAttribute goalAttr : objectRule.getGoals()) {
            goalSet.add(goalAttr.getName() + "=" + goalAttr.getValue());
        }
        
        return goalSet;
    }

    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule(ObjectRule.class.getSimpleName());
        module.addSerializer(ObjectRule.class, new ObjectRuleSerializer());
        module.addSerializer(GoalAttribute.class, new GoalAttributeSerializer());
        module.addSerializer(RuleCondition.class, new RuleConditionSerializer());
        module.addSerializer(StringConditionalAttribute.class, new StringConditionalAttributeSerializer());

        // module.addDeserializer(ObjectRule.class, new ObjectRuleDeserializer());
        module.addDeserializer(GoalAttribute.class, new GoalAttributeDeserializer());
        // module.addDeserializer(RuleCondition.class, new RuleConditionDeserializer());
        module.addDeserializer(ConditionalAttribute.class, new ConditionalAttributeDeserializer());
        objectMapper.registerModule(module);

        return objectMapper;
    }

    public static class ObjectRuleSerializer extends JsonSerializer<ObjectRule> implements
            Serializable {
        private static final long serialVersionUID = -3167505774907079697L;

        @Override
        public void serialize(ObjectRule value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            jgen.writeStartObject();
            jgen.writeStringField("id", value.getId());
            jgen.writeBooleanField("terminating", value.isTerminating());

            jgen.writeFieldName("goals");
            jgen.writeStartArray();
            for (GoalAttribute goal : value.getGoals()) {
                jgen.writeObject(goal);
            }
            jgen.writeEndArray();

            jgen.writeFieldName("conditions");
            jgen.writeStartArray();
            for (RuleCondition cond : value.getConditions()) {
                jgen.writeObject(cond);
            }
            jgen.writeEndArray();
            jgen.writeEndObject();
        }
    }

    public static class RuleConditionSerializer extends JsonSerializer<RuleCondition> implements
            Serializable {
        private static final long serialVersionUID = -8080799635819864397L;

        @Override
        public void serialize(RuleCondition value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            jgen.writeStartObject();
            jgen.writeFieldName("attributes");
            jgen.writeStartArray();
            for (ConditionalAttribute attr : value.getAttributes()) {
                jgen.writeObject(attr);
            }
            jgen.writeEndArray();
            jgen.writeEndObject();
        }
    }

    public static class RuleConditionDeserializer extends JsonDeserializer<RuleCondition> implements
            Serializable {
        private static final long serialVersionUID = -2909608158975628349L;

        @Override
        public RuleCondition deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
                JsonProcessingException {
            JsonNode jNode = jp.readValueAsTree();

            List<ConditionalAttribute> condAttrList = new ArrayList<>();
            JsonNode attributesNode = jNode.get("attributes");
            if (attributesNode != null) {
                int i = 0;
                while (attributesNode.has(i)) {
                }
            }

            RuleCondition intCond = new RuleCondition(condAttrList);

            return intCond;
        }
    }

    public static abstract class AbstractAttributeSerializer<T extends AbstractAttribute> extends JsonSerializer<T>
            implements Serializable {
        private static final long serialVersionUID = 3213603251818179544L;

        @Override
        public void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                JsonProcessingException {
            jgen.writeStringField("name", value.getName());
            jgen.writeStringField("value", value.getValue());
            jgen.writeStringField("owner", value.getOwner().toString());
            jgen.writeStringField("readerClass", value.getReaderClass());
        }
    }

    public static abstract class AbstractAttributeDeserializer<T extends AbstractAttribute> extends JsonDeserializer<T>
            implements Serializable {

        private static final long serialVersionUID = -8101181816650040177L;

        protected String getName(JsonNode jnode) {
            return getStringAttributeValue(jnode, "name");
        }

        protected String getValue(JsonNode jnode) {
            return getStringAttributeValue(jnode, "value");
        }

        protected String getOwner(JsonNode jnode) {
            return getStringAttributeValue(jnode, "owner");
        }

        protected String getReaderClass(JsonNode jnode) {
            return getStringAttributeValue(jnode, "readerClass");
        }

        protected String getStringAttributeValue(JsonNode jNode, String attributeName) {
            if (!jNode.has(attributeName)) {
                return null;
            }
            try {
                return jNode.get(attributeName).asText();
            } catch (Exception e) {
                // ignore
            }

            return null;
        }

        protected boolean getBooleanAttributeValue(JsonNode jNode, String attributeName) {
            if (!jNode.has(attributeName)) {
                return false;
            }
            try {
                return jNode.get(attributeName).asBoolean();
            } catch (Exception e) {
                // ignore
            }

            return false;
        }
    }

    public static class StringConditionalAttributeSerializer extends AbstractAttributeSerializer<StringConditionalAttribute> {
        private static final long serialVersionUID = 3124697733861107044L;

        @Override
        public void serialize(StringConditionalAttribute value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            jgen.writeStartObject();
            super.serialize(value, jgen, provider);
            jgen.writeBooleanField("negate", value.isNegated());
            jgen.writeBooleanField("caseSensitive", value.isCaseSensitive());
            jgen.writeBooleanField("regex", value.isRegex());
            jgen.writeEndObject();
        }
    }

    public static class ConditionalAttributeDeserializer extends AbstractAttributeDeserializer<StringConditionalAttribute> {
        private static final long serialVersionUID = 8957648704082629483L;

        @Override
        public StringConditionalAttribute deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
                JsonProcessingException {
            JsonNode jNode = jp.readValueAsTree();

            StringConditionalAttribute condAttr = new StringConditionalAttribute(getName(jNode), getValue(jNode),
                    getBooleanAttributeValue(jNode, "negate"), Owner.fromString(getOwner(jNode)));
            condAttr.setReaderClass(getReaderClass(jNode));
            condAttr.setOperatorFunction(jNode.get("operatorFunction") == null ? null : jNode.get("operatorFunction").asText());

            return condAttr;
        }
    }

    public static class GoalAttributeSerializer extends AbstractAttributeSerializer<GoalAttribute> {
        private static final long serialVersionUID = 2796684264865260454L;

        @Override
        public void serialize(GoalAttribute value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                JsonProcessingException {
            jgen.writeStartObject();
            super.serialize(value, jgen, provider);
            jgen.writeStringField("method", value.getMethod());
            jgen.writeStringField("writerClass", value.getWriterClass());
            jgen.writeEndObject();
        }
    }

    public static class GoalAttributeDeserializer extends AbstractAttributeDeserializer<GoalAttribute> {
        private static final long serialVersionUID = -7852063758490067254L;

        @Override
        public GoalAttribute deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
                JsonProcessingException {
            JsonNode jNode = jp.readValueAsTree();

            GoalAttribute goalAttr = new GoalAttribute(getName(jNode), getValue(jNode), getStringAttributeValue(jNode,
                    "method"), Owner.fromString(getOwner(jNode)));

            goalAttr.setReaderClass(getReaderClass(jNode));
            goalAttr.setWriterClass(getStringAttributeValue(jNode, "writerClass"));

            return goalAttr;
        }

    }
}
