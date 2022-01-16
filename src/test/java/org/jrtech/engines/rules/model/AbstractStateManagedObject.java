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

public abstract class AbstractStateManagedObject extends AbstractNamedObject implements Approvable, Serializable {

    private static final long serialVersionUID = -3681207906353579673L;

    private String state;

    private String previousVersionId;

    private String approverList;

    private String versionNumber;

    @Override
    public String getState() {
        return state;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String getApproverList() {
        return approverList;
    }

    @Override
    public void setApproverList(String approverList) {
        this.approverList = approverList;
    }

    @Override
    public String getPreviousVersionId() {
        return previousVersionId;
    }

    @Override
    public void setPreviousVersionId(String previousVersionId) {
        this.previousVersionId = previousVersionId;
    }

    @Override
    public String getVersionNumber() {
        return versionNumber;
    }

    @Override
    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

}
