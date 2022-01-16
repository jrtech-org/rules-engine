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

/**
 * The class <code>TimeSinceFunctionImpl</code> is used for comparing time value
 * as string in the following time formats:
 * <li>HH:mm:ss</li>
 * <li>HH:mm:ss.SSS</li>
 * <li>HHmmss</li>
 * <li>HHmmss.SSS</li> <br>
 * 
 * @version Nov 12, 2015 / jru : initial version <br>
 */
public class TimeSinceFunctionImpl extends AbstractTimeFunctionImpl {

	private static final long serialVersionUID = 5195111614194670821L;

	@Override
	protected boolean compareValues(BigDecimal actualTime, BigDecimal expectedTime) {
		return actualTime.subtract(expectedTime).intValue() >= 0;
	}

	@Override
	protected String getLogicalOperatorExpression() {
		return "Since";
	}

}
