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
 * The class <code>DateBeforeFunctionImpl</code> is used for comparing date
 * value as string in the following date formats:
 * <li>ISO Date</li>
 * <li>ISO Datetime</li>
 * <li>YYYY-MM-DD HH:mm:ss</li>
 * <li>YYYY-MM-DD HH:mm:ss.SSS</li>
 * <li>YYYYMMDDHHmmss</li>
 * <li>YYYYMMDDHHmmss.SSS</li> <br>
 * 
 * @version Nov 12, 2015 / jru : initial version <br>
 */
public class DateBeforeFunctionImpl extends AbstractDateFunctionImpl {

	private static final long serialVersionUID = -4152378656766997261L;

	@Override
	protected boolean compareValues(BigDecimal actualDate, BigDecimal expectedDate) {
		return actualDate.subtract(expectedDate).intValue() < 0;
	}

	@Override
	protected String getLogicalOperatorExpression() {
		return "Before";
	}

}
