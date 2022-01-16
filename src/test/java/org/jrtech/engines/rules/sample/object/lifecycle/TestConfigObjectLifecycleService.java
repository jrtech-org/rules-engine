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
package org.jrtech.engines.rules.sample.object.lifecycle;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.jrtech.common.utils.DatetimeTextParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestConfigObjectLifecycleService {

    public static final String EXCEPTION_RESULT = "[EXCEPTION_RESULT]";

    private AbstractConfigObjectLifecycleService<SampleConfigObject> service;
    
    private NumberFormat nf = NumberFormat.getIntegerInstance();

    @Before
    public void init() {
        long startTime = System.currentTimeMillis();
        service = new AbstractConfigObjectLifecycleService<SampleConfigObject>() {
            @Override
            public SampleConfigObject createInstance() {
                return new SampleConfigObject();
            }
        };
        
        Map<String, Object> contextData = new HashMap<>();
        contextData.put(SampleContextData.USER, "user1@org");
        contextData.put(SampleContextData.USER_ORGANIZATION, "org");
        service.setContextData(contextData);
        System.out.println("Initialization: " + nf.format(System.currentTimeMillis() - startTime) + " ms.");
    }

    @Test
    public void testRetrievePossibleNextActions() {
        Object[][] inputs = new Object[][] {
            // @formatter:off
            new Object[] { "New", new TreeSet<String>(Arrays.asList(StringUtils.split("save", ","))) },
            new Object[] { "Draft", new TreeSet<String>(Arrays.asList(StringUtils.split("approve,save", ","))) },
            new Object[] { "Approving", new TreeSet<String>(Arrays.asList(StringUtils.split("approve,reject", ","))) },
            new Object[] { "Approved", new TreeSet<String>(Arrays.asList(StringUtils.split("archive,deploy,reject", ","))) },
            new Object[] { "Deployed", new TreeSet<String>(Arrays.asList(StringUtils.split("archive,deploy", ","))) },
            new Object[] { "Archived", new TreeSet<String>() },
            // @formatter:on
        };
        for (Object[] input : inputs) {
            String state = (String) input[0];
            @SuppressWarnings("unchecked")
            Set<String> expectedActionSet = (Set<String>) input[1];
            long startTime = System.currentTimeMillis();
            Set<String> actualActionSet = service.retrievePossibleNextActions(
                    SampleConfigObject.class.getSimpleName(), state);
            System.out.println("Possible Actions for " + state + ": " + nf.format(System.currentTimeMillis() - startTime) + " ms.");
            Assert.assertNotNull(actualActionSet);
            Assert.assertEquals(expectedActionSet, actualActionSet);
        }
    }

    @Test
    public void saveObject() {
        long startTime = System.currentTimeMillis();
        String[][] inputs = new String[][] {
// @formatter:off
            { "New" , "Draft"},
            { "Draft" , "Draft"},
            { "Approving" , EXCEPTION_RESULT},
            { "Approved" , EXCEPTION_RESULT},
            { "Deployed" , EXCEPTION_RESULT},
            { "Archived" , EXCEPTION_RESULT},
            // @formatter:on
        };
        DatetimeTextParser dtp = new DatetimeTextParser();

        for (String[] input : inputs) {
            String originalState = input[0]; 

            SampleConfigObject appvb = service.createInstance();
            appvb.setObjectId("001");
            appvb.setState(originalState);

            try {
                appvb = service.save(appvb);
                Assert.assertEquals(input[1], appvb.getState());
                System.out.println("Save action changes lifecycle from State: [" + originalState + "] to State: ["
                        + appvb.getState()
                        + "] at: ["
                        + dtp.getStringFromDateTime(appvb.getModificationTimestamp(), Locale.GERMAN,
                                TimeZone.getDefault()) + "] by: [" + appvb.getModifiedBy() + "]");
            } catch (Exception e) {
                if (!EXCEPTION_RESULT.equals(input[1])) {
                    Assert.assertTrue(false);
                } else {
                    System.out.println("Properly captured: Invalid original State: [" + originalState + "] for save action");
                }
            }
        }
        System.out.println("Save Object: " + nf.format(System.currentTimeMillis() - startTime) + " ms.");
    }
    
    @Test
    public void approveObject() {
        long startTime = System.currentTimeMillis();
        String[][] inputs = new String[][] {
            // @formatter:off
            // Array shall contain the following values:
            // { [FROM_STATE], [TO_STATE or EXCEPTION], [APPROVER_LIST_CONTENT] }
            { "Draft" , "Approving"},
            { "Approving" , "Approved", "user2@org1"},
            { "Approved" , EXCEPTION_RESULT},
            { "Deployed" , EXCEPTION_RESULT},
            { "Archived" , EXCEPTION_RESULT},
            // @formatter:on
        };
        DatetimeTextParser dtp = new DatetimeTextParser();

        for (String[] input : inputs) {
            String originalState = input[0]; 

            SampleConfigObject appvb = service.createInstance();
            appvb.setObjectId("001");
            appvb.setState(originalState);
            if (input.length > 2) {
                appvb.setApproverList(input[2]);
            }

            try {
                appvb = service.approve(appvb);
                Assert.assertEquals(input[1], appvb.getState());
                System.out.println("Approve action changes lifecycle from State: [" + originalState + "] to State: ["
                        + appvb.getState()
                        + "] at: ["
                        + dtp.getStringFromDateTime(appvb.getModificationTimestamp(), Locale.GERMAN,
                                TimeZone.getDefault()) + "] by: [" + appvb.getModifiedBy() + "] ApproverList:[" + appvb.getApproverList() + "]");
            } catch (Exception e) {
                if (!EXCEPTION_RESULT.equals(input[1])) {
                    e.printStackTrace();
                    Assert.assertTrue(false);
                } else {
                    System.out.println("Properly captured: Invalid original State: [" + originalState + "] for approve action");
                }
            }
        }
        System.out.println("Approve Object: " + nf.format(System.currentTimeMillis() - startTime) + " ms.");
    }
}
