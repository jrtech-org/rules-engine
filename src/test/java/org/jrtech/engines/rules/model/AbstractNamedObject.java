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
import java.util.Date;

public abstract class AbstractNamedObject implements Serializable {

    private static final long serialVersionUID = 6660170748388258604L;

    private String objectId;

    private String objectType;

    private String name;

    private String description;

    private String owner;

    private String module;

    private String tags;

    private String createdBy;

    private Date creationTimestamp;

    private String modifiedBy;

    private Date modificationTimestamp;
    
    private Package scopingContainer;
    
    public AbstractNamedObject() {
        super();
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModificationTimestamp() {
        return modificationTimestamp;
    }

    public void setModificationTimestamp(Date modificationTimestamp) {
        this.modificationTimestamp = modificationTimestamp;
    }

    public Package getScopingContainer() {
        return scopingContainer;
    }

    public void setScopingContainer(Package scopingContainer) {
        this.scopingContainer = scopingContainer;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime
				* result
				+ ((creationTimestamp == null) ? 0 : creationTimestamp
						.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime
				* result
				+ ((modificationTimestamp == null) ? 0 : modificationTimestamp
						.hashCode());
		result = prime * result
				+ ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
		result = prime * result + ((module == null) ? 0 : module.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((objectId == null) ? 0 : objectId.hashCode());
		result = prime * result
				+ ((objectType == null) ? 0 : objectType.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime
				* result
				+ ((scopingContainer == null) ? 0 : scopingContainer.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		return result;
	}

    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractNamedObject other = (AbstractNamedObject) obj;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (creationTimestamp == null) {
			if (other.creationTimestamp != null)
				return false;
		} else if (!creationTimestamp.equals(other.creationTimestamp))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (modificationTimestamp == null) {
			if (other.modificationTimestamp != null)
				return false;
		} else if (!modificationTimestamp.equals(other.modificationTimestamp))
			return false;
		if (modifiedBy == null) {
			if (other.modifiedBy != null)
				return false;
		} else if (!modifiedBy.equals(other.modifiedBy))
			return false;
		if (module == null) {
			if (other.module != null)
				return false;
		} else if (!module.equals(other.module))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (objectId == null) {
			if (other.objectId != null)
				return false;
		} else if (!objectId.equals(other.objectId))
			return false;
		if (objectType == null) {
			if (other.objectType != null)
				return false;
		} else if (!objectType.equals(other.objectType))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (scopingContainer == null) {
			if (other.scopingContainer != null)
				return false;
		} else if (!scopingContainer.equals(other.scopingContainer))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		return true;
	}

    @Override
    public String toString() {
        return name + " - " + objectId;
    }
    
}
