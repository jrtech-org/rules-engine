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
package org.jrtech.engines.rules.function;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

/**
 * The class <code>AbstractTimeFunctionImpl</code> is a common defined class for
 * time function implementation for comparing time value as string in the
 * following time formats:
 * <li>HH:mm:ss</li>
 * <li>HH:mm:ss.SSS</li>
 * <li>HHmmss</li>
 * <li>HHmmss.SSS</li> <br>
 * 
 * @version Nov 12, 2015 / jru : initial version <br>
 */
public abstract class AbstractTimeFunctionImpl extends AbstractInternalFunctionImpl {

	private static final long serialVersionUID = -3470415496522230712L;

	@Override
	public boolean match(Object actualValue, Object expectedValue) {
		if (actualValue == null && expectedValue == null) {
			// No expectation -> match!
			return true;
		} else if ((actualValue != null && expectedValue == null) || actualValue == null && expectedValue != null) {
			return false;
		}

		BigDecimal actualTime = new BigDecimal(cleanupTimeSeparators("" + actualValue));
		BigDecimal expectedTime = new BigDecimal(cleanupTimeSeparators("" + expectedValue));
		
		return compareValues(actualTime, expectedTime);
	}

	@Override
	public String createLogicalExpression(String name, String value) {
		return name + " " + getLogicalOperatorExpression() + " " + value;
	}

    public static final String cleanupTimeSeparators(String isoDatetime) {
    	return StringUtils.remove(isoDatetime, ':');
    }
    
	protected abstract boolean compareValues(BigDecimal actualTime, BigDecimal expectedTime);
	
	protected abstract String getLogicalOperatorExpression();
}
