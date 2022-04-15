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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.jrtech.engines.rules.model.AbstractStateManagedObject;
import org.jrtech.engines.rules.model.GoalApplicationException;
import org.jrtech.engines.rules.model.Result;
import org.jrtech.engines.rules.model.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;


public abstract class AbstractConfigObjectLifecycleService<T extends AbstractStateManagedObject> {
    
    private static Logger logger = LoggerFactory.getLogger(AbstractConfigObjectLifecycleService.class);

//    private static final String STATE_TABLE_CACHE_NAME = "configObjectStateTable";
//
//    private static final String STATE_ACTIONS_TABLE_CACHE_NAME = "configObjectStateActionsTable";

    private static final String DEFAULT_STATE_TABLE_KEY = "Default";

    private static final String DEFAULT_STATE_TABLE_FILE = "/data/sample-config-object-lifecycle.xml";

    public static final String ACTION_APPROVE = "approve";

    public static final String ACTION_ARCHIVE = "archive";

    public static final String ACTION_DELETE = "delete";

    public static final String ACTION_DEPLOY = "deploy";

    public static final String ACTION_REJECT = "reject";

    public static final String ACTION_SAVE = "save";

    public static final String STATE_APPROVED = "Approved";

    public static final String STATE_APPROVING = "Approving";

    public static final String STATE_ARCHIVED = "Archived";

    public static final String STATE_DEPLOYED = "Deployed";

    public static final String STATE_DRAFT = "Draft";

    public static final String STATE_NEW = "New";

    public static final ConcurrentSkipListSet<String> EDITABLE_STATES = new ConcurrentSkipListSet<>();

    private static final ConcurrentMap<String, Long> ENTITY_STATE_TABLE_LAST_TS = new ConcurrentHashMap<>();

    private static final ConcurrentMap<String, String> ENTITY_SPECIFIC_STATE_TABLE_FILE = new ConcurrentHashMap<>();
    
    private static final Cache<String, ConcurrentMap<String, List<ConfigObjectLifecycleRule<AbstractStateManagedObject>>>> STATE_TABLE_CACHE = createStateTableCache();
    private static final Cache<String, ConcurrentMap<String, Set<String>>> STATE_ACTIONS_TABLE_CACHE = createStateActionsTableCache();
    
    static {
        // ENTITY_SPECIFIC_STATE_TABLE_FILE.put(Package.class.getSimpleName(), "");
        EDITABLE_STATES.add(STATE_NEW);
        EDITABLE_STATES.add(STATE_DRAFT);
    }

    private Map<String, Object> contextData = null;

    protected static <T extends AbstractStateManagedObject> Cache<String, ConcurrentMap<String, List<ConfigObjectLifecycleRule<T>>>> createStateTableCache() {
    	Cache<String, ConcurrentMap<String, List<ConfigObjectLifecycleRule<T>>>> cache = CacheUtil.createCache(1000, 10, TimeUnit.MINUTES);
        return cache;
    }

    protected static Cache<String, ConcurrentMap<String, Set<String>>> createStateActionsTableCache() {
    	Cache<String, ConcurrentMap<String, Set<String>>> cache = CacheUtil.createCache(1000, 10, TimeUnit.MINUTES);
        return cache;
    }

    protected Cache<String, ConcurrentMap<String, List<ConfigObjectLifecycleRule<AbstractStateManagedObject>>>> getStateTableCache() {
        return STATE_TABLE_CACHE;
    }

    protected Cache<String, ConcurrentMap<String, Set<String>>> getStateActionsTableCache() {
        return STATE_ACTIONS_TABLE_CACHE;
    }

    protected ConcurrentMap<String, List<ConfigObjectLifecycleRule<AbstractStateManagedObject>>> retrieveStateTable(String entityName) {
        ConcurrentMap<String, List<ConfigObjectLifecycleRule<AbstractStateManagedObject>>> stateTable = null;
        if (ENTITY_SPECIFIC_STATE_TABLE_FILE.containsKey(entityName)) {
            // Life-cycle interpreter cache is stored per entity
            stateTable = STATE_TABLE_CACHE.getIfPresent(entityName);
        } else {
            stateTable = STATE_TABLE_CACHE.getIfPresent(DEFAULT_STATE_TABLE_KEY); 
        }

        if (stateTable == null) {
            if (ENTITY_SPECIFIC_STATE_TABLE_FILE.containsKey(entityName)) {
                Long entityStateTableLastTimestamp = ENTITY_STATE_TABLE_LAST_TS.get(entityName);
                if (entityStateTableLastTimestamp == null
                        || System.currentTimeMillis() - entityStateTableLastTimestamp > 500000) {
                    // new or 5 minutes is over -> Try to load from entity specific config.xml

                    ENTITY_STATE_TABLE_LAST_TS.put(entityName, System.currentTimeMillis());
                }
            }
            if (stateTable == null) {
                // Get common state table
            	stateTable = (ConcurrentMap<String, List<ConfigObjectLifecycleRule<AbstractStateManagedObject>>>) STATE_TABLE_CACHE.getIfPresent(DEFAULT_STATE_TABLE_KEY);
            }

            if (stateTable == null) {
                // Load state table from default
                try {
                	stateTable = loadStateTable(DEFAULT_STATE_TABLE_FILE);

                    if (stateTable != null) {
                    	STATE_TABLE_CACHE.put(DEFAULT_STATE_TABLE_KEY, stateTable);
                    }
                } catch (Exception e) {
                    logger.error("Failure to load default config object lifecycle definition from default location.", e);
                }
            }
        }

        return stateTable;
    }

    protected ConcurrentMap<String, Set<String>> retrieveStateActionsTable(String entityName) {
        ConcurrentMap<String, Set<String>> stateActionsTable = null;
        if (ENTITY_SPECIFIC_STATE_TABLE_FILE.containsKey(entityName)) {
            // Life-cycle interpreter cache is stored per entity
            stateActionsTable = STATE_ACTIONS_TABLE_CACHE.getIfPresent(entityName);
        } else {
            stateActionsTable = STATE_ACTIONS_TABLE_CACHE.getIfPresent(DEFAULT_STATE_TABLE_KEY);
        }

        if (stateActionsTable == null) {
            stateActionsTable = new ConcurrentHashMap<>();
            ConcurrentMap<String, List<ConfigObjectLifecycleRule<AbstractStateManagedObject>>> stateTable = retrieveStateTable(entityName);
            if (stateTable == null) {
                return null;
            }

            // Index state actions table
            for (String stateActionKey : stateTable.keySet()) {
                String[] stateActionArray = decodeStateTableKey(stateActionKey);
                String state = stateActionArray != null && stateActionArray.length > 0 ? stateActionArray[0] : null;
                String action = stateActionArray != null && stateActionArray.length > 1 ? stateActionArray[1] : null;

                if (state == null || action == null) {
                    continue; // skip invalid data
                }
                Set<String> actionSet = stateActionsTable.get(state);
                if (actionSet == null) {
                    actionSet = new TreeSet<>();
                }

                actionSet.add(action);
                stateActionsTable.put(state, actionSet);
            }

            if (ENTITY_SPECIFIC_STATE_TABLE_FILE.containsKey(entityName)) {
                // Life-cycle interpreter cache is stored per entity
            	STATE_ACTIONS_TABLE_CACHE.put(entityName, stateActionsTable);
            } else {
            	STATE_ACTIONS_TABLE_CACHE.put(DEFAULT_STATE_TABLE_KEY, stateActionsTable);
            }
        }

        return stateActionsTable;
    }

    protected List<ConfigObjectLifecycleRule<AbstractStateManagedObject>> retrieveRuleSet(String entityName,
            String currentState, String action) {
        ConcurrentMap<String, List<ConfigObjectLifecycleRule<AbstractStateManagedObject>>> stateTable = retrieveStateTable(entityName);

        if (stateTable != null) {
            return stateTable.get(encodeStateTableKey(currentState, action));
        }

        return null;
    }

    protected ConcurrentMap<String, List<ConfigObjectLifecycleRule<AbstractStateManagedObject>>> loadStateTable(
            String resourceClasspathLocation) throws Exception {
        List<Rule<AbstractStateManagedObject>> ruleSet = ConfigObjectLifecycleRuleLoader
                .newInstance().load(getClass().getResourceAsStream(resourceClasspathLocation));

        // Index
        ConcurrentMap<String, List<ConfigObjectLifecycleRule<AbstractStateManagedObject>>> stateTable = new ConcurrentHashMap<>();
        if (ruleSet != null) {
            for (Rule<AbstractStateManagedObject> rule : ruleSet) {
                if (!(rule instanceof ConfigObjectLifecycleRule)) {
                    continue; // Skip
                }

                ConfigObjectLifecycleRule<AbstractStateManagedObject> colr = (ConfigObjectLifecycleRule<AbstractStateManagedObject>) rule;
                String key = encodeStateTableKey(colr.getState(), colr.getAction());
                List<ConfigObjectLifecycleRule<AbstractStateManagedObject>> colrList = stateTable.get(key);
                if (colrList == null) {
                    colrList = new ArrayList<>();
                }
                colrList.add(colr);
                stateTable.put(key, colrList);
            }
        }

        return stateTable;
    }

    protected static final String encodeStateTableKey(String state, String action) {
        return state + "|" + action;
    }

    protected static final String[] decodeStateTableKey(String stateActionKey) {
        return StringUtils.split(stateActionKey, "|");
    }

    public Set<String> retrievePossibleNextActions(String entityName, String state) {
        if (state == null)
            return null;

        ConcurrentMap<String, Set<String>> stateActionsTable = retrieveStateActionsTable(entityName);
        Set<String> actionSet = stateActionsTable.get(state);

        return actionSet == null ? new TreeSet<String>() : new TreeSet<String>(actionSet);
    }

    public Map<String, Object> getContextData() {
        if (contextData == null) {
            return new HashMap<String, Object>();
        }
        return contextData;
    }

    public void setContextData(Map<String, Object> contextData) {
        this.contextData = contextData;
    }

    protected T processConfigObject(T configObject, String action) throws InvalidConfigObjectLifecycleException {
        if (configObject == null) {
            throw new RuntimeException("Invalid parameter value: [NULL].");
        }
        List<ConfigObjectLifecycleRule<AbstractStateManagedObject>> ruleList = retrieveRuleSet(configObject.getClass()
                .getSimpleName(), configObject.getState(), action);

        if (ruleList == null) {
            throw new InvalidConfigObjectLifecycleException(configObject.getState(), action, configObject.getObjectId());
        }

        Map<String, Object> contextData = getContextData();
        for (ConfigObjectLifecycleRule<AbstractStateManagedObject> rule : ruleList) {
            try {
                Result applicationResult = rule.apply(configObject, configObject, contextData);
                if (Result.SUCCESS.equals(applicationResult)) {
                    return configObject;
                }
            } catch (GoalApplicationException e) {
                throw new RuntimeException("Failure to apply workflow action: [" + action + "] on object with oid: ["
                        + configObject.getObjectId() + "].", e);
            }
        }

        return configObject;
    }

    public T save(T configObject) {
        try {
            return processConfigObject(configObject, ACTION_SAVE);
        } catch (InvalidConfigObjectLifecycleException e) {
            throw new RuntimeException("Fail to save object with oid: [" + configObject.getObjectId() + "].", e);
        }
    }

    public T approve(T configObject) {
        try {
            return processConfigObject(configObject, ACTION_APPROVE);
        } catch (InvalidConfigObjectLifecycleException e) {
            throw new RuntimeException("Fail to approve object with oid: [" + configObject.getObjectId() + "].", e);
        }
    }

    public T archive(T configObject) {
        try {
            return processConfigObject(configObject, ACTION_ARCHIVE);
        } catch (InvalidConfigObjectLifecycleException e) {
            throw new RuntimeException("Fail to archive object with oid: [" + configObject.getObjectId() + "].", e);
        }
    }

    public T deploy(T configObject) {
        try {
            return processConfigObject(configObject, ACTION_DEPLOY);
        } catch (InvalidConfigObjectLifecycleException e) {
            throw new RuntimeException("Fail to deploy object with oid: [" + configObject.getObjectId() + "].", e);
        }
    }

    public T reject(T configObject) {
        try {
            return processConfigObject(configObject, ACTION_REJECT);
        } catch (InvalidConfigObjectLifecycleException e) {
            throw new RuntimeException("Fail to reject object with oid: [" + configObject.getObjectId() + "].", e);
        }
    }

    public boolean isNewVersionAllowed(String currentState) {
        return currentState == null ? false : (AbstractConfigObjectLifecycleService.STATE_DEPLOYED
                .equalsIgnoreCase(currentState) || AbstractConfigObjectLifecycleService.STATE_ARCHIVED
                .equalsIgnoreCase(currentState));
    }

    public abstract T createInstance();
}
