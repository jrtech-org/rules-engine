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
package org.jrtech.engines.rules.model.json;

import org.jrtech.engines.rules.model.AbstractStateManagedObject;
import org.jrtech.engines.rules.model.AbstractNamedObject;
import org.jrtech.engines.rules.model.AbstractRule;
import org.jrtech.engines.rules.model.BusinessAttribute;
import org.jrtech.engines.rules.model.BusinessProcessDefinition;
import org.jrtech.engines.rules.model.RoutingDefinition;

public class JsonObjectCopyUtil {

    public static final void copyValue(AbstractNamedObject source, AbstractNamedObject target) {
        target.setCreatedBy(source.getCreatedBy());
        target.setCreationTimestamp(source.getCreationTimestamp());
        target.setDescription(source.getDescription());
        target.setModificationTimestamp(source.getModificationTimestamp());
        target.setModifiedBy(source.getModifiedBy());
        target.setModule(source.getModule());
        target.setName(source.getName());
        target.setObjectId(source.getObjectId());
        target.setObjectType(source.getObjectType());
        target.setOwner(source.getOwner());
        target.setScopingContainer(source.getScopingContainer());
        target.setTags(source.getTags());
    }

    public static final void copyValue(AbstractStateManagedObject source, AbstractStateManagedObject target) {
        copyValue((AbstractNamedObject) source, (AbstractNamedObject) target);
        
        target.setApproverList(source.getApproverList());
        target.setPreviousVersionId(source.getPreviousVersionId());
        target.setState(source.getState());
        target.setVersionNumber(source.getVersionNumber());
    }

    public static final void copyValue(AbstractRule source, AbstractRule target) {
        copyValue((AbstractStateManagedObject) source, (AbstractStateManagedObject) target);
        
        target.setOrgId(source.getOrgId());
        target.setRuntimeApplicationName(source.getRuntimeApplicationName());
    }
    
    public static final void copyValue(BusinessAttribute source, BusinessAttribute target) {
        copyValue((AbstractStateManagedObject) source, (AbstractStateManagedObject) target);
        
        target.setDefaultLabel(source.getDefaultLabel());
        target.setExtendedAttributesString(source.getExtendedAttributesString());
        target.setLabelKey(source.getLabelKey());
        target.setWidgetCreator(source.getWidgetCreator());
        target.setType(source.getType());
    }
    
    public static final void copyValue(BusinessProcessDefinition source, BusinessProcessDefinition target) {
        copyValue((AbstractStateManagedObject) source, (AbstractStateManagedObject) target);

        // Parameters + Logic is inside the definition
        target.setDefinition(source.getDefinition());
        target.setDependentsString(source.getDependentsString());
        target.setPreConditions(source.getPreConditions());
        target.setPostConditions(source.getPostConditions());
    }
    
    public static final void copyValue(RoutingDefinition source, RoutingDefinition target) {
        copyValue((AbstractRule) source, (AbstractRule) target);
        
        // Parameters + Logic is inside the definition
        target.setDefinition(source.getDefinition());
        target.setDependentsString(source.getDependentsString());
        target.setPreConditions(source.getPreConditions());
        target.setPostConditions(source.getPostConditions());
    }
}
