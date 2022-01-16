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
package org.jrtech.engines.rules.model.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jrtech.engines.rules.model.BusinessProcessDefinition;
import org.jrtech.engines.rules.model.json.JsonBusinessAttribute;
import org.jrtech.engines.rules.model.json.JsonBusinessProcessDefinition;
import org.jrtech.engines.rules.model.json.JsonRoutingDefinition;
import org.jrtech.engines.rules.model.AbstractNamedObject;
import org.jrtech.engines.rules.model.BusinessAttribute;
import org.jrtech.engines.rules.model.RoutingDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IntegratorEntityUtil {

    private static Logger log = LoggerFactory.getLogger(IntegratorEntityUtil.class);

    public <T> String marshall(ObjectMapper jsonMapper, Collection<T> objectCollection, boolean pretify)
            throws JsonProcessingException {
        if (objectCollection == null) {
            return null;
        }

        if (jsonMapper == null) {
            jsonMapper = new ObjectMapper();
        }
        if (pretify) {
            return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectCollection);
        }

        return jsonMapper.writeValueAsString(objectCollection);
    }

    public <T> List<T> unmarshall(ObjectMapper jsonMapper, String objectCollectionJsonString, Class<T[]> arrayClazz)
            throws JsonParseException, JsonMappingException, IOException {
        if (objectCollectionJsonString == null || objectCollectionJsonString.trim().length() < 2) {
            return null;
        }

        List<T> objectList = new ArrayList<>();
        if ("[]".equals(objectCollectionJsonString.trim())) {
            return objectList;
        }

        return unmarshall(jsonMapper, new StringReader(objectCollectionJsonString), arrayClazz);
    }

    public <T> List<T> unmarshall(ObjectMapper jsonMapper, Reader jsonReader, Class<T[]> arrayClazz)
            throws JsonParseException, JsonMappingException, IOException {
        if (jsonReader == null) {
            return null;
        }

        List<T> objectList = new ArrayList<>();

        if (jsonMapper == null) {
            jsonMapper = new ObjectMapper();
        }

        T[] attributeArray = (T[]) jsonMapper.readValue(jsonReader, arrayClazz);
        if (attributeArray != null) {
            objectList.addAll(Arrays.asList(attributeArray));
        }

        return objectList;
    }

    public String exportBusinessAttribute(ObjectMapper jsonMapper, Collection<BusinessAttribute> attributes,
            boolean pretify) throws JsonProcessingException {
        if (attributes == null) {
            return null;
        }

        if (jsonMapper == null) {
            jsonMapper = new ObjectMapper();
        }

        return marshall(jsonMapper, JsonBusinessAttribute.fromBase(attributes), pretify);
    }

    public List<BusinessAttribute> importBusinessAttribute(ObjectMapper jsonMapper, String jsonString) {
        return importBusinessAttribute(jsonMapper, new StringReader(jsonString));
    }

    public List<BusinessAttribute> importBusinessAttribute(ObjectMapper jsonMapper, Reader jsonStringReader) {
        AbstractImporter<BusinessAttribute> importer = new AbstractImporter<BusinessAttribute>() {
            @Override
            protected List<BusinessAttribute> performSpecificObjectOperation(ObjectMapper jsonMapper,
                    List<BusinessAttribute> objectList, Reader jsonStringReader) {
                try {
                    Collection<JsonBusinessAttribute> jsonObjects = unmarshall(jsonMapper, jsonStringReader,
                            JsonBusinessAttribute[].class);
                    return JsonBusinessAttribute.toBase(jsonObjects);
                } catch (Exception e) {
                    throw createImportException(e, jsonStringReader, "attributes");
                }
            }
        };

        return importer.importObject(jsonMapper, jsonStringReader);
    }

    public String exportBusinessProcessDefinition(ObjectMapper jsonMapper,
                                                  Collection<BusinessProcessDefinition> processDefinitions, boolean pretify) throws JsonProcessingException {
        if (processDefinitions == null) {
            return null;
        }

        if (jsonMapper == null) {
            jsonMapper = new ObjectMapper();
        }

        return marshall(jsonMapper, JsonBusinessProcessDefinition.fromBase(processDefinitions), pretify);
    }

    public List<BusinessProcessDefinition> importBusinessProcessDefinition(ObjectMapper jsonMapper, String jsonString) {
        return importBusinessProcessDefinition(jsonMapper, new StringReader(jsonString));
    }

    public List<BusinessProcessDefinition> importBusinessProcessDefinition(ObjectMapper jsonMapper,
            Reader jsonStringReader) {
        AbstractImporter<BusinessProcessDefinition> importer = new AbstractImporter<BusinessProcessDefinition>() {
            @Override
            protected List<BusinessProcessDefinition> performSpecificObjectOperation(ObjectMapper jsonMapper,
                    List<BusinessProcessDefinition> objectList, Reader jsonStringReader) {
                try {
                    Collection<JsonBusinessProcessDefinition> jsonObjects = unmarshall(jsonMapper, jsonStringReader,
                            JsonBusinessProcessDefinition[].class);
                    return JsonBusinessProcessDefinition.toBase(jsonObjects);
                } catch (Exception e) {
                    throw createImportException(e, jsonStringReader, "business process definitions");
                }
            }
        };

        return importer.importObject(jsonMapper, jsonStringReader);
    }

    public String exportRoutingDefinition(ObjectMapper jsonMapper,
            Collection<RoutingDefinition> routingDefinitions, boolean pretify) throws JsonProcessingException {
        if (routingDefinitions == null) {
            return null;
        }

        if (jsonMapper == null) {
            jsonMapper = new ObjectMapper();
        }

        return marshall(jsonMapper, JsonRoutingDefinition.fromBase(routingDefinitions), pretify);
    }

    public List<RoutingDefinition> importRoutingDefinition(ObjectMapper jsonMapper, String jsonString) {
        return importRoutingDefinition(jsonMapper, new StringReader(jsonString));
    }

    public List<RoutingDefinition> importRoutingDefinition(ObjectMapper jsonMapper,
            Reader jsonStringReader) {
        AbstractImporter<RoutingDefinition> importer = new AbstractImporter<RoutingDefinition>() {
            @Override
            protected List<RoutingDefinition> performSpecificObjectOperation(ObjectMapper jsonMapper,
                    List<RoutingDefinition> objectList, Reader jsonStringReader) {
                try {
                    Collection<JsonRoutingDefinition> jsonObjects = unmarshall(jsonMapper, jsonStringReader,
                            JsonRoutingDefinition[].class);
                    return JsonRoutingDefinition.toBase(jsonObjects);
                } catch (Exception e) {
                    throw createImportException(e, jsonStringReader, "routing definitions");
                }
            }
        };

        return importer.importObject(jsonMapper, jsonStringReader);
    }

    private static RuntimeException createImportException(Exception exception, Reader jsonStringReader,
            String entityName) {
        String jsonString;
        try {
            jsonStringReader.reset();
            jsonString = IOUtils.toString(jsonStringReader);
        } catch (IOException e1) {
            jsonString = "[UNPARSABLE]";
        }
        String errorMessage = "Fail to import " + entityName + ": " + jsonString;
        log.debug(errorMessage, exception);
        return new RuntimeException(errorMessage, exception);
    }

    static abstract class AbstractImporter<T extends AbstractNamedObject> {
        public List<T> importObject(ObjectMapper jsonMapper, Reader jsonStringReader) {
            if (jsonStringReader == null) {
                return null;
            }

            List<T> objectList = new ArrayList<>();

            if (jsonMapper == null) {
                jsonMapper = new ObjectMapper();
            }

            objectList = performSpecificObjectOperation(jsonMapper, objectList, jsonStringReader);

            return objectList;
        }

        protected abstract List<T> performSpecificObjectOperation(ObjectMapper jsonMapper, List<T> objectList,
                Reader jsonStringReader);
    }
}
