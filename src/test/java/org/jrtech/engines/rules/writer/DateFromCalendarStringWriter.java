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
package org.jrtech.engines.rules.writer;

import java.util.Calendar;

import javax.xml.bind.DatatypeConverter;

import org.jrtech.engines.rules.ObjectAttributeWriter;
import org.jrtech.engines.rules.model.AbstractStateManagedObject;
import org.jrtech.common.utils.LocaleUtils;
import org.jrtech.common.utils.ObjectPropertyUtil;

public class DateFromCalendarStringWriter implements ObjectAttributeWriter<AbstractStateManagedObject, String> {

    private static final long serialVersionUID = -8094037081564602027L;

    @Override
    public boolean write(AbstractStateManagedObject targetObject, String attributeName, String value) {
        Calendar cal = Calendar.getInstance(LocaleUtils.TIMEZONE_UTC);
        if (value != null) {
            try {
                cal = DatatypeConverter.parseDateTime(value);
            } catch (Exception e) {
                cal = Calendar.getInstance(LocaleUtils.TIMEZONE_UTC);
            }
        }
        ObjectPropertyUtil.getInstance().setPropertyValue(targetObject, attributeName, cal.getTime());
        return true;
    }

}
