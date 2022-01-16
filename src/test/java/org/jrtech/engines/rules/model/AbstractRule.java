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

public abstract class AbstractRule extends AbstractStateManagedObject {
	
	private static final long serialVersionUID = 8185985303418925727L;

    private transient String orgId;
	
	private transient String runtimeApplicationName;
	
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public void setRuntimeApplicationName(String runtimeApplication) {
		this.runtimeApplicationName = runtimeApplication;
	}
	
	public String getRuntimeApplicationName() {
		return this.runtimeApplicationName;
	}
	
	protected abstract String getRuleType();
	
	public void createName(String application) {
		this.setName(this.getRuleType() + application + "-" + this.getOwner());
	}
}
