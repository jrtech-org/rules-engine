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

import java.util.Date;

public interface Auditable {
    public String getCreatedBy();

    public void setCreatedBy(String createdBy);

    public Date getCreationTimestamp();

    public void setCreationTimestamp(Date creationTimestamp);

    public String getModifiedBy();

    public void setModifiedBy(String modifiedBy);

    public Date getModificationTimestamp();

    public void setModificationTimestamp(Date modificationTimestamp);
    
    public String getVersionNumber();
    
    public void setVersionNumber(String versionNumber);

    public String getPreviousVersionId();

    public void setPreviousVersionId(String previousVersionId);
    
}
